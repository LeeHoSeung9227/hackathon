@echo off
echo Hackathon Backend 시작 중...
echo.

REM Java 클래스패스 설정
set CLASSPATH=.

REM src/main/java 디렉토리의 모든 .class 파일을 클래스패스에 추가
for /r "src\main\java" %%i in (*.class) do set CLASSPATH=!CLASSPATH!;%%i

REM src/main/resources 디렉토리를 클래스패스에 추가
set CLASSPATH=!CLASSPATH!;src\main\resources

REM Spring Boot 의존성 JAR 파일들을 다운로드 (간단한 방법)
echo Spring Boot 의존성 다운로드 중...

REM 메인 클래스 실행
echo 애플리케이션 시작...
java -cp "%CLASSPATH%" com.hackathon.HackathonApplication

pause

