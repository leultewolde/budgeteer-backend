
# Budgeteer Backend

## Overview

Budgeteer is a financial management application that allows users to manage contracts, income, and expenses. This backend service is built using Spring Boot, with RESTful APIs for interacting with the system. It utilizes a PostgreSQL database for data persistence.

## Features

- **Contract Management**: Create, update, delete, and view contracts.
- **Income Tracking**: Manage both actual and budgeted income records.
- **Expense Management**: Add, update, delete, and retrieve expense records.
- **Validation**: Input data is validated to ensure data integrity.
- **Comprehensive Testing**: Includes unit tests, integration tests, and end-to-end tests.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Docker (for running the PostgreSQL database and integration tests)

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/yourusername/budgeteer_backend.git
cd budgeteer_backend
```

### Configuration

1. **Database Configuration**: Ensure that your `application.properties` or `application.yml` contains the correct database configuration:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/budgeteer
   spring.datasource.username=your_db_username
   spring.datasource.password=your_db_password

   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```

2. **Docker Setup**: Ensure Docker is running. Use Docker to start the PostgreSQL database for development and testing.

### Build and Run

1. **Build the Application**: Use Maven to build the application.

   ```bash
   mvn clean install
   ```

2. **Run the Application**: Start the Spring Boot application.

   ```bash
   mvn spring-boot:run
   ```

### API Documentation

The API documentation is available at `/swagger-ui.html` once the application is running. It provides detailed information about available endpoints and how to use them.

### Running Tests

- **Unit and Integration Tests**: Use Maven to run the tests.

  ```bash
  mvn test
  ```

- **End-to-End Tests**: Ensure Docker is running to execute E2E tests with Testcontainers.

  ```bash
  mvn verify
  ```

## Deployment

### Docker

You can use Docker to build and run the application in a containerized environment.

1. **Build the Docker Image**:

   ```bash
   docker build -t budgeteer-backend .
   ```

2. **Run the Docker Container**:

   ```bash
   docker run -p 8080:8080 --name budgeteer-backend budgeteer-backend
   ```

### Cloud Deployment

Consider deploying the application on cloud platforms like AWS, Azure, or Google Cloud using their respective services for scalability and availability.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request for any improvements or bug fixes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

[//]: # (## Contact)

[//]: # ()
[//]: # (For questions or support, please contact [your.email@example.com]&#40;mailto:your.email@example.com&#41;.)
