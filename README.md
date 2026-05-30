# Banking Transaction Microservices

A microservices-based backend system built to handle banking transactions — account management, fund transfers, and transaction notifications — using Java, Spring Boot, and Kafka.

This is a portfolio project built to demonstrate backend microservices architecture, event-driven design, and REST API development skills gained during my time at MUFG Bank.

## What It Does

- Accepts fund transfer requests via a REST API
- - Validates account balances and authorization before processing
  - - Publishes transaction events to Kafka for async notification delivery
    - - Stores transaction records and account state in PostgreSQL
      - - Provides a notification service that consumes Kafka events and sends alerts
       
        - ## Tech Stack
       
        - | Layer | Technology |
        - |---|---|
        - | Backend | Java 17, Spring Boot 3.x |
        - | Auth | Spring Security + JWT |
        - | Messaging | Apache Kafka |
        - | Database | PostgreSQL |
        - | DevOps | Docker, Docker Compose, Jenkins |
       
        - ## Services
       
        - ```
          Client → Auth Service (JWT)
                 → Transaction Service → PostgreSQL
                                       → Kafka → Notification Service
                 → Account Service    → PostgreSQL
          ```

          - **Auth Service** — issues and validates JWT tokens
          - - **Account Service** — manages account balances and account data
            - - **Transaction Service** — handles fund transfers, validates rules, records history
              - - **Notification Service** — Kafka consumer that sends transaction alerts
               
                - ## Running Locally
               
                - **Prerequisites:** Java 17+, Docker, Docker Compose
               
                - ```bash
                  git clone https://github.com/Rajeshdevandla/banking-transaction-microservices.git
                  cd banking-transaction-microservices
                  docker-compose up -d
                  ```

                  API runs at: `http://localhost:8080`

                  ## Sample API Endpoints

                  ```
                  POST /api/auth/login          — authenticate and get JWT
                  GET  /api/accounts/{id}       — get account details
                  POST /api/transactions/transfer — initiate a fund transfer
                  GET  /api/transactions/history — get transaction history
                  ```

                  ## What I Learned / Applied

                  - Structuring a multi-service Java project with shared libraries
                  - - Using Kafka for decoupled, async event propagation between services
                    - - JWT-based stateless authentication across microservices
                      - - Writing integration tests with JUnit and Testcontainers
                        - - Setting up a basic CI pipeline with Jenkins and Docker Compose
                         
                          - ## Background
                         
                          - Inspired by real backend work done at MUFG Bank (Hyderabad, 2020–2022) where I maintained REST APIs, worked with PostgreSQL schemas, and integrated Kafka for async workflows between internal banking services.
                         
                          - ---

                          **Rajesh Kumar** — Full Stack Java Developer | Chicago, IL
                          [Portfolio](https://rajeshdevandla.github.io) · [GitHub](https://github.com/Rajeshdevandla)
                          
