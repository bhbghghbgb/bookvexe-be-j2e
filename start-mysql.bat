@echo off
REM Set base directory to project root
set SCRIPT_DIR=%~dp0
set MYSQL_HOME=%SCRIPT_DIR%external-tools\mysql\mysql-9.4.0-winx64
set DATADIR=%MYSQL_HOME%\data

REM Convert to absolute path
for %%I in ("%DATADIR%") do set ABS_DATADIR=%%~fI

cd /d %MYSQL_HOME%

REM Initialize data directory if not exists
if not exist "%ABS_DATADIR%" (
    echo Initializing MySQL data directory...
    bin\mysqld.exe --initialize-insecure --basedir="%MYSQL_HOME%" --datadir="%ABS_DATADIR%"
)

echo Starting MySQL Server...
bin\mysqld.exe --console --datadir="%ABS_DATADIR%" --port=3306 --user=root

REM Run commands to create DB and user
REM "external-tools\mysql\mysql-9.4.0-winx64\bin\mysql.exe" -u root --execute="CREATE DATABASE bookVeXeDb_J2e CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
