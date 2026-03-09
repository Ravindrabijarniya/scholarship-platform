![Java](https://img.shields.io/badge/Java-21-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.0-brightgreen?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue?logo=postgresql)
![Redis](https://img.shields.io/badge/Redis-Cache-red?logo=redis)
![Docker](https://img.shields.io/badge/Docker-Containerized-blue?logo=docker)
![License](https://img.shields.io/badge/License-MIT-green)

# 🚀 Scholarship Platform API

A production-ready **Spring Boot backend** for discovering and aggregating scholarships from multiple sources.
The platform supports **authentication, scraping, caching, rate limiting, and search APIs**.

---

# 📌 Features

✔ JWT Authentication
✔ Refresh Token System
✔ Redis Caching
✔ API Rate Limiting
✔ Scholarship Web Scraping
✔ Scheduler for Automated Scraping
✔ Swagger API Documentation
✔ PostgreSQL Database
✔ Docker & Docker Compose Support

---

# 🏗 Architecture

```
Client
   │
Spring Boot API
   │
├── JWT Authentication
├── Rate Limiting (Bucket4j)
├── Redis Cache
├── PostgreSQL Database
├── Scheduler
└── Scholarship Scrapers
```

---

# ⚙️ Tech Stack

Backend:

* Java 21
* Spring Boot 3
* Spring Security
* Spring Data JPA

Database:

* PostgreSQL

Caching:

* Redis

Other Tools:

* Docker
* Swagger (OpenAPI)
* Bucket4j (Rate Limiting)

---

# 📂 Project Structure

```
src/main/java/com/ravindra/scholarship
│
├── config          → Security, Redis, RateLimit configs
├── controller      → REST API controllers
├── service         → Business logic
├── repository      → JPA repositories
├── model           → Database entities
├── scraper         → Scholarship scrapers
└── scheduler       → Scheduled scraping jobs
```

---

# 🚀 Running the Project

### 1️⃣ Clone the repository

```
git clone https://github.com/Ravindrabijarniya/scholarship-platform.git
cd scholarship-platform
```

---

### 2️⃣ Run using Docker

```
docker compose up --build
```

Services started:

* Spring Boot API
* PostgreSQL
* Redis

---

# 📘 API Documentation

Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```

Example APIs:

```
GET /scholarships
GET /scholarships/search
GET /scholarships/upcoming
POST /auth/login
POST /auth/register
POST /scholarships/scrape
```

---

# 🔐 Security

Authentication is implemented using:

* JWT Access Tokens
* Refresh Tokens
* Token Blacklisting
* Role-based authorization

Example:

```
ROLE_ADMIN
ROLE_USER
```

---

# ⚡ Caching

Redis is used to cache scholarship queries to improve performance.

Example cached APIs:

```
GET /scholarships
GET /scholarships/search
```

---

# ⏱ Rate Limiting

API rate limiting is implemented using **Bucket4j** to prevent abuse.

Example policy:

```
20 requests per minute per client
```

              ┌─────────────┐
              │   Client    │
              └──────┬──────┘
                     │
              ┌──────▼──────┐
              │ Spring Boot │
              │     API     │
              └──────┬──────┘
        ┌────────────┼────────────┐
        │            │            │
     Redis       PostgreSQL    Scrapers
   (Cache + RL)    Database

---

# 📊 Future Improvements

* RabbitMQ/Kafka for async scraping
* Elasticsearch for advanced search
* API Gateway
* Microservices architecture

---

# 👨‍💻 Author

**Ravindra Bijarniya**

GitHub
https://github.com/Ravindrabijarniya
