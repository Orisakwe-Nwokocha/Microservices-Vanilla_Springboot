FROM maven:3.8.7 as build
COPY . .
RUN mvn -B clean package -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/gateway-0.0.1-SNAPSHOT.jar /app/gateway-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/app/gateway-0.0.1-SNAPSHOT.jar"]
