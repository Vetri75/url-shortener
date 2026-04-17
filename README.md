# 🚀 Scalable URL Shortener (System Design Project)

A high-performance URL shortener built with **Spring Boot, Redis, and MySQL**, designed to handle **high concurrency with low latency**.

---

## 🔥 Why this project stands out

Unlike basic URL shorteners, this system is designed with **real-world scalability concepts**:

* ⚡ Redis caching (ms-level response time)
* 🚦 Rate limiting (prevents abuse)
* 📊 Async analytics processing
* ⚖️ Load balancing architecture
* 🔁 Cache-aside pattern implementation

---

## 🧠 Architecture

<img width="990" height="610" alt="final3" src="https://github.com/user-attachments/assets/131128e9-9ade-4a4e-8a06-325b1288687d" />


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

* Generate unique short code
* Store mapping in MySQL database

---

### 🔹 Analytics (Async)

* Click events processed asynchronously
* Does not block user request

---

## 🚀 Performance (JMeter Tested)

* ⚡ **1–3 ms** response time (cache hit)
* 🔥 Handles **high concurrent users**
* 📈 Throughput tested under multiple load conditions

---

## 🧪 Load Testing

Tested using Apache JMeter under different loads.

📸 Screenshots:

* `/screenshots/test-50-users.png`
* `/screenshots/test-100-users.png`
* `/screenshots/test-200-users.png`
* `/screenshots/test-1000-users.png`
* `/screenshots/test-10000-users.png`

---

## 🏗️ Tech Stack

* **Backend:** Spring Boot (Java)
* **Database:** MySQL
* **Cache:** Redis
* **Testing:** JMeter
* **Containerization:** Docker (WIP)

---

## ▶️ Run Locally

```bash
mvn spring-boot:run
```

---
## ▶️ Run with Docker

```bash
docker compose up --build
```

---
## 📌 Key Concepts

* Cache Aside Pattern
* Rate Limiting (Token Bucket using Redis)
* Horizontal Scaling Design
* Async Processing
* DB + Cache coordination

---

## 🚀 Future Improvements

* Redis clustering
* Distributed rate limiting
* Kubernetes deployment
* Advanced analytics dashboard

---

## 👨‍💻 Author

Vetri K
