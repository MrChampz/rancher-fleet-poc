FROM maven:3.9.0-eclipse-temurin-19 as builder

COPY . .

RUN mvn -B -DskipTests clean package

##################################################
FROM openjdk:17-jdk-alpine

COPY --from=builder target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]