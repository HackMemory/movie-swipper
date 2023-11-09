FROM maven:3.8.5 as build

WORKDIR  /app
COPY . /app

RUN mvn clean package -DskipTests -P prod

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app
COPY --from=build /app/target/movie-swipper-0.0.1-SNAPSHOT.jar /app/app.jar

CMD [ "java","-jar","-Dspring.profiles.active=prod","/app/app.jar"]

