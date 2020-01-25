# Oddschecker-assignment

- Logging also contains all the queries that are going to be made.
- Unit and integration tests are included to cover all possible scenarios.
- Data.sql file contains all sql commands for setting up the inMemory DB 
- Basic swagger implementation for exposing the endpoints.

# Technologies
Java 8, Spring boot framework, H2 in memory Data base, gradle.

# Architecture
The project consists of one component
1. Oddschecker microservice

# Deployment
First we need to create the jar for the service. This step requires gradle and java to be installed in our machine.
Execute the bellow command.
```bash
./gradlew clean build
```
Then we can copy the jars created in the build folder to our specific folder and run the commands.
```bash
java -jar oddschecker-0.0.1.jar 
```
After these two commands, we can open a browser and paste 
**http://localhost:8090/swagger-ui.html**.
There we can find all the endpoints along with the responses and the models that we need to use for each one.

# Integration Tests
The application has integration tests for e2e testing of functionality. Run the command bellow to execute those tests.
 ```bash
 ./gradlew itest
 ```

# Postman Collection
https://www.getpostman.com/collections/35acf5b130d76e0e134e

# Static code analysis
For this project, for static analysis we have used the checkstyle plugin. You can find the rules in the etc/checkstyle/rules.xml. A gradle task is also created and the successful build depends on checkstyle outcome.
There is always a report created after each run.

# Docker
The application is dockerized, we have created a Dockerfile that describes the image and the command required. 