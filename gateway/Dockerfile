FROM maven:3.8.5 as build

WORKDIR  /app
COPY ./gateway /app

RUN --mount=type=cache,target=/root/.m2 mvn clean package

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app
COPY --from=build /app/target/gateway-0.0.1-SNAPSHOT.jar /app/app.jar

CMD [ "java","-jar","/app/app.jar"]