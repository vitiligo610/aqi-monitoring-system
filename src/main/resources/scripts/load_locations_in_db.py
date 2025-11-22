import json
import psycopg2
from psycopg2.extras import Json
import glob
import os
import traceback

DB_CONFIG = {
    "dbname": "aqi_db",
    "user": "postgres",
    "password": "password",
    "host": "localhost",
    "port": "5432"
}

FILE_PATTERN = "openaq/openaq_locations_*.json"

INSERT_SQL = """
    INSERT INTO openaq_locations (
        id, name, locality, timezone, country_code,
        location, datetime_first_utc, datetime_last_utc,
        is_mobile, is_monitor, bounds, raw_response
    )
    VALUES (
        %s, %s, %s, %s, %s,
        ST_SetSRID(ST_MakePoint(%s, %s), 4326), -- PostGIS Point creation (lon, lat)
        %s, %s,
        %s, %s,
        %s, %s
    )
    ON CONFLICT (id) DO UPDATE
    SET
        name = EXCLUDED.name,
        location = EXCLUDED.location,
        raw_response = EXCLUDED.raw_response,
        last_synced = NOW();
"""

def load_data_from_files():
    """Reads all JSON files and inserts location data into PostgreSQL."""
    conn = None
    try:
        conn = psycopg2.connect(**DB_CONFIG)
        cursor = conn.cursor()

        all_files = glob.glob(FILE_PATTERN)
        if not all_files:
            print(f"ERROR: No files found matching '{FILE_PATTERN}' in the current directory.")
            return

        total_inserted = 0

        print(f"Found {len(all_files)} JSON files. Starting batch insert...")

        for file_path in sorted(all_files):
            print(f"Processing file: {file_path}")

            with open(file_path, 'r') as f:
                locations = json.load(f)

            records = []
            for loc in locations:
                try:
                    name = loc['name'] if loc['name'] else 'Unknown'

                    dt_first = loc.get('datetimeFirst')
                    dt_last = loc.get('datetimeLast')

                    datetime_first_utc = dt_first.get('utc') if (dt_first) else None
                    datetime_last_utc = dt_last.get('utc') if (dt_last) else None

                    coordinates = loc.get('coordinates', {})

                    country = loc.get('country', {})

                    record_data = (
                        loc['id'],
                        name,
                        loc.get('locality'),
                        loc['timezone'],
                        country.get('code'),

                        coordinates.get('longitude'),
                        coordinates.get('latitude'),

                        datetime_first_utc,
                        datetime_last_utc,

                        loc.get('isMobile', False),
                        loc.get('isMonitor', False),

                        loc.get('bounds'),

                        Json(loc)
                    )

                    records.append(record_data)

                except KeyError as e:
                    print(f"  - WARNING: Skipping location ID {loc.get('id')}. Missing hard required key: {e}")
                    print(traceback.format_exc())
                    continue
                except Exception as e:
                    print(f"  - WARNING: Skipping location ID {loc.get('id')}. An unexpected error occurred: {e}")
                    print(traceback.format_exc())
                    continue

            if records:
                psycopg2.extras.execute_batch(cursor, INSERT_SQL, records)
                conn.commit()
                total_inserted += len(records)
                print(f"  - Successfully inserted/updated {len(records)} records from {os.path.basename(file_path)}.")

        print("\n--- INGESTION COMPLETE ---")
        print(f"Total records processed across all files: {total_inserted}")

    except Exception as e:
        print(f"\nCRITICAL ERROR: Failed during database operation. Rolling back.")
        print(f"Error details: {e}")
        if conn:
            conn.rollback()
    finally:
        if conn:
            conn.close()

if __name__ == "__main__":
    load_data_from_files()