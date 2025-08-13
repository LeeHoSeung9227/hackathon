import React from 'react';
import { Link } from 'react-router-dom';

function Navbar() {
  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-brand">
          쓰레기 분리 포인트 시스템
        </Link>
        <ul className="navbar-nav">
          <li><Link to="/">홈</Link></li>
          <li><Link to="/login">로그인</Link></li>
          <li><Link to="/main">대시보드</Link></li>
          <li><Link to="/camera">카메라</Link></li>
          <li><Link to="/ranking">랭킹</Link></li>
          <li><Link to="/mypage">마이페이지</Link></li>
          <li><Link to="/history">기록</Link></li>
        </ul>
      </div>
    </nav>
  );
}

export default Navbar;
