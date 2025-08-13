import React, { useState } from 'react';
import axios from 'axios';

function Camera() {
  const [selectedWasteType, setSelectedWasteType] = useState('');
  const [isProcessing, setIsProcessing] = useState(false);
  const [result, setResult] = useState(null);
  const [user] = useState({
    nickname: '김지수',
    points: 500
  });

  const wasteTypes = [
    { type: 'PET', icon: '🥤', points: 10, description: '플라스틱 병' },
    { type: 'CAN', icon: '🥫', points: 15, description: '알루미늄 캔' },
    { type: 'PAPER', icon: '📄', points: 5, description: '종이류' },
    { type: 'GLASS', icon: '🥃', points: 20, description: '유리병' },
    { type: 'PLASTIC', icon: '🔄', points: 8, description: '일반 플라스틱' }
  ];

  const handleWasteRecognition = async () => {
    if (!selectedWasteType) {
      alert('쓰레기 종류를 선택해주세요!');
      return;
    }

    setIsProcessing(true);
    setResult(null);

    try {
      // 실제 API 호출 시뮬레이션
      const response = await axios.post('/api/camera/recognize', {
        wasteType: selectedWasteType,
        userId: 2
      });

      // 성공 결과 시뮬레이션
      const selectedWaste = wasteTypes.find(w => w.type === selectedWasteType);
      const success = Math.random() > 0.2; // 80% 성공률

      if (success) {
        setResult({
          success: true,
          wasteType: selectedWasteType,
          earnedPoints: selectedWaste.points,
          message: '오늘 북극곰 한 마리를 구했습니다! 🐻‍❄️',
          newTotalPoints: user.points + selectedWaste.points
        });
      } else {
        setResult({
          success: false,
          message: '쓰레기 분리가 올바르지 않습니다. 다시 시도해주세요.',
          wasteType: selectedWasteType
        });
      }
    } catch (error) {
      setResult({
        success: false,
        message: '인식 중 오류가 발생했습니다. 다시 시도해주세요.'
      });
    } finally {
      setIsProcessing(false);
    }
  };

  const resetResult = () => {
    setResult(null);
    setSelectedWasteType('');
  };

  return (
    <div>
      <h1 className="text-center">📱 쓰레기 인식 카메라</h1>
      
      {/* 사용자 정보 */}
      <div className="card">
        <div style={{ textAlign: 'center' }}>
          <h2>{user.nickname}님의 쓰레기 분리</h2>
          <p>현재 포인트: <strong>{user.points.toLocaleString()}점</strong></p>
        </div>
      </div>

      {/* 카메라 시뮬레이션 */}
      <div className="card">
        <h3>카메라 화면</h3>
        <div style={{ 
          width: '100%', 
          height: '300px', 
          backgroundColor: '#000', 
          borderRadius: '10px',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          color: 'white',
          fontSize: '2rem',
          marginBottom: '20px'
        }}>
          {isProcessing ? '🔍 인식 중...' : '📷 카메라 준비됨'}
        </div>

        {/* 쓰레기 종류 선택 */}
        <h4>쓰레기 종류 선택:</h4>
        <div style={{ 
          display: 'grid', 
          gridTemplateColumns: 'repeat(auto-fit, minmax(120px, 1fr))', 
          gap: '10px',
          marginBottom: '20px'
        }}>
          {wasteTypes.map((waste) => (
            <button
              key={waste.type}
              onClick={() => setSelectedWasteType(waste.type)}
              style={{
                padding: '15px',
                border: selectedWasteType === waste.type ? '3px solid #007bff' : '1px solid #ddd',
                borderRadius: '10px',
                backgroundColor: selectedWasteType === waste.type ? '#e3f2fd' : 'white',
                cursor: 'pointer',
                transition: 'all 0.3s ease'
              }}
            >
              <div style={{ fontSize: '2rem', marginBottom: '5px' }}>{waste.icon}</div>
              <div style={{ fontWeight: 'bold' }}>{waste.type}</div>
              <div style={{ fontSize: '0.8rem', color: '#666' }}>{waste.description}</div>
              <div style={{ fontSize: '0.9rem', color: 'green', fontWeight: 'bold' }}>
                +{waste.points}점
              </div>
            </button>
          ))}
        </div>

        <button
          onClick={handleWasteRecognition}
          disabled={!selectedWasteType || isProcessing}
          className="btn btn-primary"
          style={{ width: '100%', fontSize: '1.2rem', padding: '15px' }}
        >
          {isProcessing ? '인식 중...' : '쓰레기 인식하기'}
        </button>
      </div>

      {/* 결과 표시 */}
      {result && (
        <div className="card">
          <h3>인식 결과</h3>
          <div style={{
            padding: '20px',
            backgroundColor: result.success ? '#d4edda' : '#f8d7da',
            borderRadius: '10px',
            border: `1px solid ${result.success ? '#c3e6cb' : '#f5c6cb'}`,
            textAlign: 'center'
          }}>
            {result.success ? (
              <>
                <div style={{ fontSize: '4rem', marginBottom: '15px' }}>🎉</div>
                <h4 style={{ color: '#155724', marginBottom: '10px' }}>인식 성공!</h4>
                <p style={{ color: '#155724', marginBottom: '15px' }}>
                  <strong>{result.wasteType}</strong> 쓰레기로 인식되었습니다.
                </p>
                <div style={{ 
                  fontSize: '2rem', 
                  color: 'green', 
                  fontWeight: 'bold',
                  marginBottom: '10px'
                }}>
                  +{result.earnedPoints}점 획득!
                </div>
                <p style={{ color: '#155724', marginBottom: '15px' }}>
                  {result.message}
                </p>
                <p style={{ color: '#155724' }}>
                  총 포인트: <strong>{result.newTotalPoints.toLocaleString()}점</strong>
                </p>
              </>
            ) : (
              <>
                <div style={{ fontSize: '4rem', marginBottom: '15px' }}>❌</div>
                <h4 style={{ color: '#721c24', marginBottom: '10px' }}>인식 실패</h4>
                <p style={{ color: '#721c24' }}>{result.message}</p>
                {result.wasteType && (
                  <p style={{ color: '#721c24' }}>
                    선택된 종류: <strong>{result.wasteType}</strong>
                  </p>
                )}
              </>
            )}
            
            <button
              onClick={resetResult}
              className="btn btn-primary"
              style={{ marginTop: '15px' }}
            >
              다시 시도하기
            </button>
          </div>
        </div>
      )}

      {/* 사용 팁 */}
      <div className="card">
        <h3>💡 쓰레기 분리 팁</h3>
        <div style={{ display: 'grid', gap: '10px' }}>
          <div style={{ padding: '10px', backgroundColor: '#f8f9fa', borderRadius: '5px' }}>
            <strong>🥤 PET:</strong> 내용물을 깨끗이 비우고 라벨을 제거하세요
          </div>
          <div style={{ padding: '10px', backgroundColor: '#f8f9fa', borderRadius: '5px' }}>
            <strong>🥫 CAN:</strong> 내용물을 완전히 비우고 압축하세요
          </div>
          <div style={{ padding: '10px', backgroundColor: '#f8f9fa', borderRadius: '5px' }}>
            <strong>📄 PAPER:</strong> 오염되지 않은 종이만 분리하세요
          </div>
          <div style={{ padding: '10px', backgroundColor: '#f8f9fa', borderRadius: '5px' }}>
            <strong>🥃 GLASS:</strong> 깨진 유리는 별도로 처리하세요
          </div>
        </div>
      </div>
    </div>
  );
}

export default Camera;
