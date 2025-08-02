# flexi-price

## Introduction

**Flexi-price** is a Spring Boot POC for calculating shopping cart prices for an online product sales company.
The company sells three types of products: high-end phones, mid-range phones, and laptops.
There are two client types: personal and professional, each with specific pricing rules based on their types and for professional on their annual revenue.
The application uses Spring Web and Spring Data JPA (Spring boot v.3.5.4), organized in a multi-layer architecture.

## Getting Started

### Requirements

- Java 24
- Maven 3.9.11
- curl

### Download and Run

1. Clone the repository:
   ```bash
   git clone https://github.com/Laurent45/Flexi-price.git
   ```
2. Enter the project directory:
   ```bash
   cd Flexi-price
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


## Example Scenarios

Below are three example scenarios demonstrating API usage for different client types.
When running the application with the \`dev\` profile, the following products are automatically added to the database:

- **MacBook Pro** (Laptop)
   - Personal client price: €1200
   - Professional client (revenue ≥ €10M): €900
   - Professional client (revenue < €10M): €1000

- **iPhone 15 Pro Max** (High-end phone)
   - Personal client price: €1500
   - Professional client (revenue ≥ €10M): €1000
   - Professional client (revenue < €10M): €1150

- **Google Pixel 7a** (Mid-range phone)
   - Personal client price: €800
   - Professional client (revenue ≥ €10M): €550
   - Professional client (revenue < €10M): €600

### 1\. Personal Client Shopping

A personal client named Kevin Marlot creates an account, adds a MacBook Pro to their cart, and retrieves the cart details.

```bash
# Create personal client
curl --request POST \
  --url http://localhost:8080/api/v1/clients/personal \
  --header 'content-type: application/json' \
  --data '{
    "username": "kmarlot",
    "firstName": "Kevin",
    "lastName": "Marlot"
  }'

# Add product to cart
curl --request POST \
  --url http://localhost:8080/api/v1/carts/1/product \
  --header 'content-type: application/json' \
  --data '{
    "name": "MacBook Pro",
    "quantity": 1
  }'

curl --request POST \
  --url http://localhost:8080/api/v1/carts/1/product \
  --header 'content-type: application/json' \
  --data '{
    "name": "iPhone 15 Pro Max",
    "quantity": 1
  }'

curl --request POST \
  --url http://localhost:8080/api/v1/carts/1/product \
  --header 'content-type: application/json' \
  --data '{
    "name": "Google Pixel 7a",
    "quantity": 1
  }'

# Get cart information
curl --request GET \
  --url http://localhost:8080/api/v1/carts/1
```

<hr>

### 2\. Professional Client (Annual Revenue >= 10 Million)
A professional client with high annual revenue creates an account, adds five MacBook Pros to their cart, and retrieves the cart details.

```bash
# Create professional client
curl --request POST \
--url http://localhost:8080/api/v1/clients/professional \
--header 'content-type: application/json' \
--data '{
"legalName": "SCI Stock Market",
"vatNumber": "FR12345678901",
"sirenNumber": "73282932033",
"annualRevenue": 10000000
}'

# Add products to cart
curl --request POST \
--url http://localhost:8080/api/v1/carts/2/product \
--header 'content-type: application/json' \
--data '{
"name": "MacBook Pro",
"quantity": 5
}'

curl --request POST \
--url http://localhost:8080/api/v1/carts/2/product \
--header 'content-type: application/json' \
--data '{
"name": "iPhone 15 Pro Max",
"quantity": 2
}'

curl --request POST \
--url http://localhost:8080/api/v1/carts/2/product \
--header 'content-type: application/json' \
--data '{
"name": "Google Pixel 7a",
"quantity": 3
}'

# Get cart information
curl --request GET \
--url http://localhost:8080/api/v1/carts/2
```

### 3\. Professional Client (Annual Revenue < 10 Million)
   A professional client with lower annual revenue creates an account, adds two mid-range phones to their cart, and retrieves the cart details.

```bash
# Create professional client
curl --request POST \
--url http://localhost:8080/api/v1/clients/professional \
--header 'content-type: application/json' \
--data '{
"legalName": "Tech Innovators",
"vatNumber": "FR98765432109",
"sirenNumber": "12345678901",
"annualRevenue": 5000000
}'

# Add products to cart
curl --request POST \
--url http://localhost:8080/api/v1/carts/3/product \
--header 'content-type: application/json' \
--data '{
"name": "Mid-range Phone",
"quantity": 2
}'

curl --request POST \
--url http://localhost:8080/api/v1/carts/3/product \
--header 'content-type: application/json' \
--data '{
"name": "MacBook Pro",
"quantity": 1
}'

curl --request POST \
--url http://localhost:8080/api/v1/carts/3/product \
--header 'content-type: application/json' \
--data '{
"name": "iPhone 15 Pro Max",
"quantity": 1
}'

# Get cart information
curl --request GET \
--url http://localhost:8080/api/v1/carts/3
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
