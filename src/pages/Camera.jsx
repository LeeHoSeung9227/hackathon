import React, { useState, useRef } from 'react';
import axios from 'axios';

function Camera() {
  const [selectedImage, setSelectedImage] = useState(null);
  const [imagePreview, setImagePreview] = useState(null);
  const [isProcessing, setIsProcessing] = useState(false);
  const [result, setResult] = useState(null);
  const [user] = useState({
    nickname: '김지수',
    points: 500
  });
  
  const fileInputRef = useRef(null);

  const wasteTypes = [
    { type: 'PET', icon: '🥤', points: 10, description: '플라스틱 병' },
    { type: 'CAN', icon: '🥫', points: 15, description: '알루미늄 캔' },
    { type: 'PAPER', icon: '📄', points: 5, description: '종이류' },
    { type: 'GLASS', icon: '🥃', points: 20, description: '유리병' },
    { type: 'PLASTIC', icon: '🔄', points: 8, description: '일반 플라스틱' }
  ];

  // 이미지 선택 처리
  const handleImageSelect = (event) => {
    const file = event.target.files[0];
    if (file) {
      setSelectedImage(file);
      
      // 이미지 미리보기 생성
      const reader = new FileReader();
      reader.onload = (e) => {
        setImagePreview(e.target.result);
      };
      reader.readAsDataURL(file);
    }
  };

  // 카메라 버튼 클릭
  const handleCameraClick = () => {
    fileInputRef.current.click();
  };

  // AI 이미지 분석
  const handleWasteRecognition = async () => {
    if (!selectedImage) {
      alert('이미지를 선택해주세요!');
      return;
    }

    setIsProcessing(true);
    setResult(null);

    try {
      // FormData 생성
      const formData = new FormData();
      formData.append('image', selectedImage);
      formData.append('userId', '1'); // 테스트용 사용자 ID

      // AI 분석 API 호출
      const response = await axios.post('/api/ai/analyze', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      const apiResult = response.data;

      if (apiResult.success) {
        // 분석 성공
        setResult({
          success: true,
          wasteType: apiResult.wasteType || '분석 중...',
          earnedPoints: apiResult.points || 0,
          message: `AI가 이미지를 분석했습니다! 🎉`,
          newTotalPoints: user.points + (apiResult.points || 0),
          details: {
            analysis: apiResult.analysis || '분석 결과가 없습니다.',
            points: apiResult.points || 0
          }
        });
      } else {
        // API 오류
        setResult({
          success: false,
          message: apiResult.message || 'AI 분석 중 오류가 발생했습니다.',
          details: {
            analysis: '',
            points: 0
          }
        });
      }
    } catch (error) {
      console.error('API 호출 오류:', error);
      setResult({
        success: false,
        message: '서버 연결 오류가 발생했습니다. 다시 시도해주세요.',
        details: {
          analysis: '',
          points: 0
        }
      });
    } finally {
      setIsProcessing(false);
    }
  };

  const resetResult = () => {
    setResult(null);
    setSelectedImage(null);
    setImagePreview(null);
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
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

      {/* 카메라/이미지 업로드 */}
      <div className="card">
        <h3>📷 이미지 업로드</h3>
        
        {/* 이미지 미리보기 */}
        <div style={{ 
          width: '100%', 
          height: '300px', 
          backgroundColor: '#f8f9fa', 
          borderRadius: '10px',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          marginBottom: '20px',
          border: '2px dashed #dee2e6',
          overflow: 'hidden'
        }}>
          {imagePreview ? (
            <img 
              src={imagePreview} 
              alt="업로드된 이미지" 
              style={{ 
                maxWidth: '100%', 
                maxHeight: '100%', 
                objectFit: 'contain' 
              }} 
            />
          ) : (
            <div style={{ textAlign: 'center', color: '#6c757d' }}>
              <div style={{ fontSize: '3rem', marginBottom: '10px' }}>📷</div>
              <div>이미지를 선택하거나 촬영하세요</div>
            </div>
          )}
        </div>

        {/* 파일 입력 (숨김) */}
        <input
          ref={fileInputRef}
          type="file"
          accept="image/*"
          onChange={handleImageSelect}
          style={{ display: 'none' }}
        />

        {/* 카메라/업로드 버튼 */}
        <div style={{ display: 'flex', gap: '10px', marginBottom: '20px' }}>
          <button
            onClick={handleCameraClick}
            className="btn btn-primary"
            style={{ flex: 1 }}
          >
            📷 이미지 선택
          </button>
        </div>

        {/* AI 이미지 분석 버튼 */}
        <button
          onClick={handleWasteRecognition}
          disabled={!selectedImage || isProcessing}
          className="btn btn-success"
          style={{ 
            width: '100%', 
            fontSize: '1.2rem', 
            padding: '15px',
            backgroundColor: isProcessing ? '#6c757d' : '#28a745'
          }}
        >
          {isProcessing ? '🔍 AI 분석 중...' : '🤖 AI 이미지 분석하기'}
        </button>
      </div>

      {/* 결과 표시 */}
      {result && (
        <div className="card">
          <h3>🤖 AI 탐지 결과</h3>
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
                <h4 style={{ color: '#155724', marginBottom: '10px' }}>AI 탐지 성공!</h4>
                <p style={{ color: '#155724', marginBottom: '15px' }}>
                  <strong>{result.wasteType}</strong>로 인식되었습니다.
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
                
                {/* 상세 정보 */}
                {result.details && result.details.count > 0 && (
                  <div style={{ 
                    marginTop: '15px', 
                    padding: '15px', 
                    backgroundColor: 'rgba(255,255,255,0.5)', 
                    borderRadius: '8px' 
                  }}>
                    <h5>🔍 탐지 상세 정보</h5>
                    <p>탐지된 객체 수: <strong>{result.details.count}개</strong></p>
                    <p>평균 신뢰도: <strong>{(result.details.confidence.reduce((a, b) => a + b, 0) / result.details.confidence.length * 100).toFixed(1)}%</strong></p>
                  </div>
                )}
              </>
            ) : (
              <>
                <div style={{ fontSize: '4rem', marginBottom: '15px' }}>❌</div>
                <h4 style={{ color: '#721c24', marginBottom: '10px' }}>탐지 실패</h4>
                <p style={{ color: '#721c24' }}>{result.message}</p>
                
                {/* 상세 정보 */}
                {result.details && (
                  <div style={{ 
                    marginTop: '15px', 
                    padding: '15px', 
                    backgroundColor: 'rgba(255,255,255,0.5)', 
                    borderRadius: '8px' 
                  }}>
                    <h5>🔍 분석 결과</h5>
                    <p>탐지된 객체 수: <strong>{result.details.count}개</strong></p>
                  </div>
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
        <h3>💡 AI 쓰레기 탐지 팁</h3>
        <div style={{ display: 'grid', gap: '10px' }}>
          <div style={{ padding: '10px', backgroundColor: '#f8f9fa', borderRadius: '5px' }}>
            <strong>📷 이미지 품질:</strong> 밝고 선명한 이미지를 사용하세요
          </div>
          <div style={{ padding: '10px', backgroundColor: '#f8f9fa', borderRadius: '5px' }}>
            <strong>🔍 쓰레기 위치:</strong> 쓰레기가 이미지 중앙에 오도록 촬영하세요
          </div>
          <div style={{ padding: '10px', backgroundColor: '#f8f9fa', borderRadius: '5px' }}>
            <strong>💡 조명:</strong> 그림자가 생기지 않도록 균등한 조명을 사용하세요
          </div>
          <div style={{ padding: '10px', backgroundColor: '#f8f9fa', borderRadius: '5px' }}>
            <strong>🎯 배경:</strong> 복잡한 배경보다는 단순한 배경에서 촬영하세요
          </div>
        </div>
      </div>
    </div>
  );
}

export default Camera;
