import requests
import json
import time
import os

API_KEY = "OPENAQ_API_KEY"
FOLDER_NAME = "openaq"
OUTPUT_FILE = "openaq_locations"

def create_folder():
    if not os.path.exists(FOLDER_NAME):
        os.makedirs(FOLDER_NAME)

def fetch_all_locations():
    url = "https://api.openaq.org/v3/locations"
    headers = {"X-API-Key": API_KEY}
    
    all_locations = []
    page = 1
    limit = 1000
    
    print("Starting download of location list...")
    
    while True:
        print(f"Fetching page {page}...")
        params = {
            "limit": limit,
            "page": page,
        }
        
        try:
            t_start = time.time()
            response = requests.get(url, headers=headers, params=params)
            if response.status_code != 200:
                print(f"Error: {response.status_code} - {response.text}")
                break
                
            data = response.json()
            results = data.get('results', [])
            
            if not results:
                break
            
            for loc in results:
                if loc.get('coordinates'):
                    entry = loc
                    all_locations.append(entry)

            file_path = os.path.join(FOLDER_NAME, f"{OUTPUT_FILE}_{page}.json")
            with open(file_path, "w") as f:
                json.dump(all_locations, f)
            print(f"Saved {page} successfully in {time.time() - t_start}s!")
            all_locations = []

            page += 1
            # time.sleep(1)
            
        except Exception as e:
            print(f"Failed: {e}")
            break

    
    print(f"Done! Saved {page}/{page} pages")

if __name__ == "__main__":
    create_folder()
    fetch_all_locations()
