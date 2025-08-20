@echo off
echo Starting Hackathon Backend...
echo.

REM Check if Java is available
java -version >nul 2>&1
if errorlevel 1 (
    echo Error: Java is not installed or not in PATH
    pause
    exit /b 1
)

echo Java found. Starting Spring Boot application...
echo.

REM Try to run the main class directly
echo Attempting to run Spring Boot application...
java -cp "src/main/java;src/main/resources" com.hackathon.HackathonApplication

if errorlevel 1 (
    echo.
    echo Spring Boot application failed to start.
    echo This is expected as Spring Boot requires proper dependencies.
    echo.
    echo To run this application, you need to:
    echo 1. Install Maven or Gradle
    echo 2. Run: mvn spring-boot:run
    echo    or: gradlew bootRun
    echo.
    echo For now, the project structure is ready for development.
)

echo.
echo Project setup complete!
pause

