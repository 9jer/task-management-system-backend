# Task Management System

## Project Description
The Task Management System is a RESTful API developed using Java and Spring Boot, designed to facilitate task creation, editing, deletion, and viewing. Each task contains a title, description, status (e.g., "pending," "in progress," "completed"), priority (e.g., "high," "medium," "low"), comments, author, and assignee. The system implements authentication, authorization, and role-based access control using JWT tokens.

## Features
- **User Authentication & Authorization**: Secure login and role-based access using Spring Security and JWT.
- **Admin Privileges**:
  - Manage all tasks: create, edit, delete, change status and priority.
  - Assign tasks to users.
  - Leave comments on tasks.
- **User Privileges**:
  - Manage assigned tasks: update status, leave comments.
- **Task Filtering & Pagination**: Retrieve tasks by author, assignee, and other parameters.
- **Error Handling & Validation**: Proper error messages and data validation.
- **API Documentation**: OpenAPI and Swagger UI integration for interactive documentation.
- **Docker Support**: Development environment setup using Docker Compose.
- **Database Support**: PostgreSQL for task storage.
- **Basic Tests**: Unit tests for core functionalities.

## Installation & Setup

### Prerequisites
- Java 21+
- Docker & Docker Compose
- Maven
- PostgreSQL (configured in `application.properties`)

### Steps to Run Locally
1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/task-management-system.git
   cd task-management-system
   ```
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run with Docker Compose:
   ```bash
   docker-compose up --build
   ```
4. Access Swagger UI at:
   ```
   http://localhost:8080/swagger-ui.html
   ```

## API Documentation
The API is documented using Swagger UI. The endpoints allow task creation, modification, filtering, and user authentication.

## Error Handling
The system returns meaningful HTTP status codes and messages:
- `400 Bad Request` – Invalid input data.
- `401 Unauthorized` – Authentication required.
- `403 Forbidden` – Insufficient permissions.
- `404 Not Found` – Resource does not exist.
- `500 Internal Server Error` – Unexpected server failure.

## Testing
Basic unit tests are included. Run tests using:
```bash
mvn test
```

## Contact

- Email: vladimir.stxsevich@gmail.com
