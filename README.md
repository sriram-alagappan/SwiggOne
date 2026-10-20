# SwiggOne - Food Delivery Management System

## Project purpose
SwiggOne is a beginner-friendly Spring Boot REST API for a food delivery service. It manages food menu items, customers and orders. A customer can place an order for one menu item with a chosen quantity. The total is calculated by the application using the current food item price.

## Technology stack
- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- Jakarta Bean Validation
- Maven

## Package structure
- `controller`: receives REST requests and returns responses.
- `service`: contains application rules and calculations.
- `repository`: reads/writes database records using JpaRepository.
- `model`: entity classes mapped to database tables.
- `dto`: request data used when placing or updating an order.
- `exception`: custom not-found exception and shared error responses.

## Database setup
1. Start MySQL.
2. Run `CREATE DATABASE swiggone_db;`
3. Edit `src/main/resources/application.properties`.
4. Replace `YOUR_MYSQL_PASSWORD` with your local MySQL password.
5. Do not put real passwords in a public Git repository. For final submission, use a local ignored configuration or environment variables.

## Run
From the project folder, run:

```bash
mvn spring-boot:run
```

The API base address is `http://localhost:8080`.

## API documentation

### Food items

| Method | URL | Request body | Purpose |
|---|---|---|---|
| GET | `/api/food-items` | None | List food items |
| GET | `/api/food-items/{id}` | None | Get one food item |
| POST | `/api/food-items` | Food JSON | Add a food item |
| PUT | `/api/food-items/{id}` | Food JSON | Update a food item |
| DELETE | `/api/food-items/{id}` | None | Delete a food item |

Example food JSON:
```json
{
  "name": "Veg Burger",
  "category": "Burger",
  "price": 99.0,
  "available": true
}
```

### Customers

| Method | URL | Request body | Purpose |
|---|---|---|---|
| GET | `/api/customers` | None | List customers |
| GET | `/api/customers/{id}` | None | Get one customer |
| POST | `/api/customers` | Customer JSON | Register a customer |
| PUT | `/api/customers/{id}` | Customer JSON | Update customer details |
| DELETE | `/api/customers/{id}` | None | Delete a customer |

Example customer JSON:
```json
{
  "name": "Arun Kumar",
  "email": "arun@example.com",
  "phone": "9876543210"
}
```

### Orders

| Method | URL | Request body | Purpose |
|---|---|---|---|
| GET | `/api/orders` | None | List orders |
| GET | `/api/orders/{id}` | None | Get one order |
| POST | `/api/orders` | Order request JSON | Place an order |
| PUT | `/api/orders/{id}` | Order request JSON | Update an order |
| DELETE | `/api/orders/{id}` | None | Delete an order |

Example order JSON (IDs must already exist):
```json
{
  "customerId": 1,
  "foodItemId": 1,
  "quantity": 2,
  "status": "PLACED"
}
```

Allowed statuses: `PLACED`, `PREPARING`, `OUT_FOR_DELIVERY`, `DELIVERED`, `CANCELLED`.

The application calculates `totalAmount` as food item price multiplied by quantity. Do not send `totalAmount` from the client.

## Common response codes
- `200 OK`: successful GET or update.
- `201 Created`: record created.
- `204 No Content`: record deleted.
- `400 Bad Request`: invalid input or unavailable food item.
- `404 Not Found`: requested customer, food item or order does not exist.

## Request flow
Client/Postman -> Controller -> Service -> Repository -> MySQL.

## Current scope and limitation
An order currently contains one food item and a quantity. A full shopping cart with multiple items per order can be added as an extension if time permits. This version does not implement payment, live delivery tracking, authentication or a frontend.

## Before submission
- Configure the local database password.
- Run the application and test all endpoints in Postman.
- Add screenshots or export a Postman collection.
- Make sure the code and documentation match.
- Be ready to explain every annotation and method in the viva.
