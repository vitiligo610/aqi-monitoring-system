-- http://postgis.net/documentation/getting_started/
CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE IF NOT EXISTS openaq_locations (
     id BIGINT PRIMARY KEY,
     name TEXT NOT NULL,
     locality TEXT,
     timezone TEXT,
     country_code CHAR(4),

    -- Geospatial Field (Crucial for distance queries)
    -- ST_SetSRID(ST_MakePoint(lon, lat), 4326) creates this value
    location GEOGRAPHY(POINT, 4326),

    -- Date/Time Fields
     datetime_first_utc TIMESTAMP WITH TIME ZONE,
     datetime_last_utc TIMESTAMP WITH TIME ZONE,

    -- Metadata Flags
     is_mobile BOOLEAN,
     is_monitor BOOLEAN,

    -- Bounding Box (Stored as a standard PostgreSQL Array of Floats)
    -- The OpenAQ 'bounds' is typically [min_lon, min_lat, max_lon, max_lat]
    bounds NUMERIC(12, 8)[],

    -- Semi-Structured Fields (Stored as JSONB for flexibility and indexing)
    raw_response JSONB NOT NULL,

    last_synced TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_loc_geography ON openaq_locations USING GIST (location);

CREATE INDEX IF NOT EXISTS idx_loc_jsonb_data ON openaq_locations USING GIN (raw_response);

CREATE INDEX IF NOT EXISTS idx_loc_country_code ON openaq_locations (country_code);