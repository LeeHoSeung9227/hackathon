# 🚀 **API 명세서 (API Specification)**

**프로젝트**: 쓰레기 분리 포인트 시스템  
**버전**: 1.0.0  
**기본 URL**: `http://localhost:8080`  
**인코딩**: UTF-8

---

## 📋 **목차**
1. [인증 API](#인증-api)
2. [사용자 관리 API](#사용자-관리-api)
3. [메인 대시보드 API](#메인-대시보드-api)
4. [카메라 인식 API](#카메라-인식-api)
5. [랭킹 시스템 API](#랭킹-시스템-api)
6. [상품 관리 API](#상품-관리-api)
7. [주문 관리 API](#주문-관리-api)
8. [공통 응답 형식](#공통-응답-형식)

---

## 🔐 **인증 API**

### **회원가입**
- **URL**: `POST /api/auth/register`
- **설명**: 새로운 사용자 계정 생성
- **요청 본문**:
  ```json
  {
    "username": "string",
    "email": "string",
    "password": "string",
    "nickname": "string",
    "campus": "string",
    "college": "string"
  }
  ```
- **응답**: `UserDto` 객체
- **상태 코드**: 200 OK

### **로그인**
- **URL**: `POST /api/auth/login`
- **설명**: 사용자 로그인 (현재는 간단한 메시지만 반환)
- **요청 본문**:
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **응답**: `"로그인 성공"` 메시지
- **상태 코드**: 200 OK

---

## 👥 **사용자 관리 API**

### **전체 사용자 조회**
- **URL**: `GET /api/users`
- **설명**: 모든 사용자 목록 조회
- **요청 파라미터**: 없음
- **응답**: `UserDto[]` 배열
- **상태 코드**: 200 OK

### **사용자 ID로 조회**
- **URL**: `GET /api/users/{id}`
- **설명**: 특정 ID의 사용자 정보 조회
- **경로 변수**: `id` (Long)
- **응답**: `UserDto` 객체
- **상태 코드**: 200 OK

### **사용자 생성**
- **URL**: `POST /api/users`
- **설명**: 새로운 사용자 생성
- **요청 본문**: `UserDto` 객체
- **응답**: 생성된 `UserDto` 객체
- **상태 코드**: 200 OK

### **사용자 정보 수정**
- **URL**: `PUT /api/users/{id}`
- **설명**: 기존 사용자 정보 수정
- **경로 변수**: `id` (Long)
- **요청 본문**: `UserDto` 객체
- **응답**: 수정된 `UserDto` 객체
- **상태 코드**: 200 OK

### **사용자 삭제**
- **URL**: `DELETE /api/users/{id}`
- **설명**: 사용자 계정 삭제
- **경로 변수**: `id` (Long)
- **응답**: 없음
- **상태 코드**: 200 OK

---

## 🏠 **메인 대시보드 API**

### **대시보드 정보 조회**
- **URL**: `GET /api/main/dashboard/{userId}`
- **설명**: 사용자의 메인 대시보드 정보 조회
- **경로 변수**: `userId` (Long)
- **응답**:
  ```json
  {
    "user": {
      "id": "number",
      "username": "string",
      "email": "string",
      "nickname": "string",
      "campus": "string",
      "college": "string",
      "points": "number",
      "membershipLevel": "string",
      "rank": "number"
    },
    "recentRecords": [
      {
        "id": "number",
        "userId": "number",
        "wasteType": "string",
        "earnedPoints": "number",
        "accumulatedPoints": "number",
        "recordedAt": "datetime",
        "status": "string"
      }
    ],
    "nextLevelPoints": "number"
  }
  ```
- **상태 코드**: 200 OK

---

## 📷 **카메라 인식 API**

### **쓰레기 인식**
- **URL**: `POST /api/camera/recognize`
- **설명**: AI 카메라를 통한 쓰레기 종류 인식 및 포인트 적립
- **요청 본문**:
  ```json
  {
    "wasteType": "string",
    "userId": "number"
  }
  ```
- **응답**:
  ```json
  {
    "success": "boolean",
    "wasteType": "string",
    "earnedPoints": "number",
    "message": "string"
  }
  ```
- **포인트 체계**:
  - **PET**: 10점
  - **CAN**: 15점
  - **PAPER**: 5점
  - **GLASS**: 20점
  - **기타**: 5점
- **상태 코드**: 200 OK

---

## 🏆 **랭킹 시스템 API**

### **카테고리별 랭킹 조회**
- **URL**: `GET /api/ranking/{category}`
- **설명**: 특정 카테고리의 랭킹 목록 조회
- **경로 변수**: `category` (String)
- **응답**: `RankingDto[]` 배열
- **상태 코드**: 200 OK

### **사용자 개별 랭킹 조회**
- **URL**: `GET /api/ranking/user/{userId}`
- **설명**: 특정 사용자의 랭킹 정보 조회
- **경로 변수**: `userId` (Long)
- **응답**: `RankingDto` 객체
- **상태 코드**: 200 OK

---

## 🛍️ **상품 관리 API**

### **전체 상품 조회**
- **URL**: `GET /api/products`
- **설명**: 모든 상품 목록 조회
- **요청 파라미터**: 없음
- **응답**: `ProductDto[]` 배열
- **상태 코드**: 200 OK

### **상품 ID로 조회**
- **URL**: `GET /api/products/{id}`
- **설명**: 특정 ID의 상품 정보 조회
- **경로 변수**: `id` (Long)
- **응답**: `ProductDto` 객체
- **상태 코드**: 200 OK

### **상품 생성**
- **URL**: `POST /api/products`
- **설명**: 새로운 상품 생성
- **요청 본문**: `ProductDto` 객체
- **응답**: 생성된 `ProductDto` 객체
- **상태 코드**: 200 OK

### **상품 정보 수정**
- **URL**: `PUT /api/products/{id}`
- **설명**: 기존 상품 정보 수정
- **경로 변수**: `id` (Long)
- **요청 본문**: `ProductDto` 객체
- **응답**: 수정된 `ProductDto` 객체
- **상태 코드**: 200 OK

### **상품 삭제**
- **URL**: `DELETE /api/products/{id}`
- **설명**: 상품 삭제
- **경로 변수**: `id` (Long)
- **응답**: 없음
- **상태 코드**: 200 OK

---

## 📦 **주문 관리 API**

### **전체 주문 조회**
- **URL**: `GET /api/orders`
- **설명**: 모든 주문 목록 조회
- **요청 파라미터**: 없음
- **응답**: `OrderDto[]` 배열
- **상태 코드**: 200 OK

### **주문 ID로 조회**
- **URL**: `GET /api/orders/{id}`
- **설명**: 특정 ID의 주문 정보 조회
- **경로 변수**: `id` (Long)
- **응답**: `OrderDto` 객체
- **상태 코드**: 200 OK

### **주문 생성**
- **URL**: `POST /api/orders`
- **설명**: 새로운 주문 생성
- **요청 본문**: `OrderDto` 객체
- **응답**: 생성된 `OrderDto` 객체
- **상태 코드**: 200 OK

### **주문 상태 수정**
- **URL**: `PUT /api/orders/{id}/status`
- **설명**: 주문 상태 업데이트
- **경로 변수**: `id` (Long)
- **쿼리 파라미터**: `status` (String)
- **응답**: 수정된 `OrderDto` 객체
- **상태 코드**: 200 OK

### **주문 삭제**
- **URL**: `DELETE /api/orders/{id}`
- **설명**: 주문 삭제
- **경로 변수**: `id` (Long)
- **응답**: 없음
- **상태 코드**: 200 OK

---

## 📊 **공통 응답 형식**

### **성공 응답**
```json
{
  "data": "응답 데이터",
  "message": "성공 메시지",
  "timestamp": "2024-01-15T10:30:00"
}
```

### **에러 응답**
```json
{
  "error": "에러 메시지",
  "status": "HTTP 상태 코드",
  "timestamp": "2024-01-15T10:30:00"
}
```

---

## 🔧 **CORS 설정**

모든 API 엔드포인트에 `@CrossOrigin(origins = "*")` 설정이 적용되어 있어, 모든 도메인에서 접근 가능합니다.

---

## 📝 **참고사항**

1. **인증**: 현재 JWT 토큰 기반 인증은 구현되지 않음
2. **데이터베이스**: H2 인메모리 데이터베이스 사용
3. **로깅**: SQL 쿼리 로그 활성화됨
4. **초기 데이터**: `data.sql`을 통한 샘플 데이터 자동 로드

---

**마지막 업데이트**: 2024년 1월 15일  
**문서 버전**: 1.0.0
