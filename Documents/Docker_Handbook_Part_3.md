# Docker Handbook for Java Backend Developers

# Part 3 -- Docker Volumes, Bind Mounts & Persistent Data

## Why Volumes Exist

Containers are **ephemeral**. If a container is removed, anything stored
inside its writable layer is lost.

Example:

    docker run mysql

MySQL stores its data inside the container.

If you later run:

``` bash
docker rm mysql
```

the container is deleted and so is the database **unless the data is
stored in a volume**.

------------------------------------------------------------------------

# 1. Persistent Storage

                     Docker Host

            +--------------------------+
            |      Named Volume        |
            |     mysql-data           |
            +------------+-------------+
                         |
                         |
                 /var/lib/mysql
                         |
                 +-------v-------+
                 |   MySQL       |
                 |   Container   |
                 +---------------+

The database files are stored in the Docker volume, not in the
container.

------------------------------------------------------------------------

# 2. Types of Storage

## A. Named Volume (Recommended)

    volumes:
      - mysql-data:/var/lib/mysql

Advantages

-   Portable
-   Managed by Docker
-   Easy backups
-   Best for databases

------------------------------------------------------------------------

## B. Bind Mount

    volumes:
      - ./mysql-data:/var/lib/mysql

Docker stores files directly inside your project folder.

Advantages

-   Easy to inspect
-   Useful during development

Disadvantages

-   OS permission issues
-   Less portable

------------------------------------------------------------------------

## C. Anonymous Volume

    -v /var/lib/mysql

Docker creates a random volume name.

Usually avoided for production.

------------------------------------------------------------------------

# 3. Volume Lifecycle

Create manually

``` bash
docker volume create mysql-data
```

List volumes

``` bash
docker volume ls
```

Example

    DRIVER    VOLUME NAME
    local     mysql-data
    local     redis-data

Inspect

``` bash
docker volume inspect mysql-data
```

Example output

-   Mount point
-   Driver
-   Labels
-   Scope

------------------------------------------------------------------------

# 4. Which Commands Delete Data?

## Safe

``` bash
docker stop mysql
```

Stops container only.

Database stays.

------------------------------------------------------------------------

``` bash
docker start mysql
```

Database stays.

------------------------------------------------------------------------

``` bash
docker restart mysql
```

Database stays.

------------------------------------------------------------------------

``` bash
docker compose down
```

Deletes

-   Containers
-   Network

Keeps

-   Images
-   Volumes

Database survives.

------------------------------------------------------------------------

## Dangerous

``` bash
docker compose down -v
```

Deletes

-   Containers
-   Network
-   Volumes

Result

    orders table
    customers table
    transactions table

    ALL LOST

------------------------------------------------------------------------

``` bash
docker volume rm mysql-data
```

Deletes only the volume.

All database files are removed.

------------------------------------------------------------------------

``` bash
docker volume prune
```

Deletes every unused volume.

Very dangerous on development machines.

------------------------------------------------------------------------

# 5. Your Project

Compose

``` yaml
mysql:

  volumes:
    - mysql-data:/var/lib/mysql

volumes:
  mysql-data:
```

Where are the orders stored?

    mysql-data

    └── InnoDB files

          orders
          customers
          transactions

The container can be deleted.

The database remains.

------------------------------------------------------------------------

# 6. Backup a Volume

Method 1

Dump MySQL

``` bash
mysqldump
```

Method 2

Archive the volume

``` bash
docker run --rm \
-v mysql-data:/data \
-v ${PWD}:/backup \
ubuntu \
tar czf /backup/mysql-backup.tar.gz /data
```

------------------------------------------------------------------------

# 7. Restore a Volume

Create volume

``` bash
docker volume create mysql-data
```

Restore archive

``` bash
docker run --rm \
-v mysql-data:/data \
-v ${PWD}:/backup \
ubuntu \
tar xzf /backup/mysql-backup.tar.gz -C /
```

------------------------------------------------------------------------

# 8. Production Recommendations

✅ Use named volumes for databases.

✅ Back up volumes regularly.

✅ Never use:

``` bash
docker compose down -v
```

unless intentionally destroying the environment.

Avoid

``` bash
docker system prune --volumes
```

on production hosts.

------------------------------------------------------------------------

# 9. Decision Table

  Command                  Container   Volume           Data
  ------------------------ ----------- ---------------- ------
  docker stop              Stops       Keeps            Safe
  docker start             Starts      Keeps            Safe
  docker restart           Restarts    Keeps            Safe
  docker rm                Deletes     Keeps            Safe
  docker compose down      Deletes     Keeps            Safe
  docker compose down -v   Deletes     Deletes          LOST
  docker volume rm         Keeps       Deletes          LOST
  docker volume prune      Keeps       Deletes unused   LOST

------------------------------------------------------------------------

# Interview Questions

### Why use Docker volumes?

Because containers are temporary while database files must persist.

------------------------------------------------------------------------

### Why not store MySQL inside the container?

Deleting the container deletes the database.

------------------------------------------------------------------------

### Named Volume vs Bind Mount?

Named volumes are managed by Docker and preferred for databases.

Bind mounts expose host directories and are preferred for source code
sharing.

------------------------------------------------------------------------

Next Part

Docker Networks

-   Bridge
-   Host
-   Overlay
-   Container DNS
-   Service discovery
-   Why `mysql:3306` works
-   Why `localhost` fails inside containers
