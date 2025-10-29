# Stellar payments

## Focus of this project

- Java Project using Spring Boot to facilitate packaging and running in standalone mode.
- This project has no UI
- The responses of the endpoint calls are returned formatted as a JSON

## Usage
1. In the same directory as the JAR file:
    ```bash
    java -jar stellar-payments.jar
    ```
2. This will start the application (port 8080). After it's running, you can send requests to http://localhost:8080/payments using curl command or a API Request Client (like Bruno or Postman). Here's an example below:
    ```bash