# Getting Started

# Appointment Management System

## Overview
The **Appointment Management System** is a Spring Boot application designed to manage appointments efficiently.  
It integrates **JWT authentication** for security, **Stripe API** for payments, and supports **externalized configuration** for credentials and environment-specific settings.

## 🔧 Configuration

### *External Properties File**
The application supports external configuration through a **properties file** stored outside the project.  
This allows sensitive credentials (e.g., database passwords, API keys) to be managed securely.

**Location:**  
/keys/appt_mgmt_credentials_<profile>.properties
Where `<profile>` represents the active Spring profile, such as `desktop`, `dev`, or `prod`.
### **Example: `appt_mgmt_credentials_dev.properties`**

##  **How to Run**
1. Ensure Java 17+ and MySQL are installed.
2. Place the **properties file** in `/keys/` (one directory above the project).
3. Start the application:
   mvn clean install cargo:run

