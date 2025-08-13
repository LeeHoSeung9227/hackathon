import React, { useState, useEffect } from 'react';
import axios from 'axios';

function Main() {
  const [user, setUser] = useState({
    nickname: '설호',
    campus: 'Seoul',
    college: 'Computer Science',
    points: 750,
    membershipLevel: 'SILVER'
  });
  const [recentRecords, setRecentRecords] = useState([]);
  const [news, setNews] = useState([
    { title: '환경의 날 이벤트 참여', content: '환경의 날을 맞아 특별 포인트 이벤트가 시작되었습니다.', type: 'EVENT' },
    { title: '새로운 뱃지 획득', content: 'Recycling Master 뱃지를 획득했습니다!', type: 'BADGE' },
    { title: '랭킹 상승', content: '전체 랭킹이 5위에서 2위로 상승했습니다.', type: 'RANKING' }
  ]);

  // 백엔드 API에서 사용자 데이터 가져오기
  useEffect(() => {
    const fetchUserData = async () => {
      try {
        // 실제 사용자 ID 1번으로 테스트 (admin 계정)
        const response = await axios.get('http://localhost:8080/api/main/dashboard/1');
        console.log('백엔드 API 응답:', response.data);
        
        if (response.data.user) {
          setUser(response.data.user);
        }
        if (response.data.recentRecords) {
          setRecentRecords(response.data.recentRecords);
        }
      } catch (error) {
        console.error('백엔드 API 호출 오류:', error);
        // API 호출 실패 시 기본 데이터 사용
      }
    };

    fetchUserData();
  }, []);

  const getMembershipColor = (level) => {
    switch (level) {
      case 'BRONZE': return '#cd7f32';
      case 'SILVER': return '#c0c0c0';
      case 'GOLD': return '#ffd700';
      case 'PLATINUM': return '#e5e4e2';
      default: return '#cd7f32';
    }
  };

  const getWasteTypeIcon = (type) => {
    switch (type) {
      case 'PET': return '🥤';
      case 'CAN': return '🥫';
      case 'PAPER': return '📄';
      case 'GLASS': return '🥃';
      default: return '♻️';
    }
  };

  return (
    <div>
      <h1 className="text-center">환영합니다, {user.nickname}님! 👋</h1>
      
      {/* 사용자 정보 카드 */}
      <div className="card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <div>
            <h2>{user.nickname}</h2>
            <p><strong>캠퍼스:</strong> {user.campus}</p>
            <p><strong>단과대:</strong> {user.college}</p>
          </div>
          <div style={{ textAlign: 'center' }}>
            <div style={{ 
              fontSize: '3rem', 
              color: getMembershipColor(user.membershipLevel),
              marginBottom: '10px'
            }}>
              {user.membershipLevel === 'BRONZE' ? '🥉' : 
               user.membershipLevel === 'SILVER' ? '🥈' : 
               user.membershipLevel === 'GOLD' ? '🥇' : '💎'}
            </div>
            <h3 style={{ color: getMembershipColor(user.membershipLevel) }}>
              {user.membershipLevel}
            </h3>
          </div>
        </div>
        
        <div style={{ textAlign: 'center', marginTop: '20px' }}>
          <h2>포인트: {user.points.toLocaleString()}점</h2>
          <div style={{ 
            width: '100%', 
            height: '20px', 
            backgroundColor: '#f0f0f0', 
            borderRadius: '10px',
            overflow: 'hidden'
          }}>
            <div style={{ 
              width: `${(user.points % 1000) / 10}%`, 
              height: '100%', 
              backgroundColor: getMembershipColor(user.membershipLevel),
              transition: 'width 0.5s ease'
            }}></div>
          </div>
          <p>다음 등급까지 {1000 - (user.points % 1000)}점 필요</p>
        </div>
      </div>

      {/* 최근 활동 */}
      <div className="card">
        <h3>최근 활동 기록</h3>
        <div style={{ display: 'grid', gap: '10px' }}>
          {recentRecords.length > 0 ? (
            recentRecords.map((record, index) => (
              <div key={index} style={{ 
                display: 'flex', 
                justifyContent: 'space-between', 
                alignItems: 'center',
                padding: '10px',
                backgroundColor: '#f8f9fa',
                borderRadius: '5px'
              }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                  <span style={{ fontSize: '1.5rem' }}>{getWasteTypeIcon(record.wasteType)}</span>
                  <span><strong>{record.wasteType}</strong></span>
                </div>
                <div style={{ textAlign: 'right' }}>
                  <span style={{ color: 'green', fontWeight: 'bold' }}>+{record.earnedPoints}점</span>
                  <br />
                  <small style={{ color: '#666' }}>
                    {new Date(record.recordedAt).toLocaleDateString('ko-KR')}
                  </small>
                </div>
              </div>
            ))
          ) : (
            <p style={{ textAlign: 'center', color: '#666' }}>아직 기록이 없습니다.</p>
          )}
        </div>
      </div>

      {/* 뉴스 및 알림 */}
      <div className="card">
        <h3>뉴스 및 알림</h3>
        <div style={{ display: 'grid', gap: '15px' }}>
          {news.map((item, index) => (
            <div key={index} style={{ 
              padding: '15px',
              backgroundColor: item.type === 'EVENT' ? '#fff3cd' : 
                             item.type === 'BADGE' ? '#d1ecf1' : '#d4edda',
              borderRadius: '5px',
              border: `1px solid ${
                item.type === 'EVENT' ? '#ffeaa7' : 
                item.type === 'BADGE' ? '#bee5eb' : '#c3e6cb'
              }`
            }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                <span style={{ fontSize: '1.2rem' }}>
                  {item.type === 'EVENT' ? '🎉' : 
                   item.type === 'BADGE' ? '🏆' : '📈'}
                </span>
                <h4 style={{ margin: 0 }}>{item.title}</h4>
              </div>
              <p style={{ margin: '5px 0 0 0', color: '#666' }}>{item.content}</p>
            </div>
          ))}
        </div>
      </div>

      {/* 빠른 액션 */}
      <div className="card">
        <h3>빠른 액션</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(150px, 1fr))', gap: '15px' }}>
          <button className="btn btn-primary">📱 쓰레기 인식하기</button>
          <button className="btn btn-success">🏆 랭킹 확인하기</button>
          <button className="btn btn-primary">📊 기록 보기</button>
          <button className="btn btn-success">🎖️ 뱃지 확인하기</button>
        </div>
      </div>
    </div>
  );
}

export default Main;
