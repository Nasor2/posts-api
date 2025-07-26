# Posts API ✨

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=spring)
![Java](https://img.shields.io/badge/Java-21-007396?logo=java)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?logo=postgresql)
![Redis](https://img.shields.io/badge/Redis-8-DC382D?logo=redis)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?logo=docker)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?logo=apachemaven)
![Swagger UI](https://img.shields.io/badge/Swagger%20UI-OpenAPI%203-purple)

A robust **RESTful API** for managing users and their blog posts, built with **Spring Boot 3** following **Hexagonal Architecture** principles. It leverages **PostgreSQL** for reliable data persistence and **Redis** for high-performance caching, all orchestrated for seamless local setup and deployment with **Docker Compose**.

---

## 🚀 Key Features

* **User Management:** Comprehensive CRUD (Create, Read, Update) operations for user profiles.
* **Post Management:** Full CRUD capabilities for blog posts, with each post being associated with a user.
* **High Performance Caching:** Integrates **Redis** to implement a **Cache-Aside pattern**, significantly speeding up data retrieval for frequently accessed information and reducing direct database load.
* **RESTful Design:** Adheres strictly to REST principles, providing clean, intuitive endpoints and utilizing appropriate HTTP status codes for robust communication.
* **Data Validation:** Robust input validation across all DTOs using `jakarta.validation` to ensure data integrity.
* **API Documentation:** Automatically generated and interactive API documentation powered by **SpringDoc OpenAPI (Swagger UI)**, making API exploration and testing straightforward.
* **Dockerized Deployment:** Facilitates easy and consistent environment setup through **Docker** and **Docker Compose**, ensuring a reproducible development and deployment process.
* **Data Persistence:** Uses **PostgreSQL** as a reliable and scalable relational database for all application data.
* **Custom Exceptions:** Implements tailored exception handling for specific error scenarios (e.g., resource not found, data conflicts) for clearer error responses.

---

## 🛠️ Technologies Used

* **Spring Boot 3.x:** The core framework for rapid Java application development, providing an opinionated approach to build production-ready applications.
    * **Spring Data JPA / Hibernate:** An abstraction layer for interacting with the PostgreSQL database, simplifying data access operations.
    * **Spring Data Redis:** Simplifies integration with Redis for caching and data access.
    * **Spring Web:** Provides the foundation for building RESTful APIs.
* **Java 21:** The programming language powering the application, utilizing modern language features.
* **PostgreSQL 16:** A powerful, open-source object-relational database system known for its reliability, feature robustness, and performance.
* **Redis 8:** An open-source, in-memory data structure store, used here as a high-performance cache.
* **Apache Maven:** The primary build automation tool, managing project dependencies and the build lifecycle.
* **Docker:** A platform for developing, shipping, and running applications in containers, ensuring isolated and portable environments.
* **Docker Compose:** A tool for defining and running multi-container Docker applications, orchestrating the API, database, and cache services together.
* **SpringDoc OpenAPI UI:** Automatically generates interactive OpenAPI 3 documentation (Swagger UI) from Spring Boot code.
* **Lombok:** A Java library that automatically plugs into your build process to reduce boilerplate code (e.g., getters, setters, constructors).
* **DTO (Data Transfer Object) Pattern:** Used extensively to decouple domain models from API representation, enabling specific validations and cleaner data transfer between layers.

---

## 🏗️ Architecture and Design Patterns

This project is meticulously structured following the principles of **Hexagonal Architecture (also known as Ports & Adapters)**. This architectural style promotes a strong separation of concerns, high maintainability, and loose coupling, ensuring that the core business logic remains independent of external technologies or frameworks.

### Hexagonal Architecture Breakdown

1.  **Domain Layer (The Core Hexagon):**
    * This is the heart of the application, containing the pure business logic and the fundamental domain models (e.g., `User`, `Post`).
    * It defines **Ports** as interfaces. These ports specify what the application's core can do (`UserService`, `PostService` - **Inbound Ports**) and what it needs from the outside world (`UserRepository`, `PostRepository` - **Outbound Ports**).
    * **Key Principle:** This layer has no dependencies on external technologies (Spring, JPA, Redis, etc.), making it highly testable and framework-agnostic.

2.  **Application Layer (Service Implementations):**
    * Implements the Inbound Ports (e.g., `PostServiceImpl`, `UserServiceImpl`).
    * Contains application-specific logic, orchestrating calls to the domain and using the Outbound Ports. This layer defines the application's use cases.

3.  **Infrastructure Layer (Adapters):**
    * These are the "adapters" that connect the core hexagon to external technologies by implementing the defined Ports.
    * **Driving Adapters (Inbound):** Primarily our REST API controllers (`UserControllerImpl`, `PostControllerImpl`). They translate incoming HTTP requests into calls to the `UserService` and `PostService` (Inbound Ports).
    * **Driven Adapters (Outbound):**
        * **Persistence Adapters:** `SqlUserRepository`, `PostPostgresRepository` implement `UserRepository` and `PostRepository` using Spring Data JPA to interact with **PostgreSQL**.
        * **Caching Adapter:** `RedisPostRepositoryImpl` implements `PostRepository` to interact with **Redis** for caching purposes.
    * **Mappers:** Components (like `PostMapper`, `UserMapper`) within this layer are crucial for translating data between domain objects (e.g., `Post`, `User`) and infrastructure-specific entities (e.g., `PostEntity`, `PostCacheEntity`) or Data Transfer Objects (DTOs).

### Data Flow & Caching Strategy (Cache-Aside with Redis)

The application employs a **Cache-Aside** pattern, managed primarily by `PostServiceImpl`, to optimize data retrieval and ensure consistency between the Redis cache and the PostgreSQL database.

* **Read Operations (e.g., Get Post by ID, Get All Posts):**
    1.  The `PostServiceImpl` first attempts to fetch the requested data from the **Redis cache**.
    2.  **Cache Hit:** If the data is found in Redis, it's immediately returned, providing a very fast response and reducing the load on the database.
    3.  **Cache Miss:** If the data is not in Redis, the `PostServiceImpl` then queries the **PostgreSQL database**.
    4.  Upon successful retrieval from PostgreSQL, the data is stored in the **Redis cache** for subsequent requests before being returned to the client.

* **Write Operations (e.g., Create, Update, Delete Post):**
    1.  The `PostServiceImpl` first performs the data modification (creation, update, or deletion) on the primary **PostgreSQL database** to ensure data durability and integrity.
    2.  After a successful database operation, the `PostServiceImpl` then **explicitly updates or removes** the corresponding entry in the **Redis cache**. This crucial step ensures that the cache remains consistent with the latest state of the data in the primary database, preventing stale data from being served.

---

## 🚀 How to Run the Project

Follow these steps to get your Posts API and its dependencies running quickly using Docker Compose.

### Prerequisites

Ensure you have the following installed on your system:

* **Docker Desktop:** This includes Docker Engine and Docker Compose CLI.
    * [Download Docker Desktop](https://www.docker.com/products/docker-desktop/)
* **Git:** To clone the repository.
    * [Install Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)

### 1. Cloning the Repository

Start by cloning the project repository to your local machine:

```bash
git clone 
cd posts-api
```

### 2. Environment Configuration
Create a file named .env in the root directory of your project (where docker-compose.yml is located). This file will hold your database credentials:

```env
DB_NAME=posts_db
DB_USER=user
DB_PASSWORD=password
```

### 3. Running the Application with Docker Compose

This is the easiest way to spin up the entire application stack (API, PostgreSQL, Redis) with a single command:

1. Ensure you are in the project's root directory (posts-api/).

2. Execute the following command to build the application image and start all defined services:

```bash
docker compose up --build -d
```

3. Check Container Status:

```bash
docker compose ps
```

4. Access the API:
The Spring Boot API should now be fully operational and accessible at http://localhost:8080/swagger-ui/index.html#/.

### 4. Stopping the Application
To stop and remove the containers (and the associated Docker network) while preserving the data in your named volumes (postgres_data, redis_data):

```bash
docker compose down
```

To stop and remove the containers, network, and all associated data volumes:

```bash
docker compose down -v
```

----

## 📖 API Documentation (Swagger UI)

The API is fully documented using SpringDoc OpenAPI, providing an interactive user interface (Swagger UI) to explore, test, and understand all endpoints.

Once the application is running, open your web browser and navigate to:

* **Swagger UI Documentation:** `http://localhost:8080/swagger-ui.html`
* **OpenAPI JSON Specification:** `http://localhost:8080/v3/api-docs`
* **OpenAPI YAML Specification:** `http://localhost:8080/v3/api-docs.yaml`

The Swagger UI interface provides:
* Detailed descriptions of each endpoint (HTTP method, path, summary).
* Information on request parameters (path, query, body), their types, and examples.
* Definitions of request and response DTOs (Data Transfer Objects).
* The ability to send direct requests to the API from the browser for testing.

---

## 📁 Project Structure

A high-level overview of the project directory structure:

```
posts-api/
├── .mvn/                           
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/nasor/postsapi/
│   │   │       ├── PostsApiApplication.java
│   │   │       ├── config/          
│   │   │       ├── post/            
│   │   │       │   ├── application/ 
│   │   │       │   ├── domain/      
│   │   │       │   └── infraestructure/ 
│   │   │       └── user/            
│   │   │           ├── application/ 
│   │   │           ├── domain/      
│   │   │           └── infraestructure/
│   │   └── resources/
│   │       └── application.properties 
│   └── test/                         
├── pom.xml                           
├── Dockerfile                        
├── docker-compose.yml                
└── .env                              
```

---

## 📝 Important Notes & Limitations

> [!IMPORTANT]
> * **User Deletion Policy:** To ensure data integrity and prevent orphaned posts, a user with existing associated posts **cannot be deleted directly**. You must first delete all posts belonging to that user via the appropriate API endpoint before attempting to delete the user.

> [!NOTE]
> * **Database Schema Management (`ddl-auto`):** For development convenience, `SPRING_JPA_HIBERNATE_DDL_AUTO: update` is set in the `docker-compose.yml` for the `app` service. This instructs Hibernate to automatically update the database schema based on your JPA entities upon application startup.
> * **For production, it is strongly recommended to set `SPRING_JPA_HIBERNATE_DDL_AUTO` to `none` or `validate`**, and manage database schema changes using dedicated migration tools like [Flyway](https://flywaydb.org/) or [Liquibase](https://www.liquibase.org/) for controlled and versioned schema evolution.

> [!NOTE]
> * **Generic Error Handling:** The current implementation uses generic `RuntimeException` for various error scenarios (e.g., "User not found", "Post not found").
> * For a production-grade application, consider implementing more specific custom exceptions and a global `@ControllerAdvice` for standardized error responses to clients.

> [!NOTE]
> * **Automatic Timestamp Management:** The `@CreationTimestamp` and `@UpdateTimestamp` annotations (from Hibernate) are used on entity fields (`created_at`, `updated_at`). This ensures that creation timestamps are set once, and update timestamps are automatically managed on every entity modification, without manual intervention in the service layer.
