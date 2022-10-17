FROM maven:3-openjdk-17 AS build

WORKDIR /pwd

COPY ./pom.xml /pwd/

RUN mvn package -Dspring-boot.repackage.skip=true

COPY ./src /pwd/src
RUN mvn package -DskipTests=true

FROM openjdk:17

COPY --from=build /pwd/target/*.jar /usr/src/app/main.jar

WORKDIR /usr/src/app
CMD java $JAVA_OPTS -jar main.jar