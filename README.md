# MEDI-STOCK
### Integrated Medical Inventory Intelligence Platform for Stock and Supplier Management

MEDI-STOCK is a web-based medical inventory management platform designed to simplify medicine stock tracking, supplier management, inventory monitoring, and reporting.

## Features

- **Dashboard:** View inventory statistics and stock summaries.
- **Medicine Management:** Add, update, search, and manage medicine records.
- **Stock Tracking:** Monitor available quantities and identify low-stock medicines.
- **Supplier Management:** Maintain supplier information and manage supplier records.
- **Analytics and Reports:** View inventory insights to support better stock management.
- **Authentication and Security:** Secure access through authentication and role-based authorization.
- **REST API Integration:** Connect frontend services with backend APIs.

## Technology Stack

- **Backend:** Java, Spring Boot, Maven
- **Frontend:** React and JavaScript (update if your actual frontend differs)
- **Database:** Configure according to your project setup
- **API:** REST APIs
- **Deployment:** Render (if deployed using the included configuration)

## Project Structure

```text
MEDI-STOCK/
├── api/
├── backend/
├── .github/
├── .mvn/
├── mvnw
├── mvnw.cmd
├── pom.xml
├── render.yaml
└── README.md
```

## Getting Started

### Prerequisites

- Java Development Kit (JDK)
- Maven or the included Maven Wrapper
- Node.js and npm, if required by the frontend
- A configured database

### Run the Backend

1. Clone the repository:

   ```bash
   git clone https://github.com/ARUNSANKARB/MEDI-STOCK.git
   ```

2. Open the project directory:

   ```bash
   cd MEDI-STOCK
   ```

3. Run the backend using the Maven Wrapper:

   **Windows:**

   ```bash
   mvnw.cmd spring-boot:run
   ```

   Run this command from the directory containing the relevant `pom.xml`. If the backend has a separate Maven project, use its directory instead.

4. Configure the required database and environment variables before running the application.

## Security

Never commit passwords, database credentials, API keys, or `.env` files containing secrets.

## Project Status

Developed as an inventory management project, with medicine stock tracking, supplier management, and analytics functionality.

## Author

Arun Sankar B.
