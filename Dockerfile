# Dockerfile
FROM eclipse-temurin:17-jre
WORKDIR /app

# Gradle 산출물 기준. CI에서 app.jar로 복사해 둘 것
COPY target/hackathon-backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar","--server.port=8080"]
