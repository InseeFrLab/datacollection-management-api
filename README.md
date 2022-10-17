# Datacollection-management-api

Project in POC status
REST API for communication between DB and Coleman-Management UI and Coleman-My-Surveys UI

## Requirements

For building and running the application you need:

- JDK 17
- Maven 3

## Install and excute unit tests

Use the maven clean and maven install

```shell
mvn clean install
```

## Running the application locally

Use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

## Application Accesses locally

To access to swagger-ui, use this url : [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Deploy application on Tomcat server

### 1. Package the application

Use the [Spring Boot Maven plugin] (https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn clean package
```

The jar will be generate in `/target` repository

### 2. Tomcat start

From a terminal navigate to tomcat/bin folder and execute

```shell
catalina.bat run (on Windows)
```

```shell
catalina.sh run (on Unix-based systems)
```

### 3. Application Access

To access to swagger-ui, use this url : [http://localhost:8080/swagger-ui.html](http://localhost:8080/pearljam/swagger-ui.html)

## Before you commit

Before committing code please ensure,  
1 - README.md is updated  
2 - A successful build is run and all tests are sucessful  
3 - All newly implemented APIs are documented  
4 - All newly added properties are documented

## End-Points

### Contact domain

- `GET /api/contacts/{id}` : Search for a contact by its identifier
- `PUT /api/contacts/{id}` : Update or create a contact
- `DELETE /api/contacts/{id}` : Delete a contact
- `GET /api/contacts` : Search for contacts, paginated
- `GET /api/contacts/{id}/address` : Search for a contact address by the contact id
- `PUT /api/contacts/{id}/address` : Update or create an address by the contact id
- `GET /api/contacts/{id}/contact-events` : Search for contactEvents by the contact identifier
- `POST /api/contacts/{id}/contact-events` : Create a contactEvent
- `DELETE /api/contacts/contact-events/{id}` : Delete a contactEvent

### Questioning domain

- `POST /api/questionings` : ereate or update a questioning
- `PUT /api/questionings/{id}` : Search for a questioning by id
- `GET /api/questionings/{id}/questioning-accreditations` Search for questioning accreditations by questioning id
- `POST /api/questionings/{id}/questioning-accreditations` Create or update a questioning accreditation for a questioning
- `GET /api/questionings/{id}/questioning-events` Search for a questioning event by questioning id
- `POST /api/questionings/questioning-events` Create a questioning event
- `DELETE /api/questionings/questioning-events` Delete a questioning event
- `GET /api/survey-units` : Search for survey units, paginated
- `PUT /api/survey-units/{id}` : Create or update a survey unit
- `GET /api/survey-units/{id}/questionings` : Search for questionings by survey unit id

### Cross domain

- `GET /api/contacts/search` : Multi-criteria search for contacts
- `GET /api/contacts/{id}/accreditations` : Search for contact accreditations
