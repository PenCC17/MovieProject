FROM amazoncorretto:17-alpine3.22
WORKDIR /app
COPY backend/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]