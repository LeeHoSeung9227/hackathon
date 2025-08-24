import React from 'react';
import { Link } from 'react-router-dom';
import './Main.css';

function Splash() {
  return (
    <div className="splash-container">
      <div className="splash-content">
        <h1 className="splash-title">🌱 EcoHack</h1>
        <p className="splash-subtitle">환경을 위한 작은 실천, 큰 변화</p>
        <div className="splash-buttons">
          <Link to="/login" className="btn btn-primary">
            시작하기
          </Link>
          <Link to="/main" className="btn btn-secondary">
            둘러보기
          </Link>
        </div>
        <div className="splash-features">
          <div className="feature">
            <span className="feature-icon">📸</span>
            <span>AI 이미지 분석</span>
          </div>
          <div className="feature">
            <span className="feature-icon">🏆</span>
            <span>포인트 적립</span>
          </div>
          <div className="feature">
            <span className="feature-icon">🎁</span>
            <span>상품 교환</span>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Splash;
