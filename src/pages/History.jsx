import React, { useState } from 'react';

function History() {
  const [user] = useState({
    nickname: '안예영',
    points: 100
  });

  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);
  const [selectedView, setSelectedView] = useState('week');

  const [weeklyStats] = useState({
    totalPoints: 45,
    totalItems: 9,
    averagePerDay: 6.4,
    streak: 7,
    mostRecycled: 'PET',
    weeklyGoal: 50,
    goalProgress: 90
  });

  const [dailyRecords] = useState([
    { date: '2024-01-15', items: [
      { type: 'PET', points: 10, time: '14:30' },
      { type: 'PAPER', points: 5, time: '16:45' }
    ], totalPoints: 15 },
    { date: '2024-01-14', items: [
      { type: 'CAN', points: 15, time: '09:15' },
      { type: 'GLASS', points: 20, time: '18:20' }
    ], totalPoints: 35 },
    { date: '2024-01-13', items: [
      { type: 'PET', points: 10, time: '12:30' }
    ], totalPoints: 10 },
    { date: '2024-01-12', items: [
      { type: 'PAPER', points: 5, time: '15:10' },
      { type: 'PLASTIC', points: 8, time: '19:45' }
    ], totalPoints: 13 },
    { date: '2024-01-11', items: [
      { type: 'CAN', points: 15, time: '11:20' }
    ], totalPoints: 15 },
    { date: '2024-01-10', items: [
      { type: 'PET', points: 10, time: '13:15' },
      { type: 'PAPER', points: 5, time: '17:30' }
    ], totalPoints: 15 },
    { date: '2024-01-09', items: [
      { type: 'GLASS', points: 20, time: '10:45' }
    ], totalPoints: 20 }
  ]);

  const [monthlyStats] = useState({
    totalPoints: 123,
    totalItems: 25,
    averagePerDay: 4.1,
    topWasteType: 'PET',
    topWasteCount: 8,
    environmentalImpact: 'CO2 2.3kg 감소'
  });

  const getWasteTypeIcon = (type) => {
    switch (type) {
      case 'PET': return '🥤';
      case 'CAN': return '🥫';
      case 'PAPER': return '📄';
      case 'GLASS': return '🥃';
      case 'PLASTIC': return '🔄';
      default: return '♻️';
    }
  };

  const getWasteTypeColor = (type) => {
    switch (type) {
      case 'PET': return '#007bff';
      case 'CAN': return '#28a745';
      case 'PAPER': return '#ffc107';
      case 'GLASS': return '#17a2b8';
      case 'PLASTIC': return '#6f42c1';
      default: return '#6c757d';
    }
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const today = new Date();
    const diffTime = Math.abs(today - date);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    
    if (diffDays === 1) return '어제';
    if (diffDays === 0) return '오늘';
    return `${diffDays}일 전`;
  };

  const getDayOfWeek = (dateString) => {
    const date = new Date(dateString);
    const days = ['일', '월', '화', '수', '목', '금', '토'];
    return days[date.getDay()];
  };

  return (
    <div>
      <h1 className="text-center">📊 활동 기록</h1>
      
      {/* 사용자 정보 */}
      <div className="card">
        <div style={{ textAlign: 'center' }}>
          <h2>{user.nickname}님의 활동 기록</h2>
          <p>현재 포인트: <strong>{user.points.toLocaleString()}점</strong></p>
        </div>
      </div>

      {/* 뷰 선택 */}
      <div className="card">
        <h3>보기 방식</h3>
        <div style={{ 
          display: 'grid', 
          gridTemplateColumns: 'repeat(auto-fit, minmax(120px, 1fr))', 
          gap: '15px'
        }}>
          <button
            onClick={() => setSelectedView('week')}
            style={{
              padding: '15px',
              border: selectedView === 'week' ? '3px solid #007bff' : '1px solid #ddd',
              borderRadius: '10px',
              backgroundColor: selectedView === 'week' ? '#e3f2fd' : 'white',
              cursor: 'pointer',
              transition: 'all 0.3s ease'
            }}
          >
            <div style={{ fontSize: '2rem', marginBottom: '5px' }}>📅</div>
            <div style={{ fontWeight: 'bold' }}>주간 보기</div>
          </button>
          
          <button
            onClick={() => setSelectedView('month')}
            style={{
              padding: '15px',
              border: selectedView === 'month' ? '3px solid #007bff' : '1px solid #ddd',
              borderRadius: '10px',
              backgroundColor: selectedView === 'month' ? '#e3f2fd' : 'white',
              cursor: 'pointer',
              transition: 'all 0.3s ease'
            }}
          >
            <div style={{ fontSize: '2rem', marginBottom: '5px' }}>📆</div>
            <div style={{ fontWeight: 'bold' }}>월간 보기</div>
          </button>
          
          <button
            onClick={() => setSelectedView('calendar')}
            style={{
              padding: '15px',
              border: selectedView === 'calendar' ? '3px solid #007bff' : '1px solid #ddd',
              borderRadius: '10px',
              backgroundColor: selectedView === 'calendar' ? '#e3f2fd' : 'white',
              cursor: 'pointer',
              transition: 'all 0.3s ease'
            }}
          >
            <div style={{ fontSize: '2rem', marginBottom: '5px' }}>🗓️</div>
            <div style={{ fontWeight: 'bold' }}>캘린더 보기</div>
          </button>
        </div>
      </div>

      {/* 주간 통계 */}
      {selectedView === 'week' && (
        <div className="card">
          <h3>📈 주간 통계</h3>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '15px', marginBottom: '20px' }}>
            <div style={{ textAlign: 'center', padding: '15px', backgroundColor: '#f8f9fa', borderRadius: '10px' }}>
              <div style={{ fontSize: '2rem', marginBottom: '5px' }}>🎯</div>
              <h4 style={{ margin: '0 0 5px 0' }}>주간 목표</h4>
              <p style={{ margin: '0', fontSize: '1.5rem', fontWeight: 'bold', color: '#28a745' }}>
                {weeklyStats.goalProgress}%
              </p>
              <p style={{ margin: '5px 0 0 0', fontSize: '0.9rem', color: '#666' }}>
                {weeklyStats.totalPoints}/{weeklyStats.weeklyGoal}점
              </p>
            </div>
            
            <div style={{ textAlign: 'center', padding: '15px', backgroundColor: '#f8f9fa', borderRadius: '10px' }}>
              <div style={{ fontSize: '2rem', marginBottom: '5px' }}>🔥</div>
              <h4 style={{ margin: '0 0 5px 0' }}>연속 기록</h4>
              <p style={{ margin: '0', fontSize: '1.5rem', fontWeight: 'bold', color: '#dc3545' }}>
                {weeklyStats.streak}일
              </p>
              <p style={{ margin: '5px 0 0 0', fontSize: '0.9rem', color: '#666' }}>
                연속 분리 중
              </p>
            </div>
            
            <div style={{ textAlign: 'center', padding: '15px', backgroundColor: '#f8f9fa', borderRadius: '10px' }}>
              <div style={{ fontSize: '2rem', marginBottom: '5px' }}>📊</div>
              <h4 style={{ margin: '0 0 5px 0' }}>일평균</h4>
              <p style={{ margin: '0', fontSize: '1.5rem', fontWeight: 'bold', color: '#007bff' }}>
                {weeklyStats.averagePerDay}점
              </p>
              <p style={{ margin: '5px 0 0 0', fontSize: '0.9rem', color: '#666' }}>
                총 {weeklyStats.totalItems}개
              </p>
            </div>
            
            <div style={{ textAlign: 'center', padding: '15px', backgroundColor: '#f8f9fa', borderRadius: '10px' }}>
              <div style={{ fontSize: '2rem', marginBottom: '5px' }}>🥤</div>
              <h4 style={{ margin: '0 0 5px 0' }}>최다 분리</h4>
              <p style={{ margin: '0', fontSize: '1.5rem', fontWeight: 'bold', color: '#ffc107' }}>
                {weeklyStats.mostRecycled}
              </p>
              <p style={{ margin: '5px 0 0 0', fontSize: '0.9rem', color: '#666' }}>
                가장 많이 분리
              </p>
            </div>
          </div>

          {/* 주간 목표 진행률 */}
          <div style={{ marginBottom: '20px' }}>
            <h4>주간 목표 진행률</h4>
            <div style={{ 
              width: '100%', 
              height: '20px', 
              backgroundColor: '#f0f0f0', 
              borderRadius: '10px',
              overflow: 'hidden'
            }}>
              <div style={{ 
                width: `${weeklyStats.goalProgress}%`, 
                height: '100%', 
                backgroundColor: '#28a745',
                transition: 'width 0.5s ease'
              }}></div>
            </div>
            <p style={{ textAlign: 'center', marginTop: '10px' }}>
              목표 달성까지 <strong>{weeklyStats.weeklyGoal - weeklyStats.totalPoints}점</strong> 남았습니다!
            </p>
          </div>
        </div>
      )}

      {/* 월간 통계 */}
      {selectedView === 'month' && (
        <div className="card">
          <h3>📊 월간 통계</h3>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '15px' }}>
            <div style={{ textAlign: 'center', padding: '15px', backgroundColor: '#f8f9fa', borderRadius: '10px' }}>
              <div style={{ fontSize: '2rem', marginBottom: '5px' }}>💎</div>
              <h4 style={{ margin: '0 0 5px 0' }}>총 포인트</h4>
              <p style={{ margin: '0', fontSize: '1.5rem', fontWeight: 'bold', color: '#28a745' }}>
                {monthlyStats.totalPoints}점
              </p>
            </div>
            
            <div style={{ textAlign: 'center', padding: '15px', backgroundColor: '#f8f9fa', borderRadius: '10px' }}>
              <div style={{ fontSize: '2rem', marginBottom: '5px' }}>♻️</div>
              <h4 style={{ margin: '0 0 5px 0' }}>총 분리</h4>
              <p style={{ margin: '0', fontSize: '1.5rem', fontWeight: 'bold', color: '#007bff' }}>
                {monthlyStats.totalItems}개
              </p>
            </div>
            
            <div style={{ textAlign: 'center', padding: '15px', backgroundColor: '#f8f9fa', borderRadius: '10px' }}>
              <div style={{ fontSize: '2rem', marginBottom: '5px' }}>🥤</div>
              <h4 style={{ margin: '0 0 5px 0' }}>최다 분리</h4>
              <p style={{ margin: '0', fontSize: '1.5rem', fontWeight: 'bold', color: '#ffc107' }}>
                {monthlyStats.topWasteType}
              </p>
              <p style={{ margin: '5px 0 0 0', fontSize: '0.9rem', color: '#666' }}>
                {monthlyStats.topWasteCount}회
              </p>
            </div>
            
            <div style={{ textAlign: 'center', padding: '15px', backgroundColor: '#f8f9fa', borderRadius: '10px' }}>
              <div style={{ fontSize: '2rem', marginBottom: '5px' }}>🌍</div>
              <h4 style={{ margin: '0 0 5px 0' }}>환경 기여</h4>
              <p style={{ margin: '0', fontSize: '1.2rem', fontWeight: 'bold', color: '#28a745' }}>
                {monthlyStats.environmentalImpact}
              </p>
            </div>
          </div>
        </div>
      )}

      {/* 일별 상세 기록 */}
      <div className="card">
        <h3>📝 상세 기록</h3>
        <div style={{ display: 'grid', gap: '15px' }}>
          {dailyRecords.map((dayRecord, index) => (
            <div key={index} style={{
              padding: '15px',
              backgroundColor: '#f8f9fa',
              borderRadius: '10px',
              border: '1px solid #dee2e6'
            }}>
              <div style={{ 
                display: 'flex', 
                justifyContent: 'space-between', 
                alignItems: 'center',
                marginBottom: '10px'
              }}>
                <h4 style={{ margin: '0' }}>
                  {dayRecord.date} ({getDayOfWeek(dayRecord.date)})
                </h4>
                <div style={{ 
                  fontSize: '1.2rem', 
                  fontWeight: 'bold', 
                  color: '#28a745'
                }}>
                  +{dayRecord.totalPoints}점
                </div>
              </div>
              
              <div style={{ display: 'grid', gap: '8px' }}>
                {dayRecord.items.map((item, itemIndex) => (
                  <div key={itemIndex} style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    padding: '8px',
                    backgroundColor: 'white',
                    borderRadius: '5px',
                    border: '1px solid #e9ecef'
                  }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                      <span style={{ fontSize: '1.2rem' }}>{getWasteTypeIcon(item.type)}</span>
                      <span style={{ fontWeight: 'bold' }}>{item.type}</span>
                      <span style={{ color: '#666', fontSize: '0.9rem' }}>{item.time}</span>
                    </div>
                    <span style={{ 
                      color: 'green', 
                      fontWeight: 'bold'
                    }}>
                      +{item.points}점
                    </span>
                  </div>
                ))}
              </div>
              
              <div style={{ 
                textAlign: 'right', 
                marginTop: '10px',
                fontSize: '0.9rem',
                color: '#666'
              }}>
                {formatDate(dayRecord.date)}
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* 환경 기여도 */}
      <div className="card">
        <h3>🌍 환경 기여도</h3>
        <div style={{ display: 'grid', gap: '15px' }}>
          <div style={{ 
            padding: '15px', 
            backgroundColor: '#d4edda', 
            borderRadius: '10px',
            border: '1px solid #c3e6cb'
          }}>
            <h4 style={{ margin: '0 0 10px 0', color: '#155724' }}>이번 주 성과</h4>
            <p style={{ margin: '0', color: '#155724' }}>
              총 {weeklyStats.totalItems}개의 쓰레기를 올바르게 분리하여 
              <strong> CO2 {Math.round(weeklyStats.totalItems * 0.1 * 10) / 10}kg</strong>를 감소시켰습니다.
            </p>
          </div>
          
          <div style={{ 
            padding: '15px', 
            backgroundColor: '#fff3cd', 
            borderRadius: '10px',
            border: '1px solid #ffeaa7'
          }}>
            <h4 style={{ margin: '0 0 10px 0', color: '#856404' }}>다음 주 목표</h4>
            <p style={{ margin: '0', color: '#856404' }}>
              연속 기록을 유지하고 <strong>일평균 8점</strong> 이상을 달성하여 
              환경 보호에 더욱 기여해보세요!
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default History;
