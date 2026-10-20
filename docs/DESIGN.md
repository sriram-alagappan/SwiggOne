# SwiggOne Design Documentation

## Problem statement
Food delivery services need to maintain a menu, customer details and order information. SwiggOne provides REST APIs to manage these records and calculates the order total from the selected food item's price and quantity.

## Layered architecture

```text
Mobile app / Postman
        |
        v
Controllers
(FoodItemController, CustomerController, FoodOrderController)
        |
        v
Services
(FoodItemService, CustomerService, FoodOrderService)
        |
        v
Repositories
(JpaRepository interfaces)
        |
        v
MySQL database
```

## Entity relationships

```text
Customer 1 -------- many FoodOrder
FoodItem 1 -------- many FoodOrder
```

Each `FoodOrder` refers to one customer and one food item. The same customer can place many orders, and the same food item can appear in many orders.

## Entity fields

### FoodItem
- id: generated primary key
- name: required food name
- category: required category
- price: positive price
- available: whether the food can currently be ordered

### Customer
- id: generated primary key
- name: required customer name
- email: required email address
- phone: required phone number

### FoodOrder
- id: generated primary key
- customer_id: reference to Customer
- food_item_id: reference to FoodItem
- quantity: must be at least one
- total_amount: calculated by the service
- status: order progress

## Validation and error handling
Bean Validation checks required fields, email format, positive prices and quantity. `ResourceNotFoundException` is converted to HTTP 404. Invalid input and unavailable food items are returned as HTTP 400.

## Scope
The current version supports one food item per order. Multiple items per order, login, payment, delivery-partner assignment and a frontend are outside the first version's scope.
