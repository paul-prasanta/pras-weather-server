# WeatherApp
Offers REST API to read current weather (`/api/weather`) and history (`/api/weather/history`) of previously requested records for mentioned location (**city, postal code**). It uses [Swagger](https://swagger.io/) for API documentation & testing (`http://localhost:8080`).

![image](https://github.com/user-attachments/assets/9e06151f-229c-4800-95d1-1c7b05b24457)

## Dependency:
- Spring Boot 3.4.0, JDK 17, Maven
- MySQL 8.0.35
- Weather API Provider: https://www.weatherapi.com/api-explorer.aspx
- Modelmapper 3.2.0
- Spring Doc (OpenAPI, Swagger) 2.7.0

## Source Code Structure:
- `com.pras`: Root package with Application class and configuration
- `com.pras.account`: Contains User account model and service to manage account registration, activation checks
- `com.pras.content`: Root package to contain controller, services and model objects to manage weather information
- `com.pras.content.dto`: Contains DTO Model and Converter to generate JSON response from weather entity
- `com.pras.content.feed`: Contains service and feed model to request and capture response from Weather Provider API
- `com.pras.utils`: Contains common utility classes. Includes Application level Exception Handler and Exception classes

## Database Schema
- `user` Table: Store user account information. User name is unique and activation field ("active") decides API access
- `weather_request` Table: Stores current weather requests for user. <ins>OneToMany</ins> mapping with `user` Table
- `weather` Table: Stores previously fetched weather records. <ins>OneToOne</ins> mapping with `weather_request` Table

## Setup
- Sign-in and generate API Key from **WeatherAPI** https://www.weatherapi.com/api-explorer.aspx
- Set API URL and Key in `application.properties`
```
com.pras.weather.api.url=https://api.weatherapi.com/v1/current.json
com.pras.weather.api.key=[YOUR_API_KEY]
```
- Use `doc/schema.sql` to create database (`weatherdb`)
- Set Database credentials in `application.properties`
```
spring.datasource.url=jdbc:mysql://localhost:3306/weatherdb
spring.datasource.username=[YOUR_DB_USER]
spring.datasource.password=[YOUR_DB_PASSWORD]
```

## Setup using Docker
- Install [Docker desktop](https://www.docker.com/products/docker-desktop/) (v 4.34.2)
- Project includes docker configuratins- **Dockerfile** and **docker-compose.yml**
- Sign-in and generate API Key from **WeatherAPI** https://www.weatherapi.com/api-explorer.aspx
- Set API URL and Key in `application.properties`
```
com.pras.weather.api.url=https://api.weatherapi.com/v1/current.json
com.pras.weather.api.key=[YOUR_API_KEY]
```
- Open CMD, Go to project root and execute command (`> docker-compose up`)
- It'll take a while to finish, services may restart multiple times during initialization
- Once complete, Go to- http://localhost:8080

![docker_compose_containers](https://github.com/user-attachments/assets/642d7a5f-2deb-4a48-8856-24b6fca564ec)

## Unit Tests
Validate functionalities using included Unit Tests (`91% code coverage`)

![pras_weatherapp_tests_coverage](https://github.com/user-attachments/assets/fe366e48-8ebf-4553-b28f-dc7772c7ecb8)

## How to use?
- Download `source code`, complete Setup, Build and Start the application 
- Assuming it is running on local machine, Go to http://localhost:8080
- [Swagger](https://swagger.io/) is configured at the `home page (/)` for API documentation & testing
- Get current weather `/api/weather?user=[USER_NAME]&location=[City Name or Postal Code]`
- Get history by user or location or both `/api/weather/history?user=[USER_NAME]&location=[City Name or Postal Code]`
- Use `active` field (*true / false*) in `user table` to grant API access to user. By default access is activated.
