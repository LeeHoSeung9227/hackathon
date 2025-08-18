# 🧑‍💻 **A개발자 담당 영역**

## 📁 **담당 Controller 폴더**
```
src/main/java/com/hackathon/controller/a/
├── AuthController.java      # 인증 + 회원가입
├── UserController.java      # 사용자 + 랭킹
├── PointController.java     # 포인트 + 교환 내역
└── AiController.java        # AI 분석 + 이미지
```

## 🎯 **담당 기능 영역**

### **1. 인증 시스템 (`AuthController`)**
- **로그인/로그아웃**: 사용자 인증 및 세션 관리
- **회원가입**: 가입 신청, 상태 확인, 인증 처리
- **세션 관리**: 토큰 기반 세션 상태 확인

### **2. 사용자 관리 (`UserController`)**
- **사용자 CRUD**: 생성, 조회, 수정, 삭제
- **랭킹 시스템**: 개인/단과대/캠퍼스별 랭킹
- **사용자 통계**: 랭킹 요약 및 포인트 정보

### **3. 포인트 시스템 (`PointController`)**
- **포인트 내역**: 사용자별, 이미지별, 타입별 포인트 기록
- **교환 내역**: 상품 교환 기록 및 통계
- **포인트 분석**: 날짜별, 범위별 포인트 추이

### **4. AI 시스템 (`AiController`)**
- **AI 분석 결과**: 이미지별, 사용자별 AI 분석 데이터
- **이미지 관리**: 사용자 이미지 업로드 및 상태 관리
- **재질 인식**: 플라스틱, 종이, 유리 등 재질별 분석

## 🔧 **개발 환경 설정**

### **필요한 의존성**
- Spring Boot 3.2.0
- Spring Security (인증)
- Spring Data JPA (데이터 접근)
- H2 Database (개발용)

### **실행 방법**
```bash
# 프로젝트 루트에서
./gradlew bootRun
```

## 📊 **API 엔드포인트**

### **인증 API** (`/api/auth`)
- `POST /api/auth/login` - 로그인
- `POST /api/auth/logout` - 로그아웃
- `GET /api/auth/session/{token}` - 세션 확인
- `POST /api/auth/signup/request` - 가입 신청
- `GET /api/auth/signup/status/{username}` - 가입 상태
- `POST /api/auth/signup/verify/{id}` - 가입 인증

### **사용자 API** (`/api/users`)
- `GET /api/users` - 전체 사용자
- `GET /api/users/{id}` - 사용자 정보
- `POST /api/users` - 사용자 생성
- `PUT /api/users/{id}` - 사용자 수정
- `DELETE /api/users/{id}` - 사용자 삭제
- `GET /api/users/{id}/rankings` - 사용자 랭킹

### **포인트 API** (`/api/points`)
- `GET /api/points/user/{userId}` - 사용자 포인트 내역
- `GET /api/points/image/{imagesId}` - 이미지별 포인트
- `GET /api/points/user/{userId}/exchanges` - 교환 내역

### **AI API** (`/api/ai`)
- `GET /api/ai/image/{imagesId}` - AI 분석 결과
- `GET /api/ai/user/{userId}` - 사용자별 AI 결과
- `GET /api/ai/images/user/{userId}` - 사용자 이미지

## 🗄️ **관련 데이터베이스 테이블**

### **핵심 엔티티**
- `User` - 사용자 정보
- `AuthLogin` - 인증 세션
- `SignupRequest` - 가입 신청
- `PointHistory` - 포인트 내역
- `ExchangeHistory` - 교환 내역
- `AiResult` - AI 분석 결과
- `Image` - 이미지 정보
- `Ranking` - 랭킹 정보

## 🚀 **개발 가이드**

### **1. 새로운 기능 추가**
```java
// A개발자 폴더에 새 Controller 추가
@RestController
@RequestMapping("/api/your-feature")
public class YourFeatureController {
    // 구현
}
```

### **2. 기존 기능 수정**
- 해당 Controller 파일을 `controller/a/` 폴더에서 수정
- 패키지명은 `com.hackathon.controller.a` 유지

### **3. 테스트 작성**
```java
@SpringBootTest
class AuthControllerTest {
    // 테스트 구현
}
```

## 🔍 **디버깅 팁**

### **로그 확인**
```yaml
# application.yml
logging:
  level:
    com.hackathon.controller.a: DEBUG
```

### **H2 콘솔**
- http://localhost:8081/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`

## 📝 **작업 체크리스트**

### **인증 시스템**
- [ ] 로그인/로그아웃 구현
- [ ] JWT 토큰 기반 인증
- [ ] 회원가입 프로세스
- [ ] 세션 관리

### **사용자 관리**
- [ ] 사용자 CRUD 완성
- [ ] 랭킹 시스템 구현
- [ ] 포인트 계산 로직

### **포인트 시스템**
- [ ] 포인트 적립/차감
- [ ] 교환 내역 관리
- [ ] 포인트 통계

### **AI 시스템**
- [ ] 이미지 업로드
- [ ] AI 분석 결과 저장
- [ ] 재질 인식 정확도

## 🤝 **협업 규칙**

### **Git 브랜치 전략**
- `feature/auth-system` - 인증 기능 개발
- `feature/user-management` - 사용자 관리
- `feature/point-system` - 포인트 시스템
- `feature/ai-integration` - AI 통합

### **커밋 메시지**
```
feat: 사용자 인증 시스템 구현
fix: 포인트 계산 로직 수정
docs: API 문서 업데이트
```

### **코드 리뷰**
- B개발자와 상호 코드 리뷰
- 공통 영역은 함께 검토
- 충돌 해결 시 소통 필수

## 📞 **연락처**
- **B개발자**: 상품/활동/뱃지 시스템 담당
- **공통 영역**: Camera, Main, Home Controller
- **충돌 발생 시**: 즉시 소통하여 해결

---
**A개발자**: 인증, 사용자, 포인트, AI 시스템 담당
**마지막 업데이트**: 2024년 현재
