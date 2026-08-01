# Docker Handbook for Java Backend Developers

# Part 11 -- Docker & Spring Boot Troubleshooting Playbook

## Purpose

This chapter is organized by **symptom**. Start with the problem you
see, then follow the diagnostic steps.

------------------------------------------------------------------------

# Problem 1: Container exits immediately

Check all containers:

``` bash
docker ps -a
```

Look for:

    STATUS
    Exited (1)
    Exited (137)
    Created
    Restarting

View logs:

``` bash
docker logs order-service
```

Common causes:

-   Spring Boot startup exception
-   Missing environment variable
-   Wrong database URL
-   Missing JAR
-   Wrong ENTRYPOINT

Inspect configuration:

``` bash
docker inspect order-service
```

------------------------------------------------------------------------

# Problem 2: Port already allocated

Error:

    bind: Only one usage of each socket address...

Find the process:

``` bash
netstat -ano | findstr :3306
netstat -ano | findstr :9092
```

Find the PID:

``` bash
tasklist | findstr <PID>
```

Stop the service or change the host port mapping.

Example:

``` yaml
ports:
  - "3307:3306"
```

------------------------------------------------------------------------

# Problem 3: Spring Boot can't connect to MySQL

Symptoms:

-   Communications link failure
-   Connection refused

Checklist:

1.  

``` bash
docker ps
```

Is MySQL running?

2.  

``` bash
docker logs mysql
```

3.  

Verify datasource:

Inside Docker:

    jdbc:mysql://mysql:3306/ordersdb

Outside Docker:

    jdbc:mysql://localhost:3307/ordersdb

------------------------------------------------------------------------

# Problem 4: Kafka connection refused

Symptoms:

    Connection to node -1

Verify Kafka:

``` bash
docker logs kafka
```

Check broker:

``` bash
docker exec kafka kafka-topics --bootstrap-server localhost:9092 --list
```

Spring Boot:

Inside Docker

    kafka:9092

Outside Docker

    localhost:9093

Never use localhost between containers.

------------------------------------------------------------------------

# Problem 5: Kafka UI shows no cluster

Verify:

``` yaml
KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS=kafka:9092
```

Then:

``` bash
docker logs kafka-ui
```

------------------------------------------------------------------------

# Problem 6: MySQL data disappeared

Did you execute?

``` bash
docker compose down -v
```

Check volumes:

``` bash
docker volume ls
```

Inspect:

``` bash
docker volume inspect mysql-data
```

If the volume is deleted, restore from backup if available.

------------------------------------------------------------------------

# Problem 7: Environment variable not applied

Inside the container:

``` bash
docker exec order-service printenv
```

Verify:

    SPRING_KAFKA_BOOTSTRAP_SERVERS
    SPRING_DATASOURCE_URL

------------------------------------------------------------------------

# Problem 8: Image changes not reflected

Rebuild:

``` bash
mvn clean package
docker compose up --build
```

If still stale:

``` bash
docker build --no-cache -t order-service:latest .
```

------------------------------------------------------------------------

# Problem 9: Container keeps restarting

Inspect:

``` bash
docker logs order-service
```

Restart policy:

``` yaml
restart: always
```

A restart policy can hide repeated failures. Always read the logs first.

------------------------------------------------------------------------

# Problem 10: Out of memory

Check:

``` bash
docker stats
```

Symptoms:

-   High memory
-   Container exits with code 137

Consider:

-   Increasing memory limits
-   Investigating memory leaks
-   Reducing JVM heap size

------------------------------------------------------------------------

# Useful Diagnostic Commands

``` bash
docker ps
docker ps -a
docker logs -f order-service
docker inspect order-service
docker exec -it order-service bash
docker stats
docker images
docker network ls
docker network inspect order-service_default
docker volume ls
docker compose ps
docker compose logs
```

------------------------------------------------------------------------

# Recommended Troubleshooting Order

1.  `docker ps -a`
2.  `docker logs`
3.  `docker inspect`
4.  Verify environment variables
5.  Verify networking
6.  Verify ports
7.  Verify volumes
8.  Rebuild only if configuration is correct

------------------------------------------------------------------------

# Quick Decision Table

  Symptom                First Command
  ---------------------- -------------------
  Container exited       docker ps -a
  App won't start        docker logs
  DB connection failed   docker logs mysql
  Kafka failed           docker logs kafka
  Port conflict          netstat -ano
  High CPU/RAM           docker stats
  Missing data           docker volume ls
  Wrong config           docker inspect

Next Part: Complete production-ready Spring Boot + MySQL + Kafka + Kafka
UI project with a fully documented `docker-compose.yml` and deployment
architecture.
