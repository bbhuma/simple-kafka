# Docker Handbook for Java Backend Developers

# Part 5 -- Dockerfile Deep Dive

## What is a Dockerfile?

A Dockerfile is a text file containing instructions that Docker executes
to build an image.

Flow:

    Dockerfile
        │
    docker build
        │
    Image
        │
    docker run
        │
    Container

------------------------------------------------------------------------

# 1. FROM

Every Dockerfile starts with a base image.

``` dockerfile
FROM eclipse-temurin:17-jre
```

Examples

``` dockerfile
FROM ubuntu:24.04
FROM nginx:latest
FROM mysql:8.4
FROM openjdk:17
```

Only one base image is used unless doing multi-stage builds.

------------------------------------------------------------------------

# 2. WORKDIR

Sets the working directory.

``` dockerfile
WORKDIR /app
```

Equivalent to:

``` bash
cd /app
```

for all following instructions.

------------------------------------------------------------------------

# 3. COPY

Copies files from your computer into the image.

``` dockerfile
COPY target/order-service.jar app.jar
```

More examples

``` dockerfile
COPY src/ /app/src
COPY . .
COPY application.yml /config/
```

Use COPY for almost all local files.

------------------------------------------------------------------------

# 4. ADD

``` dockerfile
ADD app.tar.gz /app
```

ADD can: - Copy files - Extract local archives - Download URLs (not
recommended)

Recommendation: Prefer COPY unless ADD's archive extraction is needed.

------------------------------------------------------------------------

# 5. RUN

Executes commands **during image build**.

``` dockerfile
RUN apt-get update
RUN apt-get install -y curl
```

Multiple commands:

``` dockerfile
RUN apt-get update && \
    apt-get install -y curl wget
```

Creates a new image layer.

------------------------------------------------------------------------

# 6. CMD

Default command.

``` dockerfile
CMD ["java","-jar","app.jar"]
```

Can be overridden:

``` bash
docker run myimage ls
```

------------------------------------------------------------------------

# 7. ENTRYPOINT

Defines the executable.

``` dockerfile
ENTRYPOINT ["java","-jar","app.jar"]
```

Arguments supplied to `docker run` are appended.

Spring Boot commonly uses ENTRYPOINT.

------------------------------------------------------------------------

# CMD vs ENTRYPOINT

CMD

-   Default command
-   Easily replaced

ENTRYPOINT

-   Main executable
-   Usually not replaced

Production Spring Boot:

``` dockerfile
ENTRYPOINT ["java","-jar","app.jar"]
```

------------------------------------------------------------------------

# 8. ENV

Environment variables inside the image.

``` dockerfile
ENV TZ=Asia/Kolkata
ENV JAVA_OPTS="-Xms512m -Xmx1g"
```

Available when the container starts.

------------------------------------------------------------------------

# 9. ARG

Build-time variables.

``` dockerfile
ARG JAR_FILE=target/app.jar

COPY ${JAR_FILE} app.jar
```

Available only while building.

------------------------------------------------------------------------

# 10. EXPOSE

Documents which port the application listens on.

``` dockerfile
EXPOSE 8081
```

It does **not** publish the port.

Publishing is done using:

``` bash
docker run -p 8081:8081 image
```

------------------------------------------------------------------------

# 11. USER

Run as a non-root user.

``` dockerfile
RUN useradd spring
USER spring
```

Recommended for production.

------------------------------------------------------------------------

# 12. LABEL

Metadata.

``` dockerfile
LABEL maintainer="you@example.com"
LABEL version="1.0"
```

------------------------------------------------------------------------

# 13. VOLUME

Declares a mount point.

``` dockerfile
VOLUME /data
```

For databases, prefer declaring volumes in `docker-compose.yml`.

------------------------------------------------------------------------

# 14. HEALTHCHECK

Allows Docker to verify the application.

``` dockerfile
HEALTHCHECK CMD curl -f http://localhost:8081/actuator/health || exit 1
```

Useful with Spring Boot Actuator.

------------------------------------------------------------------------

# 15. Layers

Every instruction creates a layer.

    FROM
    RUN
    COPY
    RUN
    ENTRYPOINT

If only the last COPY changes, Docker reuses previous layers.

------------------------------------------------------------------------

# 16. Layer Cache

Slow:

    COPY .
    RUN mvn package

Better:

    COPY pom.xml .
    RUN mvn dependency:go-offline

    COPY src src
    RUN mvn package

Dependencies are cached.

------------------------------------------------------------------------

# 17. Multi-stage Build

``` dockerfile
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/order-service.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
```

Benefits:

-   Smaller image
-   No Maven in runtime image
-   Faster deployments

------------------------------------------------------------------------

# Production Dockerfile

``` dockerfile
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/order-service.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","app.jar"]
```

------------------------------------------------------------------------

# Best Practices

-   Use small base images.
-   Prefer COPY over ADD.
-   Use ENTRYPOINT for Spring Boot.
-   Run as non-root.
-   Keep images immutable.
-   Use multi-stage builds.
-   Avoid unnecessary layers.

------------------------------------------------------------------------

# Cheat Sheet

  Instruction   Purpose
  ------------- -----------------------
  FROM          Base image
  WORKDIR       Working directory
  COPY          Copy files
  ADD           Copy/extract archives
  RUN           Build-time commands
  CMD           Default command
  ENTRYPOINT    Main executable
  ENV           Runtime environment
  ARG           Build variable
  EXPOSE        Document port
  USER          Run as another user
  LABEL         Metadata
  VOLUME        Mount point
  HEALTHCHECK   Container health

Next Part: Docker Compose in depth---every key, command, environment
variable, depends_on, health checks, profiles, scaling, and production
patterns.
