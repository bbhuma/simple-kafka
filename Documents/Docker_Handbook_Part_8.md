# Docker Handbook for Java Backend Developers

# Part 8 -- Docker Hub, Image Versioning & Deployment

## Why Docker Hub?

Docker Hub is a registry that stores Docker images.

Typical flow:

Developer \| docker build \| docker tag \| docker push \| Docker Hub \|
docker pull \| Production Server

------------------------------------------------------------------------

# 1. Login

``` bash
docker login
```

Logout

``` bash
docker logout
```

------------------------------------------------------------------------

# 2. Build

``` bash
docker build -t order-service:1.0 .
```

List images

``` bash
docker images
```

------------------------------------------------------------------------

# 3. Tag Images

Local image

``` text
order-service:1.0
```

Tag for Docker Hub

``` bash
docker tag order-service:1.0 yourusername/order-service:1.0
```

Latest

``` bash
docker tag order-service:1.0 yourusername/order-service:latest
```

One image may have many tags.

------------------------------------------------------------------------

# 4. Push

``` bash
docker push yourusername/order-service:1.0
```

Push latest

``` bash
docker push yourusername/order-service:latest
```

------------------------------------------------------------------------

# 5. Pull

``` bash
docker pull yourusername/order-service:1.0
```

Docker downloads only missing layers.

------------------------------------------------------------------------

# 6. Versioning Strategy

Recommended

    1.0.0
    1.0.1
    1.1.0
    2.0.0

Avoid relying only on

    latest

because it changes over time.

------------------------------------------------------------------------

# 7. Common Tags

Development

    dev

Testing

    test

Release

    1.0.0

Production

    2026.08.01

Git commit

    a34bc21

------------------------------------------------------------------------

# 8. Deploy on Another Machine

Login

``` bash
docker login
```

Pull

``` bash
docker pull yourusername/order-service:1.0
```

Run

``` bash
docker compose up -d
```

------------------------------------------------------------------------

# 9. Update Application

Developer

``` bash
mvn clean package
docker build -t order-service:1.0.1 .
docker tag order-service:1.0.1 yourusername/order-service:1.0.1
docker push yourusername/order-service:1.0.1
```

Server

``` bash
docker compose pull
docker compose up -d
```

------------------------------------------------------------------------

# 10. Rollback

If version 1.0.1 fails

``` yaml
image: yourusername/order-service:1.0.0
```

Then

``` bash
docker compose up -d
```

Rollback completes without rebuilding.

------------------------------------------------------------------------

# 11. Image Cleanup

Unused images

``` bash
docker image prune
```

All unused

``` bash
docker image prune -a
```

Remove specific

``` bash
docker rmi order-service:1.0
```

------------------------------------------------------------------------

# 12. Best Practices

-   Tag every release.
-   Keep immutable version tags.
-   Treat `latest` as a convenience tag only.
-   Never overwrite production tags intentionally.
-   Keep previous versions available for rollback.

------------------------------------------------------------------------

# Cheat Sheet

  Task            Command
  --------------- --------------------
  Login           docker login
  Logout          docker logout
  Build           docker build
  Tag             docker tag
  Push            docker push
  Pull            docker pull
  List images     docker images
  Remove image    docker rmi
  Remove unused   docker image prune

------------------------------------------------------------------------

# Interview Questions

Why is `latest` discouraged?

How do you roll back to an older version?

Difference between image tags and image IDs?

How does Docker avoid downloading duplicate layers?

------------------------------------------------------------------------

Next Part

Production Docker: - health checks - restart policies - secrets -
resource limits - logging - monitoring - security - production compose
files
