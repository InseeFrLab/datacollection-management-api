FROM eclipse-temurin:17-jre
WORKDIR /application
RUN rm -rf /application
ADD target/*.jar /application/app.jar
ENTRYPOINT ["java", "-jar",  "/application/app.jar"]