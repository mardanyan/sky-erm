FROM amazoncorretto:17.0.7-alpine

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} sky-erm.jar
EXPOSE 8080
WORKDIR /
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/sky-erm.jar"]