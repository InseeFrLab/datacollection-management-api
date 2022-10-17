FROM eclipse-temurin:17-jre
WORKDIR application
RUN rm -rf /application
ADD mon-aplication.jar /application/app.jar
ADD app.properties /application/app.properties
ENTRYPOINT ["java", "-jar",  "/application/app.jar", "--spring.config.import=/application/app.properties"]
