# Start with a base image containing Java runtime (Java 17)
FROM openjdk:21-jdk-slim as build

# Set the working directory in the Docker container
WORKDIR /app

# Copy the Maven pom.xml file
COPY pom.xml .

# Copy the source code into the Docker container
COPY src src
COPY .mvn .mvn
COPY mvnw .
# Package the application
RUN ./mvnw clean package -DskipTests

# Use the OpenJDK image to create a container for running the application
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the JAR file from the build stage into the container
COPY --from=build /app/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java","-jar","app.jar"]
