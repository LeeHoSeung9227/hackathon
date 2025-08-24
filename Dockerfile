# Dockerfile
FROM eclipse-temurin:17-jre
WORKDIR /app

# 워크플로우에서 복사된 app.jar 파일 사용
COPY app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar","--server.port=8080"]
