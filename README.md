# Datacollection-management-api

Project in POC status
REST API for communication between DB and Coleman-Management UI and Coleman-My-Surveys UI

## Requirements

For building and running the application you need:

- JDK 11
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

- `GET /api/contacts` : Search for a contacts, paginated
- `GET /api/contacts/{id}` : Search for a contact by its identifier
- `PUT /api/contacts/{id}` : Update or create a contact

```json
{
  "identifier": "CONTACT", //NOT NULL
  "externalId": "OTHER",
  "civility": "Mme",
  "lastName": "Powlowski",
  "firstName": "Shaunty",
  "function": "Direct Analyst",
  "email": "shaunte.powlowski@cocorico.fr",
  "phone": "994.107.2000",
  "address": {
    "streetNumber": "30451",
    "streetName": "Fadel River",
    "city": "Tamouth",
    "zipCode": "43059-2000",
    "countryName": "Falkland Islands (Malvinas)"
  }
}
or
{
  "identifier": "CONTACT", //NOT NULL
  "externalId": "OTHER",
  "civility": "Mme",
  "lastName": "Powlowski",
  "firstName": "Shaunty",
  "function": "Direct Analyst",
  "email": "shaunte.powlowski@cocorico.fr",
  "phone": "994.107.2000"
}

```

- `DELETE /api/contacts/{id}` : Delete a contact by its id (also delete ils events and accreditations)
- `GET /api/contacts/{id}/address` : Search for a contact address by the contact id
- `PUT /api/contacts/{id}/address` : Update or create an address by the contact id

```json
  "address": {
    "streetNumber": "30451",
    "streetName": "Fadel River",
    "city": "Tamouth",
    "zipCode": "43059-2000",
    "countryName": "Falkland Islands (Malvinas)"
  }
```

- `GET /api/contacts/{id}/contact-events` : Search for contactEvents by the contact identifier
- `POST /api/contacts/{id}/contact-events` : Create a contactEvent for a contact

```json
{
  "identifier": "CONTACT", //contact identifier NOT NULL
  "eventDate": 1665394364740,
  "type": "create", //only create or update
  "payload": { "anything": "anything" }
}
```

- `DELETE /api/contacts/contact-events/{id}` : Delete a contactEvent by its id

### Questioning domain

- `POST /api/questionings` : create or update a questioning

```json
{
  "surveyUnitId": "111111111", //NOT NULL
  "idPartitioning": "CHICKEN-2022-M12-001", //NOT NULL
  "modelName": "m00" //NOT NULL
}
```

- `GET /api/questionings/{id}` : Search for a questioning by id
- `GET /api/questionings/{id}/questioning-accreditations` Search for questioning accreditations by questioning id
- `POST /api/questionings/{id}/questioning-accreditations` Create or update a questioning accreditation for a questioning

```json
{
  "creationDate": null,
  "creationAuthor": null,
  "idContact": "CONTACT", //NOT NULL
  "main": true //is the main contact for the questioning: true or false
}
```

- `GET /api/questionings/{id}/questioning-events` Search for a questioning event by questioning id
- `POST /api/questionings/questioning-events` Create a questioning event

```json
{
  "questioningId": 1234567,
  "eventDate": 1653090357698,
  "type": "INITLA",
  "payload": null
}
```

- `DELETE /api/questionings/questioning-events` Delete a questioning event
- `GET /api/survey-units` : Search for survey units, paginated
- `PUT /api/survey-units/{id}` : Create or update a survey unit

```json
{
  "idSu": "186552144", //= identificationCode when the survey unit is a company
  "identificationCode": "186552144", //SIRET or SIREN
    //can be null for household survey unit
  "identificationName": "Reinger Inc",  //name of the company
    //can be null for household survey unit
  "address": {
    "streetNumber": "0529",
    "streetName": "Schmidt Terrace",
    "city": "Monahanbury",
    "zipCode": "79370",
    "countryName": "Venezuela"
  }
}
or
{
  "idSu": "186552144",
  "identificationCode": "186552144",
  "identificationName": "Reinger Inc",
 }

```

- `DELETE /api/survey-units/{id}` : Delete a survey unit by its id
- `GET /api/survey-units/{id}/questionings` : Search for questionings by survey unit id

### Metadata domain

:::info

A source may have one or more surveys (e.g. one per year)
A survey can have one or more campaigns (e.g. one per quarter)
A campaign can have one or more partitions (one per month for example)

Example of a source id = CHICKEN
Example of a campaign id= CHICKEN-2022
Example of a campaign id= CHICKEN-2022-T01
Example of a partition id: CHICKEN-2022-M01-000

A source has only one owner (the organisation responsible for the survey)
A source has only one support (dedicated to user support).
An owner can have several sources, a support can also have several sources.

List of owner ids=
"id" : "insee",
"id" : "dares",
"id" : "agri",

List of support ids =
"id": "agri-bsva",
"id": "insee-ssne"
:::

- `GET /api/sources` : Search for sources, paginated
- `GET /api/sources/{id}` : Search for a source by its id
- `PUT /api/sources/{id}` : Update or create a source

```json
{
  "id": "CHICKEN",
  "longWording": "Have you ever heard about CHICKEN ?",
  "shortWording": "Source about CHICKEN",
  "periodicity": "M", //"X": pluriannual, "A": annual, "S":semi-annual, "T": trimestrial, "B": bimonthly, "M":monthly
  "mandatoryMySurveys": false //true if the respondent has to go first to the Coleman-My-Surveys UI, false otherwise
}
```

- `DELETE /api/sources/{id}` : Delete a source, its surveys, campaigns, partitionings, questionings ...
- `GET /api/sources/{id}/surveys` : Search for surveys by the source id
- `GET /api/surveys/{id}` : Search for a survey by its id
- `PUT /api/surveys/{id}` : Update or create a survey

```json
{
  "id": "CHICKEN-2022", //NOT NULL
  "sourceId": "CHICKEN", //NOT NULL
  "year": 2022, //NOT NULL
  "sampleSize": 71296,
  "longWording": "Survey CHICKEN 2022",
  "shortWording": "CHICKEN 2022",
  "shortObjectives": "All about CHICKEN in 2022",
  "longObjectives": "The purpose of this survey is to find out everything you can about CHICKEN. Your response is essential to ensure the quality and reliability of the results of this survey.",
  "visaNumber": "2022IWJYCP",
  "cnisUrl": "http://cnis/CHICKEN2022",
  "diffusionUrl": "http://diffusion/CHICKEN2022",
  "noticeUrl": "http://notice/CHICKEN2022",
  "specimenUrl": "http://specimenUrl/CHICKEN2022",
  "communication": "Communication around CHICKEN2022",
  "mandatory": false
}
```

- `DELETE /api/surveys/{id}` : Delete a survey, its campaigns, partitionings, questionings ...
- `GET /api/surveys/{id}/campaigns` : Search for campaigns by the survey id
- `GET /api/campaigns/{id}` : Search for a campaign by its id
- `PUT/api/campaigns/{id}` : Update or create a campaign

```json
{
  "id": "CHICKEN-2022-M01", //NOT NULL
  "surveyId": "CHICKEN 2022", //NOT NULL
  "year": 2022, //NOT NULL
  "campaignWording": "Campaign about CHICKEN in 2022 and period T01",
  "period": "M01" //NOT NULL
  //(X00, A00, S01, S02, T01, T02, T03, T04, M01, M02, M03, ..., M12, B01, B02, ..., B06)
}
```

- `DELETE /api/campaigns/{id}` : Delete a campaign, its campaigns, partitionings, questionings ...
- `GET /api/campaigns/{id}/partitionings` : Search for partitionings by the campaign id
- `GET /api/partitionings/{id}` : Search for a partitioning by its id
- `PUT /api/partitionings/{id}` : Update or create a partitioning

```json
{
  "id": "CHICKEN-2022-M01-000", //NOT NULL
  "campaignId": "CHICKEN-2022-M01", //NOT NULL
  "status": "open",
  "openingDate": 1649658607374,
  "closingDate": 1672486325330,
  "returnDate": 1650248506148
}
```

- `DELETE /api/partitionings/{id}` : Delete a partitioning, its partitionings, partitionings, questionings ...
- `GET /api/supports ` : Search for supports, paginated
- `GET /api/supports/{id}` : Search for a support by its id
- `PUT /api/supports/{id}` : Update or create a support

```json
{
  "id": "insee-ssne",
  "label": "Insee Normandie - SSNE",
  "phoneNumber": "0100000000",
  "mail": "support2@cocorico.fr",
  "countryName": "France",
  "streetNumber": "2",
  "streetName": "rue de Paris",
  "city": "Paris",
  "zipCode": "75000"
}
```

- `GET /api/owners` : Search for owners, paginated
- `GET /api/owners/{id}` : Search for a owner by its id
- `PUT /api/owners/{id}` : Update or create a owner

```json
{
  "id": "agri",
  "label": "SSM Agriculture",
  "ministry": "de l'Agriculture et de la souveraineté alimentaire",
  "logo": "3"
}
```

- `GET /api/owners/{id}/sources` : Search for surveys by the owner id

### Cross domain

- `GET /api/contacts/search` : Multi-criteria search for contacts - identifier - lastName - firstName - email - idSu - identificationCode - identificationName - source - year - period
- `GET /api/contacts/{id}/accreditations` : Search for contact accreditations
- `GET /api/contacts/{id}/accreditations` : Get contact accreditations by the contact id
- `GET /api/my-questionings/{id}` : Get contact questionings by the contact identifier

### Webclients

- `GET /api/metadata/{id}` : Search for a partitiong and metadata by partitioning id
- `PUT /api/metadata/{id}` : Insert or update a partitiong and metadata by partitioning id

```json
{
  "partitioning": {
    "id": "CHICKEN-2022-M1-000",
    "campaignId": "CHICKEN-2022-M1",
    "status": "open",
    "openingDate": "2022-04-20T16:27:02.745+00:00",
    "closingDate": "2022-09-26T11:35:03.223+00:00",
    "returnDate": "2022-05-26T06:28:27.086+00:00"
  },
  "campaign": {
    "id": "CHICKEN-2022-M1",
    "surveyId": "CHICKEN-2022",
    "year": 2022,
    "campaignWording": "Campaign about CHICKEN in 2022 and period M1",
    "period": "M1"
  },
  "survey": {
    "id": "CHICKEN-2022",
    "sourceId": "CHICKEN",
    "year": 2022,
    "sampleSize": 23716,
    "longWording": "Survey CHICKEN 2022",
    "shortWording": "CHICKEN-2022",
    "shortObjectives": "All about CHICKEN-2022",
    "longObjectives": "The purpose of this survey is to find out everything you can about CHICKEN. Your response is essential to ensure the quality and reliability of the results of this survey.",
    "visaNumber": "2022FOPZPD",
    "cnisUrl": "http://cnis/CHICKEN-2022",
    "diffusionUrl": "http://diffusion/CHICKEN-2022",
    "noticeUrl": "http://notice/CHICKEN-2022",
    "specimenUrl": "http://specimenUrl/CHICKEN-2022",
    "communication": "Communication around CHICKEN-2022",
    "mandatory": false
  },
  "source": {
    "id": "CHICKEN",
    "longWording": "Have you ever heard about CHICKEN ?",
    "shortWording": "Source about CHICKEN",
    "periodicity": "M",
    "mandatoryMySurveys": false
  },
  "owner": {
    "id": "agri",
    "label": "SSM Agriculture",
    "ministry": "de l'Agriculture et de la souveraineté alimentaire",
    "logo": "3"
  },
  "support": {
    "id": "agri-bsva",
    "label": "SSM Agriculture BSVA",
    "phoneNumber": "0100000000",
    "mail": "support3@cocorico.fr",
    "countryName": "France",
    "streetNumber": "1",
    "streetName": "rue de Paris",
    "city": "Paris",
    "zipCode": "75000"
  }
}
```

- `GET /api/questionings` : Get questioning for webclients
- `PUT /api/questionings` : Create or update questioning for webclients

```json
{
  "idPartitioning": "CHICKEN-2022-M1-000",
  "modelName": "test",
  "surveyUnit": {
    "idSu": "999999999",
    "identificationCode": "999999999",
    "identificationName": "cocorico team",
    "address": {
      "streetNumber": "130",
      "streetName": "avenue Kennedy",
      "city": "Lille",
      "zipCode": "59000",
      "countryName": "France"
    }
  },
  "contacts": [
    //only one main contact by questioning
    {
      "identifier": "BETTYBE",
      "externalId": "BETTYBE",
      "main": "false",
      "civility": "Mme",
      "lastName": "Jacobi",
      "firstName": "Charley",
      "function": "Principal Executive",
      "email": "charley.jacobi@cocorico.fr",
      "phone": "493-039-9455 x2852",
      "address": {
        "streetNumber": "8214",
        "streetName": "Gustavo Ranch",
        "city": "East Denice",
        "zipCode": "59000",
        "countryName": "France"
      }
    },
    {
      "identifier": "BETTYB2",
      "externalId": "BETTYB2",
      "main": "true",
      "civility": "Mme",
      "lastName": "Jacobi",
      "firstName": "Charley",
      "function": "Principal Executive",
      "email": "charley.jacobi@cocorico.fr",
      "phone": "493-039-9455 x2852",
      "address": {
        "streetNumber": "8214",
        "streetName": "Gustavo Ranch",
        "city": "East Denice",
        "zipCode": "59000",
        "countryName": "France"
      }
    },
    {
      "identifier": "BETTYB3",
      "externalId": "BETTYB3",
      "main": "false",
      "civility": "Mme",
      "lastName": "Jacobi",
      "firstName": "Charley",
      "function": "Principal Executive",
      "email": "charley.jacobi@cocorico.fr",
      "phone": "493-039-9455 x2852",
      "address": {
        "streetNumber": "8214",
        "streetName": "Gustavo Ranch",
        "city": "East Denice",
        "zipCode": "59000",
        "countryName": "France"
      }
    }
  ]
}
```

- `GET /api/partitionings/{idPartitioning}/survey-units/{idSu}/extract` : Indicates whether a questioning should be extract or not (questioning event in VALINT and PARTIELINT -> true, false otherwise)
- `GET /api/partitionings/{idPartitioning}/survey-units/{idSu}/follow-up` : Indicates whether a questioning should be follow up or not (questioning event in VALINT, VALPAP, REFUSAL, WASTE, HC -> false, true otherwise)
