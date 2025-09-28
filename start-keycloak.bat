@echo off
set SCRIPT_DIR=%~dp0
set KEYCLOAK_HOME=%SCRIPT_DIR%external-tools\keycloak\keycloak-26.3.5
set EXPORT_DIR=%SCRIPT_DIR%keycloak\export

cd /d %KEYCLOAK_HOME%

REM First-time import
if not exist "data" (
    echo First start detected. Restoring export data...
    call bin\kc.bat import --dir "%EXPORT_DIR%"
)

echo Starting Keycloak on port 8081...
call bin\kc.bat start-dev --http-port=8081
