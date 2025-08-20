# 🧑‍💻 **B개발자 담당 영역**

## 📁 **담당 Controller 폴더**
```
src/main/java/com/hackathon/controller/b/
├── ProductController.java    # 상품 + 주문
├── ActivityController.java   # 활동 + 일간/주간
└── BadgeController.java      # 뱃지 시스템
```

## 🎯 **담당 기능 영역**

### **1. 상품/주문 시스템 (`ProductController`)**
- **상품 관리**: 상품 CRUD, 카테고리 관리
- **주문 시스템**: 주문 생성, 상태 관리, 주문 내역
- **재고 관리**: 상품 수량 및 가용성 관리

### **2. 활동 기록 시스템 (`ActivityController`)**
- **활동 기록**: 사용자별 활동 내역 및 통계
- **일간 활동**: 일별 포인트 적립 및 활동 요약
- **주간 활동**: 주별 활동 통계 및 순위

### **3. 뱃지 시스템 (`BadgeController`)**
- **뱃지 관리**: 뱃지 CRUD, 카테고리별 분류
- **획득 조건**: 포인트 기반 뱃지 획득 시스템
- **뱃지 표시**: 사용자별 획득 뱃지 관리

## 🔧 **개발 환경 설정**

### **필요한 의존성**
- Spring Boot 3.2.0
- Spring Data JPA (데이터 접근)
- H2 Database (개발용)
- Spring Web (REST API)

### **실행 방법**
```bash
# 프로젝트 루트에서
./gradlew bootRun
```

## 📊 **API 엔드포인트**

### **상품/주문 API** (`/api/products`)
- `GET /api/products` - 전체 상품 조회
- `GET /api/products/{id}` - 상품 상세 정보
- `POST /api/products` - 새 상품 등록
- `PUT /api/products/{id}` - 상품 정보 수정
- `DELETE /api/products/{id}` - 상품 삭제
- `GET /api/products/orders` - 전체 주문 조회
- `POST /api/products/orders` - 주문 생성
- `PUT /api/products/orders/{id}/status` - 주문 상태 변경

### **활동 기록 API** (`/api/activity`)
- `GET /api/activity/user/{userId}` - 사용자 활동 기록
- `GET /api/activity/user/{userId}/date/{date}` - 특정 날짜 활동
- `GET /api/activity/user/{userId}/type/{activityType}` - 활동 타입별 기록
- `GET /api/activity/user/{userId}/daily` - 일간 활동 요약
- `GET /api/activity/user/{userId}/weekly` - 주간 활동 요약

### **뱃지 API** (`/api/badges`)
- `GET /api/badges` - 전체 뱃지 조회
- `GET /api/badges/{id}` - 뱃지 상세 정보
- `GET /api/badges/category/{category}` - 카테고리별 뱃지
- `GET /api/badges/points/{requiredPoints}` - 포인트별 뱃지
- `POST /api/badges` - 새 뱃지 생성
- `PUT /api/badges/{id}` - 뱃지 정보 수정
- `DELETE /api/badges/{id}` - 뱃지 삭제

## 🗄️ **관련 데이터베이스 테이블**

### **핵심 엔티티**
- `Product` - 상품 정보
- `Order` - 주문 정보
- `OrderItem` - 주문 상품 항목
- `ActivityHistory` - 활동 기록
- `DailyActivity` - 일간 활동 요약
- `WeeklyActivity` - 주간 활동 요약
- `Badge` - 뱃지 정보
- `UserBadge` - 사용자별 뱃지 획득

## 🚀 **개발 가이드**

### **1. 새로운 기능 추가**
```java
// B개발자 폴더에 새 Controller 추가
@RestController
@RequestMapping("/api/your-feature")
public class YourFeatureController {
    // 구현
}
```

### **2. 기존 기능 수정**
- 해당 Controller 파일을 `controller/b/` 폴더에서 수정
- 패키지명은 `com.hackathon.controller.b` 유지

### **3. 테스트 작성**
```java
@SpringBootTest
class ProductControllerTest {
    // 테스트 구현
}
```

## 🔍 **디버깅 팁**

### **로그 확인**
```yaml
# application.yml
logging:
  level:
    com.hackathon.controller.b: DEBUG
```

### **H2 콘솔**
- http://localhost:8081/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`

## 📝 **작업 체크리스트**

### **상품/주문 시스템**
- [ ] 상품 CRUD 완성
- [ ] 주문 생성 및 관리
- [ ] 주문 상태 변경
- [ ] 재고 관리 시스템

### **활동 기록 시스템**
- [ ] 활동 기록 저장
- [ ] 일간/주간 통계
- [ ] 활동 타입별 분류
- [ ] 포인트 적립 로직

### **뱃지 시스템**
- [ ] 뱃지 CRUD 완성
- [ ] 획득 조건 설정
- [ ] 사용자별 뱃지 관리
- [ ] 뱃지 표시 시스템

## 🤝 **협업 규칙**

### **Git 브랜치 전략**
- `feature/product-system` - 상품/주문 기능
- `feature/activity-system` - 활동 기록 시스템
- `feature/badge-system` - 뱃지 시스템
- `feature/integration` - 시스템 통합

### **커밋 메시지**
```
feat: 상품 관리 시스템 구현
fix: 주문 상태 변경 로직 수정
docs: API 문서 업데이트
```

### **코드 리뷰**
- A개발자와 상호 코드 리뷰
- 공통 영역은 함께 검토
- 충돌 해결 시 소통 필수

## 📞 **연락처**
- **A개발자**: 인증/사용자/포인트/AI 시스템 담당
- **공통 영역**: Camera, Main, Home Controller
- **충돌 발생 시**: 즉시 소통하여 해결

## 🔄 **시스템 연동**

### **A개발자와의 연동**
- **포인트 시스템**: A개발자의 포인트 적립과 연동
- **사용자 정보**: A개발자의 사용자 데이터 활용
- **AI 결과**: A개발자의 AI 분석 결과 활용

### **공통 영역과의 연동**
- **카메라 인식**: 쓰레기 인식 결과를 활동 기록에 반영
- **메인 대시보드**: 사용자 활동 요약 표시
- **홈 컨트롤러**: 시스템 상태 및 버전 정보

## 📈 **성능 최적화**

### **데이터베이스 최적화**
- 인덱스 설정 (사용자별, 날짜별)
- 쿼리 최적화 (N+1 문제 해결)
- 캐싱 전략 (Redis 고려)

### **API 응답 최적화**
- 페이징 처리 (대량 데이터)
- 응답 압축 (GZIP)
- 비동기 처리 (대용량 작업)

---
**B개발자**: 상품/주문, 활동 기록, 뱃지 시스템 담당
**마지막 업데이트**: 2024년 현재
