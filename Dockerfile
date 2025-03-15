# Use an official OpenJDK image as the base
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Maven build files into the container
COPY pom.xml .

# Install Maven (if necessary, as we'll use Maven to build the app)
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

# Copy the whole project into the container
COPY . .

# Build the JavaFX application
RUN mvn clean package -DskipTests

# Expose the necessary port if your application is web-based (or just to be sure)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/demo-1.0-SNAPSHOT.jar"]
