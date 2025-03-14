# Appointment Management System

The Appointment Management System is a Spring Boot application designed for efficient appointment scheduling and management. It includes:
- JWT authentication for secure access control 
- Stripe API integration for seamless payment processing 
- Externalized configuration to securely manage sensitive credentials and environment-specific settings

Secure External Configuration
The application loads sensitive credentials (e.g., database passwords, API keys) from an external properties file, ensuring security and flexibility.
This properties file is stored outside the project directory to prevent accidental exposure in version control.

To clone the app to local computer: git clone https://github.com/rmaheto/appointment-mgmt.git

---
## Build/Installation
Prerequisites
- Ensure **Java 17+** and **MySQL** are installed.
- Ensure **Maven** is installed (`mvn -version` to check).
- Update Environment Variable in Windows Command Prompt 
- Set environment variable for encryption and decryption permanently with this command: 

```
  - Windows: 
    setx ENCRYPTION_SECRET_KEY {placeSecretHere}
    
  - Mac/Linux: Depending on the shell you're using, add the following line to the corresponding file
    echo 'export ENCRYPTION_SECRET_KEY={placeHolderForSecret}' >> ~/.bashrc
    source ~/.bashrc
    
    echo 'export ENCRYPTION_SECRET_KEY={placeHolderForSecret}' >> ~/.bash_profile
    source ~/.bash_profile
    
    echo 'export ENCRYPTION_SECRET_KEY={placeHolderForSecret}' >> ~/.zshrc
    source ~/.zshrc

 ```

- properties file (file containing Passwords) inside the `/keys/` directory (one level above the project directory).
Location To Place propertiesFile
- /keys/appt_mgmt_credentials_<profile>.properties Where `<profile>` represents the active Spring profile, such as `desktop`, `dev`, or `prod`.
- C:\keys\appt_mgmt_credentials_desktop.properties  (Windows)

## Running the Application locally
To start Tomcat: clean install cargo:run
Debugging port: 8001

## Accessing the Application
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

## SAMPLE API Endpoints
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




