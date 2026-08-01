# Docker Handbook for Java Backend Developers

# Part 10 -- CI/CD with Docker, GitHub Actions and Jenkins

## What is CI/CD?

**CI (Continuous Integration)** automatically builds and tests your
application whenever code changes.

**CD (Continuous Delivery/Deployment)** automatically packages and
deploys the application.

Typical flow:

    Developer
        |
     git push
        |
    GitHub
        |
    GitHub Actions / Jenkins
        |
    mvn clean verify
        |
    docker build
        |
    docker push
        |
    Docker Registry
        |
    Production Server
        |
    docker compose pull
    docker compose up -d

------------------------------------------------------------------------

# 1. Manual Deployment

``` bash
mvn clean package
docker build -t order-service:1.0 .
docker tag order-service:1.0 youruser/order-service:1.0
docker push youruser/order-service:1.0
```

Production server:

``` bash
docker compose pull
docker compose up -d
```

------------------------------------------------------------------------

# 2. Why CI/CD?

Without CI/CD:

-   Developers build manually.
-   Releases are inconsistent.
-   Mistakes happen.

With CI/CD:

-   Repeatable builds.
-   Automated tests.
-   Consistent deployments.

------------------------------------------------------------------------

# 3. Typical Pipeline

1.  Checkout source
2.  Install JDK
3.  Build project
4.  Run tests
5.  Package JAR
6.  Build Docker image
7.  Push image
8.  Deploy

------------------------------------------------------------------------

# 4. GitHub Actions Workflow

Location:

    .github/workflows/build.yml

Example:

``` yaml
name: Build

on:
  push:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v4

      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 17

      - run: mvn clean verify

      - run: docker build -t order-service:${{ github.sha }} .
```

------------------------------------------------------------------------

# 5. Login to Docker Hub

``` yaml
- uses: docker/login-action@v3
  with:
    username: ${{ secrets.DOCKER_USERNAME }}
    password: ${{ secrets.DOCKER_PASSWORD }}
```

Store credentials as repository secrets.

------------------------------------------------------------------------

# 6. Build and Push

``` yaml
- run: docker build -t youruser/order-service:${{ github.sha }} .

- run: docker push youruser/order-service:${{ github.sha }}
```

------------------------------------------------------------------------

# 7. Jenkins Pipeline

Example stages:

    Checkout
    ↓

    Build

    ↓

    Test

    ↓

    Docker Build

    ↓

    Docker Push

    ↓

    Deploy

------------------------------------------------------------------------

# 8. Versioning

Recommended tags:

    1.0.0
    1.0.1
    2026.08.01
    <git-sha>

Avoid deploying only `latest`.

------------------------------------------------------------------------

# 9. Deployment Server

Update compose:

``` yaml
image: youruser/order-service:1.0.1
```

Deploy:

``` bash
docker compose pull
docker compose up -d
```

------------------------------------------------------------------------

# 10. Rollback

Previous version:

``` yaml
image: youruser/order-service:1.0.0
```

Run:

``` bash
docker compose up -d
```

Rollback is fast because the old image is already available.

------------------------------------------------------------------------

# 11. Pipeline Best Practices

-   Run unit tests before building images.
-   Fail fast on test failures.
-   Tag every build.
-   Keep release history.
-   Do not store passwords in YAML.
-   Use secrets provided by the CI platform.

------------------------------------------------------------------------

# 12. Common Mistakes

❌ Skipping tests

❌ Building directly on production

❌ Using mutable image tags only

❌ Committing credentials

❌ Deploying without rollback plan

------------------------------------------------------------------------

# CI/CD Cheat Sheet

  Stage        Tool
  ------------ --------------------------
  Source       Git
  Build        Maven
  Test         JUnit
  Package      Spring Boot
  Image        Docker
  Registry     Docker Hub
  Deploy       Docker Compose
  Automation   GitHub Actions / Jenkins

------------------------------------------------------------------------

Next Part

Debugging Docker & Spring Boot

-   Container exits immediately
-   OOMKilled
-   Port already allocated
-   Kafka connection issues
-   MySQL startup failures
-   Health check failures
-   Step-by-step troubleshooting checklist
