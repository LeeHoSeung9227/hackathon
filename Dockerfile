# Dockerfile
FROM eclipse-temurin:17-jre
WORKDIR /app

# Gradle 산출물 기준. build/libs/ 폴더에서 JAR 파일 복사
COPY build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar","--server.port=8080"]
