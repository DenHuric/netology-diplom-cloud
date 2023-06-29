
# Diploma project "Cloud"

This is my graduation project completed while studying Java developer courses at [Netology](https://netology.ru).

### Project description
The task is to develop a REST service. The service must provide a REST interface for uploading files and listing the user's files that have already been uploaded.

All requests to the service must be authorized. A pre-prepared web application (FRONT) should connect to the developed service without modifications, and also use the FRONT functionality for authorization, loading and listing user files.

### Application Requirements
The service must provide a REST interface for integration with FRONT.
The service must implement all the methods described in the yaml file:
- Output a list of files.
- Adding a file.
- Deleting a file.
- Authorization.
All settings must be read from the settings file (yml).
Information about users of the service (logins for authorization) and data must be stored in a database (at the student's choice).

### Implementation Requirements
- The application is developed using Spring Boot.
- Used package builder gradle/maven.
- It uses docker, docker-compose to run.
- The code is hosted on Github.
- The code is covered by unit tests using mockito.
- Added integration tests using testcontainers.


## Technologies
To write the backend, I used the following technologies:
- MYSQL for storing user data.
- JPA and Hibernate
- Spring Boot, Spring Web
- JUnit and Testcontainers
- Docker compose

## Deployment

To deploy this project run 2 commands.
mv in directory netology-diplom-backend and run: 

```bash
  gradle build
  docker compode up
```


## Authors

- [@DenHuric](https://www.github.com/DenHuric)


## Documentation

[Documentation](https://github.com/netology-code/jd-homeworks/blob/master/diploma/cloudservice.md)

