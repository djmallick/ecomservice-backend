FROM maven:3.8.6-openjdk-11
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:11.0.15-jdk-slim
COPY --from=build /target/vendor-reciew-system-2.0-0.0.1-SNAPSHOT.jar vendor-reciew-system-2.0.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","vendor-reciew-system-2.0.jar"]