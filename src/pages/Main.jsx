import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './Main.css';

const Main = () => {
  const navigate = useNavigate();
  const [user, setUser] = useState({
    nickname: '새싹 하은',
    points: 9081,
    level: 1,
    profileImage: '/images/profile.jpg'
  });
  const [recentHistory, setRecentHistory] = useState([
    { type: '교환', item: '에코백', points: -500, date: '2024.01.15' },
    { type: '획득', item: '플라스틱 분리수거', points: +100, date: '2024.01.14' },
    { type: '획득', item: '종이 분리수거', points: +50, date: '2024.01.13' }
  ]);
  const [news, setNews] = useState([
    { type: '뱃지', title: '플라스틱 마스터 뱃지 획득!', date: '2024.01.14' },
    { type: '레벨업', title: '레벨 2 달성!', date: '2024.01.10' }
  ]);

  // 백엔드 연결 테스트용 상태
  const [debugInfo, setDebugInfo] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    fetchUserData();
  }, []);

  const fetchUserData = async () => {
    try {
      const userId = localStorage.getItem('userId') || '1';
      
      // 사용자 정보 조회
      const userResponse = await axios.get(`http://43.203.226.243:8080/api/users/${userId}`);
      if (userResponse.data.success) {
        const userData = userResponse.data.data;
        setUser({
          nickname: userData.name || userData.username,
          points: userData.pointsTotal || 9081,
          level: userData.level || 1, // 레벨 정보 추가
          profileImage: userData.profileImage || '/images/profile.jpg'
        });
      }

      // 최근 활동 내역 조회
      const pointsResponse = await axios.get(`http://43.203.226.243:8080/api/points/user/${userId}/type/all`);
      if (Array.isArray(pointsResponse.data)) {
        const recentPoints = pointsResponse.data.slice(0, 3).map(point => ({
          type: point.changeType === 'EARNED' ? '획득' : '사용',
          item: point.description || '포인트 적립',
          points: point.changeType === 'EARNED' ? `+${point.points}` : `-${point.points}`,
          date: new Date(point.createdAt).toLocaleDateString('ko-KR')
        }));
        setRecentHistory(recentPoints);
      }

      // 교환 내역 조회
      try {
        const exchangeResponse = await axios.get(`http://43.203.226.243:8080/api/exchanges/user/${userId}`);
        if (exchangeResponse.data.success && exchangeResponse.data.data) {
          const recentExchanges = exchangeResponse.data.data.slice(0, 2).map(exchange => ({
            type: '교환',
            item: exchange.productName || '상품',
            points: `-${exchange.pointsUsed}`,
            date: new Date(exchange.createdAt).toLocaleDateString('ko-KR')
          }));
          setRecentHistory(prev => [...recentExchanges, ...prev.slice(0, 1)]);
        }
      } catch (error) {
        console.log('교환 내역 조회 실패:', error);
      }

      // 뱃지 정보 조회
      try {
        const badgeResponse = await axios.get(`http://43.203.226.243:8080/api/badges/user/${userId}`);
        if (badgeResponse.data.success && badgeResponse.data.data) {
          const earnedBadges = badgeResponse.data.data.filter(badge => badge.earned);
          if (earnedBadges.length > 0) {
            const latestBadge = earnedBadges[0];
            setNews(prev => [{
              type: '뱃지',
              title: `${latestBadge.name} 뱃지 획득!`,
              date: new Date(latestBadge.earnedAt || latestBadge.createdAt).toLocaleDateString('ko-KR')
            }, ...prev.slice(0, 1)]);
          }
        }
      } catch (error) {
        console.log('뱃지 정보 조회 실패:', error);
      }

    } catch (error) {
      console.error('사용자 데이터 조회 오류:', error);
    }
  };

  // 백엔드 연결 테스트 함수들
  const testBackendConnection = async () => {
    setIsLoading(true);
    setDebugInfo('백엔드 연결 테스트 시작...\n');
    
    try {
      const userId = localStorage.getItem('userId') || '1';
      
      // 1. 사용자 정보 테스트
      setDebugInfo(prev => prev + '1. 사용자 정보 조회 중...\n');
      const userResponse = await axios.get(`http://43.203.226.243:8080/api/users/${userId}`);
      if (userResponse.data.success) {
        const userData = userResponse.data.data;
        setDebugInfo(prev => prev + `✅ 사용자 정보 성공!\n- 닉네임: ${userData.nickname || userData.name}\n- 포인트: ${userData.pointsTotal}\n- 레벨: ${userData.level}\n\n`);
      } else {
        setDebugInfo(prev => prev + `❌ 사용자 정보 실패: ${userResponse.data.error}\n\n`);
      }

      // 2. 포인트 히스토리 테스트
      setDebugInfo(prev => prev + '2. 포인트 히스토리 조회 중...\n');
      const pointsResponse = await axios.get(`http://43.203.226.243:8080/api/points/user/${userId}/type/all`);
      if (Array.isArray(pointsResponse.data)) {
        setDebugInfo(prev => prev + `✅ 포인트 히스토리 성공! (${pointsResponse.data.length}개)\n\n`);
      } else {
        setDebugInfo(prev => prev + `❌ 포인트 히스토리 실패\n\n`);
      }

      // 3. 교환 내역 테스트
      setDebugInfo(prev => prev + '3. 교환 내역 조회 중...\n');
      try {
        const exchangeResponse = await axios.get(`http://43.203.226.243:8080/api/exchanges/user/${userId}`);
        if (exchangeResponse.data.success) {
          setDebugInfo(prev => prev + `✅ 교환 내역 성공! (${exchangeResponse.data.data?.length || 0}개)\n\n`);
        } else {
          setDebugInfo(prev => prev + `❌ 교환 내역 실패: ${exchangeResponse.data.error}\n\n`);
        }
      } catch (error) {
        setDebugInfo(prev => prev + `❌ 교환 내역 오류: ${error.message}\n\n`);
      }

      // 4. 뱃지 정보 테스트
      setDebugInfo(prev => prev + '4. 뱃지 정보 조회 중...\n');
      try {
        const badgeResponse = await axios.get(`http://localhost:8082/api/badges/user/${userId}`);
        if (badgeResponse.data.success) {
          setDebugInfo(prev => prev + `✅ 뱃지 정보 성공! (${badgeResponse.data.data?.length || 0}개)\n\n`);
        } else {
          setDebugInfo(prev => prev + `❌ 뱃지 정보 실패: ${badgeResponse.data.error}\n\n`);
        }
      } catch (error) {
        setDebugInfo(prev => prev + `❌ 뱃지 정보 오류: ${error.message}\n\n`);
      }

      setDebugInfo(prev => prev + '🎉 백엔드 연결 테스트 완료!\n');
      
    } catch (error) {
      setDebugInfo(prev => prev + `❌ 전체 테스트 실패: ${error.message}\n`);
    } finally {
      setIsLoading(false);
    }
  };

  const clearDebugInfo = () => {
    setDebugInfo('');
  };

  // 레벨 이름 반환
  const getLevelName = (level) => {
    switch (level) {
      case 1: return '씨앗';
      case 2: return '작은 새싹';
      case 3: return '새싹';
      case 4: return '큰 새싹';
      case 5: return '나무';
      default: return '씨앗';
    }
  };

  // 다음 레벨까지 필요한 포인트
  const getNextLevelPoints = (points) => {
    if (points < 1000) return 1000 - points;
    if (points < 3000) return 3000 - points;
    if (points < 6000) return 6000 - points;
    if (points < 10000) return 10000 - points;
    return 0;
  };

  return (
    <div className="main-container">
      {/* 상단 네비게이션 */}
      <div className="top-nav">
        <div className="nav-left">
          <span className="location">📍 한양대학교</span>
        </div>
        <div className="nav-right">
          <button className="notification-btn">🔔</button>
          <button className="profile-btn" onClick={() => navigate('/mypage')}>
            <img src={user.profileImage} alt="프로필" />
          </button>
        </div>
      </div>

      {/* 메인 대시보드 */}
      <div className="dashboard">
        <div className="user-info">
          <div className="user-avatar">
            <img src={user.profileImage} alt="프로필" />
            <div className="level-badge">{user.level}</div>
          </div>
          <div className="user-details">
            <h2 className="user-name">{user.nickname}</h2>
            <p className="user-level">{getLevelName(user.level)}</p>
            <div className="points-info">
              <span className="points">{user.points.toLocaleString()}P</span>
              <span className="next-level">다음 레벨까지 {getNextLevelPoints(user.points)}P</span>
            </div>
          </div>
        </div>

        {/* 백엔드 연결 테스트 섹션 */}
        <div className="debug-section" style={{ margin: '20px 0', padding: '15px', backgroundColor: '#f8f9fa', borderRadius: '8px', border: '1px solid #dee2e6' }}>
          <h3 style={{ margin: '0 0 15px 0', color: '#495057' }}>🔧 백엔드 연결 테스트</h3>
          <div style={{ display: 'flex', gap: '10px', marginBottom: '15px' }}>
            <button 
              onClick={testBackendConnection} 
              disabled={isLoading}
              style={{ 
                padding: '8px 16px', 
                backgroundColor: '#007bff', 
                color: 'white', 
                border: 'none', 
                borderRadius: '4px', 
                cursor: isLoading ? 'not-allowed' : 'pointer',
                opacity: isLoading ? 0.6 : 1
              }}
            >
              {isLoading ? '테스트 중...' : '백엔드 연결 테스트'}
            </button>
            <button 
              onClick={clearDebugInfo}
              style={{ 
                padding: '8px 16px', 
                backgroundColor: '#6c757d', 
                color: 'white', 
                border: 'none', 
                borderRadius: '4px', 
                cursor: 'pointer' 
              }}
            >
              결과 지우기
            </button>
          </div>
          {debugInfo && (
            <div style={{ 
              backgroundColor: 'white', 
              padding: '10px', 
              borderRadius: '4px', 
              border: '1px solid #dee2e6',
              fontFamily: 'monospace',
              fontSize: '12px',
              whiteSpace: 'pre-wrap',
              maxHeight: '200px',
              overflowY: 'auto'
            }}>
              {debugInfo}
            </div>
          )}
        </div>

        {/* 빠른 액션 버튼들 */}
        <div className="quick-actions">
          <button className="action-btn camera-btn" onClick={() => navigate('/camera')}>
            <span className="icon">📷</span>
            <span className="text">촬영하기</span>
          </button>
          <button className="action-btn shop-btn" onClick={() => navigate('/shop')}>
            <span className="icon">🛍️</span>
            <span className="text">상점가기</span>
          </button>
          <button className="action-btn ranking-btn" onClick={() => navigate('/ranking')}>
            <span className="icon">🏆</span>
            <span className="text">랭킹보기</span>
          </button>
        </div>
      </div>

      {/* 최근 활동 내역 */}
      <div className="recent-activity">
        <div className="section-header">
          <h3>최근 활동</h3>
          <button className="view-all-btn" onClick={() => navigate('/history')}>전체보기</button>
        </div>
        <div className="activity-list">
          {recentHistory.map((activity, index) => (
            <div key={index} className="activity-item">
              <div className="activity-icon">
                {activity.type === '획득' ? '➕' : '➖'}
              </div>
              <div className="activity-details">
                <span className="activity-type">{activity.type}</span>
                <span className="activity-item">{activity.item}</span>
              </div>
              <div className="activity-points">
                <span className={`points ${typeof activity.points === 'string' && activity.points.startsWith('+') ? 'positive' : 'negative'}`}>
                  {activity.points}
                </span>
                <span className="date">{activity.date}</span>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* 뉴스 및 알림 */}
      <div className="news-section">
        <div className="section-header">
          <h3>뉴스</h3>
        </div>
        <div className="news-list">
          {news.map((item, index) => (
            <div key={index} className="news-item">
              <div className="news-icon">
                {item.type === '뱃지' ? '🏅' : '⭐'}
              </div>
              <div className="news-content">
                <span className="news-title">{item.title}</span>
                <span className="news-date">{item.date}</span>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Main;
