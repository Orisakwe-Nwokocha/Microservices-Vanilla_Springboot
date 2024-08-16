# Microservices Application Documentation
## Overview

This documentation provides an overview of a microservices architecture designed for a book management and borrowing 
system. The application consists of five main components: eureka-server, gateway-reactive, user-service, book-service, 
and borrow-service. Each component plays a crucial role in the system's functionality, and their interactions are 
facilitated through service discovery and API routing.

### Services
1. #### `eureka-server`
Description:
   The eureka-server is the service registry for the application. It enables service discovery by maintaining a 
   registry of all available services. Other services can register themselves with eureka-server, and clients can 
   query it to discover services.

Responsibilities:
* Registering services for discovery.
* Providing a directory of available services for client applications.
    
Configuration: 
* Port: 8761 (default)
* Endpoint for service discovery: http://localhost:8761/eureka/


2. #### `gateway`
Description:
   The gateway service acts as the entry point for all incoming requests. It routes requests to the appropriate 
   services based on the URI path and handles cross-cutting concerns such as authentication and authorization.

Responsibilities:
* Routing requests to user-service, book-service, and borrow-service.
* Providing a unified API endpoint for clients.

Configuration:
* Port: 8080 (default)
* Route Configuration:
  * /users/** → user-service
  * /books/** → book-service
  * /borrow/** →  borrow-service


3. #### `user-service`
Description:
   The user-service handles user management, including registration, authentication, and authorization. It provides 
   endpoints for user-related operations and ensures secure access to other services.

Responsibilities:
* User registration and management.
* Authentication (login, token generation).
* Authorization (access control based on user roles/authorities).

Configuration:
* Port: 8083
* Endpoints:
  * /api/v1/auth/register - User registration.
  * /api/v1/auth/login - User authentication and token generation.
  * /api/v1/auth/logout - User token blacklisting and invalidation.


4. #### `book-service`
Description:
   The book-service manages book-related operations. It allows for the addition, retrieval, and management of 
   book records.

Responsibilities:
* CRUD operations for books.
* Providing book information to other services.

Configuration:
* Port: 8081
* Endpoints:
   * /books - Add a new book. (POST)
  * /books - Update book information. (PATCH)
   * /books - Retrieve all books. (GET)
  * /books/{id} - Retrieve a book by ID. (GET)


5. #### `borrow-service`
Description:
   The borrow-service manages the borrowing of books. It communicates with the book-service via the gateway to retrieve
   book details and perform borrowing operations.

Responsibilities:
* Handling book borrowing requests.
* Communicating with book-service to check book availability.
* Managing borrowing records.

Configuration:
* Port: 8082 
* Endpoints:
  * /borrow?book_id - Borrow a book by ID. 
  * /returns?borrow_id - Return a borrowed book by ID. 

### Service Interaction
* Service Discovery: All services register themselves with eureka-server and query it to discover other services.
* Routing: The gateway service routes incoming requests to the appropriate service based on the request URI.
* Authentication and Authorization: Requests to book-service and borrow-service are authenticated and authorized. 
The gateway service handles the forwarding of authentication tokens to other services.

### Testing Strategy
#### Unit Testing
Purpose:
Unit testing ensures that individual components or classes within each service work as expected in isolation. Each test focuses on a small, specific piece of functionality, typically at the method or class level.

Key Points:
* Testing Frameworks: JUnit and AssertJ are commonly used for unit testing in this application.
* Code Coverage: Aim for high code coverage by writing tests for all critical methods, including edge cases and error conditions.

Example:
In `user-service`, you might write a unit test for the authentication logic, ensuring that a valid token is generated for correct credentials and that invalid credentials result in an error.

### Integration Testing
Purpose:
Integration testing verifies that different services or components work together correctly. These tests cover the interaction between various parts of the application, such as database operations, service communication, and overall system behavior.

Key Points:
* Testing Frameworks: Spring Boot provides support for integration testing with tools like @SpringBootTest.
* Service Interaction: Test how services interact with each other, including HTTP requests between services (e.g., borrow-service interacting with book-service).
* Database Testing: Use embedded databases (e.g., H2) or test containers for testing database-related functionality.
* API Testing: Test the API endpoints exposed by each service, ensuring that they return the correct responses and handle errors gracefully.

Example:
In borrow-service, an integration test might verify that a book can be successfully borrowed by communicating with book-service via the gateway to check the availability and update the borrow record.

### Deployment
#### Local Deployment
To run the services locally, follow these steps:
1. Start eureka-server on port 8761.
2. Start gateway on port 8080.
3. Start book-service on port 8081. 
4. Start borrow-service on port 8082. 
5. Start user-service on port 8083.
   
Ensure that all services are registered with eureka-server and that the gateway is properly configured to route requests to the appropriate services.

### Docker Deployment
For deploying using Docker, create Docker images for each service and configure a docker-compose.yml file to manage 
the multi-container setup. Ensure that the services can communicate with each other and with eureka-server through 
the Docker network.

## Conclusion
This microservices architecture provides a scalable and maintainable approach to managing book-related operations 
and user interactions. The eureka-server facilitates service discovery, the gateway handles routing and security, 
while the individual services focus on their specific functionalities.