# Docker Handbook for Java Backend Developers

# Part 9 -- Production Docker Best Practices

## Goal

Running containers is easy. Running them reliably in production requires
additional configuration.

This chapter explains the most important production practices.

------------------------------------------------------------------------

# 1. Restart Policies

``` yaml
restart: "no"
restart: always
restart: unless-stopped
restart: on-failure
```

  Policy           Behavior                           Recommended
  ---------------- ---------------------------------- -------------
  no               Never restart                      Debugging
  on-failure       Restart only after non-zero exit   Batch jobs
  unless-stopped   Restart unless manually stopped    Development
  always           Always restart                     Production

------------------------------------------------------------------------

# 2. Health Checks

A container may be **running** but the application inside it may not be
ready.

Spring Boot example:

``` yaml
healthcheck:
  test: ["CMD","curl","-f","http://localhost:8081/actuator/health"]
  interval: 30s
  timeout: 5s
  retries: 5
  start_period: 30s
```

Use with:

``` yaml
depends_on:
  mysql:
    condition: service_healthy
```

This waits for MySQL before starting dependent services.

------------------------------------------------------------------------

# 3. Resource Limits

Limit memory:

``` yaml
deploy:
  resources:
    limits:
      memory: 1G
```

Limit CPU:

``` yaml
deploy:
  resources:
    limits:
      cpus: "1.0"
```

Benefits: - Prevents one container from consuming the entire host. -
Improves stability.

------------------------------------------------------------------------

# 4. Logging

View logs:

``` bash
docker logs order-service
docker logs -f order-service
```

Rotate logs:

``` yaml
logging:
  driver: json-file
  options:
    max-size: "10m"
    max-file: "5"
```

Never allow unlimited log growth.

------------------------------------------------------------------------

# 5. Environment Variables

Avoid hardcoding:

``` yaml
SPRING_DATASOURCE_PASSWORD=root
```

Better:

``` yaml
env_file:
  - .env
```

`.env`

    SPRING_DATASOURCE_PASSWORD=change-me

Do not commit secrets to Git.

------------------------------------------------------------------------

# 6. Docker Secrets

Instead of plain environment variables:

``` yaml
secrets:
  db_password:
    file: ./db_password.txt
```

Production platforms commonly support secret management.

------------------------------------------------------------------------

# 7. Non-root Containers

Avoid:

``` dockerfile
USER root
```

Preferred:

``` dockerfile
RUN useradd spring
USER spring
```

Reduces security risk.

------------------------------------------------------------------------

# 8. Read-only Filesystem

For stateless applications:

``` yaml
read_only: true
```

Write only to mounted volumes.

------------------------------------------------------------------------

# 9. Image Tags

Avoid:

    latest

Prefer:

    1.0.0
    1.0.1
    2026.08.01

Immutable tags make rollbacks easier.

------------------------------------------------------------------------

# 10. Persistent Data

Use named volumes:

``` yaml
volumes:
  - mysql-data:/var/lib/mysql
```

Avoid deleting with:

``` bash
docker compose down -v
```

unless intentionally resetting data.

------------------------------------------------------------------------

# 11. Monitoring

Useful commands:

``` bash
docker stats
docker ps
docker inspect
docker events
```

Typical production monitoring stacks include metrics and centralized
logs.

------------------------------------------------------------------------

# 12. Security Checklist

-   Use official images.
-   Keep images updated.
-   Scan images regularly.
-   Run as non-root.
-   Limit exposed ports.
-   Remove unused images.
-   Rotate credentials.
-   Back up volumes.

------------------------------------------------------------------------

# 13. Production Checklist

Before deployment:

-   Images tagged
-   Health checks configured
-   Restart policy configured
-   Volumes configured
-   Secrets externalized
-   Resource limits reviewed
-   Logs verified
-   Backups tested

------------------------------------------------------------------------

# 14. Common Production Mistakes

❌ Using `latest` only

❌ Running everything as root

❌ No health checks

❌ No restart policy

❌ Deleting volumes accidentally

❌ Hardcoding passwords

❌ Unlimited logs

------------------------------------------------------------------------

# Quick Reference

  Area         Recommendation
  ------------ -----------------------
  Restart      always
  Database     Named volumes
  Health       Configure healthcheck
  Security     Non-root user
  Passwords    Secrets / .env
  Logging      Rotation
  Image Tags   Immutable versions

Next Part

CI/CD with Docker: - GitHub Actions - Jenkins - Build pipelines -
Automatic image publishing - Deployment workflow
