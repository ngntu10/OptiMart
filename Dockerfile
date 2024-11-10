FROM openjdk:17-jdk-slim
# Working directory
WORKDIR /app
# Copy from your host(PC, laptop) to container
COPY target/OptiMart.jar Optimart.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "Optimart.jar"]