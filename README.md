# 🔗 Scalable URL Shortener

A production-oriented URL shortener built using **Spring Boot, Redis, and MySQL**, designed for **low-latency redirects, high throughput, and fault tolerance**.

---

## 🌐 Live Demo

👉 **API Base URL:** https://your-live-url.com
👉 Try: https://your-live-url.com/{shortCode}

> Recruiters: Click and test directly. No setup required.

---

## 🚀 Key Features

* ⚡ Sub-5ms redirect latency (cache hit)
* 🔢 Base62 encoding for compact short URLs
* 🧠 Redis caching (cache-aside pattern)
* 🛡️ Rate limiting (IP-based protection)
* 💾 MySQL for durable storage
* 🔁 Fault-tolerant (Redis → DB fallback)
* 📊 Load tested up to 10,000 users

---

## 🧠 System Design

### 🔹 Why Redis?

* O(1) lookup for short URLs
* Reduces DB load drastically
* Improves latency from ~100ms → <5ms

---

### 🔹 Why Base62 Encoding?

* Short, URL-safe IDs
* Avoids collisions
* Efficient ID representation

---

### 🔹 Why Cache-Aside Pattern?

1. Check Redis
2. If miss → query DB
3. Store in Redis

👉 Ensures **fast reads + consistency**

---

### 🔹 Scaling Strategy

* Horizontal scaling (stateless API)
* Redis handles high read traffic
* DB optimized for writes
* Future: sharding + load balancer

---

## 🏗️ Architecture

Client → Load Balancer → Spring Boot → Redis → MySQL

* Redis = fast access layer
* MySQL = source of truth
* API = stateless & scalable

---

## 📡 API Endpoints

### 🔹 Shorten URL

POST `/shorten`

```json
{
  "url": "https://example.com"
}
```

Response:

```json
{
  "shortUrl": "https://your-live-url.com/abc123"
}
```

---

### 🔹 Redirect

GET `/{shortCode}`

* Redis hit → instant redirect
* Cache miss → DB fallback

---

## 📊 Performance Metrics (JMeter Tested)

| Load | Users | Avg Latency | Max Latency | Throughput  | Error Rate |
| ---- | ----- | ----------- | ----------- | ----------- | ---------- |
| Low  | 50    | ~3 ms       | 170 ms      | 1 req/sec   | 0%         |
| Mid  | 200   | ~1 ms       | 170 ms      | 26 req/sec  | 0%         |
| High | 10K   | ~1187 ms    | 32 sec      | 178 req/sec | 0.02%      |

---

### 🔍 Key Insights

* ⚡ Ultra-fast (<5ms) under normal load
* 🚀 Sustains ~178 req/sec at peak
* 📉 Latency increases at extreme load → DB bottleneck
* ✅ System remains stable (no crash under 10K users)

---

## ⚖️ Redis vs DB Performance

| Operation | Redis   | MySQL           |
| --------- | ------- | --------------- |
| Read      | ~1-5 ms | ~50-150 ms      |
| Write     | Fast    | Moderate        |
| Role      | Cache   | Source of truth |

👉 Redis improves performance by **10–50x for reads**

---

## 🧪 Load Testing

* Tool: Apache JMeter
* Users simulated: 50 → 10,000
* Metrics tracked:

  * Latency
  * Throughput
  * Error rate

---

## 🔁 Failure Handling

* Redis failure → fallback to MySQL
* Prevents downtime
* Ensures availability over performance

---

## 🐳 Docker Deployment

```bash
docker build -t url-shortener .
docker-compose up -d
```

---

## ☁️ Cloud Deployment (AWS)

* EC2 → Spring Boot app
* RDS → MySQL
* ElastiCache → Redis

---

## 📈 Production Improvements (Planned)

* DB sharding for scalability
* Kafka for async processing
* Distributed rate limiting
* CDN caching
* Load balancer (NGINX)

---

## 👨‍💻 Author

Vetri K
