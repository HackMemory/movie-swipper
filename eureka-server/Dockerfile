FROM maven:3.8.5 as build

WORKDIR  /app
COPY ./eureka-server /app

RUN --mount=type=cache,target=/root/.m2 mvn clean package

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app
COPY --from=build /app/target/eureka-server-0.0.1-SNAPSHOT.jar /app/app.jar

CMD [ "java","-jar","/app/app.jar"]

