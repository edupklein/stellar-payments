# Stellar payments

## Focus of this project

- Java Project using Spring Boot to facilitate packaging and running in standalone mode.
- This project has no UI
- The responses of the endpoint calls are returned formatted as a JSON
- This project uses Testnet connection from Stellar environment server

## Considerations and Time spent
- This application took around 3 hours to complete and test.
- For the GET /payments endpoint, I limited the recent payment list to 100 recent records, however this could be improved further using pagination 
- For the Notification Service, it was done all inside the same application (as requested) using new Threads to listen to the 'stream' method from PaymentsRequestBuilder from Stellar Java SDK.
  - This could be improved further by separating this role into separate applications.
  - Currently, this Message Service only prints out the new payments in the stdout. 
  - In a production-grade architecture, I would decouple this service from the main API using a message broker (Kafka, RabbitMQ) or Redis Pub/Sub.  
  - The payment listener would publish events to the broker, and one or more consumer services (like a WebSocket gateway or analytics service) could subscribe to those topics.
  - This approach improves scalability, resilience, and allows multiple systems to react to new payments (notifications, dashboards, audits, etc.).
  - Lastly, I'd also add proper reconnection logic and retry handling to the listener in case the connection with Horizon is temporarily lost.
  
## Usage
1. First, there needs to exist an environment variable with the Main Account Secret.
   1. Linux / macOS:
      ```bash
      export MAIN_ACCOUNT_SECRET_KEY="SXXXXXXX..."
      ```
   2. Windows:
      ```powershell
      setx MAIN_ACCOUNT_SECRET_KEY "SXXXXXXX..."
      ```
1. In the same directory as the JAR file, run the application:
    ```bash
    java -jar stellar-payments.jar
    ```
2. This will start the application (port 8080). After it's running, you can send requests to http://localhost:8080/payments using curl command or a API Request Client (like Bruno or Postman). Here's a list of all the available endpoints:
   1. Create new payment (change field "toAddress" to include destination address and "amount" for the amount):
      ```bash
      curl --request POST \
      --url http://localhost:8080/payments \
      --header 'content-type: application/json' \
      --data '{
      "toAddress": "PXXXXXXXX...",
      "amount": 1.0
      }'
      ```
   2. Show a list of recent 100 payments to and from the Main Address:
      ```bash
      curl --request GET \
      --url http://localhost:8080/payments \
      --header 'content-type: application/json'
      ```
   3. Start the notification service to show new payments done to anf from the Main Address:
      ```bash
      curl --request GET \
      --url http://localhost:8080/payments/notifications/start
      ```
   4. Stop the notification service:
      ```bash
      curl --request GET \
      --url http://localhost:8080/payments/notifications/stop
      ```
   5. Shutdown the whole application:
      ```bash
      curl --request POST \
      --url http://localhost:8080/shutdown
      ```