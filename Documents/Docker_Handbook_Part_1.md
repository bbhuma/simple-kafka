# Docker Handbook for Java Backend Developers

**Part 1 -- Docker Fundamentals & Safe Commands**

## 1. Docker Architecture

Docker consists of four main building blocks:

-   **Image** -- Read-only template (like a class in Java).
-   **Container** -- Running instance of an image (like an object).
-   **Volume** -- Persistent storage for data.
-   **Network** -- Allows containers to communicate.

Example:

    Spring Boot Image
            │
    docker run
            ▼
    Spring Boot Container
            │
            ├── MySQL Volume (persistent)
            └── Docker Network

------------------------------------------------------------------------

## 2. Image vs Container vs Volume

  ----------------------------------------------------------------------------------
  Component   Purpose         Deleted by            Deleted by
                              `docker rm`?          `docker compose down -v`?
  ----------- --------------- --------------------- --------------------------------
  Image       Application     No                    No
              template                              

  Container   Running         Yes                   Yes
              application                           

  Volume      Persistent data No                    Yes

  Network     Communication   No                    Yes
  ----------------------------------------------------------------------------------

------------------------------------------------------------------------

# 3. Safe Command Reference

## Build an image

``` bash
docker build -t order-service:1.0 .
```

Use when: - Java code changes - Dockerfile changes

------------------------------------------------------------------------

## List images

``` bash
docker images
```

or

``` bash
docker image ls
```

------------------------------------------------------------------------

## Run a container

``` bash
docker run -p 8081:8081 order-service:1.0
```

Runs one container without Docker Compose.

------------------------------------------------------------------------

## Docker Compose

Start all services:

``` bash
docker compose up
```

Background mode:

``` bash
docker compose up -d
```

Rebuild images:

``` bash
docker compose up --build
```

------------------------------------------------------------------------

## Stop containers safely

``` bash
docker compose down
```

Deletes: - Containers - Compose network

Keeps: - Images - Volumes

**Production:** Safe.

------------------------------------------------------------------------

## Dangerous command

``` bash
docker compose down -v
```

Deletes: - Containers - Network - Volumes

⚠️ MySQL/PostgreSQL data is permanently deleted unless backed up.

------------------------------------------------------------------------

## Stop without deleting

``` bash
docker compose stop
```

Keeps everything.

Restart:

``` bash
docker compose start
```

------------------------------------------------------------------------

## List containers

Running only:

``` bash
docker ps
```

All:

``` bash
docker ps -a
```

------------------------------------------------------------------------

## Logs

``` bash
docker logs order-service
```

Follow continuously:

``` bash
docker logs -f order-service
```

------------------------------------------------------------------------

## Execute inside a container

``` bash
docker exec -it order-service bash
```

------------------------------------------------------------------------

# 4. Volumes

List:

``` bash
docker volume ls
```

Inspect:

``` bash
docker volume inspect mysql-data
```

Delete one volume:

``` bash
docker volume rm mysql-data
```

Delete unused volumes:

``` bash
docker volume prune
```

⚠️ Production: verify backups before removing volumes.

------------------------------------------------------------------------

# 5. Cleanup Commands

Remove stopped containers:

``` bash
docker container prune
```

Remove unused images:

``` bash
docker image prune
```

Remove everything unused:

``` bash
docker system prune
```

Remove everything including volumes:

``` bash
docker system prune -a --volumes
```

⚠️ This is destructive.

------------------------------------------------------------------------

# 6. Safe vs Dangerous

  Command                         Safe   Data Lost?
  ------------------------------- ------ ------------------
  docker compose stop             ✅     No
  docker compose start            ✅     No
  docker compose down             ✅     No
  docker compose up --build       ✅     No
  docker rm                       ✅     No (volume kept)
  docker volume rm                ❌     Yes
  docker compose down -v          ❌     Yes
  docker system prune --volumes   ❌     Yes

------------------------------------------------------------------------

Next Part: - Dockerfile in depth - COPY vs ADD - CMD vs ENTRYPOINT -
Layer caching - Multi-stage builds - Spring Boot Docker best practices
