# RouteHub – Transportation Management & Development Platform

## Overview

RouteHub is a comprehensive transportation management platform designed to digitalize and optimize bus operator businesses.

The system supports end-to-end operations including:

- Passenger booking & trip management
- Driver & fleet management
- Hub/station coordination
- Financial & operational reporting
- Real-time monitoring & administration

RouteHub is built with a microservices architecture, scalable infrastructure, and cloud-native deployment strategy.

---
### Core Principles

- Microservices-based backend architecture
- RESTful API design
- Stateless services
- Containerized deployment (Docker)
- Kubernetes-ready infrastructure
- GitOps-based CI/CD workflow

---

## Repository Structure

The RouteHub ecosystem consists of multiple repositories organized by domain and responsibility.

### Backend Services

| Repository | Description |
|------------|------------|
| go-routex-user-service | User management, authentication, role & permission |
| go-routex-driver-service | Driver profile, availability, and operations |
| go-routex-trip-service | Trip scheduling and route management |
| go-routex-booking-service | Passenger booking & ticket handling |
| go-routex-payment-service | Payment integration & transaction processing |
| go-routex-gateway | API Gateway & request routing |
| go-routex-config | Centralized configuration management |

---

### Frontend Applications

| Application | Description |
|------------|------------|
| Driver App | Mobile application for drivers |
| Customer App | Mobile booking application |
| Admin Portal | Web-based internal management dashboard |
| Global Portal | Public-facing web platform |

---

### Infrastructure & DevOps

| Repository | Description |
|------------|------------|
| go-routex-deployment-config | GitOps deployment configuration |
| CI/CD Pipelines | Automated build, tag, release & deployment |
| Kubernetes Manifests | Cluster deployment resources |

---

## Technology Stack

### Backend
- Java 21
- Spring Boot
- Spring Security
- JPA / Hibernate
- PostgreSQL
- Redis (optional caching)
- Kafka (optional event streaming)

### Mobile
- React Native / Expo

### Frontend Web
- React / Next.js

### DevOps
- Docker
- Kubernetes
- GitHub / GitLab CI
- GHCR (GitHub Container Registry)

---

## Security

- JWT-based authentication
- Role-based access control (RBAC)
- Encrypted credential storage
- Secure service-to-service communication

---

## Deployment Strategy

RouteHub follows a GitOps-based deployment model:

1. Code merged into `master`
2. CI auto-build & auto-tag
3. Docker image pushed to container registry
4. GitOps repository updated
5. Kubernetes auto-sync deployment

### Environments

- dev
- staging
- production

---

## Core Business Domains

RouteHub manages the following core entities:

- Users (Admin / Staff / Customer)
- Drivers
- Vehicles
- Routes
- Trips
- Bookings
- Payments
- Hubs / Stations

---

## Contribution Workflow

1. Create feature branch
2. Submit Pull Request
3. Code review required
4. Protected master branch (no direct merge)

---

## License

Private project – All rights reserved.