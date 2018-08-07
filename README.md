# Spring-RESTful-api

This is a RESTful api based on Java Spring framework with basic authentication (Spring Security). Hibernate is used as an ORM

Functionality:
- basic authentication using email and password
- profile of users with some information (name, surname, creation/update time, role etc)
- user (regular) and admin (godmode) roles
- CRUD operations on all users are done via HTTP methods (POST, GET, PUT, DELETE, respectively)
- flexible logging (may be configured from log4j2-spring.xml)

Instruction for execution:
1. Configure application.properties file for your DBMS (PostgreSQL in this case)
2. Create api_users database
3. Enjoy! (Postman was used in my case)
