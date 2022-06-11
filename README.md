# spring-security-jwt-example

Sample project used to demonstrate the usage of Spring Security featuring JWT for authentication and authorization

## Tech Stack

Spring Boot, Spring Security, Spring Data MongoDB, Spring Test, Spring Security Test, JWT, Maven

## Description

The project demonstrates how Spring Security can be configured to handle authentication and authorization
i.e. how to protect exposed API methods. Two authentication methods are supported - Basic Auth and Token auth (JWT).
The JWT auth is the most important part of the project.  
At the application startup two users with different roles will be stored into the DB (MongoDB), and their credentials can be used to test the security configuration. The exposed API methods are very simple and they will just return different "hello" messages.
Beside the token generation and validation the JWT part also provides a token blacklist (very simplified by using a HashMap). Refresh token functionality is not supported.