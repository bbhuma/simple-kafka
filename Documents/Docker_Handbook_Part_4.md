# Docker Handbook for Java Backend Developers

# Part 4 -- Docker Networking Explained

## Why Networking Matters

Every container runs in its own isolated network namespace.

Inside a container:

    localhost

always refers to **that container**, not your laptop and not another
container.

------------------------------------------------------------------------

# 1. Network Types

## Bridge (Default)

    +---------------- Docker Bridge Network ----------------+

     Spring Boot  --->  MySQL
          |               ^
          v               |
        Kafka --------> Kafka UI

Most applications use the bridge network.

Containers communicate by **service name**.

Example:

    jdbc:mysql://mysql:3306/ordersdb

NOT

    jdbc:mysql://localhost:3306/ordersdb

------------------------------------------------------------------------

## Host Network

    Container uses Host Network directly

No network isolation.

Linux only (commonly used).

Useful for high-performance networking.

------------------------------------------------------------------------

## None Network

Container has no network.

Useful for security or batch jobs.

------------------------------------------------------------------------

## Overlay Network

Used across multiple Docker hosts.

Primarily used with Docker Swarm.

------------------------------------------------------------------------

# 2. What is localhost?

Suppose your compose file is:

``` yaml
services:

  mysql:

  kafka:

  order-service:
```

Inside order-service

    localhost

means

    order-service container

It does NOT mean

-   Windows
-   Docker Host
-   MySQL Container
-   Kafka Container

------------------------------------------------------------------------

# 3. Why mysql Works

Compose automatically creates DNS.

    mysql

becomes

    172.x.x.x

inside Docker.

Therefore

    jdbc:mysql://mysql:3306/ordersdb

works.

------------------------------------------------------------------------

# 4. Host Ports vs Container Ports

Compose

``` yaml
ports:
  - "3307:3306"
```

Meaning

    HOST            CONTAINER

    3307  ------>   3306

Your laptop connects to

    localhost:3307

Containers connect to

    mysql:3306

Never use localhost between containers.

------------------------------------------------------------------------

# 5. Example

Spring Boot running from STS

    localhost:3307
    localhost:9093

Spring Boot running inside Docker

    mysql:3306
    kafka:9092

------------------------------------------------------------------------

# 6. List Networks

``` bash
docker network ls
```

Example

    bridge
    host
    none
    order-service_default

------------------------------------------------------------------------

# 7. Inspect Network

``` bash
docker network inspect order-service_default
```

Shows

-   subnet
-   gateway
-   connected containers
-   IP addresses

------------------------------------------------------------------------

# 8. Create Network

``` bash
docker network create backend-network
```

Run container

``` bash
docker run --network backend-network nginx
```

------------------------------------------------------------------------

# 9. Connect Existing Container

``` bash
docker network connect backend-network order-service
```

Disconnect

``` bash
docker network disconnect backend-network order-service
```

------------------------------------------------------------------------

# 10. Remove Network

``` bash
docker network rm backend-network
```

Remove unused

``` bash
docker network prune
```

------------------------------------------------------------------------

# 11. Docker Compose Networking

Compose automatically creates

    project_default

Example

    order-service_default

Every service joins automatically.

No manual setup needed.

------------------------------------------------------------------------

# 12. Common Mistakes

❌ Using localhost inside containers

    jdbc:mysql://localhost:3306

Correct

    jdbc:mysql://mysql:3306

------------------------------------------------------------------------

❌ Kafka

    localhost:9092

Correct

    kafka:9092

------------------------------------------------------------------------

# 13. Production Tips

-   Use service names instead of IP addresses.
-   Never hardcode container IPs.
-   Use environment variables for host names.
-   Keep frontend and backend on separate networks when appropriate.
-   Expose only the ports that external clients need.

------------------------------------------------------------------------

# Quick Cheat Sheet

  Need                Command
  ------------------- -----------------------------
  List networks       docker network ls
  Inspect network     docker network inspect NAME
  Create network      docker network create NAME
  Connect container   docker network connect
  Disconnect          docker network disconnect
  Delete network      docker network rm
  Delete unused       docker network prune

------------------------------------------------------------------------

Next Part

Dockerfile Deep Dive

-   FROM
-   COPY
-   ADD
-   RUN
-   CMD
-   ENTRYPOINT
-   ENV
-   ARG
-   USER
-   WORKDIR
-   EXPOSE
-   HEALTHCHECK
-   Layer caching
-   Multi-stage builds
-   Spring Boot optimization
