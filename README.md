# 🌐 Intelligent AQI Monitoring System – Backend

This is the backend service for the Intelligent Air Quality Index Monitoring and Notification System. Built with **Spring Boot**, it connects to external AQI APIs, manages user accounts, sends Gmail alerts, and provides real-time air quality data for Pakistani cities.

---

## ⚙️ Features

- **User Authentication & Management**  
  JWT-based login, registration, and profile updates.

- **AQI Data Integration**  
  Fetches real-time AQI from external APIs and stores it in PostgreSQL.

- **Location-Based Alerts**  
  Sends Gmail notifications when air quality becomes unsafe in a user’s location.

- **Scheduled Polling**  
  Periodically checks AQI levels and triggers alerts using Spring Scheduler.

- **Chatbot Integration (Planned)**  
  Offers health advice based on AQI and user preferences.

---

## 🧱 Tech Stack

- **Framework**: Spring Boot
- **Database**: PostgreSQL (Dockerized)
- **Security**: Spring Security + JWT
- **Notifications**: Gmail API (SMTP or OAuth2)
- **External API**: AQI data provider for Pakistan
- **Build Tool**: Maven

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Docker
- PostgreSQL client (optional)
- Maven

### Setup PostgreSQL with Docker

```bash
docker run --name postgres-airpulse \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=airpulse_db \
  -p 5432:5432 -d postgres:latest

```


## 📊 API Endpoints

| Method | Endpoint              | Description                     |
|--------|-----------------------|---------------------------------|
| GET    | `/api/aqi`            | Fetch AQI data by location      |
| POST   | `/api/auth/register` | Register a new user             |
| POST   | `/api/auth/login`    | Authenticate user               |
| PUT    | `/api/user/location` | Update user location            |
| GET    | `/api/user/alerts`   | Get AQI alerts for user         |
