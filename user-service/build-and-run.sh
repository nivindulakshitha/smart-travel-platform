#!/bin/bash

# Build the application
echo "Building User Service..."
cd "d:/Progressing/GitHub/2025-12-14/smart-travel-platform/user-service"

# Try to build with maven wrapper
if command -v timeout &> /dev/null; then
    timeout 180 bash ./mvnw clean package -DskipTests -q
else
    bash ./mvnw clean package -DskipTests -q
fi

# Check if build was successful
if [ -f "target/travel-0.0.1-SNAPSHOT.jar" ]; then
    echo "✓ Build successful!"
    echo ""
    echo "Starting User Service on port 8081..."
    echo "Navigate to http://localhost:8081/users/1 to test"
    echo ""
    java -jar target/travel-0.0.1-SNAPSHOT.jar
else
    echo "Build may have failed. Checking pom.xml and source files..."
    echo ""
    echo "Created files:"
    echo "- src/main/java/com/travel/travel/controller/UserController.java"
    echo "- src/main/java/com/travel/travel/service/UserService.java"
    echo "- src/main/java/com/travel/travel/dto/UserResponse.java"
    echo "- src/main/java/com/travel/travel/exception/UserNotFoundException.java"
    echo "- src/main/java/com/travel/travel/exception/GlobalExceptionHandler.java"
fi
