# 🏦 Bancapp - Banking Application API

A comprehensive RESTful API for a banking system built with Spring Boot. This project demonstrates best practices in backend development, including entity relationships, transaction management, and exception handling.

> **📚 Educational Project:** This application is designed for learning purposes, showcasing Spring Boot architecture, REST API design, and banking domain modeling.

---

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Domain Model](#-domain-model)
- [API Endpoints](#-api-endpoints)
- [Getting Started](#-getting-started)
- [Configuration](#-configuration)
- [Exception Handling](#-exception-handling)
- [Translation Note](#-translation-note)

---

## ✨ Features

### Core Banking Operations
- ✅ **User Management** - Create and manage bank users
- ✅ **Account Management** - Savings and checking accounts
- ✅ **Deposits** - Add funds to accounts
- ✅ **Transfers** - Transfer money between accounts
- ✅ **Service Payments** - Pay utility bills (water, electricity, internet, phone, gas, cable)
- ✅ **Loan Management** - Request and manage loans with status tracking

### Technical Features
- ✅ **RESTful API** - Complete CRUD operations for all entities
- ✅ **Transaction Management** - Atomic operations for financial transactions
- ✅ **Balance Validation** - Prevents overdrafts and invalid transfers
- ✅ **Audit Trail** - Automatic timestamps (createdAt, updatedAt) on all entities
- ✅ **Global Exception Handling** - Consistent error responses
- ✅ **Data Validation** - Bean validation on all inputs
- ✅ **JPA Auditing** - Automated entity tracking

---

## 🛠️ Tech Stack

### Core Technologies
- **Java 17+** - Programming language
- **Spring Boot 3.x** - Application framework
- **Spring Data JPA** - Data persistence
- **Hibernate** - ORM implementation
- **Maven** - Dependency management

### Database
- **H2 Database** (Development) - In-memory database for testing
- **PostgreSQL/MySQL** (Production) - Production-ready databases

### Additional Libraries
- **Lombok** - Reduces boilerplate code
- **Jakarta Validation** - Bean validation
- **Spring Web** - RESTful web services

---

## 📁 Project Structure

```
src/main/java/com/intepy/bancapp/
│
├── config/
│   └── JpaAuditingConfig.java          # JPA auditing configuration
│
├── controllers/                         # REST API endpoints
│   ├── UserController.java
│   ├── AccountController.java
│   ├── LoanController.java
│   ├── BillController.java
│   ├── DepositController.java
│   ├── TransferController.java
│   └── ServicePaymentController.java
│
├── dto/
│   └── ErrorResponse.java              # Standardized error responses
│
├── entities/                            # Domain models
│   ├── User.java
│   ├── Account.java
│   ├── Loan.java
│   ├── Service.java
│   ├── Deposit.java
│   ├── Transfer.java
│   ├── ServicePayment.java
│   └── enums/
│       ├── AccountType.java            # SAVINGS, CHECKING
│       ├── LoanStatus.java             # APPROVED, PENDING, REJECTED
│       └── ServiceType.java            # WATER, ELECTRICITY, INTERNET, etc.
│
├── exceptions/                          # Custom exceptions
│   ├── EntityNotFoundException.java
│   ├── InsufficientBalanceException.java
│   ├── InvalidTransferException.java
│   ├── ValidationException.java
│   └── GlobalExceptionHandler.java     # Centralized exception handling
│
├── repositories/                        # Data access layer
│   ├── UserRepository.java
│   ├── AccountRepository.java
│   ├── LoanRepository.java
│   ├── ServiceRepository.java
│   ├── DepositRepository.java
│   ├── TransferRepository.java
│   └── ServicePaymentRepository.java
│
└── services/                            # Business logic layer
    ├── UserService.java
    ├── AccountService.java
    ├── LoanService.java
    ├── BillService.java
    ├── DepositService.java
    ├── TransferService.java
    └── ServicePaymentService.java
```

---

## 🗄️ Domain Model

### Entity Relationships

```
User (1) ────── (*) Account
  │
  └────── (*) Loan

Account (1) ────── (*) Deposit
   │
   ├────── (*) Transfer (as source)
   │
   ├────── (*) Transfer (as destination)
   │
   └────── (*) ServicePayment

Service (1) ────── (*) ServicePayment
```

### Key Entities

**User**
- `id` - Unique identifier
- `name` - User's full name
- `email` - Contact email
- `accounts` - List of bank accounts
- `loans` - List of loan applications

**Account**
- `id` - Unique identifier
- `number` - Account number
- `balance` - Current balance
- `accountType` - SAVINGS or CHECKING
- `user` - Account owner
- `deposits` - Transaction history
- `outgoingTransfers` - Sent transfers
- `incomingTransfers` - Received transfers
- `payments` - Service payments

**Transfer**
- `id` - Unique identifier
- `amount` - Transfer amount
- `sourceAccount` - Origin account
- `destinationAccount` - Target account
- Validates: sufficient balance, different accounts

**Loan**
- `id` - Unique identifier
- `amount` - Loan amount
- `status` - APPROVED, PENDING, or REJECTED
- `user` - Loan applicant

---

## 🌐 API Endpoints

### Base URL
```
http://localhost:8080/api
```

### Users
```http
GET    /api/users          # List all users
GET    /api/users/{id}     # Get user by ID
POST   /api/users          # Create new user
PUT    /api/users/{id}     # Update user
DELETE /api/users/{id}     # Delete user
```

### Accounts
```http
GET    /api/accounts          # List all accounts
GET    /api/accounts/{id}     # Get account by ID
POST   /api/accounts          # Create new account
PUT    /api/accounts/{id}     # Update account
DELETE /api/accounts/{id}     # Delete account
```

### Deposits
```http
GET    /api/deposits          # List all deposits
GET    /api/deposits/{id}     # Get deposit by ID
POST   /api/deposits          # Create deposit (adds to balance)
PUT    /api/deposits/{id}     # Update deposit
DELETE /api/deposits/{id}     # Delete deposit (reverts balance)
```

### Transfers
```http
GET    /api/transfers          # List all transfers
GET    /api/transfers/{id}     # Get transfer by ID
POST   /api/transfers          # Create transfer
PUT    /api/transfers/{id}     # Update transfer
DELETE /api/transfers/{id}     # Delete transfer (reverts transaction)
```

### Service Payments
```http
GET    /api/payments          # List all payments
GET    /api/payments/{id}     # Get payment by ID
POST   /api/payments          # Make payment
PUT    /api/payments/{id}     # Update payment
DELETE /api/payments/{id}     # Delete payment (refunds account)
```

### Bills (Utility Services)
```http
GET    /api/bills          # List available services
GET    /api/bills/{id}     # Get service by ID
POST   /api/bills          # Create new service
PUT    /api/bills/{id}     # Update service
DELETE /api/bills/{id}     # Delete service
```

### Loans
```http
GET    /api/loans          # List all loans
GET    /api/loans/{id}     # Get loan by ID
POST   /api/loans          # Request new loan
PUT    /api/loans/{id}     # Update loan (approve/reject)
DELETE /api/loans/{id}     # Delete loan
```

---

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- (Optional) PostgreSQL or MySQL for production

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/bancapp.git
   cd bancapp
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the API**
   ```
   http://localhost:8080/api
   ```

### Quick Test

Create a user:
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com"
  }'
```

---

## ⚙️ Configuration

### application.properties

**Development (H2 Database):**
```properties
# H2 Database
spring.datasource.url=jdbc:h2:mem:bancappdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# H2 Console (for debugging)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

**Production (PostgreSQL):**
```properties
# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/bancappdb
spring.datasource.username=your_username
spring.datasource.password=your_password

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

---

## ⚠️ Exception Handling

The API returns standardized error responses:

### Error Response Format
```json
{
  "status": 404,
  "message": "User not found with id: 123",
  "timestamp": "2025-10-15T20:45:00"
}
```

### HTTP Status Codes

| Code | Description | Example |
|------|-------------|---------|
| `200` | OK | Successful GET, PUT operations |
| `201` | Created | Successful POST (entity created) |
| `204` | No Content | Successful DELETE |
| `400` | Bad Request | Validation error, insufficient balance |
| `404` | Not Found | Entity not found |
| `500` | Internal Server Error | Unexpected server error |

### Custom Exceptions

- **EntityNotFoundException** - When requested entity doesn't exist
- **InsufficientBalanceException** - When account balance is too low
- **InvalidTransferException** - When transfer validation fails
- **ValidationException** - When input validation fails

---

## 📚 Learning Objectives

This project demonstrates:

1. **Spring Boot Architecture**
   - Layered architecture (Controller → Service → Repository)
   - Dependency injection
   - Configuration management

2. **RESTful API Design**
   - Resource-based URLs
   - Proper HTTP methods
   - Status codes
   - CRUD operations

3. **JPA & Hibernate**
   - Entity relationships (@OneToMany, @ManyToOne)
   - Cascading operations
   - JPA auditing
   - Query methods

4. **Transaction Management**
   - @Transactional annotations
   - Atomic operations
   - Rollback handling

5. **Exception Handling**
   - Global exception handler
   - Custom exceptions
   - Consistent error responses

6. **Validation**
   - Bean Validation (Jakarta)
   - Custom validators
   - Error message handling

7. **Business Logic**
   - Balance management
   - Transfer validation
   - Transaction reversal

---

## 🔄 Translation Note

This project was originally developed in Spanish and has been professionally translated to English to make it more accessible to international developers. All code, comments, API endpoints, and documentation are now in English while maintaining 100% of the original functionality.

**Translation includes:**
- ✅ All class and method names
- ✅ All variable names
- ✅ All API endpoints
- ✅ All validation and error messages
- ✅ All code comments

---

## 📖 API Documentation

For detailed API documentation, you can:

1. **Import to Postman**: Create a collection with the endpoints listed above
2. **Use Swagger/OpenAPI**: Add SpringDoc dependency for interactive documentation
3. **Manual Testing**: Use curl or any HTTP client

### Example Requests

**Create Account:**
```json
POST /api/accounts
{
  "number": "1234567890",
  "balance": 1000.00,
  "accountType": "SAVINGS",
  "user": {
    "id": 1
  }
}
```

**Make Transfer:**
```json
POST /api/transfers
{
  "amount": 100.00,
  "sourceAccount": {
    "id": 1
  },
  "destinationAccount": {
    "id": 2
  }
}
```

**Pay Service:**
```json
POST /api/payments
{
  "amount": 50.00,
  "account": {
    "id": 1
  },
  "service": {
    "id": 1
  }
}
```

---

## 🎯 Key Features Explained

### 1. Automatic Balance Management
When you create a deposit, the account balance is automatically updated. When you delete a deposit, the balance is reverted.

### 2. Transfer Validation
The system validates:
- ✅ Source account has sufficient balance
- ✅ Source and destination are different accounts
- ✅ Both accounts exist

### 3. Transaction Reversal
Deleting a transfer or payment automatically reverts the balance changes, maintaining data integrity.

### 4. Audit Trail
All entities have `createdAt` and `updatedAt` timestamps that are automatically managed by JPA auditing.

---

## 🤝 Contributing

This is an educational project. Feel free to:
- Fork the repository
- Add new features
- Improve documentation
- Report issues
- Submit pull requests

---

## 📝 License

This project is for educational purposes. Feel free to use it for learning and teaching.

---

## 👨‍💻 Author

**Intepy**

---

## 🙏 Acknowledgments

- Built with Spring Boot
- Designed for educational purposes
- Translated to English for international accessibility

---

## 📬 Contact

For questions about this educational project, please open an issue on GitHub.

---

**Happy Learning! 🚀**
