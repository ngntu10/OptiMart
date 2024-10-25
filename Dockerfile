FROM openjdk:17.0.2-oraclelinux8
## Working directory
#WORKDIR /app
## Copy from your host(PC, laptop) to container
#COPY . /app
#RUN ./mvnw dependency:go-offline
## RUN ./mvnw package
#CMD ["./mvnw", "spring-boot:run"]
#EXPOSE 8080