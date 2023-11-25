FROM maven:3.8.6-openjdk-11 AS build-stage
COPY ./target/*.jar /shopping-app-backend.jar

FROM openjdk:11.0.15-jdk-slim
COPY . /shopping-app-backend.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/shopping-app-backend.jar"]