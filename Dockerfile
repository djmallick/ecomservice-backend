FROM maven:3.8.6-openjdk-11
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:11.0.15-jdk-slim
COPY --from=build /target/shopping-app-backend.jar shopping-app-backend.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","shopping-app-backend.jar"]