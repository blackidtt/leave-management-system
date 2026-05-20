# Leave Management System

## Requirements

- Java 17
- PostgreSQL
- Maven

## Run Project

```bash
mvn clean install
mvn spring-boot:run
```

## Access Swagger
/swagger-ui/index.html


## API

### API Mengajukan Cuti
POST /api/leaves

### API Melihat History Cuti Sendiri
POST /api/leaves/history

### API Approve
POST /api/leaves/approve

### API Reject
POST /api/leaves/reject

### API Melihat Seluruh Data
POST /api/leaves/all
