@echo off

set IMAGE=apb.registry-img.com/api/v2/pbpro/moh-api-mobile-service:v0.0.1

echo ====================================
echo Building Spring Boot application...
echo ====================================
call mvnw clean package -DskipTests

if %ERRORLEVEL% neq 0 (
    echo Maven build failed.
    exit /b %ERRORLEVEL%
)

echo ====================================
echo Building Docker image...
echo ====================================
docker build -t %IMAGE% .

if %ERRORLEVEL% neq 0 (
    echo Docker build failed.
    exit /b %ERRORLEVEL%
)

echo ====================================
echo Pushing Docker image...
echo ====================================
docker push %IMAGE%

if %ERRORLEVEL% neq 0 (
    echo Docker push failed.
    exit /b %ERRORLEVEL%
)

echo ====================================
echo Successfully pushed:
echo %IMAGE%
echo ====================================

pause