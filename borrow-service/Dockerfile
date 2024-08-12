FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/borrow-service-0.0.1-SNAPSHOT.jar /app/borrow-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/app/borrow-service-0.0.1-SNAPSHOT.jar"]
