# Docker Handbook for Java Backend Developers

# Part 12 -- Production Reference Architecture (Spring Boot + MySQL + Kafka)

## Goal

This chapter brings together everything covered so far into a
production-style deployment.

------------------------------------------------------------------------

# 1. Architecture

                     Internet
                         |
                     Reverse Proxy
                   (Nginx/Load Balancer)
                         |
              +----------+----------+
              |                     |
          Spring Boot A        Spring Boot B
              |                     |
              +----------+----------+
                         |
                  Docker Network
            +------------+------------+
            |                         |
          MySQL                  Kafka Broker
            |                         |
            +------------+------------+
                         |
                     Kafka UI
                   (Internal/Admin)

------------------------------------------------------------------------

# 2. Recommended Folder Layout

    project/
    │
    ├── src/
    ├── Dockerfile
    ├── docker-compose.yml
    ├── .dockerignore
    ├── .env
    ├── nginx/
    ├── scripts/
    └── backups/

------------------------------------------------------------------------

# 3. Environment Separation

Development

    application.yml

Docker

    application-docker.yml

Production

    application-prod.yml

Activate using:

``` bash
SPRING_PROFILES_ACTIVE=prod
```

Never hardcode production credentials.

------------------------------------------------------------------------

# 4. Production Compose Principles

Every service should define:

-   restart policy
-   healthcheck
-   named volume (when stateful)
-   resource limits
-   environment variables
-   logging policy

------------------------------------------------------------------------

# 5. Stateful vs Stateless

## Stateless

-   Spring Boot
-   Nginx

Can be replaced at any time.

## Stateful

-   MySQL
-   Kafka

Require persistent storage.

------------------------------------------------------------------------

# 6. Scaling

Spring Boot

    Spring Boot A
    Spring Boot B
    Spring Boot C

Scale horizontally behind a load balancer.

Databases are not normally scaled the same way.

------------------------------------------------------------------------

# 7. Backups

Back up:

-   Database
-   Volumes
-   Configuration
-   Secrets

Do not rely on Docker images as backups.

------------------------------------------------------------------------

# 8. Logging

Applications write logs.

Container runtime collects logs.

Centralized logging systems aggregate logs for searching and alerting.

Rotate logs to prevent disk exhaustion.

------------------------------------------------------------------------

# 9. Monitoring

Monitor:

-   CPU
-   Memory
-   Disk
-   Network
-   JVM
-   Kafka
-   MySQL

Create alerts before resources are exhausted.

------------------------------------------------------------------------

# 10. Security Checklist

✓ Official base images

✓ Non-root containers

✓ Minimal exposed ports

✓ Secrets outside source control

✓ Regular image updates

✓ Regular backups

✓ Health checks

✓ Immutable image tags

------------------------------------------------------------------------

# 11. Deployment Checklist

Before deployment verify:

-   Build succeeds
-   Tests pass
-   Docker image tagged
-   Compose validated
-   Environment variables supplied
-   Health checks succeed
-   Volumes mounted
-   Logs visible
-   Rollback image available

------------------------------------------------------------------------

# 12. Disaster Recovery

If a Spring Boot container fails:

-   Restart or replace the container.

If MySQL fails:

-   Restore from backups if necessary.
-   Reattach the persistent volume.

If Kafka fails:

-   Restore configuration and persistent data according to your
    replication strategy.

------------------------------------------------------------------------

# 13. Common Production Anti-Patterns

❌ Running databases without persistent volumes

❌ Using latest image tags only

❌ Exposing database ports unnecessarily

❌ Committing passwords into Git

❌ Ignoring health checks

❌ Deleting volumes without backups

------------------------------------------------------------------------

# Final Deployment Flow

    Developer
       |
    Git Push
       |
    CI Pipeline
       |
    Run Tests
       |
    Build JAR
       |
    Build Docker Image
       |
    Push Image
       |
    Update Compose
       |
    Deploy
       |
    Health Checks
       |
    Traffic

------------------------------------------------------------------------

# Next Learning Path

After mastering Docker:

1.  Kubernetes
2.  Helm
3.  Ingress Controllers
4.  Service Mesh
5.  Observability
6.  Cloud deployments (AWS, Azure, GCP)
7.  GitOps
8.  Docker security scanning
