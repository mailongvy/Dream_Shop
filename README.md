﻿# Dream_Shop
# Dreamshop API

## Description

Dreamshop API is a backend service for an e-commerce platform, providing functionalities for user authentication and other shop-related operations.

## Prerequisites

*   Java Development Kit (JDK) 17 or later
*   Maven 3.6 or later
*   An IDE like IntelliJ IDEA or VS Code (optional, but recommended)

## Getting Started

1.  **Clone the repository:**
    ```bash
    git clone <your-repository-url>
    cd dreamshop
    ```

2.  **Build the project:**
    ```bash
    mvn clean install
    ```

3.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```
    Alternatively, you can run the `DreamshopApplication.java` file from your IDE.

The application will start on `http://localhost:8080` (or the port configured in `application.properties`).

## API Endpoints

### Authentication

*   **POST** `${apiPrefix}/auth/login`
    *   **Description:** Authenticates a user and returns a JWT token.
    *   **Request Body:**
        ```json
        {
          "email": "user@example.com",
          "password": "yourpassword"
        }
        ```
    *   **Success Response (200 OK):**
        ```json
        {
          "message": "Login success",
          "data": {
            "id": 1, // User ID
            "token": "your.jwt.token"
          }
        }
        ```
    *   **Error Response (401 Unauthorized):**
        ```json
        {
          "message": "Login failed",
          "data": null
        }
        ```

## Technologies Used

*   Java
*   Spring Boot
    *   Spring Security (for authentication and authorization)
    *   Spring Web
    *   Spring Data JPA (if used, not explicitly shown in provided snippets)
*   Maven (for dependency management and build)
*   JWT (JSON Web Tokens for stateless authentication)
*   Lombok (to reduce boilerplate code)

## Configuration

Application-specific configurations can be found and modified in `src/main/resources/application.properties`. This includes:

*   Server port (`server.port`)
*   Database connection details (if applicable)
*   JWT secret and expiration (`jwtSecret`, `jwtExpirationMs` - ensure these are set securely)
*   API prefix (`apiPrefix`)

---
