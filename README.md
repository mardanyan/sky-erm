# SKY ERM

This is a ERM project demonstrating the use of Spring Boot, Spring Data JPA, Spring Security, Swagger, Docker, Docker Compose, Postgres, Lombok, Mapstruct, Junit, Mockito

**Table of contents**

- [Requirements](#requirements)
- [Project specifications](#project-specifications)
- [Postgres setup](#postgres-setup)
  * [Create database](#create-database)
  * [Verify the database is created](#verify-the-database-is-created)
  * [Stop the postgres container](#stop-the-postgres-container)
- [Building the application](#building-the-application)
- [Building the docker image](#building-the-docker-image)
- [Swagger](#swagger)

## Requirements
- Docker
- Java 17
- Postgres 16.4

## Project specifications

The project is built using gradle. 
The project consist of following modules
- service
- database
- dto
- auth

Authentication is done using JWT.

As a database it's using Postgres.
The application is containerized using docker-compose.
Project secrets are stored in `docker-compose.yml` file.

## Postgres setup

Download the postgres image from docker hub
```
docker pull postgres:16.4
```

Before running the postgres container read the following instructions

- Replace `mysecretpassword` with your desired password
- Note: Password should be the same as the one in the application.properties file
- Follow instruction https://hub.docker.com/_/postgres to create a volume to persist data
- Warn: Store the password in a secure place and use file path in the command to access it
```
docker run --name sky-erm-postgres -e POSTGRES_PASSWORD=mysecretpassword -d -p 5432:5432 postgres:16.4
```

### Create database

It's mandatory to create a database with the name `sky-erm-db` before running the application. Follow the instructions below to create the database

Connect to the postgres container
```
docker exec -it sky-erm-postgres bash
```
Connect to the postgres database
```
psql -h localhost -U postgres
```
Create a database with name sky-erm-db
```
CREATE DATABASE 'sky-erm-db';
```

### Verify the database is created

```
\l
```

Example output
```
 postgres=# \l

 Name    |  Owner   | Encoding | Locale Provider |  Collate   |   Ctype    | ICU Locale | ICU Rules |   Access privileges
------------+----------+----------+-----------------+------------+------------+------------+-----------+-----------------------
 sky-erm-db | postgres | UTF8     | libc            | en_US.utf8 | en_US.utf8 |            |           |
```

Note: To exit from postgres and container run `exit` command until you are back to the host machine
e.g.
```
postgres=# exit
root@58a43aa378de:/# exit
exit
```

### Stop the postgres container

We are going to use docker-compose to run the application. So, stop the postgres container
```
docker stop sky-erm-postgres
```

## Building the application

```
./gradlew clean build
```

## Building the docker image

```
docker build -t sky-erm-app .
```

## Running the application

```
docker-compose up
```

## Swagger
Swagger is enabled in the application. To access the swagger UI, navigate to the following URL
```
http://localhost:8080/swagger-ui/index.html
```
