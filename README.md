# 쓰레기 분리 포인트 시스템 - Spring Boot Backend

환경 보호를 위한 쓰레기 분리 포인트 시스템의 **Spring Boot 백엔드 API**입니다.

## 🚀 **프로젝트 개요**

현재는 백엔드 초기세팅팅

## 🛠️ **기술 스택**

### **Backend Framework**
- **Spring Boot 3.2.0** - 메인 백엔드 프레임워크
- **Java 17** - 프로그래밍 언어
- **Gradle 8.5** - 빌드 도구

### **Database & ORM**
- **H2 Database 2.2.224** - 인메모리 데이터베이스
- **Spring Data JPA** - 데이터 영속성
- **Hibernate** - JPA 구현체

### **Security & Authentication**
- **Spring Security 6.2.0** - 인증 및 권한 관리
- **JWT (JSON Web Token)** - 사용자 인증

### **API & Communication**
- **Spring Web** - REST API 컨트롤러
- **Spring Boot DevTools** - 개발 도구
- **CORS 설정** - 프론트엔드 통신 허용

## 📁 **프로젝트 구조**

```
src/main/java/com/hackathon/
├── HackathonApplication.java          # 메인 애플리케이션 클래스
├── config/
│   └── SecurityConfig.java           # Spring Security 설정
├── controller/                        # REST API 엔드포인트
│   ├── AuthController.java           # 인증 관련 API
│   ├── CameraController.java         # 카메라 인식 API
│   ├── MainController.java           # 메인 대시보드 API
│   ├── OrderController.java          # 주문/교환 API
│   ├── ProductController.java        # 상품 관리 API
│   ├── RankingController.java        # 랭킹 시스템 API
│   └── UserController.java           # 사용자 관리 API
├── service/                          # 비즈니스 로직 계층
│   ├── OrderService.java             # 주문 서비스
│   ├── ProductService.java           # 상품 서비스
│   ├── RankingService.java           # 랭킹 서비스
│   ├── UserService.java              # 사용자 서비스
│   └── WasteRecordService.java       # 쓰레기 기록 서비스
├── repository/                       # 데이터 접근 계층
│   ├── OrderRepository.java          # 주문 데이터 접근
│   ├── ProductRepository.java        # 상품 데이터 접근
│   ├── UserRepository.java           # 사용자 데이터 접근
│   └── WasteRecordRepository.java    # 쓰레기 기록 데이터 접근
├── entity/                           # JPA 엔티티
│   ├── Badge.java                   # 뱃지 엔티티
│   ├── News.java                     # 뉴스 엔티티
│   ├── Order.java                    # 주문 엔티티
│   ├── OrderItem.java                # 주문 아이템 엔티티
│   ├── Product.java                  # 상품 엔티티
│   ├── User.java                     # 사용자 엔티티
│   ├── UserBadge.java                # 사용자 뱃지 엔티티
│   └── WasteRecord.java              # 쓰레기 기록 엔티티
├── dto/                              # 데이터 전송 객체
│   ├── OrderDto.java                 # 주문 DTO
│   ├── ProductDto.java               # 상품 DTO
│   ├── RankingDto.java               # 랭킹 DTO
│   ├── UserDto.java                  # 사용자 DTO
│   └── WasteRecordDto.java           # 쓰레기 기록 DTO
└── exception/
    └── GlobalExceptionHandler.java    # 전역 예외 처리
```

## 🗄️ **데이터베이스 스키마**


### **초기 데이터**
- `data.sql`에 샘플 사용자, 상품, 쓰레기 기록 데이터 포함
- 애플리케이션 시작 시 자동으로 데이터베이스에 로드

## 🔌 **REST API 엔드포인트**

### **인증 API** (`/api/auth`)
- `POST /api/auth/login` - 사용자 로그인
- `POST /api/auth/register` - 사용자 회원가입

### **메인 대시보드 API** (`/api/main`)
- `GET /api/main/dashboard/{userId}` - 사용자 대시보드 정보

### **사용자 관리 API** (`/api/users`)
- `GET /api/users/{id}` - 사용자 정보 조회
- `PUT /api/users/{id}` - 사용자 정보 수정
- `GET /api/users/{id}/points` - 포인트 조회

### **쓰레기 기록 API** (`/api/waste-records`)
- `POST /api/waste-records` - 쓰레기 분리 기록 생성
- `GET /api/waste-records/user/{userId}` - 사용자별 기록 조회

### **랭킹 시스템 API** (`/api/ranking`)

## 🐳 **Docker 배포**

### **로컬 빌드 및 테스트**
```bash
# JAR 파일 빌드
./gradlew clean bootJar

# Docker 이미지 빌드
docker build -t hackathon-backend .

# 로컬에서 실행
docker run -p 8080:8080 hackathon-backend
```

### **AWS ECR 배포**
1. **GitHub Secrets 설정**
   - `AWS_ACCOUNT_ID`: AWS 계정 ID
   - `AWS_ACCESS_KEY_ID`: ECR 푸시 권한이 있는 액세스 키
   - `AWS_SECRET_ACCESS_KEY`: 시크릿 액세스 키
   - `EC2_HOST`: EC2 인스턴스 퍼블릭 IP 또는 호스트명
   - `EC2_SSH_KEY`: EC2 프라이빗 키 원문 전체

2. **자동 배포**
   - `main` 브랜치에 푸시하면 자동으로 ECR에 이미지가 푸시되고 EC2에 배포됩니다
   - GitHub Actions 워크플로우: `.github/workflows/deploy-docker.yml`

3. **EC2 환경 설정**
   - EC2에 Docker와 Docker Compose 설치 필요
   - `/opt/app/` 디렉토리에 `docker-compose.yml`과 `.env` 파일 배치
   - `.env` 파일 예시:
     ```bash
     ECR_REGISTRY=your-account-id.dkr.ecr.ap-northeast-2.amazonaws.com
     SPRING_PROFILES_ACTIVE=prod
     ```

### **헬스 체크**
- 헬스 엔드포인트: `http://localhost:8080/actuator/health`
- 정보 엔드포인트: `http://localhost:8080/actuator/info`
- `GET /api/ranking/individual` - 개인 랭킹
- `GET /api/ranking/college` - 단과대별 랭킹
- `GET /api/ranking/campus` - 캠퍼스별 랭킹

### **상품/주문 API** (`/api/products`, `/api/orders`)
- `GET /api/products` - 상품 목록 조회
- `POST /api/orders` - 주문 생성
- `GET /api/orders/user/{userId}` - 사용자별 주문 내역

## 🚀 **실행 방법**

### **1. 사전 요구사항**
- **Java 17** 이상 설치
- **Gradle** (프로젝트에 Gradle Wrapper 포함)

### **2. 프로젝트 클론**
```bash
git clone https://github.com/LeeHoSeung9227/hackathon.git
cd hackathon
```

### **3. 백엔드 실행**
```bash
# Windows
gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```

### **4. 접속 확인**
- **애플리케이션**: http://localhost:8080
- **H2 콘솔**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (비어있음)

## 🔧 **설정 파일**

### **application.yml**
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: 
  
  h2:
    console:
      enabled: true
      path: /h2-console
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  
  sql:
    init:
      mode: always
      data-locations: classpath:data.sql
```

### **build.gradle**
- Spring Boot 3.2.0
- Spring Security, JPA, H2, Lombok 등 의존성 포함

## 🧪 **테스트 데이터**

### **초기 사용자**
| ID | 사용자명 | 닉네임 | 포인트 | 등급 | 랭킹 |
|----|----------|--------|--------|------|------|
| 1  | admin    | 관리자 | 1000   | GOLD | 1위  |
| 2  | user1    | 설호   | 750    | SILVER | 2위 |
| 3  | user2    | 김지수 | 500    | SILVER | 3위 |
| 4  | user3    | 이가은 | 300    | BRONZE | 4위 |
| 5  | user4    | 이호승 | 200    | BRONZE | 5위 |

## 🔒 **보안 설정**

### **CORS 설정**
```java
@CrossOrigin(origins = "*")
```
- 모든 도메인에서 API 접근 허용
- 개발 환경용 설정

### **Spring Security**
- 기본 인증 및 권한 관리
- JWT 토큰 기반 인증 (구현 예정)

## 📊 **API 응답 예시**


## 🚀 **개발 환경 설정**

### **IDE 설정**
- **IntelliJ IDEA** 또는 **Eclipse** 권장
- **Spring Boot DevTools** 활성화로 자동 재시작
- **H2 Database** 콘솔 접근 가능

### **디버깅**
- `application.yml`에서 `show-sql: true`로 SQL 쿼리 로그 확인
- H2 콘솔에서 데이터베이스 상태 실시간 모니터링

## 🔮 **향후 개발 계획**
