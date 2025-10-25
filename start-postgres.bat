@echo off
REM Set base directory to project root
set SCRIPT_DIR=%~dp0
set POSTGRES_HOME=%SCRIPT_DIR%external-tools\postgres\postgresql-18.0-2-windows-x64-binaries\pgsql
set DATADIR=%POSTGRES_HOME%\data

REM Convert to absolute path
for %%I in ("%DATADIR%") do set ABS_DATADIR=%%~fI

cd /d %POSTGRES_HOME%

REM Initialize data directory if not exists
if not exist "%ABS_DATADIR%" (
    echo Initializing PostgreSQL data directory...
    bin\initdb.exe -D "%ABS_DATADIR%" -U postgres --encoding=UTF8 --locale=en_US.UTF-8
)

echo Starting PostgreSQL Server...
bin\pg_ctl.exe -D "%ABS_DATADIR%" -l "%POSTGRES_HOME%\logfile.txt" start

REM Optional: Run SQL script to create DB and user
REM bin\psql.exe -U postgres -c "CREATE DATABASE bookVeXeDb_J2e ENCODING 'UTF8';"
