# Culinary Compass API

Culinary Compass is a comprehensive solution for food enthusiasts and culinary explorers. Our API provides detailed information on recipes, including ingredients, preparation instructions, and more, catering to a variety of food types and dietary preferences.

## Features

- **Explore Recipes**: Access a wide range of recipes from various cuisines and dietary needs.
- **Ingredient Details**: Get insights into recipe ingredients, quantities, and alternatives.
- **Full-Text Search**: Easily find recipes based on keywords, ingredients, or dietary restrictions.
- **Filtering and Sorting**: Narrow down your search with customizable filtering options, including food type, servings, and specific ingredients.
- **Ready to deploy** Easily deploy the App on Azure App Services using GitHub Actions.
- **Live Demo** Test the live demo on Azure App Service (https://culinary-compass.azurewebsites.net/culinary/swagger-ui.html)
- **Resource Management**  Create your required resources on Azure utilizing Azure Bicep(IaC).
- **Database Management** Manage MongoDB by Liquibase.
- **Security** Protect APIs utilizing a basic and minimal Spring Security config.
- **API Docs** Access the API docs through Swagger-ui
- **Seamless delivery** Validate all developed features in a production-mimicking test environment.
- **Dummy Data** Conduct API testing with pre-populated dummy data.
  
**The username and password required for authentication in the live demo are provided in the email that also contains the repository address**

## Getting Started 

Follow these instructions to get the Culinary Compass API up and running on your local machine for development and testing purposes.

### Prerequisites

Ensure you have the following installed:
- Docker
- Docker Compose
- Java 17 or newer
- Maven (if running outside of Docker)

### Installation

1. **Clone the Repository**

   ```bash
   git clone https://github.com/am6nl/culinary-compass.git && 
   cd culinary-compass 
   ```

2. **Start the Application with Docker Compose**

   Culinary Compass comes with a docker-compose.yml file that orchestrates the application and its dependent services, such as MongoDB.
   
   Please consider you need to initialize the MongoDB and App's admin username and password using following environment variables before running docker compose:

Bash:
```bash
export MONGO_USER=myuser
export MONGO_PASSWORD=mypassword 
export ADMIN_USER=admin
export ADMIN_PASS=pass
docker-compose up --build
```   
CMD: 
   ```CMD
set MONGO_USER=myuser
set MONGO_PASSWORD=mypassword
set ADMIN_USER=admin
set ADMIN_PASS=pass
docker-compose up --build
   ```
Powershell:
   ```Powershell
$env:MONGO_USER="myuser"
$env:MONGO_PASSWORD="mypassword"
$env:ADMIN_USER=admin
$env:ADMIN_PASS=pass
docker-compose up --build
   ```
   This command builds the Docker image for the application, starts all services defined in docker-compose.yml, and attaches to the logs of all running services.

3. **Accessing the API**

   Once the containers are up, the API's Swagger page is accessible at: http://localhost:8992/swagger-ui.html.

### Running Without Docker
To run the application without Docker, ensure your local environment meets the prerequisites (Java 17+, Maven) and you have a MongoDB instance accessible.

1. **Configure Database Connection**
   Edit src/main/resources/application-dev.yml to point to your local MongoDB
   and initialize App's admin user pass(for authentication):
```yaml
spring:
  data:
    mongodb:
      uri: ${MONGO_URL}
repository:
  users:
    - role: admin
      username: ${ADMIN_USER}
      password: ${ADMIN_PASS}


```
   or simply add these 3 environment variables in your local development environment:

Bash:
``` bash
export MONGO_URL= [YOUR_CONNECTION_STRING]
export ADMIN_USER=admin
export ADMIN_PASS=pass
```
CMD:
``` cmd
set MONGO_URL= [YOUR_CONNECTION_STRING]
set ADMIN_USER=admin
set ADMIN_PASS=pass
```
Powershell:
``` powershell
$env:MONGO_URL= [YOUR_CONNECTION_STRING]
$env:ADMIN_USER=admin
$env:ADMIN_PASS=pass
```
2. **Build and Run**
   Execute the following Maven commands:
```bash
mvn clean package
mvn spring-boot:run
```
