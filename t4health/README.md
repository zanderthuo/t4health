# Project Overview
A Spring Boot-based microservice for Presto Technologies for Updating, retrieving, and softdeleting customers.
This service supports individual customers only and sends a welcome SMS via a mocked SMS Gateway.

# Features
- Register new customers
- Update customer details
- Get customer by ID
- List all customers (active only)
- Soft delete customer
- Duplicate checks (ID, email, phone)
- Mock SMS notification service
- H2 in-memory database
- Swagger API documentation
- Dockerized microservice

# Architecture Overview

┌──────────────────────────────┐
│ CustomerController           │
└───────────────▲──────────────┘
│
┌───────────────┴──────────────┐
│ CustomerService + Impl        │
└───────────────▲──────────────┘
│
┌───────────────┴──────────────┐
│ CustomerRepository (JPA)      │
└───────────────▲──────────────┘
│
┌───────────────┴──────────────┐
│ H2 Database                   │
└──────────────────────────────┘
Additional supporting components:
 - CustomerMapper (Entity ↔ DTO)
 - GlobalExceptionHandler
 - MockSmsService

# API Documentation
http://localhost:8080/swagger-ui/index.html

# API Endpoints
- `POST /api/customers` - Register a new customer
- `PUT /api/customers/{id}` - Update customer details
- `GET /api/customers/{id}` - Get customer by ID
- `GET /api/customers` - List all active customers
- `DELETE /api/customers/{id}` - Soft delete customer

# Running Tests
To run the unit and integration tests, use the following Maven command:
```
mvn test
```
