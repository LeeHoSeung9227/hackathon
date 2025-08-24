import React, { useState, useEffect } from 'react';
import axios from 'axios';

function MyPage() {
  const [user, setUser] = useState({
    nickname: '이호승',
    username: 'user4',
    email: 'user4@hackathon.com',
    campus: 'Seoul',
    college: 'Arts',
    points: 200,
    membershipLevel: 'BRONZE',
    rank: 5,
    joinDate: '2024-01-01'
  });

  const [badges, setBadges] = useState([]);
  const [exchangeHistory, setExchangeHistory] = useState([]);
  const [activityStats, setActivityStats] = useState({
    totalRecycling: 0,
    streak: 0,
    badgeCount: 0,
    environmentalContribution: 0
  });

  // 백엔드에서 마이페이지 데이터 가져오기
  useEffect(() => {
    const fetchMyPageData = async () => {
      try {
        // 로컬 스토리지에서 사용자 ID 가져오기
        const userId = localStorage.getItem('userId') || '4';

        // 사용자 정보 조회
        const userResponse = await axios.get(`http://localhost:8082/api/users/${userId}`);
        if (userResponse.data.success) {
          const userData = userResponse.data.data;
          setUser({
            nickname: userData.name || userData.username,
            username: userData.username,
            email: userData.email || 'user@hackathon.com',
            campus: userData.campus,
            college: userData.college,
            points: userData.pointsTotal,
            membershipLevel: getMembershipLevel(userData.pointsTotal),
            rank: userData.rank || 5,
            joinDate: userData.createdAt ? userData.createdAt.split('T')[0] : '2024-01-01'
          });
        }

        // 뱃지 정보 조회
        const badgeResponse = await axios.get('http://localhost:8082/api/badges');
        if (badgeResponse.data.success) {
          const allBadges = badgeResponse.data.data;
          
          // 사용자 획득 뱃지 조회
          try {
            const userBadgeResponse = await axios.get(`http://localhost:8082/api/badges/user/${userId}`);
            const userBadges = userBadgeResponse.data.success ? userBadgeResponse.data.data : [];
            
            const badgesWithStatus = allBadges.map(badge => ({
              ...badge,
              acquired: userBadges.some(ub => ub.badgeId === badge.id),
              acquiredAt: userBadges.find(ub => ub.badgeId === badge.id)?.earnedAt || null
            }));
            
            setBadges(badgesWithStatus);
          } catch (badgeError) {
            console.log('사용자 뱃지 조회 실패, 기본 뱃지 데이터 사용');
            const defaultBadges = allBadges.map(badge => ({
              ...badge,
              acquired: badge.pointsRequired <= user.points,
              acquiredAt: badge.pointsRequired <= user.points ? '2024-01-10' : null
            }));
            setBadges(defaultBadges);
          }
        } else {
          // 기본 뱃지 데이터
          setBadges([
            { id: 1, name: 'Recycling Master', description: '첫 번째 재활용 성취', pointsRequired: 100, acquired: user.points >= 100, acquiredAt: user.points >= 100 ? '2024-01-10' : null },
            { id: 2, name: 'Point Collector', description: '500점 획득', pointsRequired: 500, acquired: user.points >= 500 },
            { id: 3, name: 'Waste Warrior', description: '50개 아이템 재활용', pointsRequired: 1000, acquired: user.points >= 1000 },
            { id: 4, name: 'Eco Hero', description: '환경 챔피언', pointsRequired: 2000, acquired: user.points >= 2000 }
          ]);
        }

        // 포인트 히스토리로 활동 통계 계산
        const historyResponse = await axios.get(`http://localhost:8082/api/points/user/${userId}/type/all`);
        if (Array.isArray(historyResponse.data)) {
          const positiveHistory = historyResponse.data.filter(item => item.points > 0);
          const totalRecycling = positiveHistory.length;
          const badgeCount = badges.filter(badge => badge.acquired).length;
          
          // 연속 활동일 계산 (간단한 구현)
          const recentDays = positiveHistory
            .map(item => new Date(item.createdAt).toDateString())
            .filter((date, index, arr) => arr.indexOf(date) === index) // 중복 제거
            .length;

          setActivityStats({
            totalRecycling,
            streak: recentDays,
            badgeCount,
            environmentalContribution: Math.round((totalRecycling * 0.85) * 100) / 100
          });
        }

        // 포인트 교환 내역 (교환 시스템이 없으므로 시뮬레이션)
        const negativeHistory = historyResponse.data?.filter(item => item.points < 0) || [];
        const exchangeData = negativeHistory.map((item, index) => ({
          id: index + 1,
          item: item.description?.replace('PRODUCT_PURCHASE', '상품 구매') || '포인트 사용',
          points: Math.abs(item.points),
          date: new Date(item.createdAt).toISOString().split('T')[0],
          status: '사용완료'
        }));
        
        setExchangeHistory(exchangeData.length > 0 ? exchangeData : [
          { id: 1, item: '커피 쿠폰', points: 50, date: '2024-01-12', status: '사용완료' },
          { id: 2, item: '영화 티켓', points: 100, date: '2024-01-08', status: '사용완료' },
          { id: 3, item: '도서 쿠폰', points: 30, date: '2024-01-05', status: '사용완료' }
        ]);

      } catch (error) {
        console.error('마이페이지 데이터 조회 오류:', error);
        // 오류 발생 시 기본 데이터 사용
      }
    };

    fetchMyPageData();
  }, []);

  // 포인트에 따른 멤버십 레벨 계산
  const getMembershipLevel = (points) => {
    if (points >= 2000) return 'PLATINUM';
    if (points >= 1000) return 'GOLD';
    if (points >= 500) return 'SILVER';
    return 'BRONZE';
  };

  const getMembershipColor = (level) => {
    switch (level) {
      case 'BRONZE': return '#cd7f32';
      case 'SILVER': return '#c0c0c0';
      case 'GOLD': return '#ffd700';
      case 'PLATINUM': return '#e5e4e2';
      default: return '#cd7f32';
    }
  };

  const getMembershipIcon = (level) => {
    switch (level) {
      case 'BRONZE': return '🥉';
      case 'SILVER': return '🥈';
      case 'GOLD': return '🥇';
      case 'PLATINUM': return '💎';
      default: return '🥉';
    }
  };

  const getNextLevelPoints = (currentPoints) => {
    if (currentPoints < 100) return 100 - currentPoints;
    if (currentPoints < 500) return 500 - currentPoints;
    if (currentPoints < 1000) return 1000 - currentPoints;
    if (currentPoints < 2000) return 2000 - currentPoints;
    return 0;
  };

  const getNextLevel = (currentLevel) => {
    switch (currentLevel) {
      case 'BRONZE': return 'SILVER';
      case 'SILVER': return 'GOLD';
      case 'GOLD': return 'PLATINUM';
      default: return 'MAX';
    }
  };

  return (
    <div>
      <h1 className="text-center">👤 마이페이지</h1>
      
      {/* 프로필 정보 */}
      <div className="card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <div>
            <h2>{user.nickname}</h2>
            <p><strong>사용자명:</strong> {user.username}</p>
            <p><strong>이메일:</strong> {user.email}</p>
            <p><strong>캠퍼스:</strong> {user.campus}</p>
            <p><strong>단과대:</strong> {user.college}</p>
            <p><strong>가입일:</strong> {user.joinDate}</p>
          </div>
          <div style={{ textAlign: 'center' }}>
            <div style={{ 
              fontSize: '4rem', 
              color: getMembershipColor(user.membershipLevel),
              marginBottom: '10px'
            }}>
              {getMembershipIcon(user.membershipLevel)}
            </div>
            <h3 style={{ color: getMembershipColor(user.membershipLevel), margin: '0 0 5px 0' }}>
              {user.membershipLevel}
            </h3>
            <p style={{ margin: '0', fontSize: '1.2rem' }}>
              <strong>{user.points.toLocaleString()}점</strong>
            </p>
            <p style={{ margin: '5px 0 0 0', fontSize: '0.9rem', color: '#666' }}>
              전체 {user.rank}위
            </p>
          </div>
        </div>
        
        {/* 등급 진행률 */}
        <div style={{ marginTop: '20px', textAlign: 'center' }}>
          <h4>다음 등급까지</h4>
          <div style={{ 
            width: '100%', 
            height: '20px', 
            backgroundColor: '#f0f0f0', 
            borderRadius: '10px',
            overflow: 'hidden',
            marginBottom: '10px'
          }}>
            <div style={{ 
              width: `${(user.points % 500) / 5}%`, 
              height: '100%', 
              backgroundColor: getMembershipColor(user.membershipLevel),
              transition: 'width 0.5s ease'
            }}></div>
          </div>
          <p>
            <strong>{getNextLevel(user.membershipLevel)}</strong> 등급까지 
            <strong style={{ color: 'green' }}> {getNextLevelPoints(user.points)}점</strong> 필요
          </p>
        </div>
      </div>

      {/* 뱃지 현황 */}
      <div className="card">
        <h3>🎖️ 뱃지 현황</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '15px' }}>
          {badges.map((badge) => (
            <div key={badge.id} style={{
              padding: '15px',
              backgroundColor: badge.acquired ? '#d4edda' : '#f8f9fa',
              borderRadius: '10px',
              border: `1px solid ${badge.acquired ? '#c3e6cb' : '#dee2e6'}`,
              textAlign: 'center',
              opacity: badge.acquired ? 1 : 0.6
            }}>
              <div style={{ fontSize: '3rem', marginBottom: '10px' }}>
                {badge.acquired ? '🏆' : '🔒'}
              </div>
              <h4 style={{ margin: '0 0 10px 0' }}>{badge.name}</h4>
              <p style={{ margin: '0 0 10px 0', fontSize: '0.9rem', color: '#666' }}>
                {badge.description}
              </p>
              <div style={{ 
                fontSize: '0.8rem', 
                color: badge.acquired ? 'green' : '#666',
                marginBottom: '10px'
              }}>
                필요 포인트: {badge.pointsRequired || badge.requiredPoints || 0}점
              </div>
              {badge.acquired && (
                <div style={{ fontSize: '0.8rem', color: 'green' }}>
                  획득일: {badge.acquiredAt}
                </div>
              )}
            </div>
          ))}
        </div>
      </div>

      {/* 포인트 교환 내역 */}
      <div className="card">
        <h3>💱 포인트 교환 내역</h3>
        <div style={{ display: 'grid', gap: '10px' }}>
          {exchangeHistory.map((item) => (
            <div key={item.id} style={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              padding: '15px',
              backgroundColor: '#f8f9fa',
              borderRadius: '10px',
              border: '1px solid #dee2e6'
            }}>
              <div>
                <h4 style={{ margin: '0 0 5px 0' }}>{item.item}</h4>
                <p style={{ margin: '0', color: '#666', fontSize: '0.9rem' }}>
                  교환일: {item.date}
                </p>
              </div>
              <div style={{ textAlign: 'right' }}>
                <div style={{ 
                  fontSize: '1.2rem', 
                  color: 'red', 
                  fontWeight: 'bold',
                  marginBottom: '5px'
                }}>
                  -{item.points}점
                </div>
                <span style={{ 
                  padding: '3px 8px', 
                  backgroundColor: '#28a745', 
                  color: 'white',
                  borderRadius: '15px',
                  fontSize: '0.8rem'
                }}>
                  {item.status}
                </span>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* 계정 설정 */}
      <div className="card">
        <h3>⚙️ 계정 설정</h3>
        <div style={{ display: 'grid', gap: '15px' }}>
          <button className="btn btn-primary">📝 프로필 수정</button>
          <button className="btn btn-primary">🔒 비밀번호 변경</button>
          <button className="btn btn-primary">🔔 알림 설정</button>
          <button className="btn btn-primary">🌐 개인정보 설정</button>
        </div>
      </div>

      {/* 통계 요약 */}
      <div className="card">
        <h3>📊 활동 통계</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(150px, 1fr))', gap: '15px' }}>
          <div style={{ textAlign: 'center', padding: '15px', backgroundColor: '#f8f9fa', borderRadius: '10px' }}>
            <div style={{ fontSize: '2rem', marginBottom: '5px' }}>📱</div>
            <h4 style={{ margin: '0 0 5px 0' }}>총 분리 횟수</h4>
            <p style={{ margin: '0', fontSize: '1.5rem', fontWeight: 'bold', color: '#28a745' }}>{activityStats.totalRecycling}회</p>
          </div>
          
          <div style={{ textAlign: 'center', padding: '15px', backgroundColor: '#f8f9fa', borderRadius: '10px' }}>
            <div style={{ fontSize: '2rem', marginBottom: '5px' }}>📅</div>
            <h4 style={{ margin: '0 0 5px 0' }}>연속 분리</h4>
            <p style={{ margin: '0', fontSize: '1.5rem', fontWeight: 'bold', color: '#007bff' }}>{activityStats.streak}일</p>
          </div>
          
          <div style={{ textAlign: 'center', padding: '15px', backgroundColor: '#f8f9fa', borderRadius: '10px' }}>
            <div style={{ fontSize: '2rem', marginBottom: '5px' }}>🏆</div>
            <h4 style={{ margin: '0 0 5px 0' }}>획득 뱃지</h4>
            <p style={{ margin: '0', fontSize: '1.5rem', fontWeight: 'bold', color: '#ffc107' }}>{activityStats.badgeCount}개</p>
          </div>
          
          <div style={{ textAlign: 'center', padding: '15px', backgroundColor: '#f8f9fa', borderRadius: '10px' }}>
            <div style={{ fontSize: '2rem', marginBottom: '5px' }}>💚</div>
            <h4 style={{ margin: '0 0 5px 0' }}>환경 기여도</h4>
            <p style={{ margin: '0', fontSize: '1.5rem', fontWeight: 'bold', color: '#28a745' }}>{activityStats.environmentalContribution}%</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default MyPage;
