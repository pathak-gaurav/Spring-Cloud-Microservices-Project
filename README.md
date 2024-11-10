# Microservices Architecture with Spring Cloud - In Development

This project demonstrates a microservices-based architecture using Spring Cloud components for service discovery, centralized configuration, resilience, security, and messaging. The architecture includes three core microservices—Product Service, Order Service, and User Service—with supporting components such as Eureka Server, Config Server, API Gateway, and Kafka.

## Architecture Overview

### Core Services
- **Product Service**: Manages product-related data.
- **Order Service**: Manages orders and processes order-related transactions.
- **User Service**: Manages user data and authentication.

Each of these services communicates via REST APIs with added security, resilience, and gateway features.

### Supporting Components
- **Spring Cloud Config Server**: Centralized configuration management using a Git repository for config storage.
- **Eureka Server (Service Discovery)**: Manages service registration and discovery.
- **Spring Cloud API Gateway**: Acts as the entry point, handling routing, load balancing, and security.
- **Circuit Breaker (Resilience4j)**: Protects against cascading failures and adds resilience.
- **Security (OAuth2 + JWT)**: Implements authentication and authorization using OAuth2 and JWT.
- **Kafka**: Facilitates asynchronous communication between services.

## Project Components

1. **Config Server**
   - Centralized configuration for all services.
   - Stores configuration files in a Git repository.
   - Each microservice retrieves its configuration from the Config Server.

2. **Eureka Server (Service Discovery)**
   - Registers each microservice, making them discoverable.
   - Enables load balancing and scaling by allowing services to find each other dynamically.

3. **API Gateway**
   - Provides a unified entry point for all requests to the microservices.
   - Routes requests to the appropriate service based on routing rules.
   - Secures routes using JWT-based authentication.

4. **Circuit Breaker (Resilience4j)**
   - Adds resilience by preventing cascading failures.
   - Configures fallback methods for each service, ensuring graceful degradation in case of service failure.

5. **Security (OAuth2 + JWT)**
   - User Service issues JWT tokens after authentication.
   - Gateway verifies JWT tokens for secure access to protected routes.

6. **Kafka**
   - Provides asynchronous communication between services.
   - Order Service sends events when an order is placed, and Product Service listens to update product stock.

## UML Diagram

```mermaid
classDiagram
    %% Product Service
    namespace ProductService {
        class Product {
            +Long id
            +String name
            +int stock
            +BigDecimal price
        }
        class ProductRepository {
            +save(Product product)
            +findById(Long id) Product
            +findAll() List~Product~
        }
    }

    %% Order Service
    namespace OrderService {
        class Order {
            +Long id
            +Long productId
            +Long userId
            +int quantity
            +BigDecimal totalPrice
            +OrderStatus status
        }
        class OrderStatus {
            <<enumeration>>
            PENDING
            COMPLETED
            CANCELED
        }
        class OrderRepository {
            +save(Order order)
            +findById(Long id) Order
            +findAll() List~Order~
        }
    }

    %% User Service
    namespace UserService {
        class User {
            +Long id
            +String username
            +String password
            +String email
        }
        class UserRepository {
            +save(User user)
            +findById(Long id) User
            +findByUsername(String username) User
        }
        class AuthToken {
            +String token
            +Long userId
            +Date expiryDate
        }
        class AuthTokenRepository {
            +save(AuthToken token)
            +findByUserId(Long userId) AuthToken
        }
    }

    %% Relationships
    Product --> Order : Places Order
    Order --> User : Created by
    User --> AuthToken : Authorized with JWT
    
    %% Repository relationships
    Product --> ProductRepository : managed by
    Order --> OrderRepository : managed by
    User --> UserRepository : managed by
    AuthToken --> AuthTokenRepository : managed by
    
    Order --> OrderStatus : has status
```

## Sequence Diagram

```mermaid
sequenceDiagram
    actor User
    participant API Gateway
    participant User Service (Auth)
    participant Order Service
    participant Product Service
    participant User Database
    participant Order Database
    participant Product Database
    participant Kafka
    participant Eureka Server
    participant Config Server

    %% Initial Service Registration and Config
    Note over Config Server,Eureka Server: Service Registration & Configuration
    Config Server->>User Service (Auth): Provide Configurations
    Config Server->>Order Service: Provide Configurations
    Config Server->>Product Service: Provide Configurations
    User Service (Auth)->>Eureka Server: Register Service
    Order Service->>Eureka Server: Register Service
    Product Service->>Eureka Server: Register Service

    %% Authentication Flow
    Note over User,User Service (Auth): Authentication Flow
    User->>API Gateway: Register/Login
    API Gateway->>User Service (Auth): Forward Authentication Request
    User Service (Auth)->>User Database: Save/Validate User
    User Database-->>User Service (Auth): Confirm User Details
    User Service (Auth)-->>API Gateway: Return JWT Token
    API Gateway-->>User: Return JWT Token

    %% Order Placement Flow
    Note over User,Product Database: Order Placement Flow
    User->>API Gateway: Place Order (with JWT)
    API Gateway->>Order Service: Forward Order Request
    Order Service->>Product Service: Check Product Stock
    Product Service->>Product Database: Query Stock
    Product Database-->>Product Service: Return Stock Status

    alt Stock Available
        Product Service-->>Order Service: Confirm Stock
        Order Service->>Order Database: Save Order
        Order Service->>Kafka: Publish Order Event
        Order Service-->>API Gateway: Order Confirmed
        API Gateway-->>User: Order Confirmation
    else Stock Not Available
        Product Service-->>Order Service: Stock Unavailable
        Order Service-->>API Gateway: Cannot Process Order
        API Gateway-->>User: Out of Stock Message
    end

    %% Async Stock Update
    Note over Kafka,Product Database: Async Stock Update
    Kafka->>Product Service: Consume Order Event
    Product Service->>Product Database: Update Stock Level
```

## Setup Instructions

1. Clone the repository.
2. Configure the Config Server with your Git repository for centralized configuration.
3. Set up and run the Eureka Server, API Gateway, and each microservice (Product, Order, and User).
4. Configure Kafka for event-driven communication.
5. Deploy and test using the API Gateway entry point.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
