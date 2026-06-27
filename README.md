# Banking Transaction Microservices

A microservices-based backend system for handling banking transactions: account management, fund transfers, and async notifications. Built with Java 17, Spring Boot 3, and Apache Kafka.

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://openjdk.org)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=flat-square&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Kafka](https://img.shields.io/badge/Apache_Kafka-231F20?style=flat-square&logo=apache-kafka&logoColor=white)](https://kafka.apache.org)
[![Docker](https://img.shields.io/badge/Docker-ready-2496ED?style=flat-square&logo=docker)](https://docker.com)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=flat-square&logo=postgresql&logoColor=white)](https://postgresql.org)

## What Problem This Solves

Monolithic banking backends are hard to scale and maintain. This project demonstrates how to decompose core banking operations into focused, independently deployable microservices connected by Kafka for async event flow - the same pattern used in production at enterprise scale.

**Based on real work:** This is a portfolio project based on backend systems I built at MUFG Bank (Hyderabad, 2020-2022), where I built and maintained REST APIs, wrote PostgreSQL queries and stored procedures, and used Kafka for async communication between internal banking services handling 10K+ daily transactions.

## System Architecture

```
Client
  |
  v
Auth Service (JWT validation)
  |
  v
API Gateway
  |-- POST /transactions/transfer
  |           |
  |           v
  |   Transaction Service
  |   |-- validate account balance
  |   |-- record transaction -> PostgreSQL
  |   |-- publish event -> Kafka
  |           |
  |           v
  |   Notification Service (Kafka consumer)
  |   |-- receive transaction event
  |   |-- send customer alert
  |
  |-- GET /accounts/{id}
  |           |
  |           v
  |   Account Service
  |   |-- fetch account data -> PostgreSQL
  |   |-- return balance, details
```

**Services:**

| Service | Responsibility |
|---|---|
| Auth Service | Issues and validates JWT tokens for all requests |
| Account Service | Manages account balances, customer data, account history |
| Transaction Service | Handles fund transfers, validates business rules, records history |
| Notification Service | Kafka consumer that sends transaction alerts to customers |

**Key design decisions:**
- **Kafka for decoupling** - Transaction service publishes events and returns immediately; Notification service processes async. This means a slow notification never blocks a transfer.
- **JWT-based stateless auth** - Tokens validated at each service boundary; no shared session state between services.
- **Testcontainers for integration tests** - Spins up real PostgreSQL and Kafka instances in tests; no mocking infrastructure.
- **Idempotency keys** - Transfer endpoint accepts an idempotency key to prevent duplicate transactions on retry.

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.x |
| Auth | Spring Security + JWT |
| Messaging | Apache Kafka |
| Database | PostgreSQL |
| Testing | JUnit 5, Testcontainers |
| DevOps | Docker, Docker Compose, Jenkins |

## Running Locally

**Prerequisites:** Java 17+, Docker, Docker Compose

```bash
git clone https://github.com/Rajeshdevandla/banking-transaction-microservices.git
cd banking-transaction-microservices
docker-compose up -d
```

All services start automatically: PostgreSQL, Kafka, ZooKeeper, Auth Service, Account Service, Transaction Service, Notification Service.

**API gateway:** http://localhost:8080

## API Reference

| Method | Path | Service | Description |
|---|---|---|---|
| POST | /api/auth/login | Auth | Authenticate and receive JWT |
| GET | /api/accounts/{id} | Account | Fetch account details and balance |
| POST | /api/transactions/transfer | Transaction | Initiate a fund transfer |
| GET | /api/transactions/history | Transaction | View transaction history |

**Transfer request:**

```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Authorization: Bearer {jwt_token}" \
  -H "Content-Type: application/json" \
  -d '{
    "from_account_id": "ACC001",
    "to_account_id": "ACC002",
    "amount": 500.00,
    "currency": "USD",
    "idempotency_key": "txn-unique-id-123"
  }'
```

**Response:**
```json
{
  "transaction_id": "TXN-2024-001",
  "status": "COMPLETED",
  "from_account": "ACC001",
  "to_account": "ACC002",
  "amount": 500.00,
  "timestamp": "2024-03-15T10:30:00Z"
}
```

Kafka event published (async, triggers Notification Service):
```json
{
  "event_type": "TRANSFER_COMPLETED",
  "transaction_id": "TXN-2024-001",
  "customer_id": "CUST-001",
  "amount": 500.00,
  "timestamp": "2024-03-15T10:30:00Z"
}
```

## Project Structure

```
banking-transaction-microservices/
|-- auth-service/
|   |-- src/main/java/.../
|   |   |-- JwtTokenProvider.java
|   |   |-- AuthController.java
|   |   |-- SecurityConfig.java
|   |-- src/test/
|-- account-service/
|   |-- src/main/java/.../
|   |   |-- AccountController.java
|   |   |-- AccountService.java
|   |   |-- AccountRepository.java
|   |-- src/test/
|-- transaction-service/
|   |-- src/main/java/.../
|   |   |-- TransactionController.java
|   |   |-- TransactionService.java     # validates + records + publishes Kafka event
|   |   |-- KafkaEventPublisher.java
|   |-- src/test/
|-- notification-service/
|   |-- src/main/java/.../
|   |   |-- TransactionEventConsumer.java  # Kafka listener
|   |   |-- NotificationService.java
|-- shared/
|   |-- dtos/                              # shared DTOs across services
|   |-- exceptions/                        # shared exception handling
|-- docker-compose.yml
|-- Jenkinsfile
```

## What I'd Build Next

- **Circuit breakers** - add Resilience4j circuit breakers so a failing downstream service doesn't cascade
- **Distributed tracing** - integrate Zipkin or Jaeger to trace requests across service boundaries
- **API gateway** - add Spring Cloud Gateway for rate limiting, auth delegation, and routing
- **Saga pattern** - implement a saga for multi-step transfers that need to roll back on failure
- **Event sourcing** - store all transaction events as the source of truth for a full audit log

## Related Projects

- [Insurance Customer Portal](https://github.com/Rajeshdevandla/insurance-customer-portal) - Full-stack Angular + Spring Boot portal
- [AI Document Intelligence Platform](https://github.com/Rajeshdevandla/ai-document-intelligence-platform) - Document processing with Java microservices + OCR
- [DevOps & Cloud Infrastructure](https://github.com/Rajeshdevandla/devops-cloud-infrastructure) - Terraform and CI/CD configs for AWS deployments

---

Built by [Rajesh Kumar](https://rajeshdevandla.github.io) - Full Stack Java & AI Developer | Chicago, IL# Banking Transaction Microservices

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
