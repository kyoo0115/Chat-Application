# Use a base image with OpenJDK 17
FROM openjdk:17-oracle

# Set the working directory
WORKDIR /app

# Copy the application jar file to the container
COPY build/libs/RealtimeChatApplication-1.0.jar RealtimeChatApplication-1.0.jar

# Expose the application port
EXPOSE 8080

# Define environment variables
ENV SPRING_DATASOURCE_URL=jdbc:mariadb://database-1.cbyskwqq6s69.ap-northeast-2.rds.amazonaws.com:3306/chatapp-aws
ENV SPRING_DATASOURCE_USERNAME=kyoomin1
ENV SPRING_DATASOURCE_PASSWORD=kyoominlee1
ENV SPRING_MAIL_HOST=smtp.gmail.com
ENV SPRING_MAIL_PORT=587
ENV SPRING_MAIL_USERNAME=kyoominmir41@gmail.com
ENV SPRING_MAIL_PASSWORD=aekhytcsgyiguldg
ENV SPRING_ELASTICSEARCH_URIS=http://elasticsearch:9200

# Run the application
ENTRYPOINT ["java", "-jar", "RealtimeChatApplication-1.0.jar"]