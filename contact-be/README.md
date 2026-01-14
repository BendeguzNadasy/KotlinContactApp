# Contact App - Backend

This project is a RESTful backend application built with Spring Boot and Kotlin. 
It provides a complete CRUD (Create, Read, Update, Delete) interface for managing contacts, 
including support for profile image uploads.

The application uses an embedded SQLite database for data persistence and the local file system for 
storing uploaded images, making it fully portable and easy to review locally.

## Tech Stack

* **Language:** Kotlin
* **Framework:** Spring Boot 4.0.1
* **Database:** SQLite (Embedded file-based DB)
* **ORM:** Spring Data JPA (Hibernate)
* **API Documentation:** OpenAPI 3 (SpringDoc / Swagger UI)
* **Build Tool:** Gradle (Kotlin)

## Getting Started

### Prerequisites
* JDK 17 or higher installed.

### How to Run
Open a terminal in the project root directory and run the following command:

**Linux / Mac:**
```bash
./gradlew bootRun
```

### Once the application starts:

The server will be available at http://localhost:8080.

The database file contact-app.db will be created automatically in the root folder.

The uploads/ directory for images will be created automatically.

### API Documentation (Swagger UI)
Interactive API documentation is available via Swagger UI. 
You can test all endpoints directly from the browser:
http://localhost:8080/swagger-ui/index.html

## Project Structure

src/main/kotlin/io/github/bendeguznadasy/contact    
├── 📂 config    
├── 📂 controller    
├── 📂 model            
│    └── 📂 dto   
├── 📂 repository    
└── 📂 service

## Database (SQLite)
**File:** The database is stored in contact-app.db in the project root.

**Schema:** The table structure is defined in src/main/resources/schema.sql.

**Initialization:** The application automatically runs the schema script on startup to ensure the contacts table exists.