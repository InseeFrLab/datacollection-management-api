FROM 3.8.6-eclipse-temurin:17-jdk-alpine
WORKDIR application
RUN rm -rf /application
ADD *.jar /application/app.jar
ENTRYPOINT ["java", "-jar",  "/application/app.jar"]
