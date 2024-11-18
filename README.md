# RiskNarrative Company Search API

### Introduction

A Restful web-service application that provides functionality to support company search and officer search activities:
-

- search

#### Pre-requisites

- Java 17
- Maven


### Run locally

Application can be run in below multiple ways

```bash
java -jar target/company-search-exercise-1.0.jar
```

```
mvn spring-boot:run
```

````
Open the project in any IDE, and run the CompanyApplication.class
````
## General Assumptions/Considerations:
In order to achieve the functionality of a company search service in the given stipulated time, following assumptions and considerations are made.

1. Use of H2 in-memory database to persist the companies (by company_number) and its officers and addresses in a database 
and return the result from there if the endpoint is called with companyNumber (Bonus task). As a result of this task Companies, 
Officers and Addresses tables will be created in the database along with the data returned from endpoint.
2. Data Validations are in place on the apis.
3. SOLID patterns used across.
4. Proper error handling and appropriate HTTP status codes used.
5. Simple unit tests with mockito and integration test with wiremock have been added.
6. DTO and Domain objects segregation.
7. MapStruct is used for mapping to/from request payloads to domain objects.
8. Design patterns like Facade pattern, Strategy pattern and Builder pattern have been used
9. active flag needs to be added to request parameter to retrieve active companies/officers

## Request Payloads and Requests:

Once the application is run locally (http://localhost:8080), you can test the application using the request payload located below.

    http://localhost:8080/search

Following is sample body with companyname and coompanynumber. API key has to be included as part of the request header.

{
"companyName" : "BBC LIMITED",
"companyNumber" : "06500244"
}






