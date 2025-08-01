FROM openjdk:17-jdk-slim
COPY target/fhir-patient-service.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
