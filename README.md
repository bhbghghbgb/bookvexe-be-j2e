# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.6/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.6/maven-plugin/build-image.html)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/3.5.6/specification/configuration-metadata/annotation-processor.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.5.6/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.5.6/reference/using/devtools.html)
* [Docker Compose Support](https://docs.spring.io/spring-boot/3.5.6/reference/features/dev-services.html#features.dev-services.docker-compose)
* [OAuth2 Authorization Server](https://docs.spring.io/spring-boot/3.5.6/reference/web/spring-security.html#web.security.oauth2.authorization-server)
* [OAuth2 Client](https://docs.spring.io/spring-boot/3.5.6/reference/web/spring-security.html#web.security.oauth2.client)
* [OAuth2 Resource Server](https://docs.spring.io/spring-boot/3.5.6/reference/web/spring-security.html#web.security.oauth2.server)
* [Spring Security](https://docs.spring.io/spring-boot/3.5.6/reference/web/spring-security.html)
* [Validation](https://docs.spring.io/spring-boot/3.5.6/reference/io/validation.html)
* [WebSocket](https://docs.spring.io/spring-boot/3.5.6/reference/messaging/websockets.html)

### Guides

The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
* [Using WebSocket to build an interactive web application](https://spring.io/guides/gs/messaging-stomp-websocket/)

### Docker Compose support

This project contains a Docker Compose file named `compose.yaml`.
In this file, the following services have been defined:

* mysql: [`mysql:latest`](https://hub.docker.com/_/mysql)

Please review the tags of the used images and set them to the same as you're running in production.

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the
parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

# On-Demand Keycloak and MySQL Setup (No Docker, No Service)

This guide explains how to download, configure, and run Keycloak and MySQL on-demand without using Docker or running
them as system services. Both applications will store data locally in their respective folders and can be
started/stopped from the command line.

## Downloading Tools

### Keycloak

1. Visit the official Keycloak download page: https://www.keycloak.org/downloads
2. Download the ZIP version suitable for your OS.
3. Extract the ZIP archive to a folder of your choice (e.g., `C:\keycloak`).

### MySQL

1. Download the MySQL Community Server from https://dev.mysql.com/downloads/mysql/
2. Choose the ZIP Archive version (not the installer).
3. Extract it to a folder of your choice (e.g., `C:\mysql`).

## Running Keycloak

- Modify `start-keycloak.bat` to set the correct path for `KEYCLOAK_HOME` where Keycloak was extracted.
- On the first start, the script will attempt to restore export data from the `export` folder inside `KEYCLOAK_HOME`.
- Run `start-keycloak.bat` from the command line to start Keycloak. The console will remain open showing logs.
- Stop Keycloak by closing the command prompt window.

## Running MySQL

- Modify `start-mysql.bat` to set the correct path for `MYSQL_HOME` where MySQL was extracted.
- The batch file initializes the data directory on the first run.
- The MySQL server starts on-demand from the command line and stays in the prompt window.
- The script will run a migration SQL script on startup to prepare the database schema.
- To stop MySQL, close the command prompt window running `mysqld`.

## Connecting Spring Boot

- Configure your Spring Boot application's database connection properties to point to:
    - Host: `localhost`
    - User: `root` (or as configured)
    - Password: as set in `start-mysql.bat`
    - Port: 3306 (default MySQL port)

Spring Boot's migration (e.g., Flyway or Liquibase) will run as usual when the app starts.

## Notes

- Both Keycloak and MySQL run as foreground processes. To stop them, simply close their command prompt windows.
- Ensure your export data for Keycloak is placed in the `export` folder inside the Keycloak directory before the first
  run.
- Customize and secure MySQL credentials appropriately for your environment.
