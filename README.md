# Spring SQL pratice
Pratice or example of using Spring Data, Hibernate to query a MySql database, used redis to cache the result.

## Getting Started
mvn spring boot:run or run the test class

## Prerequisites
* JAVA 8 Runtime
* Maven 3.3
* MySQL database
* Redis server 4.0.1
* JUnit 4

### Remarks
1. UserDao used hibernate method to implements the database query, UserRepository used Spring data jpa repositiory to query the database.
[Spring Data Jpa Query Creation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation)
2. Used lettuce to connect to redis. [Lettuce](https://lettuce.io/)
