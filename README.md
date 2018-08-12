# Spring-RESTful-api

This is a RESTful api based on Java Spring framework with basic authentication (Spring Security). 
Hibernate (ORM) and repository pattern are used to work with data.

Functionality:
- basic authentication using email and password
- email correctness validation
- profile of users with some information (name, surname, creation/update time, role etc)
- user (regular) and admin (godmode) roles
- CRUD operations on all users are done via HTTP methods (POST, GET, PUT, DELETE, respectively)
- flexible logging (may be configured from log4j2-spring.xml)

Instruction for execution:
1. Configure application.properties file for your DBMS (PostgreSQL in this case)
2. Create api_users database
3. Enjoy! (Postman was used in my case)

P.S.: For registration, username(email) and password - mandatory, first_name and last_name - optional were typed as Body->form-data (key-value) in my case. As for profile update, any of four fields optionally are fed as Body->raw JSON(application/json).
