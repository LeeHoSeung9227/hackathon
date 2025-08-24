import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './IntegratedTestPage.css';

const IntegratedTestPage = () => {
  const [userId, setUserId] = useState('1');
  const [points, setPoints] = useState(0);
  const [pointHistory, setPointHistory] = useState([]);
  const [products, setProducts] = useState([]);
  const [badges, setBadges] = useState([]);
  const [userBadges, setUserBadges] = useState([]);
  const [rankings, setRankings] = useState([]);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  
  // 사진 업로드 관련
  const [selectedImage, setSelectedImage] = useState(null);
  const [imagePreview, setImagePreview] = useState(null);
  const [analysisResult, setAnalysisResult] = useState(null);
  
  // 포인트 추가 관련
  const [pointAmount, setPointAmount] = useState(100);
  const [pointDescription, setPointDescription] = useState('');
  
  // 상품 구매 관련
  const [selectedProduct, setSelectedProduct] = useState('');
  
  // 뱃지 관련
  const [selectedBadge, setSelectedBadge] = useState('');
  
  const API_BASE_URL = 'http://localhost:8082';

  // 초기 데이터 로드
  useEffect(() => {
    fetchAllData();
  }, [userId]);

  // 모든 데이터 조회
  const fetchAllData = async () => {
    await Promise.all([
      fetchUserPoints(),
      fetchPointHistory(),
      fetchProducts(),
      fetchBadges(),
      fetchUserBadges(),
      fetchRankings()
    ]);
  };

  // 사용자 포인트 정보 조회
  const fetchUserPoints = async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/api/points/user/${userId}/type/all`);
      // 백엔드에서 직접 배열을 반환하므로 response.data 사용
      const history = response.data;
      const totalEarned = history.filter(h => h.points > 0).reduce((sum, h) => sum + h.points, 0);
      const totalSpent = Math.abs(history.filter(h => h.points < 0).reduce((sum, h) => sum + h.points, 0));
      const currentPoints = totalEarned - totalSpent;
      setPoints(currentPoints);
    } catch (error) {
      console.error('포인트 조회 실패:', error);
    }
  };

  // 포인트 히스토리 조회
  const fetchPointHistory = async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/api/points/user/${userId}/type/all`);
      // 백엔드에서 직접 배열을 반환하므로 response.data 사용
      console.log('포인트 히스토리 응답:', response.data);
      setPointHistory(response.data);
    } catch (error) {
      console.error('포인트 히스토리 조회 실패:', error);
    }
  };

  // 상품 목록 조회
  const fetchProducts = async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/api/points/products`);
      if (response.data.success) {
        setProducts(response.data.data);
      }
    } catch (error) {
      console.error('상품 조회 실패:', error);
    }
  };

  // 뱃지 목록 조회
  const fetchBadges = async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/api/badges`);
      if (response.data.success) {
        setBadges(response.data.data);
      }
    } catch (error) {
      console.error('뱃지 조회 실패:', error);
    }
  };

  // 사용자 뱃지 조회
  const fetchUserBadges = async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/api/badges/user/${userId}`);
      if (response.data.success) {
        setUserBadges(response.data.data);
      }
    } catch (error) {
      console.error('사용자 뱃지 조회 실패:', error);
    }
  };

  // 랭킹 조회
  const fetchRankings = async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/api/rankings/college?scopeType=TOTAL`);
      if (response.data.success) {
        setRankings(response.data.data);
      }
    } catch (error) {
      console.error('랭킹 조회 실패:', error);
    }
  };

  // 이미지 선택 처리
  const handleImageSelect = (event) => {
    const file = event.target.files[0];
    if (file) {
      setSelectedImage(file);
      const reader = new FileReader();
      reader.onload = (e) => setImagePreview(e.target.result);
      reader.readAsDataURL(file);
    }
  };

  // AI 이미지 분석
  const analyzeImage = async () => {
    if (!selectedImage) {
      setMessage('이미지를 선택해주세요.');
      return;
    }

    setLoading(true);
    try {
      const formData = new FormData();
      formData.append('image', selectedImage);
      formData.append('userId', userId);

      const response = await axios.post(`${API_BASE_URL}/api/ai/analyze`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });

      console.log('AI 분석 응답 전체:', response.data);

      if (response.data.success) {
        setAnalysisResult(response.data);
        setMessage('이미지 분석이 완료되었습니다! 포인트가 적립되었습니다.');
        
        // 분석된 이미지를 전역 상태에 저장 (이미지 ID와 함께)
        if (response.data.data && response.data.data.imageId) {
          const imageData = {
            imageId: response.data.data.imageId,
            imageUrl: imagePreview, // 선택된 이미지의 미리보기 URL 저장
            analysisData: response.data.data
          };
          
          console.log('이미지 데이터 저장:', imageData);
          
          // 포인트 히스토리에 이미지 정보 추가
          const newHistoryItem = {
            id: Date.now(), // 임시 ID
            type: 'AI_ANALYSIS',
            points: response.data.data.pointsEarned || 0,
            description: 'AI 이미지 분석',
            createdAt: new Date().toISOString(),
            imageId: response.data.data.imageId,
            imageInfo: imageData
          };
          
          console.log('새 히스토리 항목:', newHistoryItem);
          
          setPointHistory(prev => {
            const newHistory = [newHistoryItem, ...prev];
            console.log('업데이트된 히스토리:', newHistory);
            return newHistory;
          });
        } else {
          console.log('이미지 ID가 없음:', response.data.data);
        }
        
        // 포인트 정보만 새로고침 (이미지는 이미 추가됨)
        setTimeout(() => {
          fetchUserPoints();
          fetchPointHistory();
        }, 1000);
      } else {
        setMessage('이미지 분석 실패: ' + response.data.message);
      }
    } catch (error) {
      console.error('AI 분석 에러:', error);
      setMessage('이미지 분석 실패: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  // 포인트 추가
  const addPoints = async () => {
    if (!pointDescription || pointAmount <= 0) {
      setMessage('설명과 포인트 금액을 입력해주세요.');
      return;
    }

    setLoading(true);
    try {
      const response = await axios.post(`${API_BASE_URL}/api/points/add`, {
        userId: parseInt(userId),
        points: pointAmount,
        type: 'MANUAL_ADD',
        description: pointDescription
      });

      if (response.data.success) {
        setMessage('포인트가 성공적으로 추가되었습니다!');
        setPointDescription('');
        setPointAmount(100);
        fetchAllData();
      } else {
        setMessage('포인트 추가 실패: ' + response.data.message);
      }
    } catch (error) {
      setMessage('포인트 추가 실패: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  // 상품 구매
  const purchaseProduct = async () => {
    if (!selectedProduct) {
      setMessage('구매할 상품을 선택해주세요.');
      return;
    }

    const product = products.find(p => p.id == selectedProduct);
    if (!product) {
      setMessage('상품을 찾을 수 없습니다.');
      return;
    }

    if (points < product.pointsRequired) {
      setMessage('포인트가 부족합니다.');
      return;
    }

    setLoading(true);
    try {
      const response = await axios.post(`${API_BASE_URL}/api/points/orders`, {
        userId: parseInt(userId),
        productId: parseInt(selectedProduct),
        quantity: 1
      });

      if (response.data.success) {
        setMessage('상품 구매가 완료되었습니다!');
        setSelectedProduct('');
        fetchAllData();
      } else {
        setMessage('상품 구매 실패: ' + response.data.message);
      }
    } catch (error) {
      setMessage('상품 구매 실패: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  // 뱃지 획득
  const earnBadge = async () => {
    if (!selectedBadge) {
      setMessage('획득할 뱃지를 선택해주세요.');
      return;
    }

    const badge = badges.find(b => b.id == selectedBadge);
    if (!badge) {
      setMessage('뱃지를 찾을 수 없습니다.');
      return;
    }

    if (points < badge.pointsRequired) {
      setMessage('뱃지 획득에 필요한 포인트가 부족합니다.');
      return;
    }

    setLoading(true);
    try {
      const response = await axios.post(`${API_BASE_URL}/api/badges/earn`, {
        userId: parseInt(userId),
        badgeId: parseInt(selectedBadge)
      });

      if (response.data.success) {
        setMessage('뱃지를 획득했습니다! 포인트가 적립되었습니다.');
        setSelectedBadge('');
        fetchAllData();
      } else {
        setMessage('뱃지 획득 실패: ' + response.data.message);
      }
    } catch (error) {
      setMessage('뱃지 획득 실패: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  // 뱃지 초기화
  const initializeBadges = async () => {
    setLoading(true);
    try {
      const response = await axios.post(`${API_BASE_URL}/api/badges/initialize`);
      
      if (response.data.success) {
        setMessage('기본 뱃지가 초기화되었습니다!');
        fetchAllData();
      } else {
        setMessage('뱃지 초기화 실패: ' + response.data.error);
      }
    } catch (error) {
      setMessage('뱃지 초기화 실패: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  // 모든 뱃지 조건 체크
  const checkAllBadgeConditions = async () => {
    setLoading(true);
    try {
      const response = await axios.post(`${API_BASE_URL}/api/badges/check-all-conditions`, {
        userId: parseInt(userId)
      });
      
      if (response.data.success) {
        setMessage('모든 뱃지 조건을 확인했습니다! 조건에 맞는 뱃지가 자동으로 지급되었을 수 있습니다.');
        fetchAllData();
      } else {
        setMessage('뱃지 조건 체크 실패: ' + response.data.error);
      }
    } catch (error) {
      setMessage('뱃지 조건 체크 실패: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  // 소스 타입 구분
  const getSourceType = (type, imageId) => {
    if (imageId) return '사진';
    if (type === 'BADGE_EARNED') return '뱃지';
    if (type === 'MANUAL_ADD') return '수동';
    return '기타';
  };

  // 타입 라벨 변환 함수
  const getTypeLabel = (type) => {
    switch (type) {
      case 'AI_ANALYSIS':
        return 'AI 분석';
      case 'MANUAL_ADD':
        return '수동 추가';
      case 'BADGE_EARNED':
        return '뱃지 획득';
      case 'PRODUCT_PURCHASE':
        return '상품 구매';
      case 'POINT_DEDUCTION':
        return '포인트 차감';
      default:
        return type || '기타';
    }
  };

  // 이미지 표시 함수 - 간단하게 수정
  const renderImage = (imageId, imageInfo) => {
    console.log('renderImage 호출:', { imageId, imageInfo });
    
    if (!imageId) {
      console.log('이미지 ID가 없음');
      return null;
    }
    
    // 가장 간단한 방법: 현재 선택된 이미지 표시
    if (imagePreview) {
      console.log('현재 선택된 이미지로 표시:', imagePreview);
      return (
        <div className="image-container">
          <img 
            src={imagePreview}
            alt="AI 분석 이미지" 
            className="analysis-image"
            style={{ maxWidth: '200px', maxHeight: '200px', objectFit: 'cover', borderRadius: '8px' }}
          />
        </div>
      );
    }
    
    // 이미지 정보가 있는 경우
    if (imageInfo && imageInfo.imageUrl) {
      console.log('이미지 정보에서 URL로 표시:', imageInfo.imageUrl);
      return (
        <div className="image-container">
          <img 
            src={imageInfo.imageUrl} 
            alt="AI 분석 이미지" 
            className="analysis-image"
            style={{ maxWidth: '200px', maxHeight: '200px', objectFit: 'cover', borderRadius: '8px' }}
          />
        </div>
      );
    }
    
    // 플레이스홀더 표시
    return (
      <div className="image-container">
        <div className="image-placeholder">
          <span className="image-icon">📸</span>
          <span className="image-text">AI 분석 이미지</span>
          <span className="image-id">ID: {imageId}</span>
        </div>
      </div>
    );
  };

  return (
    <div className="integrated-test-page">
      <h1>🚀 통합 테스트 페이지</h1>
      
      {/* 사용자 설정 */}
      <div className="section">
        <h2>👤 사용자 설정</h2>
        <div className="input-group">
          <label>사용자 ID:</label>
          <input
            type="number"
            value={userId}
            onChange={(e) => setUserId(e.target.value)}
            min="1"
          />
          <button onClick={fetchAllData}>새로고침</button>
        </div>
        <div className="current-points">
          <strong>현재 포인트: {points.toLocaleString()}P</strong>
        </div>
      </div>

      {/* AI 이미지 분석 */}
      <div className="section">
        <h2>📸 AI 이미지 분석</h2>
        <div className="image-upload">
          <input
            type="file"
            accept="image/*"
            onChange={handleImageSelect}
            id="image-input"
          />
          <label htmlFor="image-input" className="file-upload-btn">
            이미지 선택
          </label>
        </div>
        {imagePreview && (
          <div className="image-preview">
            <img src={imagePreview} alt="미리보기" />
          </div>
        )}
        <button 
          onClick={analyzeImage} 
          disabled={loading || !selectedImage}
          className="btn-primary"
        >
          {loading ? '분석중...' : 'AI 분석 시작'}
        </button>
        {analysisResult && (
          <div className="analysis-result">
            <h4>분석 결과:</h4>
            <pre>{JSON.stringify(analysisResult, null, 2)}</pre>
          </div>
        )}
      </div>

      {/* 포인트 추가 */}
      <div className="section">
        <h2>💰 포인트 추가</h2>
        <div className="input-group">
          <label>설명:</label>
          <input
            type="text"
            value={pointDescription}
            onChange={(e) => setPointDescription(e.target.value)}
            placeholder="포인트 추가 이유"
          />
        </div>
        <div className="input-group">
          <label>포인트:</label>
          <input
            type="number"
            value={pointAmount}
            onChange={(e) => setPointAmount(parseInt(e.target.value))}
            min="1"
            max="10000"
          />
        </div>
        <button 
          onClick={addPoints} 
          disabled={loading}
          className="btn-primary"
        >
          포인트 추가
        </button>
      </div>

      {/* 뱃지 획득 */}
      <div className="section">
        <h2>🏆 뱃지 획득</h2>
        <div className="input-group">
          <label>뱃지 선택:</label>
          <select 
            value={selectedBadge} 
            onChange={(e) => setSelectedBadge(e.target.value)}
          >
            <option value="">뱃지를 선택하세요</option>
            {badges.map(badge => (
              <option key={badge.id} value={badge.id}>
                {badge.name} - {badge.pointsRequired}P 필요
              </option>
            ))}
          </select>
        </div>
        <button 
          onClick={earnBadge} 
          disabled={loading || !selectedBadge}
          className="btn-secondary"
        >
          뱃지 획득
        </button>
      </div>

      {/* 뱃지 초기화 */}
      <div className="section">
        <h2>🔄 뱃지 초기화</h2>
        <button 
          onClick={initializeBadges} 
          disabled={loading}
          className="btn-warning"
        >
          {loading ? '초기화 중...' : '기본 뱃지 초기화'}
        </button>
      </div>

      {/* 모든 뱃지 조건 체크 */}
      <div className="section">
        <h2>🔍 모든 뱃지 조건 체크</h2>
        <button 
          onClick={checkAllBadgeConditions} 
          disabled={loading}
          className="btn-info"
        >
          {loading ? '체크 중...' : '모든 뱃지 조건 확인'}
        </button>
      </div>

      {/* 상품 구매 */}
      <div className="section">
        <h2>🛒 상품 구매</h2>
        <div className="input-group">
          <label>상품 선택:</label>
          <select 
            value={selectedProduct} 
            onChange={(e) => setSelectedProduct(e.target.value)}
          >
            <option value="">상품을 선택하세요</option>
            {products.map(product => (
              <option key={product.id} value={product.id}>
                {product.name} - {product.pointsRequired}P
              </option>
            ))}
          </select>
        </div>
        <button 
          onClick={purchaseProduct} 
          disabled={loading || !selectedProduct}
          className="btn-secondary"
        >
          상품 구매
        </button>
      </div>

      {/* 메시지 표시 */}
      {message && (
        <div className={`message ${message.includes('성공') || message.includes('완료') ? 'success' : 'error'}`}>
          {message}
        </div>
      )}

      {/* 포인트 히스토리 */}
      <div className="section">
        <h2>📊 포인트 히스토리</h2>
        <div className="point-history">
            <h3>포인트 히스토리</h3>
            <div className="history-list">
                {console.log('포인트 히스토리 렌더링:', pointHistory)}
                {pointHistory.map((item, index) => {
                    console.log(`히스토리 항목 ${index}:`, item);
                    return (
                        <div key={index} className="history-item">
                            <div className="history-content">
                                <div className="history-main">
                                    <span className="history-type">{getTypeLabel(item.type)}</span>
                                    <span className="history-points">{item.points > 0 ? '+' : ''}{item.points}점</span>
                                </div>
                                <div className="history-description">{item.description}</div>
                                <div className="history-time">
                                    {new Date(item.createdAt).toLocaleString()}
                                </div>
                            </div>
                            {renderImage(item.imageId, item.imageInfo)}
                        </div>
                    );
                })}
            </div>
        </div>
      </div>

      {/* 사용자 뱃지 */}
      <div className="section">
        <h2>🏅 보유 뱃지</h2>
        <div className="badge-list">
          {userBadges.length === 0 ? (
            <p>획득한 뱃지가 없습니다.</p>
          ) : (
            userBadges.map((userBadge, index) => (
              <div key={index} className="badge-item">
                <span className="badge-name">{userBadge.badgeName}</span>
                <span className="badge-date">
                  {new Date(userBadge.earnedAt).toLocaleDateString()}
                </span>
              </div>
            ))
          )}
        </div>
      </div>

      {/* 랭킹 */}
      <div className="section">
        <h2>🏆 단과대 랭킹</h2>
        <div className="ranking-list">
          {rankings.length === 0 ? (
            <p>랭킹 정보가 없습니다.</p>
          ) : (
            rankings.map((ranking, index) => (
              <div key={index} className="ranking-item">
                <span className="ranking-position">#{ranking.rankPosition}</span>
                <span className="ranking-username">{ranking.username}</span>
                <span className="ranking-points">{ranking.points}P</span>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default IntegratedTestPage;
