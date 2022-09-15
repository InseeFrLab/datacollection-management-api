FROM maven:3-jdk-11-slim AS build

WORKDIR /pwd

COPY ./pom.xml /pwd/

RUN mvn package -Dspring-boot.repackage.skip=true

COPY ./src /pwd/src
RUN mvn package -DskipTests=true

FROM tomcat:9.0.38-jdk11-openjdk

COPY --from=build /pwd/target/*.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]