# Docker Handbook for Java Backend Developers

# Part 6 -- Docker Compose Deep Dive

## What is Docker Compose?

Docker Compose lets you define and run multiple containers as a single
application.

Instead of:

``` bash
docker run mysql
docker run kafka
docker run redis
docker run springboot
```

you define everything once:

``` yaml
services:
  mysql:
  kafka:
  order-service:
```

and start all services with:

``` bash
docker compose up
```

------------------------------------------------------------------------

# 1. Structure of docker-compose.yml

``` yaml
version: "3.9"

services:
  order-service:
  mysql:
  kafka:

volumes:
  mysql-data:

networks:
  backend:
```

Top-level sections:

-   services
-   volumes
-   networks
-   configs (advanced)
-   secrets (production)

------------------------------------------------------------------------

# 2. service

Each container is a service.

``` yaml
services:
  mysql:
    image: mysql:8.4
```

------------------------------------------------------------------------

# 3. image

Use an existing image.

``` yaml
image: mysql:8.4
```

Compose pulls it automatically if missing.

------------------------------------------------------------------------

# 4. build

Build from your Dockerfile.

``` yaml
build: .
```

Custom Dockerfile:

``` yaml
build:
  context: .
  dockerfile: Dockerfile.dev
```

------------------------------------------------------------------------

# 5. container_name

``` yaml
container_name: order-service
```

Without this Docker generates random names.

Production: optional.

------------------------------------------------------------------------

# 6. ports

``` yaml
ports:
  - "8081:8081"
```

Syntax:

    HOST:CONTAINER

Examples

``` yaml
3307:3306
9093:9092
8080:8080
```

------------------------------------------------------------------------

# 7. environment

``` yaml
environment:
  SPRING_PROFILES_ACTIVE: docker
  SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
```

Equivalent to:

``` bash
docker run -e NAME=value
```

------------------------------------------------------------------------

# 8. env_file

Instead of writing many variables:

``` yaml
env_file:
  - .env
```

Example `.env`

    MYSQL_PASSWORD=root
    MYSQL_DATABASE=ordersdb

------------------------------------------------------------------------

# 9. volumes

Named volume

``` yaml
volumes:
  - mysql-data:/var/lib/mysql
```

Bind mount

``` yaml
volumes:
  - ./logs:/logs
```

------------------------------------------------------------------------

# 10. depends_on

``` yaml
depends_on:
  - mysql
  - kafka
```

Starts dependencies first.

Important:

It **does not wait** until MySQL or Kafka are ready.

Use health checks for readiness.

------------------------------------------------------------------------

# 11. restart

``` yaml
restart: always
```

Options

    no
    always
    unless-stopped
    on-failure

Recommendation:

-   Development: unless-stopped
-   Production: always or on-failure (depends on deployment strategy)

------------------------------------------------------------------------

# 12. healthcheck

Example

``` yaml
healthcheck:
  test: ["CMD","mysqladmin","ping","-h","localhost"]
  interval: 10s
  timeout: 5s
  retries: 5
```

Combined with:

``` yaml
depends_on:
  mysql:
    condition: service_healthy
```

This prevents Spring Boot from starting before MySQL is ready.

------------------------------------------------------------------------

# 13. command

Overrides CMD.

``` yaml
command: sleep infinity
```

Useful for debugging.

------------------------------------------------------------------------

# 14. entrypoint

Overrides Dockerfile ENTRYPOINT.

``` yaml
entrypoint: ["java","-jar","app.jar"]
```

Rarely needed.

------------------------------------------------------------------------

# 15. hostname

``` yaml
hostname: mysql-server
```

Changes hostname inside container.

Usually unnecessary.

------------------------------------------------------------------------

# 16. networks

``` yaml
networks:
  backend:
```

Attach service

``` yaml
networks:
  - backend
```

Compose creates a default network if omitted.

------------------------------------------------------------------------

# 17. profiles

Example

``` yaml
profiles:
  - dev
```

Run only selected profile

``` bash
docker compose --profile dev up
```

------------------------------------------------------------------------

# 18. Compose Commands

Start

``` bash
docker compose up
```

Detached

``` bash
docker compose up -d
```

Rebuild

``` bash
docker compose up --build
```

Stop

``` bash
docker compose stop
```

Restart

``` bash
docker compose restart
```

Delete containers

``` bash
docker compose down
```

Delete containers + volumes

``` bash
docker compose down -v
```

Logs

``` bash
docker compose logs
docker compose logs -f
```

Build only

``` bash
docker compose build
```

Pull latest images

``` bash
docker compose pull
```

Show configuration

``` bash
docker compose config
```

Show running services

``` bash
docker compose ps
```

Execute inside container

``` bash
docker compose exec order-service bash
```

------------------------------------------------------------------------

# 19. Development vs Production

Development

-   bind mounts for source code
-   debug ports
-   verbose logging

Production

-   immutable images
-   named volumes
-   health checks
-   restart policies
-   secrets
-   resource limits

------------------------------------------------------------------------

# 20. Common Mistakes

❌ Using localhost between containers.

❌ Forgetting volumes for databases.

❌ Using `docker compose down -v` accidentally.

❌ Hardcoding passwords.

❌ Depending only on `depends_on`.

------------------------------------------------------------------------

# Compose Cheat Sheet

  Task                Command
  ------------------- ---------------------------
  Start               docker compose up
  Background          docker compose up -d
  Rebuild             docker compose up --build
  Stop                docker compose stop
  Remove containers   docker compose down
  Remove everything   docker compose down -v
  Logs                docker compose logs -f
  Shell               docker compose exec
  List                docker compose ps
  Build               docker compose build
  Pull                docker compose pull

Next Part: Spring Boot + Docker end-to-end: project structure, Maven
build, Dockerfile, Compose, debugging, environment variables, profiles,
and deployment workflow.
