# 쓰레기 분리 포인트 시스템 (React Frontend)

환경 보호를 위한 쓰레기 분리 포인트 시스템의 React 프론트엔드입니다.

## 🚀 주요 기능

### 📱 메인 대시보드 (Main)
- 사용자 정보 및 포인트 현황
- 멤버십 등급 표시 (BRONZE, SILVER, GOLD, PLATINUM)
- 최근 활동 기록 (최근 3개)
- 뉴스 및 알림
- 빠른 액션 버튼

### 📷 AI 카메라 인식 (Camera)
- 쓰레기 종류별 포인트 시스템
- 실시간 인식 시뮬레이션
- 성공/실패 결과 표시
- 쓰레기 분리 팁 제공

### 🏆 랭킹 시스템 (Ranking)
- 개인/단과대/캠퍼스별 랭킹
- 실시간 순위 업데이트
- 랭킹별 보상 시스템
- 랭킹 올리는 팁

### 👤 마이페이지 (MyPage)
- 상세 프로필 정보
- 뱃지 현황 (획득/미획득)
- 포인트 교환 내역
- 계정 설정 옵션
- 활동 통계 요약

### 📊 활동 기록 (History)
- 주간/월간/캘린더 뷰
- 상세 활동 기록
- 환경 기여도 측정
- 목표 달성률 표시

## 🛠️ 기술 스택

### Frontend
- **React 18.2.0** - 사용자 인터페이스 라이브러리
- **React Router DOM 6.3.0** - 클라이언트 사이드 라우팅
- **Axios 1.3.4** - HTTP 클라이언트
- **CSS3** - 스타일링 및 반응형 디자인

### Backend
- **Spring Boot 3.2.0** - 백엔드 프레임워크
- **Java 17** - 프로그래밍 언어
- **Spring Data JPA** - 데이터 영속성
- **H2 Database** - 인메모리 데이터베이스
- **Spring Security** - 인증 및 권한 관리

## 📁 프로젝트 구조

```
HACKATHON/
├── package.json                 # React 프로젝트 설정
├── public/
│   └── index.html              # HTML 템플릿
├── src/
│   ├── components/             # 재사용 가능한 컴포넌트
│   │   └── Navbar.js          # 네비게이션 바
│   ├── pages/                  # 페이지 컴포넌트
│   │   ├── Home.js            # 홈페이지
│   │   ├── Login.js           # 로그인/회원가입
│   │   ├── Main.js            # 메인 대시보드
│   │   ├── Camera.js          # 카메라 인식
│   │   ├── Ranking.js         # 랭킹 시스템
│   │   ├── MyPage.js          # 마이페이지
│   │   └── History.js         # 활동 기록
│   ├── App.js                  # 메인 앱 컴포넌트
│   ├── App.css                 # 앱 스타일
│   ├── index.js                # 앱 진입점
│   └── index.css               # 전역 스타일
└── src/main/java/com/hackathon/  # Spring Boot 백엔드
    ├── controller/             # REST API 컨트롤러
    ├── service/                # 비즈니스 로직
    ├── repository/             # 데이터 접근 계층
    ├── entity/                 # JPA 엔티티
    └── dto/                    # 데이터 전송 객체
```

## 🚀 실행 방법

### 1. 백엔드 실행
```bash
# Spring Boot 애플리케이션 실행
gradlew.bat bootRun
```
백엔드는 `http://localhost:8080`에서 실행됩니다.

### 2. 프론트엔드 실행
```bash
# 의존성 설치
npm install

# 개발 서버 실행
npm start
```
프론트엔드는 `http://localhost:3000`에서 실행됩니다.

### 3. 빌드
```bash
# 프로덕션 빌드
npm run build
```

## 🔑 테스트 계정

| 사용자명 | 닉네임 | 비밀번호 | 포인트 | 등급 | 랭킹 |
|---------|--------|----------|--------|------|------|
| user1   | 설호   | password | 750    | SILVER | 2위 |
| user2   | 김지수 | password | 500    | SILVER | 3위 |
| user3   | 이가은 | password | 300    | BRONZE | 4위 |
| user4   | 이호승 | password | 200    | BRONZE | 5위 |
| user5   | 안예영 | password | 100    | BRONZE | 6위 |

## 🌟 주요 특징

### 🎨 사용자 경험
- **반응형 디자인**: 모바일과 데스크톱 모두 지원
- **직관적 인터페이스**: 사용하기 쉬운 UI/UX
- **실시간 업데이트**: 포인트와 랭킹 실시간 반영
- **시각적 피드백**: 이모지와 색상으로 정보 전달

### 🔒 보안 기능
- **Spring Security**: 백엔드 API 보안
- **JWT 토큰**: 사용자 인증 (구현 예정)
- **CORS 설정**: 프론트엔드-백엔드 통신 허용

### 📊 데이터 관리
- **H2 인메모리 DB**: 빠른 개발 및 테스트
- **JPA 엔티티**: 객체-관계 매핑
- **데이터 초기화**: 샘플 데이터 자동 로드

## 🔮 향후 개발 계획

### 단기 계획
- [ ] JWT 인증 시스템 구현
- [ ] 실시간 채팅 기능
- [ ] 푸시 알림 시스템
- [ ] 이미지 업로드 기능

### 중기 계획
- [ ] PWA (Progressive Web App) 지원
- [ ] 오프라인 모드
- [ ] 다국어 지원
- [ ] 테마 커스터마이징

### 장기 계획
- [ ] AI 쓰레기 인식 모델 연동
- [ ] 블록체인 기반 포인트 시스템
- [ ] IoT 디바이스 연동
- [ ] 글로벌 서비스 확장

## 🤝 기여 방법

1. 이 저장소를 포크합니다
2. 기능 브랜치를 생성합니다 (`git checkout -b feature/AmazingFeature`)
3. 변경사항을 커밋합니다 (`git commit -m 'Add some AmazingFeature'`)
4. 브랜치에 푸시합니다 (`git push origin feature/AmazingFeature`)
5. Pull Request를 생성합니다

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다.

## 📞 문의

프로젝트에 대한 문의사항이 있으시면 이슈를 생성해주세요.

---

**환경을 지키고 포인트를 모으세요! 🌱♻️**
