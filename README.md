# URL Shortener

A scalable URL shortener built using Spring Boot, Redis, and MySQL, designed for low latency and high concurrency.

---

## Features

* Short URL generation
* Custom alias support
* Expiry-based links
* Redis caching for fast redirection (ms-level latency)
* Click analytics tracking
* Rate limiting (Redis-based)

---

## Architecture

<img width="990" height="610" alt="final3" src="https://github.com/user-attachments/assets/dcf90489-5cc1-4b03-9e94-065c49fb9564" />


### Flow:

1. Client sends request
2. Rate limiter (Redis) filters excessive traffic
3. Load balancer distributes requests
4. Application servers handle logic

**Redirection:**

* Cache Hit → return URL instantly
* Cache Miss → fetch from DB → store in cache → return

**URL Generation:**

* Generate short URL
* Store mapping in DB

**Analytics:**

* Click events processed asynchronously

---

## Tech Stack

* Java (Spring Boot)
* MySQL (Primary Database)
* Redis (Cache + Rate Limiting)
* Docker (Containerization)
* JMeter (Load Testing)

---

## Performance

* ~1–3 ms response time (cache hit)
* Handles concurrent users efficiently
* Tested using JMeter under different loads

---

## Load Testing

Load testing performed using Apache JMeter.

Add your screenshots here:

```
/screenshots/JMeter-test-1.png
/screenshots/JMeter-test-2.png
/screenshots/JMeter-test-3.png
/screenshots/JMeter-test-4.png
/screenshots/JMeter-test-5.png
```

---

## Project Structure

```
src/
 ├── controller/
 ├── service/
 ├── repository/
 ├── entity/
 └── config/

pom.xml
docker-compose.yml
README.md
```

---

## ▶️ How to Run

### Run locally

```bash
mvn spring-boot:run
```

---

### Run with Docker (optional)

```bash
docker compose up --build
```

---

## Key Concepts Demonstrated

* Caching strategy (Cache Aside Pattern)
* Rate limiting (Token Bucket / Counter using Redis)
* Load distribution
* Async processing (analytics)
* Database + Cache coordination

---

## Future Improvements

* Distributed cache (clustered Redis)
* CDN integration
* Advanced analytics dashboard
* Horizontal scaling with Kubernetes

---

## 👨‍💻 Author

Vetri K
