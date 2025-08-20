import React, { useState, useEffect } from 'react';
import axios from 'axios';

function Ranking() {
  const [selectedCategory, setSelectedCategory] = useState('INDIVIDUAL');
  const [rankings, setRankings] = useState([]);
  const [userRank, setUserRank] = useState(null);
  const [user] = useState({
    nickname: '이가은',
    points: 300,
    rank: 4
  });

  const categories = [
    { key: 'INDIVIDUAL', name: '개인 랭킹', icon: '👤' },
    { key: 'COLLEGE', name: '단과대 랭킹', icon: '🏛️' },
    { key: 'CAMPUS', name: '캠퍼스 랭킹', icon: '🏫' }
  ];

  // 시뮬레이션 데이터
  const mockRankings = {
    INDIVIDUAL: [
      { rank: 1, nickname: '관리자', campus: 'Seoul', college: 'Computer Science', points: 1000, membershipLevel: 'GOLD' },
      { rank: 2, nickname: '설호', campus: 'Seoul', college: 'Computer Science', points: 750, membershipLevel: 'SILVER' },
      { rank: 3, nickname: '김지수', campus: 'Seoul', college: 'Engineering', points: 500, membershipLevel: 'SILVER' },
      { rank: 4, nickname: '이가은', campus: 'Seoul', college: 'Business', points: 300, membershipLevel: 'BRONZE' },
      { rank: 5, nickname: '이호승', campus: 'Seoul', college: 'Arts', points: 200, membershipLevel: 'BRONZE' },
      { rank: 6, nickname: '안예영', campus: 'Seoul', college: 'Science', points: 100, membershipLevel: 'BRONZE' }
    ],
    COLLEGE: [
      { rank: 1, name: 'Computer Science', points: 1750, memberCount: 2 },
      { rank: 2, name: 'Engineering', points: 500, memberCount: 1 },
      { rank: 3, name: 'Business', points: 300, memberCount: 1 },
      { rank: 4, name: 'Arts', points: 200, memberCount: 1 },
      { rank: 5, name: 'Science', points: 100, memberCount: 1 }
    ],
    CAMPUS: [
      { rank: 1, name: 'Seoul Campus', points: 2850, memberCount: 6 }
    ]
  };

  useEffect(() => {
    setRankings(mockRankings[selectedCategory]);
    
    if (selectedCategory === 'INDIVIDUAL') {
      setUserRank(mockRankings.INDIVIDUAL.find(r => r.nickname === user.nickname));
    }
  }, [selectedCategory, user.nickname]);

  const getMembershipIcon = (level) => {
    switch (level) {
      case 'BRONZE': return '🥉';
      case 'SILVER': return '🥈';
      case 'GOLD': return '🥇';
      case 'PLATINUM': return '💎';
      default: return '🥉';
    }
  };

  const getRankIcon = (rank) => {
    switch (rank) {
      case 1: return '🥇';
      case 2: return '🥈';
      case 3: return '🥉';
      default: return `#${rank}`;
    }
  };

  const getRankColor = (rank) => {
    switch (rank) {
      case 1: return '#ffd700';
      case 2: return '#c0c0c0';
      case 3: return '#cd7f32';
      default: return '#666';
    }
  };

  return (
    <div>
      <h1 className="text-center">🏆 랭킹 시스템</h1>
      
      {/* 사용자 정보 */}
      <div className="card">
        <div style={{ textAlign: 'center' }}>
          <h2>{user.nickname}님의 랭킹</h2>
          <div style={{ 
            display: 'flex', 
            justifyContent: 'center', 
            alignItems: 'center', 
            gap: '20px',
            marginTop: '15px'
          }}>
            <div style={{ 
              fontSize: '3rem', 
              color: getRankColor(user.rank)
            }}>
              {getRankIcon(user.rank)}
            </div>
            <div>
              <h3 style={{ margin: '0 0 5px 0', color: getRankColor(user.rank) }}>
                {user.rank}위
              </h3>
              <p style={{ margin: '0', fontSize: '1.2rem' }}>
                <strong>{user.points.toLocaleString()}점</strong>
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* 카테고리 선택 */}
      <div className="card">
        <h3>랭킹 카테고리</h3>
        <div style={{ 
          display: 'grid', 
          gridTemplateColumns: 'repeat(auto-fit, minmax(150px, 1fr))', 
          gap: '15px'
        }}>
          {categories.map((category) => (
            <button
              key={category.key}
              onClick={() => setSelectedCategory(category.key)}
              style={{
                padding: '15px',
                border: selectedCategory === category.key ? '3px solid #007bff' : '1px solid #ddd',
                borderRadius: '10px',
                backgroundColor: selectedCategory === category.key ? '#e3f2fd' : 'white',
                cursor: 'pointer',
                transition: 'all 0.3s ease',
                textAlign: 'center'
              }}
            >
              <div style={{ fontSize: '2rem', marginBottom: '5px' }}>{category.icon}</div>
              <div style={{ fontWeight: 'bold' }}>{category.name}</div>
            </button>
          ))}
        </div>
      </div>

      {/* 랭킹 표 */}
      <div className="card">
        <h3>{categories.find(c => c.key === selectedCategory)?.name}</h3>
        <div style={{ display: 'grid', gap: '10px' }}>
          {rankings.map((item, index) => (
            <div key={index} style={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              padding: '15px',
              backgroundColor: item.rank <= 3 ? '#fff3cd' : '#f8f9fa',
              borderRadius: '10px',
              border: item.rank <= 3 ? '2px solid #ffeaa7' : '1px solid #dee2e6'
            }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '15px' }}>
                <div style={{ 
                  fontSize: '2rem', 
                  color: getRankColor(item.rank),
                  minWidth: '50px',
                  textAlign: 'center'
                }}>
                  {getRankIcon(item.rank)}
                </div>
                <div>
                  <h4 style={{ margin: '0 0 5px 0' }}>
                    {selectedCategory === 'INDIVIDUAL' ? item.nickname : item.name}
                  </h4>
                  {selectedCategory === 'INDIVIDUAL' && (
                    <p style={{ margin: '0', color: '#666', fontSize: '0.9rem' }}>
                      {item.campus} • {item.college}
                    </p>
                  )}
                  {selectedCategory !== 'INDIVIDUAL' && (
                    <p style={{ margin: '0', color: '#666', fontSize: '0.9rem' }}>
                      멤버: {item.memberCount}명
                    </p>
                  )}
                </div>
              </div>
              <div style={{ textAlign: 'right' }}>
                <div style={{ 
                  fontSize: '1.5rem', 
                  fontWeight: 'bold', 
                  color: '#28a745'
                }}>
                  {item.points.toLocaleString()}점
                </div>
                {selectedCategory === 'INDIVIDUAL' && (
                  <div style={{ 
                    fontSize: '1rem', 
                    color: '#666',
                    marginTop: '5px'
                  }}>
                    {getMembershipIcon(item.membershipLevel)} {item.membershipLevel}
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* 보상 시스템 */}
      <div className="card">
        <h3>🎁 랭킹 보상</h3>
        <div style={{ display: 'grid', gap: '15px' }}>
          <div style={{ 
            padding: '15px', 
            backgroundColor: '#fff3cd', 
            borderRadius: '10px',
            border: '1px solid #ffeaa7'
          }}>
            <h4 style={{ margin: '0 0 10px 0', color: '#856404' }}>🥇 1위 보상</h4>
            <p style={{ margin: '0', color: '#856404' }}>
              특별 뱃지 + 포인트 2배 보너스 + 환경의 날 초대권
            </p>
          </div>
          
          <div style={{ 
            padding: '15px', 
            backgroundColor: '#d1ecf1', 
            borderRadius: '10px',
            border: '1px solid #bee5eb'
          }}>
            <h4 style={{ margin: '0 0 10px 0', color: '#0c5460' }}>🥈 2-3위 보상</h4>
            <p style={{ margin: '0', color: '#0c5460' }}>
              특별 뱃지 + 포인트 1.5배 보너스
            </p>
          </div>
          
          <div style={{ 
            padding: '15px', 
            backgroundColor: '#d4edda', 
            borderRadius: '10px',
            border: '1px solid #c3e6cb'
          }}>
            <h4 style={{ margin: '0 0 10px 0', color: '#155724' }}>🏅 4-10위 보상</h4>
            <p style={{ margin: '0', color: '#155724' }}>
              포인트 1.2배 보너스
            </p>
          </div>
        </div>
      </div>

      {/* 랭킹 팁 */}
      <div className="card">
        <h3>💡 랭킹 올리는 팁</h3>
        <div style={{ display: 'grid', gap: '10px' }}>
          <div style={{ padding: '10px', backgroundColor: '#f8f9fa', borderRadius: '5px' }}>
            <strong>📱 매일 쓰레기 분리:</strong> 꾸준한 활동이 포인트의 핵심입니다
          </div>
          <div style={{ padding: '10px', backgroundColor: '#f8f9fa', borderRadius: '5px' }}>
            <strong>🥤 고점수 쓰레기:</strong> 유리(20점), 캔(15점)을 우선적으로 분리하세요
          </div>
          <div style={{ padding: '10px', backgroundColor: '#f8f9fa', borderRadius: '5px' }}>
            <strong>🏆 이벤트 참여:</strong> 특별 이벤트에서 추가 포인트를 획득하세요
          </div>
          <div style={{ padding: '10px', backgroundColor: '#f8f9fa', borderRadius: '5px' }}>
            <strong>👥 친구 초대:</strong> 친구를 초대하면 보너스 포인트를 받을 수 있습니다
          </div>
        </div>
      </div>
    </div>
  );
}

export default Ranking;
