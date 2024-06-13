FROM openjdk:17-jdk-slim

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod

# Copy the application JAR file
COPY build/libs/RealtimeChatApplication-1.0.jar RealtimeChatApplication-1.0.jar

# Expose the application port
EXPOSE 8080

# Entry point for the application
ENTRYPOINT ["java", "-jar", "RealtimeChatApplication-1.0.jar"]
