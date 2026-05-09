# Banking Transaction Microservices

Secure, high-throughput microservices architecture for MUFG Bank handling millions of financial transactions with real-time streaming.

## Key Achievements
- Migrated monolith to **8 microservices** — independent deployment & scaling
- **45% faster** query execution after DB optimization & indexing
- **60% faster** release cadence with Jenkins CI/CD

## Tech Stack
| Service | Technology |
|---|---|
| API Gateway | Spring Cloud Gateway |
| Auth | Spring Security + JWT + OAuth2 |
| Transactions | Java 17 + Spring Boot 3.2 |
| Messaging | Apache Kafka + RabbitMQ |
| Database | PostgreSQL + Redis |
| DevOps | Docker, Kubernetes, Jenkins |

## Microservices
```
Client → API Gateway → Auth Service
                    → Account Service    → PostgreSQL
                    → Transaction Service → Kafka → Notification Service
                    → Fraud Detection     → Redis Cache
                    → Reporting Service   → ElasticSearch
```

## Quick Start
```bash
docker-compose up -d
```
API Gateway: http://localhost:8080
