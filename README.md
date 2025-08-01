# FHIR Backend Integration Assignment

This is a Spring Boot application implementing a backend integration with the [HAPI FHIR](https://hapifhir.io/hapi-fhir/docs/) library. The system enables creating, retrieving, and searching **Patients** and their managing **Organizations** in accordance with the **FHIR R4** specification.

## Features

- Store and retrieve Patient and Organization resources in a standardized format.
- FHIR-compliant REST endpoints (R4).
- Secure endpoints using JWT authentication with READ/WRITE role-based access control.
- Support for:
  - Creating Patients and Organizations
  - Retrieving a Patient by ID
  - Retrieving all Patients and Organizations
  - Searching Patients by family name (minimum 3 characters)
- JSON input/output for all endpoints.
- Deployed in a fully containerized environment (e.g., Kubernetes).
- Backed by a relational database.

## Technologies Used

- Java 17+
- Spring Boot
- HAPI FHIR (Spring Boot Starter)
- Maven
- JWT for security
- Docker

## Endpoints

### Base URL

```
http://localhost:8080/fhir/r4
```

### Patient

| Method | Endpoint                        | Description                          |
|--------|----------------------------------|--------------------------------------|
| GET    | /Patient/{id}                   | Retrieve a Patient by FHIR ID        |
| GET    | /Patient                        | Retrieve all Patients                |
| POST   | /Patient                        | Create a new Patient (JSON body)     |
| GET    | /Patient?family={name}          | Search Patients by family name       |

### Organization

| Method | Endpoint         | Description                          |
|--------|------------------|--------------------------------------|
| GET    | /Organization    | Retrieve all Organizations           |
| POST   | /Organization    | Create a new Organization (JSON body)|

## FHIR Resources

The system supports standard FHIR R4 JSON messages for both `Patient` and `Organization` resources. See official specs for details:

- [Patient Resource](https://hl7.org/fhir/R4/patient.html)
- [Organization Resource](https://hl7.org/fhir/R4/organization.html)

## Authentication

All endpoints are secured using JWT-based authentication.

- Each request must include a valid JWT in the `Authorization` header:
  
  ```
  Authorization: Bearer <your_token_here>
  ```

- Roles:
  - `ROLE_READ` for read access (GET)
  - `ROLE_WRITE` for write access (POST)

## Build & Run

### Prerequisites

- JDK 17+
- Maven
- Docker (for containerization)
- A running relational database (e.g., PostgreSQL or MySQL)

### Build the project

```bash
mvn clean install
```

### Run locally

```bash
mvn spring-boot:run
```

### Run with Docker

```bash
docker build -t fhir-backend .
docker run -p 8080:8080 fhir-backend
```
