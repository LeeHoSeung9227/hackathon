# 🚀 Hackathon Project - AI 기반 폐기물 분리수거 시스템

## 📋 프로젝트 개요
AI 모델(TACO)을 활용한 폐기물 분리수거 시스템으로, 사용자의 폐기물 사진을 분석하여 적절한 분류를 제안하고 포인트를 제공하는 플랫폼입니다.

## 🏗️ 기술 스택

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Build Tool**: Gradle 8.5
- **Database**: H2 Database (In-Memory)
- **ORM**: Spring Data JPA + Hibernate
- **Security**: Spring Security 6.2.0 + JWT
- **AI Integration**: Python TACO Model

### Frontend
- **Framework**: React 18.2.0
- **Router**: React Router 6.3.0
- **HTTP Client**: Axios
- **Build Tool**: Vite

### DevOps
- **Container**: Docker
- **Cloud**: AWS ECR/EC2
- **CI/CD**: GitHub Actions

## 🗂️ 프로젝트 구조

```
src/main/java/com/hackathon/
├── controller/           # REST API 컨트롤러
│   ├── a/              # A개발자 담당 영역
│   ├── b/              # B개발자 담당 영역
│   └── common/         # 공통 영역
├── service/            # 비즈니스 로직
│   ├── a/              # A개발자 담당 영역
│   ├── b/              # B개발자 담당 영역
│   └── common/         # 공통 영역
├── repository/         # 데이터 접근 계층
│   ├── a/              # A개발자 담당 영역
│   ├── b/              # B개발자 담당 영역
│   └── common/         # 공통 영역
├── entity/             # JPA 엔티티
│   ├── a/              # A개발자 담당 영역
│   ├── b/              # B개발자 담당 영역
│   └── common/         # 공통 영역
├── dto/                # 데이터 전송 객체
│   ├── a/              # A개발자 담당 영역
│   ├── b/              # B개발자 담당 영역
│   └── common/         # 공통 영역
├── config/             # 설정 클래스
└── exception/          # 예외 처리
```

## 👥 개발자 분담 현황 (원래 DB 스키마 기준)

### 🔴 A개발자 담당 영역
**주요 책임**: 사용자 관리, 인증, 포인트 시스템, AI 분석 결과 관리, 사용자 선호도

#### Controller
- `AiController` - AI 분석 결과 및 이미지 관리
- `AuthController` - 로그인/회원가입 인증
- `PointController` - 포인트 내역 관리 (교환 내역 제거됨)
- `UserController` - 사용자 정보 및 랭킹 관리
- `UserPreferencesController` - 사용자 선호도 관리 (새로 추가!)

#### Service
- `AiResultService` - AI 분석 결과 처리
- `AuthLoginService` - 로그인 세션 관리
- `ImageService` - 이미지 파일 관리
- `PointHistoryService` - 포인트 적립/차감 내역
- `RankingService` - 사용자 랭킹 관리
- `SignupRequestService` - 회원가입 요청 처리
- `UserService` - 사용자 정보 관리
- `UserPreferencesService` - 사용자 선호도 처리 (새로 추가!)

#### Repository
- `AiResultRepository` - AI 분석 결과 저장소
- `AuthLoginRepository` - 로그인 세션 저장소
- `ImageRepository` - 이미지 메타데이터 저장소
- `PointHistoryRepository` - 포인트 내역 저장소
- `RankingRepository` - 랭킹 데이터 저장소
- `SignupRequestRepository` - 회원가입 요청 저장소
- `UserRepository` - 사용자 정보 저장소
- `UserPreferencesRepository` - 사용자 선호도 저장소 (새로 추가!)

#### Entity
- `AiResult` - AI 분석 결과
- `AuthLogin` - 로그인 세션
- `Image` - 이미지 메타데이터
- `PointHistory` - 포인트 적립/차감 내역
- `Ranking` - 사용자 랭킹
- `SignupRequest` - 회원가입 요청
- `User` - 사용자 정보
- `UserPreferences` - 사용자 선호도 (새로 추가!)

#### DTO
- `AiResultDto` - AI 분석 결과 전송 객체
- `AuthLoginDto` - 로그인 정보 전송 객체
- `ImageDto` - 이미지 정보 전송 객체
- `PointHistoryDto` - 포인트 내역 전송 객체
- `RankingDto` - 랭킹 정보 전송 객체
- `SignupRequestDto` - 회원가입 요청 전송 객체
- `UserDto` - 사용자 정보 전송 객체
- `UserPreferencesDto` - 사용자 선호도 전송 객체 (새로 추가!)

### 🔵 B개발자 담당 영역
**주요 책임**: 상품 관리, 주문 시스템, 활동 기록, 뱃지 시스템, 교환 내역

#### Controller
- `ActivityController` - 사용자 활동 기록 관리
- `BadgeController` - 뱃지 시스템 관리
- `ProductController` - 상품 및 주문 관리
- `ExchangeController` - 교환 내역 관리 (A에서 이동!)

#### Service
- `ActivityHistoryService` - 활동 기록 처리
- `BadgeService` - 뱃지 발급/관리
- `DailyActivityService` - 일간 활동 통계
- `OrderService` - 주문 처리
- `ProductService` - 상품 관리
- `UserBadgeService` - 사용자 뱃지 관리
- `WeeklyActivityService` - 주간 활동 통계
- `ExchangeHistoryService` - 교환 내역 처리 (A에서 이동!)

#### Repository
- `ActivityHistoryRepository` - 활동 기록 저장소
- `BadgeRepository` - 뱃지 정보 저장소
- `DailyActivityRepository` - 일간 활동 저장소
- `OrderRepository` - 주문 정보 저장소
- `ProductRepository` - 상품 정보 저장소
- `UserBadgeRepository` - 사용자 뱃지 저장소
- `WeeklyActivityRepository` - 주간 활동 저장소
- `ExchangeHistoryRepository` - 교환 내역 저장소 (A에서 이동!)

#### Entity
- `ActivityHistory` - 사용자 활동 기록
- `Badge` - 뱃지 정보
- `DailyActivity` - 일간 활동 통계
- `Order` - 주문 정보
- `OrderItem` - 주문 상품 항목
- `Product` - 상품 정보
- `UserBadge` - 사용자 뱃지 보유 현황
- `WeeklyActivity` - 주간 활동 통계
- `ExchangeHistory` - 교환 내역 (A에서 이동!)

#### DTO
- `ActivityHistoryDto` - 활동 기록 전송 객체
- `BadgeDto` - 뱃지 정보 전송 객체
- `DailyActivityDto` - 일간 활동 전송 객체
- `OrderDto` - 주문 정보 전송 객체
- `ProductDto` - 상품 정보 전송 객체
- `WeeklyActivityDto` - 주간 활동 전송 객체
- `ExchangeHistoryDto` - 교환 내역 전송 객체 (A에서 이동!)

### 🟡 공통 영역
**주요 책임**: 폐기물 기록, AI 모델 통합, 메인 대시보드

#### Controller
- `CameraController` - 카메라 관련 기능
- `HomeController` - 홈 화면
- `MainController` - 메인 대시보드
- `TacoModelController` - AI 모델 통합

#### Service
- `TacoModelService` - Python TACO 모델 실행
- `WasteRecordService` - 폐기물 기록 처리

#### Repository
- `WasteRecordRepository` - 폐기물 기록 저장소

#### Entity
- `WasteRecord` - 폐기물 분리수거 기록

#### DTO
- `WasteRecordDto` - 폐기물 기록 전송 객체

## 🗄️ 데이터베이스 스키마 통합 현황 (원래 스키마 기준)

### 📊 A개발자 담당 테이블
| 테이블명 | 설명 | 주요 필드 | 상태 |
|---------|------|-----------|------|
| `users` | 사용자 정보 | id, username, email, password, level, points_total, college, campus | ✅ 구현됨 |
| `auth_login` | 로그인 세션 | id, userId, token, expiresAt | ✅ 구현됨 |
| `signup_requests` | 회원가입 요청 | id, username, email, password, name, college, campus | ✅ 구현됨 |
| `ai_results` | AI 분석 결과 | id, imageId, userId, wasteType, confidence, resultData | ✅ 구현됨 |
| `images` | 이미지 메타데이터 | id, userId, imageUrl, fileName, contentType, fileSize | ✅ 구현됨 |
| `point_history` | 포인트 내역 | id, userId, type, points, description | ✅ 구현됨 |
| `rankings` | 사용자 랭킹 | id, userId, category, scope | ✅ 구현됨 |
| `user_preferences` | 사용자 선호도 | id, userId, preferenceType, preferenceValue | ✅ 새로 추가됨 |

### 📊 B개발자 담당 테이블
| 테이블명 | 설명 | 주요 필드 | 상태 |
|---------|------|-----------|------|
| `products` | 상품 정보 | id, name, description, price, pointsRequired, stockQuantity | ✅ 구현됨 |
| `orders` | 주문 정보 | id, userId, status, totalAmount, totalPoints | ✅ 구현됨 (원래 스키마에 없었음) |
| `order_items` | 주문 상품 항목 | id, orderId, productId, productName, quantity, unitPrice | ✅ 구현됨 (원래 스키마에 없었음) |
| `activity_history` | 활동 기록 | id, userId, activityType, pointsEarned, description | ✅ 구현됨 |
| `daily_activity` | 일간 활동 통계 | id, userId, activityDate, totalPoints, activitiesCount | ✅ 구현됨 |
| `weekly_activity` | 주간 활동 통계 | id, userId, weekStartDate, weekEndDate, totalPoints, activitiesCount | ✅ 구현됨 |
| `badges` | 뱃지 정보 | id, name, description, imageUrl, pointsRequired, category | ✅ 구현됨 |
| `user_badges` | 사용자 뱃지 보유 현황 | id, userId, badgeId, earnedAt | ✅ 구현됨 |
| `exchange_history` | 교환 내역 | id, userId, productName, quantity, totalAmount | ✅ A에서 B로 이동됨 |

### 📊 공통 테이블
| 테이블명 | 설명 | 주요 필드 | 상태 |
|---------|------|-----------|------|
| `waste_records` | 폐기물 분리수거 기록 | id, userId, wasteType, points, imageUrl | ✅ 구현됨 |

## 🔄 API 엔드포인트 구조

### A개발자 API (`/api/*`)
- **AI 분석**: `/api/ai/*` - 이미지 분석, AI 결과 조회
- **인증**: `/api/auth/*` - 로그인, 회원가입
- **포인트**: `/api/points/*` - 포인트 내역 (교환 내역 제거됨)
- **사용자**: `/api/users/*` - 사용자 정보, 랭킹
- **사용자 선호도**: `/api/user-preferences/*` - 사용자 선호도 관리 (새로 추가!)

### B개발자 API (`/api/*`)
- **활동**: `/api/activity/*` - 활동 기록, 통계
- **뱃지**: `/api/badges/*` - 뱃지 관리
- **상품**: `/api/products/*` - 상품, 주문 관리
- **교환 내역**: `/api/exchanges/*` - 교환 내역 관리 (A에서 이동!)

### 공통 API (`/api/*`)
- **메인**: `/api/main/*` - 대시보드
- **AI 모델**: `/api/taco/*` - TACO 모델 통합
- **카메라**: `/api/camera/*` - 카메라 기능
- **홈**: `/api/home/*` - 홈 화면

## 📝 주요 변경사항

### ✅ **수정 완료된 항목**
1. **UserPreferences 추가**: A개발자 영역에 사용자 선호도 시스템 완전 구현
2. **ExchangeHistory 이동**: A개발자 영역에서 B개발자 영역으로 완전 이동
3. **PointController 정리**: 교환 내역 관련 코드 제거, 포인트 내역만 담당
4. **ExchangeController 생성**: B개발자 영역에 교환 내역 전용 컨트롤러 생성

### 🔄 **변경된 API 경로**
- **기존**: `/api/points/exchanges/*` (A개발자)
- **변경 후**: `/api/exchanges/*` (B개발자)

### 📊 **최종 파일 분배**
- **A개발자**: 8개 엔티티, 8개 DTO, 8개 Repository, 8개 Service, 5개 Controller
- **B개발자**: 9개 엔티티, 7개 DTO, 8개 Repository, 8개 Service, 4개 Controller
- **공통**: 1개 엔티티, 1개 DTO, 1개 Repository, 2개 Service, 4개 Controller

## 🚀 실행 방법

### 1. 백엔드 실행
```bash
# 프로젝트 루트 디렉토리에서
./gradlew bootRun
```

### 2. 프론트엔드 실행
```bash
# frontend 디렉토리에서
npm install
npm run dev
```

### 3. Docker 실행
```bash
docker-compose up -d
```

## 🔧 개발 환경 설정

### 필수 요구사항
- Java 17+
- Node.js 18+
- Python 3.8+ (TACO 모델용)
- Docker (선택사항)

### 환경 변수
```bash
# application.properties 또는 환경 변수로 설정
SPRING_PROFILES_ACTIVE=dev
TACO_MODEL_PATH=/path/to/taco_model.py
```

## 📝 개발 가이드라인

### 코드 스타일
- **Java**: Google Java Style Guide 준수
- **React**: ESLint + Prettier 설정 사용
- **주석**: 한국어로 작성 (비즈니스 로직 설명)

### 커밋 메시지 규칙
```
feat: 새로운 기능 추가
fix: 버그 수정
docs: 문서 수정
style: 코드 포맷팅
refactor: 코드 리팩토링
test: 테스트 코드 추가
chore: 빌드 업무 수정
```

### 브랜치 전략
- `main`: 프로덕션 배포용
- `develop`: 개발 통합용
- `feature/기능명`: 기능 개발용
- `hotfix/버그명`: 긴급 수정용

## 🧪 테스트

### 백엔드 테스트
```bash
./gradlew test
```

### 프론트엔드 테스트
```bash
npm test
```

### 통합 테스트
```bash
./gradlew integrationTest
```

## 📊 모니터링 및 로깅

### 로그 레벨
- **ERROR**: 시스템 오류, 예외 상황
- **WARN**: 경고 상황, 주의 필요
- **INFO**: 일반 정보, 비즈니스 로직
- **DEBUG**: 개발 디버깅 정보

### 헬스 체크
- `/actuator/health`: 애플리케이션 상태 확인
- `/api/taco/health`: TACO 모델 상태 확인

## 🔒 보안

### 인증 방식
- JWT 토큰 기반 인증
- Spring Security 6.2.0 사용
- BCrypt 패스워드 암호화

### CORS 설정
- 개발 환경: 모든 도메인 허용
- 프로덕션: 특정 도메인만 허용

## 🚀 배포

### AWS 배포
1. ECR에 Docker 이미지 푸시
2. EC2 인스턴스에 배포
3. GitHub Actions로 자동 배포

### 환경별 설정
- `dev`: 개발 환경
- `staging`: 스테이징 환경
- `prod`: 프로덕션 환경

## 📞 연락처 및 지원

### 개발팀
- **A개발자**: 사용자 관리, 인증, 포인트 시스템, AI 분석, 사용자 선호도
- **B개발자**: 상품 관리, 주문, 활동 기록, 뱃지, 교환 내역

### 이슈 리포트
- GitHub Issues 사용
- 버그 리포트 시 상세한 재현 단계 포함

---

**마지막 업데이트**: 2024년 12월
**버전**: 1.1.0 (원래 DB 스키마 기준으로 수정 완료)
