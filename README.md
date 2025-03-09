# Appointment Management System

## Overview
The **Appointment Management System** is a Spring Boot application designed to manage appointments efficiently.  
It integrates **JWT authentication** for security, **Stripe API** for payments, and supports **externalized configuration** for credentials and environment-specific settings.

---

## Configuration

### **External Properties File**
The application supports external configuration through a **properties file** stored outside the project.  
This allows sensitive credentials (e.g., database passwords, API keys) to be managed securely.

 **Location:**  
/keys/appt_mgmt_credentials_<profile>.properties Where `<profile>` represents the active Spring profile, such as `desktop`, `dev`, or `prod`.
**Example: `appt_mgmt_credentials_dev.properties`**

---
## **How to Run the Application**
1. **Prerequisites**
    - Ensure **Java 17+** and **MySQL** are installed.
    - Ensure **Maven** is installed (`mvn -version` to check).
    - Ensure the external properties file is placed correctly.

2. **Setup Configuration**
    - Place the **properties file** inside the `/keys/` directory (one level above the project directory).
    - Example path:
      ```
      C:\keys\appt_mgmt_credentials_dev.properties  (Windows)
      /home/user/keys/appt_mgmt_credentials_dev.properties  (Linux/Mac)
      ```

3. **Run the Application**
    - Use Maven to build and start the application with an embedded Tomcat server using Cargo:
      ```sh
      mvn clean install cargo:run
      ```

4. **Access the Application**
    - Generate JWT token using this endpoint:
      ```
      localhost:8080/appointment-mgmt/api/auth/login
      ```
    - The app runs locally at:
      ```
      http://localhost:8080/appointment-mgmt/api/<your-endpoint>
      ```
    - Example API call:
      ```
      http://localhost:8080/appointment-mgmt/api/appointments
      ```
---

##  ** SAMPLE API Endpoints**
| Method | Endpoint                                                 | Description                       |
|--------|----------------------------------------------------------|-----------------------------------|
| `GET`  | `/api/appointments/{appointmentId}`                      | Fetch an appointment by id        |
| `GET`  | `/api/appointments/user/{userId}`                        | Fetch all appointments for a user |
| `POST` | `/api/appointments/initiate-booking?userId=1&slotId=75`  | Initiate a booking for a user     |
| `POST` | `/api/appointments/reschedule`                           | Reschedule an appointment         |

## Example Request (Initiate Booking)
** POST http://localhost:8080/api/appointments/initiate-booking?userId=1&slotId=5

POST http://localhost:8080/api/appointments/reschedule
Content-Type: application/json

{
"appointmentId": 10,
"newSlotId": 7
}

---

## **Support**
If you encounter any issues, open a GitHub issue or contact the development team.




