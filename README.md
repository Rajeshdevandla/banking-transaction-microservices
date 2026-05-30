# Banking Transaction Microservices

A microservices-based backend system for handling banking transactions — account management, fund transfers, and async notifications — built with Java, Spring Boot, and Kafka.

This is a portfolio project that demonstrates backend microservices architecture and event-driven design, based on real work at MUFG Bank (Hyderabad, 2020–2022).

## What It Does

- Accepts fund transfer requests via REST API
- Validates account balances before processing transfers
- Publishes transaction events to Kafka for async notification delivery
- Stores transaction records and account state in PostgreSQL
- Notification service consumes Kafka events and sends alerts

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.x |
| Auth | Spring Security + JWT |
| Messaging | Apache Kafka |
| Database | PostgreSQL |
| DevOps | Docker, Docker Compose, Jenkins |

## Service Architecture

```
Client → Auth Service (JWT validation)
       → Transaction Service → PostgreSQL
                             → Kafka → Notification Service
       → Account Service    → PostgreSQL
```

**Auth Service** — issues and validates JWT tokens
**Account Service** — manages account balances and data
**Transaction Service** — handles fund transfers, validates rules, records history
**Notification Service** — Kafka consumer that sends transaction alerts

## Running Locally

Prerequisites: Java 17+, Docker, Docker Compose

```bash
git clone https://github.com/Rajeshdevandla/banking-transaction-microservices.git
cd banking-transaction-microservices
docker-compose up -d
```

API runs at: `http://localhost:8080`

## Sample Endpoints

```
POST /api/auth/login                 — authenticate and receive JWT
GET  /api/accounts/{id}              — fetch account details
POST /api/transactions/transfer      — initiate a fund transfer
GET  /api/transactions/history       — view transaction history
```

## What I Applied Here

- Multi-service Java project structure with shared DTOs and exception handling
- Kafka for decoupled async event flow between services
- JWT-based stateless auth across service boundaries
- Integration tests with JUnit 5 and Testcontainers
- Jenkins CI pipeline with Docker Compose for local dev

## Background

Based on real backend work at MUFG Bank where I built and maintained REST APIs, wrote PostgreSQL queries and stored procedures, and used Kafka for async communication between internal banking services.

---

**Rajesh Kumar** — Full Stack Java Developer | Chicago, IL
[Portfolio](https://rajeshdevandla.github.io) · [GitHub](https://github.com/Rajeshdevandla)
