# SalesSavvy — E-Commerce Backend

A production-grade RESTful backend for an e-commerce platform built with Spring Boot, featuring JWT authentication, role-based access control, and a clean layered architecture.

**Live Frontend:** [https://ecommerce-frontend-oii1.vercel.app](https://ecommerce-frontend-oii1.vercel.app)  
**Frontend Repository:** [https://github.com/aqibs143/ecommerce-frontend](https://github.com/aqibs143/ecommerce-frontend)

---

## Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot, Spring MVC, Spring Security
- **Authentication:** JWT (JSON Web Token)
- **ORM:** JPA / Hibernate
- **Database:** MySQL
- **Build Tool:** Maven
- **Testing:** Postman
- **Version Control:** Git / GitHub

---

## Features

- JWT-based stateless authentication with custom `JwtFilter`
- Role-based authorization — **Admin** and **User** roles
- 15+ RESTful APIs covering:
  - User registration and login
  - Product management (CRUD)
  - Cart operations (add, update, remove)
  - Order placement and management
- Normalized MySQL database schema with entity relationships
- Controller-Service-Repository layered architecture
- Global exception handling
- CORS configuration for frontend integration

---

## Project Structure

```
src/main/java/com/salessavvy/
├── controller/        # REST API controllers
├── service/           # Business logic layer
├── repository/        # Spring Data JPA repositories
├── model/             # JPA entity classes
├── dto/               # Request/Response DTOs
├── security/          # JWT filter, SecurityConfig, UserDetailsService
└── exception/         # Global exception handler
```

---

## API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login and get JWT token |

### Products
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | Get all products |
| GET | `/api/products/{id}` | Get product by ID |
| POST | `/api/products` | Add product (Admin) |
| PUT | `/api/products/{id}` | Update product (Admin) |
| DELETE | `/api/products/{id}` | Delete product (Admin) |

### Cart
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/cart` | Get user cart |
| POST | `/api/cart/add` | Add item to cart |
| PUT | `/api/cart/update` | Update cart item |
| DELETE | `/api/cart/remove/{id}` | Remove item from cart |

### Orders
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/orders/place` | Place order |
| GET | `/api/orders` | Get user orders |
| GET | `/api/orders/all` | Get all orders (Admin) |

---

## Getting Started

### Prerequisites
- Java 17+
- Maven
- MySQL

### Installation

```bash
# Clone the repository
git clone https://github.com/aqibs143/E-Commerce-backend-project.git

# Navigate to project directory
cd E-Commerce-backend-project
```

### Database Setup

```sql
CREATE DATABASE salessavvy_db;
```

### Configuration

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/salessavvy_db
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=your_jwt_secret_key
jwt.expiration=86400000
```

### Run the Application

```bash
mvn spring-boot:run
```

Server starts at: `http://localhost:8080`

---

## Testing APIs

Import the Postman collection or test manually:

1. Register a user — `POST /api/auth/register`
2. Login — `POST /api/auth/login` → copy the JWT token
3. Use token in `Authorization: Bearer <token>` header for all protected endpoints

---

