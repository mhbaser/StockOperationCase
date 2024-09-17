
# Stock Management System

This project provides a stock management system that allows employees to create, list, and cancel stock orders on behalf of customers. Additionally, customers can manage their assets and perform deposit/withdrawal transactions in TRY.

## Features

- Deposit and withdraw money for customers
- List customer assets
- Create, list, and cancel stock orders
- Perform transactions in TRY (deposit/withdraw)
- Admin privileges for modifying order statuses


### Installation

Clone the project by running the following command in your terminal:
`git clone https://github.com/mhbaser/StockOperationCase.git`

Then navigate to the project directory:

`cd stock-management`

Install the required dependencies using 
Maven: `mvn clean install`

Start the database (if using H2 database). The database URL will be:
`jdbc:h2:mem:testdb`
Run the application using the following command:
`mvn spring-boot:run`
You can access the application at:
`http://localhost:8081`


### Usage

#### Deposit Money

To deposit money, you can send a **POST** request to `/asset/deposit` with the following JSON body:

```
{
    "customerId": 1,
    "amount": 100,
    "assetName": "TRY"
}
```

#### Create Stock Order
To create a stock order, send a **POST** request to `/order` with the following JSON body:

```
{
    "customerId": 1,
    "assetName": "BTC",
    "orderSide": "BUY",
    "size": 1.5,
    "price": 40000
}
```


#### List Customer Orders
To list customer orders, send a **GET** request to `/order` with optional query parameters such as customerId, startDate, and endDate.

#### Withdraw Money
To withdraw money, send a **POST** request to `/asset/withdraw` with the following JSON body:

```
{
    "customerId": 1,
    "amount": 50,
    "assetName": "TRY"
}
```

#### Cancel Pending Order

To cancel a pending order, send a **DELETE** request to `/order/{orderId}`. Only orders with a status of PENDING can be canceled.


### Testing

To run the unit and integration tests, use the following command in your terminal:

`mvn test`

The project uses JUnit and Mockito for unit testing.

API Documentation

The Swagger API documentation is available at:

`http://localhost:8081/swagger-ui.html`

### Dependencies

The project uses the following dependencies:

- Spring Boot
- Spring Data JPA
- H2 Database
- Spring Security
- JUnit 5
- Mockito
- Contributing