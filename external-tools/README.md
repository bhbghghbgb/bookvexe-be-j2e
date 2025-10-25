# On-Demand Keycloak and MySQL Setup (No Docker, No Service)

This guide explains how to download, configure, and run Keycloak and MySQL on-demand without using Docker or running
them as system services. Both applications will store data locally in their respective folders under the project folder
and can be started/stopped from the command line.

## Folder for External Tools

All third-party tools like Keycloak and MySQL should be located inside the `external-tools` folder at the root of your
project. This folder is kept in Git with a README file but the actual binaries are **not committed** and must be
downloaded manually.

## Downloading Tools

### Keycloak

1. Download Keycloak from the official site: https://www.keycloak.org/downloads
2. Extract the ZIP version into the project under `external-tools/keycloak`

### MySQL

1. Download MySQL Community Server ZIP archive from https://dev.mysql.com/downloads/mysql/
2. Extract it into the project under `external-tools/mysql`

## Running Keycloak

- Modify `start-keycloak.bat` to set `KEYCLOAK_HOME` to `external-tools\keycloak`
- On the first start, the script will restore export data from `external-tools\keycloak\export`
- Run `start-keycloak.bat` from your project root to start Keycloak on demand.

## Running MySQL

- Modify `start-mysql.bat` to set `MYSQL_HOME` to `external-tools\mysql`
- The script initializes the data directory and starts MySQL on demand
- Run migrations as needed during startup
- Stop MySQL by closing the command prompt window

## Running PostgreSQL

- Modify `start-postgres.bat` to set `POSTGRES_HOME` to `external-tools\postgres`
- On the first start, the script will initialize the PostgreSQL data directory
- Run `start-postgres.bat` from your project root to start PostgreSQL on demand
- Logs will be written to `logfile.txt` inside the PostgreSQL folder
- Stop PostgreSQL by running:
  ```
  bin\pg_ctl.exe -D data stop
  ```

## Connecting Spring Boot

Use connection parameters:

- MySQL
    - Configure your Spring Boot application's database connection properties to point to:
        - Host: `localhost`
        - User: `root` (or as configured)
        - Password: as set in `start-mysql.bat`
        - Port: 3306 (default MySQL port)
        - Database: as created in your migration or setup script
- Postgres
    - Use connection parameters:
        - Host: `localhost`
        - Port: `5432` (default PostgreSQL port)
        - User: `postgres`
        - Password: (set manually or via script)
        - Database: as created in your migration or setup script

---

Place your `start-keycloak.bat` and `start-mysql.bat` scripts in your project root alongside the `external-tools` folder
for relative path convenience.
