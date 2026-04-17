# 🚀 Scalable URL Shortener (Low-Latency System)

A scalable URL shortener designed to handle **high concurrency with sub-5ms redirects using Redis caching and optimized backend architecture**.

---

## 🔥 Why This Project

This project demonstrates how real-world backend systems handle:

* High traffic using caching strategies
* Abuse prevention with rate limiting
* Low-latency responses
* Scalable system design

---

## ✨ Features

* ⚡ Fast redirects using **Redis Cache-Aside pattern**
* 🛡️ Rate Limiting using **Redis (Token Bucket / Counter)**
* ⏳ URL expiry with TTL support
* 📊 Asynchronous Click Analytics (non-blocking)
* 🔤 Base62 encoding for short URLs
* ✅ Custom alias support
* 🗄️ Optimized MySQL with indexing
* 📦 Docker support (basic setup)
* 🚀 Designed for horizontal scaling

---

## 🧠 Architecture

<img width="990" height="610" alt="architecture" src="https://github.com/user-attachments/assets/e22ddfaf-717a-4fd0-8a66-f71d86f34a04" />


---

## ⚙️ System Flow

### 🔹 URL Redirection

1. Request hits **Rate Limiter (Redis)**
2. Passed to **Load Balancer**
3. Routed to **Application Server**
4. **Cache Check (Redis)**:

   * ✅ Cache Hit → Instant redirect
   * ❌ Cache Miss → Fetch from DB → Store in cache → Redirect

---

### 🔹 URL Generation

* Generate short code using Base62
* Store mapping in MySQL

---

### 🔹 Analytics (Async)

* Click events processed asynchronously
* Does not block user requests

---

## ⚡ Performance Highlights

* 🚀 **1–3 ms response time** (cache hit)
* 🔥 Tested with **high concurrent users (JMeter)**
* 📈 High throughput under load
* ⚡ Non-blocking analytics processing

---

## 🧪 Load Testing

Tested using Apache JMeter under multiple load conditions.

📸 Screenshots available in:

```id="lg47jw"
screenshots/test-50-users.png
screenshots/test-100-users.png
screenshots/test-200-users.png
screenshots/test-1000-users.png
screenshots/test-10000-users.png
```

---

## 🏗️ Tech Stack

* **Backend:** Spring Boot (Java 17)
* **Database:** MySQL 8
* **Cache:** Redis
* **Rate Limiting:** Redis
* **Build Tool:** Maven
* **Containerization:** Docker (basic setup)
* **Testing:** Apache JMeter

---

## 📋 API Endpoints

| Method | Endpoint                         | Description              |
| ------ | -------------------------------- | ------------------------ |
| GET    | `/api/url/generate`              | Generate short URL       |
| GET    | `/{shortCode}`                   | Redirect to original URL |
| GET    | `/api/url/analytics/{shortCode}` | Get click analytics      |

---

## 🔧 Example Usage

### Generate Short URL

```bash
curl "http://localhost:8080/api/url/shorten?longUrl=https://example.com"
```

---

### Redirect

```id="z11yqw"
http://localhost:8080/abc123
```

---

### Get Analytics

```bash
curl "http://localhost:8080/api/url/analytics/abc123"
```

---

## ▶️ How to Run

### Run Locally

```bash
mvn spring-boot:run
```

---

### Run with Docker

```bash
docker compose up --build
```

---

## 📁 Project Structure

```id="wqtcs7"
src/main/java/com/URLShortener/
├── config/
├── controller/
├── entity/
├── repository/
├── service/
└── scheduler/
```

---

## 🧠 Key Concepts Demonstrated

* Cache-Aside Pattern
* Rate Limiting using Redis
* Asynchronous Processing
* Horizontal Scaling Design
* DB + Cache coordination
* TTL-based Expiry Handling

---

## 🔮 Future Improvements

* Redis clustering
* Distributed rate limiting
* Kubernetes deployment
* Advanced analytics dashboard

---

## 👨‍💻 Author

**Vetri K**
