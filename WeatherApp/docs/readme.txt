## Application: WeatherApp (v1.0)
- Read current weather, history of previously requested weather records using location (city, postal code)

## Development Note

# Dependency:
1. Spring Boot 3.4.0, JDK 17, Maven
2. MySQL 8.0.35
3. Weather API Provider: https://www.weatherapi.com/api-explorer.aspx
4. Modelmapper 3.2.0
5. Spring Doc (OpenAPI, Swagger) 2.7.0

# Source Code Structure:
1. "com.pras" 
- Root package with Application class and configuration
2. "com.pras.account" 
- Contains User account model and service to manage user registration, account activation checks
3. "com.pras.content"
- Root package to contain controller, services and model objects to manage weather information
4. "com.pras.content.dto"
- Contains DTO (JSON response model) for this Application and Converter to generate DTO from weather entity
5. "com.pras.content.feed"
- Contains service and feed model to request and capture response from Weather Provider API
6. "com.pras.utils"
- Contains common utility classes. Includes Application level Exception Handler and Exception classes

# Database Schema
1. user Table: 
- Store user account information. User name is unique and activation field ("active") decides API access
2. weather_request Table:
- Stores current weather requests for user. OneToMany mapping with user Table
3. weather Table:
- Stores previously fetched weather records. OneToOne mapping with weather_request Table

## Setup

API Key Setup
1. Sign-in and generate API Key from WeatherAPI (https://www.weatherapi.com/api-explorer.aspx)
2. Set API URL and Key in application.properties

com.pras.weather.api.url=https://api.weatherapi.com/v1/current.json
com.pras.weather.api.key=[YOUR_API_KEY]

Database Setup
1. Use doc/schema.sql to create database (weatherdb)
2. Set Database credentials in application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/weatherdb
spring.datasource.username=[YOUR_DB_USER]
spring.datasource.password=[YOUR_DB_PASSWORD]
 
## Unit Tests

Check and validate included Unit Tests
 
## How to use?

1. Build and Start the application 
2. Assuming it is running on local machine, Go to http://localhost:8080
3. Swagger is configured at the home page (/) to check and execute APIs
3. Get current weather - /api/weather?user=[USER_NAME]&location=[City Name or Postal Code]
4. Get history by user or location or both - /api/weather/history?user=[USER_NAME]&location=[City Name or Postal Code]
5. Use "active" flag in "user" table to activate / deactivate user access. By default access is activated.

## Docker Setup
- Install Docker desktop (v 4.34.2)
- Project includes- Dockerfile and docker-compose.yml
- Open CMD, Go to project root and execute command (> docker-compose up)
- During build process services may restart multiple times and it'll take a while to finish
- Once complete, Go to- http://localhost:8080
