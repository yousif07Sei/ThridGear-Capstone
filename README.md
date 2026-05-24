# ThirdGear 🚗

A peer-to-peer marketplace for manual car enthusiasts. ThirdGear allows users to list, browse, and inquire about manual transmission cars.

---

## Tech Stack

- **Backend:** Java 17, Spring Boot 4.0.6
- **Database:** PostgreSQL 16
- **Security:** JWT Authentication
- **Email:** Mailpit (development)
- **Containerization:** Docker & Docker Compose

---

## Getting Started

### Prerequisites

- Docker & Docker Compose installed
- Java 17
- Maven

### Run with Docker

```bash
# 1. Build the jar
mvn package -DskipTests

# 2. Start all containers
docker-compose up --build
```

The app will be available at `http://localhost:8080`

Mailpit (email UI) will be available at `http://localhost:8025`

### Run Locally (Development)

```bash
mvn spring-boot:run
```

Make sure PostgreSQL is running on port `5433` with a database named `thirdgear_db_v2`.

---

## Environment

| Variable | Value |
|---|---|
| App Port | 8080 |
| Database | PostgreSQL |
| DB Name | thirdgear_db |
| Mail Host | Mailpit (port 1025) |
| JWT Expiration | 24 hours |

---

## API Endpoints

### 🔓 Auth (Public)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/register` | Register a new user |
| POST | `/auth/login` | Login and receive JWT token |
| GET | `/auth/verify-email?token=` | Verify email address |
| POST | `/auth/forgot-password` | Request password reset email |
| POST | `/auth/reset-password?token=` | Reset password |

---

### 👤 Users (Admin Only)

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Soft delete user |
| PUT | `/api/users/{id}/change-password` | Change user password |

---

### 🏷️ Categories

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/categories` | Get all categories | Public |
| GET | `/api/categories/{id}` | Get category by ID | Public |
| POST | `/api/categories` | Create category | Admin Only |
| PUT | `/api/categories/{id}` | Update category | Admin Only |
| DELETE | `/api/categories/{id}` | Delete category | Admin Only |

---

### 🚗 Cars

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/cars` | Get all cars | Public |
| GET | `/api/cars/{id}` | Get car by ID | Public |
| POST | `/api/cars` | Create a car listing | Authenticated |
| PUT | `/api/cars/{id}` | Update a car listing | Authenticated |
| DELETE | `/api/cars/{id}` | Delete a car listing | Authenticated |

---

### 🖼️ Car Images

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/cars/{carId}/images` | Get all images for a car | Public |
| POST | `/api/cars/{carId}/images` | Upload multiple images | Authenticated |
| DELETE | `/api/cars/{carId}/images/{imageId}` | Delete an image | Authenticated |

> For uploading images use `form-data` with key `files` and select multiple files.

> Images are accessible at: `http://localhost:8080/uploads/cars/{filename}`

---

### 💬 Inquiries

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/inquiries` | Get all inquiries | Authenticated |
| GET | `/api/inquiries/{id}` | Get inquiry by ID | Authenticated |
| POST | `/api/inquiries` | Create an inquiry | Authenticated |
| PUT | `/api/inquiries/{id}` | Update an inquiry | Authenticated |
| DELETE | `/api/inquiries/{id}` | Delete an inquiry | Authenticated |

---

## Authentication

All protected endpoints require a JWT token in the `Authorization` header:

```
Authorization: Bearer <your_token>
```

You can get a token by calling `POST /auth/login`.

---

## User Roles

| Role | Description |
|---|---|
| `ROLE_USER` | Regular user, can list cars and send inquiries |
| `ROLE_ADMIN` | Full access including category and user management |

---

## Data Seeding

The app automatically seeds the database on startup with:
- 3 Categories (Sports, Classic, JDM)
- 2 Users (1 admin, 1 regular user)
- 5 Cars
- 3 Inquiries

---

## Project Structure

```
src/main/java/com/ga/thirdgear/
├── controller/        # REST controllers
├── service/           # Business logic
├── repository/        # JPA repositories
├── model/             # Entity classes
├── security/          # JWT & Spring Security
├── config/            # Web & app configuration
├── seeder/            # Database seeders
└── exception/         # Global exception handling
```