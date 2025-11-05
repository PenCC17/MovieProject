FROM amazoncorretto:17-alpine3.22
WORKDIR /app
COPY moviesproject/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]