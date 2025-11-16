@echo off
echo "=== BookVexe BE J2E Test Runner ==="
echo.

echo "1. Cleaning and compiling project..."
call mvn clean compile -q
if %ERRORLEVEL% neq 0 (
    echo "ERROR: Compilation failed!"
    pause
    exit /b 1
)

echo "2. Running basic test..."
call mvn test -Dtest=BasicTest -q
if %ERRORLEVEL% neq 0 (
    echo "WARNING: Basic test failed, but continuing..."
)

echo "3. Running repository tests..."
call mvn test -Dtest=SimpleRouteRepositoryTest -q
if %ERRORLEVEL% neq 0 (
    echo "WARNING: Repository test failed, but continuing..."
)

echo "4. Running customer repository test..."
call mvn test -Dtest=CustomerRepositoryTest -q
if %ERRORLEVEL% neq 0 (
    echo "WARNING: Customer repository test failed, but continuing..."
)

echo "5. Running integration test..."
call mvn test -Dtest=RouteIntegrationTest -q
if %ERRORLEVEL% neq 0 (
    echo "WARNING: Integration test failed, but continuing..."
)

echo.
echo "=== Test Summary ==="
echo "All tests have been executed. Check the output above for results."
echo "Detailed reports can be found in target/surefire-reports/"
echo.

pause