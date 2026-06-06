#  Enterprise Library Management System

A production-grade, desktop-based **Library Management System** built with **Java Swing**, **JDBC**, and **PostgreSQL**. This project utilizes the **Model-View-Controller (MVC)** architectural pattern to handle high-integrity data flows, transactional multi-table updates, and automated overdue fine calculations.

---

##  Key Features

*   **Secure Book Issuing**: Implements multi-step database operations to verify asset availability, log transaction histories, and update asset statuses.
*   **Automated Returns Processing**: Closes active checkout records with live timestamps and updates inventory availability seamlessly.
*   **Dynamic Fine Calculation Engine**: Uses Java’s modern `java.time` API to calculate overdue days and assess penalties on late returns automatically.
*   **ACID-Compliant Transactions**: Explicitly manages transaction boundaries (`setAutoCommit(false)`, `commit()`, and `rollback()`) to ensure no partial updates or orphaned records occur if an operational step fails.
*   **Responsive Desktop Interface**: Built with a clean, grid-aligned Java Swing UI structured cleanly via a tabbed dashboard component.

---

##  Project Architecture & Design Pattern

The application strictly follows the **MVC (Model-View-Controller)** separation of concerns to maximize modularity and maintainability:

```text
src/
├── config/       # Core database configuration factories (DatabaseUtil.java)
├── models/       # Data representation classes / Entities (Book, IssueRecord)
├── repository/   # Data Access Object (DAO) layer running parameterized SQL
├── service/      # Core transactional engine & business logic coordinator
└── ui/           # Modular Java Swing desktop screen layouts & event loops
```

---

##  Tech Stack & Prerequisites

*   **Language**: Java (JDK 8 or higher)
*   **Database**: PostgreSQL (v12+)
*   **GUI Framework**: Java Swing
*   **Driver**: PostgreSQL JDBC Driver
*   **IDE**: IntelliJ IDEA

---

##  Database Installation & Schema Setup

1. Open your PostgreSQL terminal (`psql`) or **pgAdmin** query panel.
2. Initialize the database schema by executing the creation sequence below:

```sql
CREATE DATABASE library_db;

-- Connect to your library_db container before running the code below
CREATE TABLE books (
    book_id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    status VARCHAR(20) DEFAULT 'AVAILABLE'
);

CREATE TABLE members (
    member_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE issued_books (
    issue_id SERIAL PRIMARY KEY,
    book_id INT REFERENCES books(book_id) ON DELETE CASCADE,
    member_id INT REFERENCES members(member_id) ON DELETE CASCADE,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE NULL,
    fine_amount DECIMAL(6,2) DEFAULT 0.00
);

-- Seed initial test values
INSERT INTO books (title, status) VALUES ('Clean Code', 'AVAILABLE'), ('Effective Java', 'AVAILABLE'), ('Design Patterns', 'AVAILABLE');
INSERT INTO members (name) VALUES ('Rahul Sharma'), ('Ananya Patel');
```

---

##  Setup & Execution Instructions

### 1. Project Import
* Clone this repository to your local system.
* Launch **IntelliJ IDEA** and choose **Open**, selecting the root `library-management-system` folder.

### 2. Configure Database Credentials
* Open `src/config/DatabaseUtil.java`.
* Update the `USER` and `PASSWORD` string variables to match your local PostgreSQL server configuration settings:
  ```java
  private static final String USER = "postgres"; 
  private static final String PASSWORD = "your_secure_password"; 
  ```

### 3. Add the JDBC Library Driver
* Download the official driver jar from the [PostgreSQL JDBC Website](https://postgresql.org).
* In IntelliJ, navigate to **File** ➡️ **Project Structure** ➡️ **Libraries**.
* Click the **+** (New Project Library) icon, target your downloaded `.jar` file, and apply the structural path.

### 4. Run the Engine
* Open `src/Main.java`.
* Right-click anywhere in the editor panel and select **Run 'Main.main()'**.

---

##  Robust Security & Defensive Design Choices

*   **Defensive SQL Parameter Injection Safeguards**: All raw data inputs extracted from UI fields are injected into database boundaries utilizing **`PreparedStatement`** bindings, entirely blocking malicious SQL-injection attack methods.
*   **Fail-Safe Transactions**: Uses a strict global rollback method. If a network disruption occurs mid-checkout, the database automatically drops uncommitted updates to keep accounts error-free.
*   **Defensive Input Verification**: Form buttons filter inputs with robust exception blocks (`try-catch` structures handling `NumberFormatException`) to prevent application crashes caused by improper alphabetic keystroke inputs.
