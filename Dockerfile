FROM openjdk:17-slim
WORKDIR /app
COPY moviesproject/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]