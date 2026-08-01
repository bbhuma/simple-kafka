# Docker Handbook for Java Backend Developers

# Part 2 -- Docker Images, Containers & Core Commands

## Goal

In this chapter you'll learn every day-to-day Docker command in a
logical order.

------------------------------------------------------------------------

# 1. Build an Image

An image is like a Java `.jar` plus its operating system.

Example Dockerfile:

``` dockerfile
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/order-service.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
```

Build it:

``` bash
docker build -t order-service:1.0 .
```

Options:

``` bash
docker build -t order-service:1.0 .
docker build --no-cache -t order-service:1.0 .
docker build -f Dockerfile.dev -t order-service:dev .
```

Use `--no-cache` when Docker keeps using old layers.

------------------------------------------------------------------------

# 2. List Images

``` bash
docker images
docker image ls
```

Example:

    REPOSITORY        TAG     IMAGE ID
    order-service     1.0     abcd1234
    mysql             8.4     efgh5678

------------------------------------------------------------------------

# 3. Inspect an Image

``` bash
docker image inspect order-service:1.0
```

Shows metadata, environment variables, layers, entrypoint and more.

------------------------------------------------------------------------

# 4. Image History

``` bash
docker history order-service:1.0
```

Useful to understand Docker layers.

------------------------------------------------------------------------

# 5. Tag an Image

``` bash
docker tag order-service:1.0 yourdockerhub/order-service:v1
```

Tags don't duplicate the image; they create another reference.

------------------------------------------------------------------------

# 6. Push to Docker Hub

Login:

``` bash
docker login
```

Push:

``` bash
docker push yourdockerhub/order-service:v1
```

------------------------------------------------------------------------

# 7. Pull an Image

``` bash
docker pull mysql:8.4
docker pull redis:7
docker pull nginx:latest
```

Docker downloads only missing layers.

------------------------------------------------------------------------

# 8. Create vs Run

Create only:

``` bash
docker create --name myapp order-service:1.0
```

Starts nothing.

Run:

``` bash
docker run order-service:1.0
```

Creates + Starts.

------------------------------------------------------------------------

# 9. Run a Container

Foreground:

``` bash
docker run order-service:1.0
```

Detached:

``` bash
docker run -d order-service:1.0
```

With port:

``` bash
docker run -p 8081:8081 order-service:1.0
```

With environment variables:

``` bash
docker run \
-e SPRING_PROFILES_ACTIVE=prod \
-p 8081:8081 \
order-service:1.0
```

With volume:

``` bash
docker run \
-v mysql-data:/var/lib/mysql \
mysql:8.4
```

------------------------------------------------------------------------

# 10. List Containers

Running:

``` bash
docker ps
```

All:

``` bash
docker ps -a
```

Important STATUS values:

-   Created
-   Up
-   Exited
-   Restarting

------------------------------------------------------------------------

# 11. Start / Stop

Start:

``` bash
docker start order-service
```

Stop gracefully:

``` bash
docker stop order-service
```

Force stop:

``` bash
docker kill order-service
```

Restart:

``` bash
docker restart order-service
```

Production: - Prefer `stop` - Avoid `kill` unless necessary.

------------------------------------------------------------------------

# 12. Remove Containers

Single:

``` bash
docker rm order-service
```

Force:

``` bash
docker rm -f order-service
```

Stopped containers only:

``` bash
docker container prune
```

------------------------------------------------------------------------

# 13. Execute Commands

Open bash:

``` bash
docker exec -it order-service bash
```

Run one command:

``` bash
docker exec order-service ls /app
```

------------------------------------------------------------------------

# 14. Logs

Current logs:

``` bash
docker logs order-service
```

Live logs:

``` bash
docker logs -f order-service
```

Last 100 lines:

``` bash
docker logs --tail 100 order-service
```

------------------------------------------------------------------------

# 15. Inspect Container

``` bash
docker inspect order-service
```

Shows: - IP - Network - Mounts - Ports - Environment variables - Restart
policy

------------------------------------------------------------------------

# 16. Container Statistics

``` bash
docker stats
```

Shows CPU, RAM, Network and Disk usage.

------------------------------------------------------------------------

# 17. Copy Files

Host → Container

``` bash
docker cp app.jar order-service:/app
```

Container → Host

``` bash
docker cp order-service:/app/log.txt .
```

------------------------------------------------------------------------

# Quick Decision Guide

  Task               Command
  ------------------ ------------------------
  Build image        docker build
  Download image     docker pull
  Upload image       docker push
  Create container   docker create
  Create + Start     docker run
  Start existing     docker start
  Stop gracefully    docker stop
  Force stop         docker kill
  View running       docker ps
  View all           docker ps -a
  Open shell         docker exec -it
  Logs               docker logs -f
  Remove container   docker rm
  Remove stopped     docker container prune

Next Part: - Docker Volumes in depth - Bind mounts vs named volumes -
Anonymous volumes - Volume lifecycle - Persistent databases - Safe
backup and restore
