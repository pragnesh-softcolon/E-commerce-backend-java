# Use a base image with Java pre-installed
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled JAR file into the container
COPY target/*.jar app.jar

# Define the command to run your application
CMD ["java", "-jar", "app.jar"]
