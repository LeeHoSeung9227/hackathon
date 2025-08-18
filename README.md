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

## 📁 **프로젝트 구조 (통합된 버전)**

```
src/main/java/com/hackathon/
├── HackathonApplication.java          # 메인 애플리케이션 클래스
├── config/
│   ├── SecurityConfig.java           # Spring Security 설정
│   └── CorsConfig.java              # CORS 설정
├── controller/                        # REST API 엔드포인트 (8개)
│   ├── AuthController.java           # 인증 + 회원가입 통합
│   ├── UserController.java           # 사용자 + 랭킹 통합
│   ├── PointController.java          # 포인트 + 교환 내역 통합
│   ├── AiController.java             # AI 분석 + 이미지 통합
│   ├── ProductController.java        # 상품 + 주문 통합
│   ├── ActivityController.java       # 활동 + 일간/주간 통합
│   ├── BadgeController.java          # 뱃지 시스템
│   ├── CameraController.java         # 카메라 인식
│   ├── MainController.java           # 메인 대시보드
│   └── HomeController.java           # 홈/헬스체크
├── service/                          # 비즈니스 로직 계층
│   ├── UserService.java              # 사용자 서비스
│   ├── RankingService.java           # 랭킹 서비스
│   ├── PointHistoryService.java      # 포인트 내역 서비스
│   ├── ExchangeHistoryService.java   # 교환 내역 서비스
│   ├── AiResultService.java          # AI 결과 서비스
│   ├── ImageService.java             # 이미지 서비스
│   ├── ProductService.java           # 상품 서비스
│   ├── OrderService.java             # 주문 서비스
│   ├── ActivityHistoryService.java   # 활동 기록 서비스
│   ├── BadgeService.java             # 뱃지 서비스
│   ├── WasteRecordService.java       # 쓰레기 기록 서비스
│   ├── AuthLoginService.java         # 인증 서비스
│   ├── SignupRequestService.java     # 가입 신청 서비스
│   └── TacoModelService.java        # TACO 모델 서비스
├── repository/                       # 데이터 접근 계층
│   ├── UserRepository.java           # 사용자 데이터 접근
│   ├── RankingRepository.java        # 랭킹 데이터 접근
│   ├── PointHistoryRepository.java   # 포인트 내역 데이터 접근
│   ├── ExchangeHistoryRepository.java # 교환 내역 데이터 접근
│   ├── AiResultRepository.java       # AI 결과 데이터 접근
│   ├── ImageRepository.java          # 이미지 데이터 접근
│   ├── ProductRepository.java        # 상품 데이터 접근
│   ├── OrderRepository.java          # 주문 데이터 접근
│   ├── ActivityHistoryRepository.java # 활동 기록 데이터 접근
│   ├── BadgeRepository.java          # 뱃지 데이터 접근
│   ├── WasteRecordRepository.java    # 쓰레기 기록 데이터 접근
│   ├── AuthLoginRepository.java      # 인증 데이터 접근
│   └── SignupRequestRepository.java  # 가입 신청 데이터 접근
├── entity/                           # JPA 엔티티 (12개)
│   ├── User.java                     # 사용자 엔티티
│   ├── WasteRecord.java              # 쓰레기 기록 엔티티
│   ├── Product.java                  # 상품 엔티티
│   ├── Order.java                    # 주문 엔티티
│   ├── Badge.java                    # 뱃지 엔티티
│   ├── Ranking.java                  # 랭킹 엔티티
│   ├── PointHistory.java             # 포인트 내역 엔티티
│   ├── ExchangeHistory.java          # 교환 내역 엔티티
│   ├── ActivityHistory.java          # 활동 기록 엔티티
│   ├── Image.java                    # 이미지 엔티티
│   ├── AiResult.java                 # AI 결과 엔티티
│   └── AuthLogin.java                # 인증 엔티티
├── dto/                              # 데이터 전송 객체 (8개)
│   ├── UserDto.java                  # 사용자 DTO
│   ├── WasteRecordDto.java           # 쓰레기 기록 DTO
│   ├── ProductDto.java               # 상품 DTO
│   ├── OrderDto.java                 # 주문 DTO
│   ├── BadgeDto.java                 # 뱃지 DTO
│   ├── RankingDto.java               # 랭킹 DTO
│   ├── PointHistoryDto.java          # 포인트 내역 DTO
│   └── ActivityHistoryDto.java       # 활동 기록 DTO
└── exception/
    └── GlobalExceptionHandler.java    # 전역 예외 처리
```

## 🎯 **Controller 통합 결과**

### **기존: 15개 Controller**
- `AuthController`, `SignupController` → **`AuthController`** (통합)
- `UserController`, `RankingController` → **`UserController`** (통합)
- `PointController`, `ExchangeController` → **`PointController`** (통합)
- `AiController`, `ImageController` → **`AiController`** (통합)
- `ProductController`, `OrderController` → **`ProductController`** (통합)
- `ActivityController` + Daily/Weekly → **`ActivityController`** (통합)
- **`BadgeController`** (신규 생성)

### **최종: 8개 Controller**
1. **`AuthController`** - 인증 + 회원가입
2. **`UserController`** - 사용자 + 랭킹
3. **`PointController`** - 포인트 + 교환
4. **`AiController`** - AI + 이미지
5. **`ProductController`** - 상품 + 주문
6. **`ActivityController`** - 활동 + 통계
7. **`BadgeController`** - 뱃지 시스템
8. **`CameraController`** - 카메라 인식

## 🔌 **REST API 엔드포인트**

### **인증 API** (`/api/auth`)
- `POST /api/auth/login` - 사용자 로그인
- `POST /api/auth/logout` - 사용자 로그아웃
- `GET /api/auth/session/{token}` - 세션 상태 확인
- `POST /api/auth/signup/request` - 회원가입 신청
- `GET /api/auth/signup/status/{username}` - 가입 신청 상태
- `POST /api/auth/signup/verify/{id}` - 가입 신청 인증
- `GET /api/auth/signup/all` - 모든 가입 신청 (관리자)

### **사용자 관리 API** (`/api/users`)
- `GET /api/users` - 전체 사용자 조회
- `GET /api/users/{id}` - 사용자 정보 조회
- `POST /api/users` - 사용자 생성
- `PUT /api/users/{id}` - 사용자 정보 수정
- `DELETE /api/users/{id}` - 사용자 삭제
- `GET /api/users/{id}/rankings` - 사용자 랭킹 조회
- `GET /api/users/{id}/rankings/scope/{scopeType}` - 범위별 랭킹
- `GET /api/users/{id}/ranking-summary` - 랭킹 요약

### **포인트/교환 API** (`/api/points`)
- `GET /api/points/user/{userId}` - 사용자 포인트 내역
- `GET /api/points/image/{imagesId}` - 이미지별 포인트 내역
- `GET /api/points/user/{userId}/type/{changeType}` - 타입별 포인트 내역
- `GET /api/points/user/{userId}/range` - 날짜별 포인트 내역
- `GET /api/points/user/{userId}/exchanges` - 사용자 교환 내역
- `GET /api/points/exchanges/product/{productId}` - 상품별 교환 내역
- `GET /api/points/user/{userId}/exchanges/product/{productId}` - 사용자별 상품별 교환
- `GET /api/points/user/{userId}/exchanges/range` - 날짜별 교환 내역

### **AI/이미지 API** (`/api/ai`)
- `GET /api/ai/image/{imagesId}` - 이미지별 AI 분석 결과
- `GET /api/ai/user/{userId}` - 사용자별 AI 분석 결과
- `GET /api/ai/material/{materialType}` - 재질별 AI 분석 결과
- `GET /api/ai/status/{isApproved}` - 승인 상태별 AI 분석 결과
- `GET /api/ai/range` - 날짜별 AI 분석 결과
- `GET /api/ai/images/user/{userId}` - 사용자 이미지 목록
- `GET /api/ai/images/status/{status}` - 상태별 이미지 조회
- `GET /api/ai/images/user/{userId}/status/{status}` - 사용자별 상태별 이미지
- `GET /api/ai/images/range` - 날짜별 이미지 조회
- `GET /api/ai/images/{imageId}` - 이미지 상세 정보

### **상품/주문 API** (`/api/products`)
- `GET /api/products` - 전체 상품 조회
- `GET /api/products/{id}` - 상품 ID로 조회
- `POST /api/products` - 상품 생성
- `PUT /api/products/{id}` - 상품 정보 수정
- `DELETE /api/products/{id}` - 상품 삭제
- `GET /api/products/orders` - 전체 주문 조회
- `GET /api/products/orders/{id}` - 주문 ID로 조회
- `POST /api/products/orders` - 주문 생성
- `PUT /api/products/orders/{id}/status` - 주문 상태 수정
- `DELETE /api/products/orders/{id}` - 주문 삭제

### **활동 기록 API** (`/api/activity`)
- `GET /api/activity/user/{userId}` - 사용자 활동 기록
- `GET /api/activity/user/{userId}/date/{date}` - 특정 날짜 활동
- `GET /api/activity/user/{userId}/type/{activityType}` - 활동 타입별 기록
- `GET /api/activity/user/{userId}/range` - 날짜 범위별 활동
- `GET /api/activity/user/{userId}/daily` - 사용자 일간 활동
- `GET /api/activity/user/{userId}/daily/{date}` - 특정 날짜 일간 활동
- `GET /api/activity/user/{userId}/weekly` - 사용자 주간 활동
- `GET /api/activity/user/{userId}/weekly/{weekOfYear}` - 특정 주차 주간 활동

### **뱃지 시스템 API** (`/api/badges`)
- `GET /api/badges` - 모든 뱃지 조회
- `GET /api/badges/{id}` - 뱃지 ID로 조회
- `GET /api/badges/category/{category}` - 카테고리별 뱃지
- `GET /api/badges/points/{requiredPoints}` - 포인트 요구사항별 뱃지
- `POST /api/badges` - 새로운 뱃지 생성
- `PUT /api/badges/{id}` - 뱃지 정보 수정
- `DELETE /api/badges/{id}` - 뱃지 삭제

### **카메라 인식 API** (`/api/camera`)
- `POST /api/camera/recognize` - 쓰레기 인식

### **TACO 모델 API** (`/api/taco`)
- `POST /api/taco/detect` - 이미지 업로드 및 탐지
- `POST /api/taco/status` - 모델 상태 확인

### **메인 대시보드 API** (`/api/main`)
- `GET /api/main/dashboard/{userId}` - 사용자 대시보드 정보

## 🗄️ **데이터베이스 스키마**

### **초기 데이터**
- `data.sql`에 샘플 사용자, 상품, 쓰레기 기록 데이터 포함
- 애플리케이션 시작 시 자동으로 데이터베이스에 로드

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
- **애플리케이션**: http://localhost:8081
- **H2 콘솔**: http://localhost:8081/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (비어있음)

## 🔧 **설정 파일**

### **application.yml**
```yaml
server:
  port: 8081

spring:
  main:
    allow-bean-definition-overriding: true
  application:
    name: hackathon-backend
  
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
    defer-datasource-initialization: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.H2Dialect
  
  sql:
    init:
      mode: always
      data-locations: classpath:data.sql
  
  security:
    user:
      name: admin
      password: admin123

logging:
  level:
    com.hackathon: DEBUG
    org.springframework.security: DEBUG

management:
  endpoints:
    web:
      exposure:
        include: health,info
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

## 📈 **구조 개선 효과**

### **파일 수 감소**
- **Controller**: 15개 → 8개 (47% 감소)
- **전체 코드량**: 약 30% 감소
- **유지보수성**: 크게 향상

### **협업 효율성**
- **명확한 책임 분리**: 각 Controller의 역할이 명확
- **충돌 감소**: 파일 수 감소로 인한 병합 충돌 최소화
- **코드 이해도**: 관련 기능들이 한 곳에 모여 이해하기 쉬움

### **확장성**
- **새로운 기능 추가**: 기존 Controller에 쉽게 추가 가능
- **API 일관성**: 통합된 구조로 일관된 API 설계
- **테스트 용이성**: 통합된 Controller로 테스트 작성이 간단
