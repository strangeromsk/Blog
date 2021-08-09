# syntax=docker/dockerfile:1
FROM openjdk:16-alpine3.13
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
COPY .gitignore .gitignore
COPY pom.xml pom.xml
COPY Readme.md Readme.md
CMD ["./mvnw", "spring-boot:run"]