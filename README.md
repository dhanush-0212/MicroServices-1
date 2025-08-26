# E-commerce Microservices Application



This repository contains a basic-featured e-commerce application built using a microservices architecture. It demonstrates key patterns and technologies for building robust, scalable, and maintainable distributed systems. The system includes services for managing products, inventory, and orders, along with asynchronous notifications, service discovery, a centralized API gateway, and distributed tracing.

**It is mainly developed to understand microservices,so basic api endpoints are only added..**

## ✨ Features

* Modular, scalable microservice setup

* Kafka-based asynchronous messaging

* Distributed tracing with Zipkin

* JWT/OAuth2 secured endpoints

* Circuit breaker with Resilience4j

<img width="1108" height="691" alt="diagram-export-8-26-2025-10_43_19-PM" src="https://github.com/user-attachments/assets/c975587d-ff54-42a8-8cd1-f6e512ef2d42" />

## Architecture Overview

The application is composed of several independent services that communicate with each other over the network. This design promotes separation of concerns, independent deployment, and scalability.

*   **API Gateway**: The single entry point for all client requests. It routes traffic to the appropriate downstream service, handles authentication via OAuth2/JWT, and facilitates distributed tracing.
   
*   **Discovery Service (Eureka)**: A service registry where all other microservices register themselves. This allows services to dynamically discover and communicate with each other without hardcoded URLs.
  
*   **Product Service**: Manages the product catalog. It provides REST endpoints for creating and listing products, using **MongoDB** for data persistence.
*   **Inventory Service**: Tracks the stock levels for each product. It communicates with the Order Service to verify stock availability and uses a **MySQL** database.
*   **Order Service**: Handles the core business logic of placing orders. It performs a synchronous call to the Inventory Service to check stock and, upon successful validation, saves the order to its **MySQL** database. It then publishes an `OrderPlacedEvent` to a Kafka topic for asynchronous processing. It also includes a **Resilience4j Circuit Breaker** to handle failures when communicating with the Inventory Service.
*   **Notification Service**: A message-driven service that listens to the `notificationTopic` on **Apache Kafka**. It consumes `OrderPlacedEvent` messages and logs them, simulating an email or push notification process.
*   **Distributed Tracing (Zipkin)**: All services are instrumented with Micrometer Tracing and Brave to send trace data to a Zipkin server, allowing for end-to-end visibility of requests as they travel through the system.
*   **Messaging (Kafka & Zookeeper)**: Used for asynchronous, event-driven communication between services, decoupling the Order Service from the Notification Service.



## Tech Stack

| Component                | Technology                                                                                                  |
| ------------------------ | ----------------------------------------------------------------------------------------------------------- |
| **Backend Framework**    | Java 17, Spring Boot 3                                                                                      |
| **Microservices Suite**  | Spring Cloud                                                                                                |
| **Databases**            | MongoDB (Product Service), MySQL (Order & Inventory Service)                                                |
| **API Gateway**          | Spring Cloud Gateway                                                                                        |
| **Service Discovery**    | Netflix Eureka                                                                                              |
| **Asynchronous Messaging** | Apache Kafka                                                                                                |
| **Resilience**           | Resilience4j (Circuit Breaker)                                                                              |
| **Security**             | Spring Security, OAuth2 / JWT (integration with Keycloak)                                                   |
| **Observability**        | Micrometer, Brave, Zipkin                                                                                   |
| **Containerization**     | Docker, Docker Compose                                                                                      |
| **Build Tool**           | Maven                                                                                                       |

## Prerequisites

*   Java 17+
*   Docker & Docker Compose
*   Maven
*   An IDE like IntelliJ IDEA or VS Code
*   Keycloak

## Getting Started

Follow these steps to set up and run the entire application on your local machine.

### 1. Clone the Repository

```bash
git clone https://github.com/dhanush-0212/MicroServices-1.git
cd MicroServices-1
```

### 2. Start Infrastructure Services

The application depends on several external services. You can start most of them using Docker.

**Start Kafka and Zookeeper:**
The provided `docker-compose.yml` file will start Kafka and Zookeeper.

```bash
docker-compose up -d
```

**Start MySQL, MongoDB, and Zipkin:**
Run the following commands to start the remaining infrastructure components.

```bash
# Start Zipkin for distributed tracing
docker run -d -p 9411:9411 --name zipkin openzipkin/zipkin

# Start MongoDB for the Product Service
docker run -d -p 27017:27017 --name mongodb mongo

# Start MySQL for the Order and Inventory Services
docker run -d -p 3306:3306 --name mysql -e MYSQL_ROOT_PASSWORD=1234 mysql:8.0
```

### 3. Database Setup

The Order and Inventory services require separate databases in MySQL. Spring Boot's `ddl-auto=update` configuration will create the necessary tables automatically once the databases exist.

Connect to your MySQL instance and create the two databases:

```sql
CREATE DATABASE order_service;
CREATE DATABASE InventoryService;
```

### 4. Security Setup (Keycloak)
The API Gateway is configured to use Keycloak for authentication.

- **Issuer URI**: `http://localhost:8180/realms/microservices2`
You will need to run a Keycloak instance, create a realm named `microservices2`, and configure a public client to issue JWTs.

### 5. Build and Run the Microservices

Each microservice is a separate Spring Boot application. You must run them in the correct order to ensure dependencies are met. Open a new terminal for each service.

**Build all modules:**
From the root directory, run the Maven command to build all services.

```bash
mvn clean install -DskipTests
```

**Run the services:**
Navigate into each service's directory and run it using the Maven wrapper.

1.  **Discovery Service**
    ```bash
    cd DiscoveryService
    ./mvnw spring-boot:run
    ```
    Wait for it to start, then check the Eureka dashboard at `http://localhost:8761`.

2.  **Product Service**
    ```bash
    cd ../ProductService
    ./mvnw spring-boot:run
    ```

3.  **Inventory Service**
    ```bash
    cd ../InventoryService
    ./mvnw spring-boot:run
    ```

4.  **Order Service**
    ```bash
    cd ../OrderService
    ./mvnw spring-boot:run
    ```

5.  **Notification Service**
    ```bash
    cd ../notifiaction-service
    ./mvnw spring-boot:run
    ```

6.  **API Gateway**
    ```bash
    cd ../APIGateway
    ./mvnw spring-boot:run
    ```

After starting all services, you should see them registered in the Eureka dashboard.

## API Endpoints

All requests should be sent through the API Gateway, which runs on port `8080`.

#### Product Service

*   **Create Product**
    *   `POST /api/product/create-product`
    *   Body:
        ```json
        {
            "name": "iPhone 15",
            "description": "Latest iPhone model",
            "price": 999.99
        }
        ```

*   **Get All Products**
    *   `GET /api/product/getAll`

#### Order Service

*   **Place Order**
    *   `POST /api/order/place`
    *   Body (Ensure the `ordercode` matches a product you have added to the inventory):
        ```json
        {
          "orderLineitemsDto": [
            {
              "ordercode": "iphone_15",
              "price": 999.99,
              "quantity": 1
            }
          ]
        }
        ```
    *   **Note**: Before placing an order, manually add stock for the corresponding `ordercode` in the `inventory` table of the `InventoryService` database.

#### Inventory Service (Internal)

*   **Check Stock**
    *   `GET /api/inventory?code=iphone_15`
    *   This endpoint is intended for internal use by the Order Service but can be called directly for testing.
 
### 6.🧩 Troubleshooting
Service not registering? Check Eureka logs and port conflicts.

Auth issues? Ensure Keycloak is correctly configured and running.

Kafka not publishing? Restart Zookeeper and Kafka containers.
