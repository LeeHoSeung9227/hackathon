import React, { useState, useEffect } from 'react';
import './Main.css';

function Shop() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      const response = await fetch('http://43.203.226.243:8080/api/products');
      if (response.ok) {
        const data = await response.json();
        setProducts(data.data || []);
      } else {
        setError('상품 목록을 불러올 수 없습니다.');
      }
    } catch (err) {
      setError('서버 연결에 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  const handleExchange = async (productId, requiredPoints) => {
    try {
      const response = await fetch('http://43.203.226.243:8080/api/exchanges', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          userId: 1, // 임시 사용자 ID
          productId: productId,
          points: requiredPoints
        })
      });

      if (response.ok) {
        alert('상품 교환이 완료되었습니다!');
        fetchProducts(); // 목록 새로고침
      } else {
        alert('상품 교환에 실패했습니다.');
      }
    } catch (err) {
      alert('서버 연결에 실패했습니다.');
    }
  };

  if (loading) return <div className="loading">상품 목록을 불러오는 중...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="shop-container">
      <h1 className="page-title">🏪 포인트 상점</h1>
      <p className="page-subtitle">포인트로 다양한 상품을 교환하세요!</p>
      
      <div className="products-grid">
        {products.length > 0 ? (
          products.map((product) => (
            <div key={product.id} className="product-card">
              <div className="product-image">
                <span className="product-icon">🎁</span>
              </div>
              <div className="product-info">
                <h3 className="product-name">{product.name}</h3>
                <p className="product-description">{product.description}</p>
                <div className="product-points">
                  <span className="points-label">필요 포인트:</span>
                  <span className="points-value">{product.requiredPoints}점</span>
                </div>
                <button 
                  className="btn btn-primary exchange-btn"
                  onClick={() => handleExchange(product.id, product.requiredPoints)}
                >
                  교환하기
                </button>
              </div>
            </div>
          ))
        ) : (
          <div className="no-products">
            <p>등록된 상품이 없습니다.</p>
          </div>
        )}
      </div>
    </div>
  );
}

export default Shop;
