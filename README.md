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

The war will be generate in `/target` repository

### 2. Install tomcat and deploy war

To deploy the war file in Tomcat, you need to :
Download Apache Tomcat and unpackage it into a tomcat folder  
Copy your WAR file from target/ to the tomcat/webapps/ folder

### 3. Tomcat config

Before to startup the tomcat server, some configurations are needed :

#### External Properties file

Create pearljambo.properties near war file and complete the following properties:

```shell

```

#### External log file

Create log4j2.xml near war file and define your external config for logs.

### 4. Tomcat start

From a terminal navigate to tomcat/bin folder and execute

```shell
catalina.bat run (on Windows)
```

```shell
catalina.sh run (on Unix-based systems)
```

### 5. Application Access

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

### Cross domain

- `GET /api/contacts/search` : Search contacts
