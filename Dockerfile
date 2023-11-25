FROM maven:3.8.6-openjdk-11 AS build-stage
ARG JAR_FILE=./target/shopping-app-backend.jar
COPY ${JAR_FILE} /shopping-app-backend.jar

FROM openjdk:11.0.15-jdk-slim
COPY ${JAR_FILE} /shopping-app-backend.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","shopping-app-backend.jar"]