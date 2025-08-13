import React from 'react';
import { Link } from 'react-router-dom';

function Home() {
  return (
    <div>
      <div className="hero">
        <h1>쓰레기 분리 포인트 시스템</h1>
        <p>환경을 지키고 포인트를 모으세요!</p>
        <Link to="/login" className="btn btn-primary">
          시작하기
        </Link>
      </div>

      <div className="features">
        <div className="feature-card">
          <h3>📱 AI 카메라 인식</h3>
          <p>스마트폰으로 쓰레기를 촬영하면 자동으로 분류하고 포인트를 적립합니다.</p>
        </div>
        
        <div className="feature-card">
          <h3>🏆 포인트 시스템</h3>
          <p>쓰레기 분리 활동에 따라 포인트를 획득하고 다양한 혜택을 받으세요.</p>
        </div>
        
        <div className="feature-card">
          <h3>📊 실시간 랭킹</h3>
          <p>개인, 단과대, 캠퍼스별 랭킹을 확인하고 경쟁해보세요.</p>
        </div>
        
        <div className="feature-card">
          <h3>🎖️ 뱃지 시스템</h3>
          <p>특정 목표를 달성하면 뱃지를 획득할 수 있습니다.</p>
        </div>
        
        <div className="feature-card">
          <h3>📈 활동 기록</h3>
          <p>나의 쓰레기 분리 활동을 한눈에 확인하고 통계를 분석해보세요.</p>
        </div>
        
        <div className="feature-card">
          <h3>💚 환경 보호</h3>
          <p>작은 실천이 모여 지구를 지킵니다. 함께 시작해보세요!</p>
        </div>
      </div>

      <div className="text-center mt-3">
        <h2>데모 계정으로 체험해보세요</h2>
        <div className="card">
          <p><strong>설호 (user1)</strong> - 750점, SILVER 등급</p>
          <p><strong>김지수 (user2)</strong> - 500점, SILVER 등급</p>
          <p><strong>이가은 (user3)</strong> - 300점, BRONZE 등급</p>
          <p><strong>이호승 (user4)</strong> - 200점, BRONZE 등급</p>
          <p><strong>안예영 (user5)</strong> - 100점, BRONZE 등급</p>
          <p>모든 계정의 비밀번호: <strong>password</strong></p>
        </div>
      </div>
    </div>
  );
}

export default Home;
