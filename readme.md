# Error Ticket Manager

## Overview
This is a full-stack project for managing error tickets. It comprises a React frontend, a Spring Boot backend, and a PostgreSQL database. The entire application is containerized and orchestrated using Docker Compose.

## Tech Stack
* **Frontend**: React 19, React Router DOM, Vite
* **Backend**: Java 17, Spring Boot 4.x, Gradle, Java-JWT
* **Database**: PostgreSQL (with pgAdmin4)
* **Infrastructure**: Docker & Docker Compose, Makefile

## Project Structure
* `Frontend/`: The React web application.
* `ErrorTicketManager/`: The Java Spring Boot backend.
* `table.sql`: Database schema definition script.
* `fixed.sql`: SQL scripts for database placeholder and testing data.
* `postgresql.env`: Database environment variables for the Docker-compose containers.
* `compose.yml`: Docker Compose configuration orchestrating frontend, backend, database, and pgAdmin containers.
* `makefile`: Simplified command interface for building and running the project.
* `diagram.drawio`, `ERD.png`, `specification_breakdown.docx`: UI sketches, database entity-relationship diagram, and related requirement specifications.

## Environment Configuration

The project uses one primary files for configurations and environment variables:

### 1. `.env`
This file configures the Spring Boot application and JWT properties:
```env
POSTGRES_USER: sa
POSTGRES_DB: app
POSTGRES_PASSWORD: example
PGADMIN_DEFAULT_EMAIL: a@a.a
PGADMIN_DEFAULT_PASSWORD: example

SERVER_ADDRESS: 0.0.0.0
SERVER_PORT: 8081
JWT_COOKIE_NAME: Auth
JWT_LIFETIME: 86400
JWT_ISSUER: LKT
JWT_SECRET: a-string-for-testing
```

## Getting Started

### Prerequisites
* [Docker and Docker Compose](https://www.docker.com/)
* Java 17

### Running the Application

To build the Spring Boot library and start the entire stack using Docker, run:

```bash
make run
```

This command will output the `.jar` build via Gradle and spin up the Docker containers.

Once running, the application services will be exposed at:
* **Frontend**: [http://localhost:4173](http://localhost:4173)
* **Backend API**: [http://localhost:8081](http://localhost:8081)
* **pgAdmin**: [http://localhost:80](http://localhost:80)
* **PostgreSQL Database**: runs on the standard `5432` port within the docker network.

### Scripts provided
* `table.sql` for table schema initialization.
* `fixed.sql` for inserting testing data.