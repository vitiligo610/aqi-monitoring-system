# OpenAQ Location Data Pipeline

This project provides a two-step pipeline to fetch global location metadata from the OpenAQ API and store it efficiently in a PostgreSQL database using PostGIS for optimized geospatial queries.

---

## Setup and Prerequisites

### 1. Environment Setup

First, create and activate a Python virtual environment (`venv`) and install the required dependencies.

```bash
# Create the virtual environment
python3 -m venv venv

# Activate the virtual environment
source venv/bin/activate  # On Linux/macOS
# .\venv\Scripts\activate # On Windows (Command Prompt)

# Install dependencies
pip install -r requirements.txt
````

### 2. Database Setup

You must have a running PostgreSQL instance with the **PostGIS extension** enabled.

Execute the provided SQL file to create the necessary table schema:

```bash
psql -d YOUR_DATABASE_NAME -U YOUR_USERNAME -f openaq_locations.sql
```

*(Note: Replace `YOUR_DATABASE_NAME` and `YOUR_USERNAME`.)*

### 3. Configuration

Before running the scripts, you must update the credential variables in both Python files.

| File                        | Variable to Update | Details                                                                            |
|:----------------------------|:-------------------|:-----------------------------------------------------------------------------------|
| `fetch_openaq_locations.py` | `API_KEY`          | Your **OpenAQ API Key**.                                                           |
| `load_locations_in_db.py`   | `DB_CONFIG`        | Your PostgreSQL connection details (`dbname`, `user`, `password`, `host`, `port`). |

-----

## Execution Instructions

The scripts **must be executed sequentially** to ensure the database loading script has the JSON data it needs.

### Step 1: Fetch Data from OpenAQ

This script paginates through the OpenAQ API, downloads all location data, and saves it to a list of JSON files (e.g., `openaq/openaq_locations_1.json`, `openaq/openaq_locations_2.json`).

```bash
python fetch_openaq_locations.py
```

### Step 2: Load Data into PostgreSQL

This script reads all the generated JSON files (e.g., `openaq/penaq_locations_*.json`) and inserts the data into the `openaq_locations` table using fast batch insertion (`psycopg2.extras.execute_batch`).

```bash
python load_locations_in_db.py
```

-----

## Result

Once both scripts run successfully, your PostgreSQL table will contain all OpenAQ location metadata, including a **PostGIS `GEOGRAPHY` column** ready for server-side "nearest station" queries.
