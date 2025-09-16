FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
WORKDIR /app/BGClear
RUN mvn clean package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/BGClear/target/BGClear-v1.0.jar BGClear-v1.0.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "BGClear-v1.0.jar"]