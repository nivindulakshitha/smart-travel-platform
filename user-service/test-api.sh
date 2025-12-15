#!/bin/bash

# User Service API Test Script
# This script tests the User Service REST endpoints

BASE_URL="http://localhost:8081"
USERS_ENDPOINT="$BASE_URL/users"

echo "================================"
echo "User Service API Test Script"
echo "================================"
echo ""
echo "Base URL: $BASE_URL"
echo ""

# Test 1: Get User 1
echo "Test 1: Get User with ID 1"
echo "Request: GET $USERS_ENDPOINT/1"
echo "---"
curl -X GET "$USERS_ENDPOINT/1" -H "Content-Type: application/json" -w "\nHTTP Status: %{http_code}\n"
echo ""
echo ""

# Test 2: Get User 2
echo "Test 2: Get User with ID 2"
echo "Request: GET $USERS_ENDPOINT/2"
echo "---"
curl -X GET "$USERS_ENDPOINT/2" -H "Content-Type: application/json" -w "\nHTTP Status: %{http_code}\n"
echo ""
echo ""

# Test 3: Get User 3
echo "Test 3: Get User with ID 3"
echo "Request: GET $USERS_ENDPOINT/3"
echo "---"
curl -X GET "$USERS_ENDPOINT/3" -H "Content-Type: application/json" -w "\nHTTP Status: %{http_code}\n"
echo ""
echo ""

# Test 4: Get Non-existent User (Error case)
echo "Test 4: Get Non-existent User with ID 999 (Expected: 404)"
echo "Request: GET $USERS_ENDPOINT/999"
echo "---"
curl -X GET "$USERS_ENDPOINT/999" -H "Content-Type: application/json" -w "\nHTTP Status: %{http_code}\n"
echo ""
echo ""

# Test 5: Get Another Non-existent User (Error case)
echo "Test 5: Get Non-existent User with ID 500 (Expected: 404)"
echo "Request: GET $USERS_ENDPOINT/500"
echo "---"
curl -X GET "$USERS_ENDPOINT/500" -H "Content-Type: application/json" -w "\nHTTP Status: %{http_code}\n"
echo ""
echo ""

echo "================================"
echo "All tests completed!"
echo "================================"
