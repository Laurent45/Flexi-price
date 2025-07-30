# flexi-price

## Introduction

**flexi-price** is a Spring Boot POC for calculating shopping cart prices for an online product sales company.
The company sells three types of products: high-end phones, mid-range phones, and laptops.
There are two client types: personal and professional, each with specific pricing rules based on their profile and annual revenue.
The application uses Spring Web and Spring Data JPA (Spring boot v.3.5.4), organized in a multi-layer architecture.

## Getting Started

### Requirements

- Java 24
- Maven Wrapper (included, no need to install Maven)
- curl

### Download and Run

1. Clone the repository:
   ```bash
   git clone https://github.com/Laurent45/Flexi-price.git
   ```
2. Enter the project directory:
   ```bash
   cd flexi-price
   ```
3. Build the project:
   ```bash
   ./mvnw clean package
   ```
4. Run the application with H2 database (dev profile):
   ```bash
   SPRING_PROFILES_ACTIVE=dev ./mvnw spring-boot:run
   ```
   > For MySQL, use the default profile, add the dependency in `pom.xml`, and set up a local database (e.g., with Docker).

## API Usage (curl examples)

### Create a Professional Client
```bash
curl --request POST \
  --url http://localhost:8080/api/v1/clients/professional \
  --header 'content-type: application/json' \
  --data '{
    "legalName": "SCI Stock Market",
    "vatNumber": "FR12345678901",
    "sirenNumber": "73282932033",
    "annualRevenue": 10000000
  }'
```

### Retrieve Professional Client Info
```bash
curl --request GET \
  --url http://localhost:8080/api/v1/clients/professional/73282932033
```

### Create a Personal Client
```bash
curl --request POST \
  --url http://localhost:8080/api/v1/clients/personal \
  --header 'content-type: application/json' \
  --data '{
    "username": "kmarlot",
    "firstName": "Kevin",
    "lastName": "Marlot"
  }'
```

### Retrieve Personal Client Info
```bash
curl --request GET \
  --url http://localhost:8080/api/v1/clients/personal/kmarlot
```

### List Available Products
```bash
curl --request GET \
  --url http://localhost:8080/api/v1/products
```

### Add Product to Cart
```bash
curl --request POST \
  --url http://localhost:8080/api/v1/carts/1/product \
  --header 'content-type: application/json' \
  --data '{
    "name": "MacBook Pro",
    "quantity": 5
  }'
```

### Get Cart Information
```bash
curl --request GET \
  --url http://localhost:8080/api/v1/carts/1
```

## Suggestions for Improvement

- Add more APIs and documentation (Swagger)
- Improve exception handling
- Use MapStruct for DTO mapping
- Optimize SQL statements for performance
- Add more unit and integration tests (Testcontainers)
- Enforce development best practices (Checkstyle, SpotBugs, SonarQube)
- Set up CI/CD pipelines
- Consider hexagonal architecture for better maintainability
