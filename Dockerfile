FROM openjdk:11.0.15-jdk-slim
EXPOSE 8080
ADD target/shopping-app-backend.jar shopping-app-backend.jar
ENTRYPOINT ["java","-jar","shopping-app-backend.jar"]