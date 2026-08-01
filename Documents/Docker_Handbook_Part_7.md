# Docker Handbook for Java Backend Developers

# Part 7 -- Spring Boot + Docker End-to-End

## Objective

In this chapter we build, package, containerize and run a Spring Boot
application using Docker and Docker Compose.

Project:

    Spring Boot
        |
    MySQL
        |
    Kafka
        |
    Kafka UI

------------------------------------------------------------------------

# 1. Project Structure

    order-service/
    │
    ├── src/
    ├── target/
    ├── Dockerfile
    ├── docker-compose.yml
    ├── pom.xml
    └── application.yml

------------------------------------------------------------------------

# 2. Build the JAR

Always build first.

``` bash
mvn clean package
```

Output

    target/order-service.jar

If using Spring Boot:

``` bash
mvn clean package -DskipTests
```

------------------------------------------------------------------------

# 3. Dockerfile

``` dockerfile
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/order-service.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","app.jar"]
```

Build image

``` bash
docker build -t order-service:1.0 .
```

------------------------------------------------------------------------

# 4. Run Without Compose

``` bash
docker run -p 8081:8081 order-service:1.0
```

Problem:

Spring Boot cannot reach MySQL or Kafka unless they also exist.

Compose solves this.

------------------------------------------------------------------------

# 5. docker-compose.yml

Typical services

    mysql
    kafka
    kafka-ui
    order-service

Compose starts everything together.

------------------------------------------------------------------------

# 6. Environment Variables

Instead of hardcoding

    localhost

use

``` yaml
SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/ordersdb
SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
```

application.yml

``` yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3307/ordersdb}

  kafka:
    bootstrap-servers: ${SPRING_KAFKA_BOOTSTRAP_SERVERS:localhost:9093}
```

Benefits

-   STS works
-   Docker works
-   No code changes

------------------------------------------------------------------------

# 7. Build vs Up

Rebuild image

``` bash
docker compose build
```

Start

``` bash
docker compose up
```

Build and start

``` bash
docker compose up --build
```

Use `--build` whenever Java code or the packaged JAR changes.

------------------------------------------------------------------------

# 8. Development Workflow

1.  

```{=html}
<!-- -->
```
    Modify Java

2.  

``` bash
mvn clean package
```

3.  

``` bash
docker compose up --build
```

4.  

Test API.

------------------------------------------------------------------------

# 9. Production Workflow

Developer

    git push

↓

CI

    mvn clean package
    docker build
    docker push

↓

Server

    docker compose pull
    docker compose up -d

------------------------------------------------------------------------

# 10. Debugging

Containers

``` bash
docker compose ps
```

Logs

``` bash
docker compose logs -f order-service
```

Shell

``` bash
docker compose exec order-service bash
```

Environment

``` bash
docker exec order-service printenv
```

------------------------------------------------------------------------

# 11. Common Problems

## Cannot connect to MySQL

Wrong

    localhost

Correct

    mysql

inside Docker.

------------------------------------------------------------------------

## Cannot connect to Kafka

Wrong

    localhost:9092

Correct

    kafka:9092

inside Docker.

------------------------------------------------------------------------

## Port already allocated

Find process

``` bash
netstat -ano
```

or change host mapping

    3307:3306
    9093:9092

------------------------------------------------------------------------

## Database disappeared

Most likely

``` bash
docker compose down -v
```

was executed.

------------------------------------------------------------------------

# 12. Release Checklist

Before deployment

-   Build succeeds
-   Tests pass
-   Image built
-   Correct image tag
-   Environment variables configured
-   Volumes configured
-   Health checks pass
-   Logs verified

------------------------------------------------------------------------

# Quick Workflow

    Edit Code
        │
    mvn clean package
        │
    docker compose up --build
        │
    Test
        │
    docker compose logs

------------------------------------------------------------------------

# Interview Questions

Why use Docker Compose instead of many docker run commands?

How do you externalize Spring Boot configuration?

Why use environment variables?

Why use named volumes for MySQL?

How do you debug a container that exits immediately?

------------------------------------------------------------------------

Next Part

Spring Boot production deployment:

-   image versioning
-   Docker Hub
-   tagging strategy
-   rolling updates
-   CI/CD
-   deployment patterns
-   zero-downtime basics
