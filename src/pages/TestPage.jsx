import React, { useState } from 'react';
import './Main.css';

function TestPage() {
  const [testResults, setTestResults] = useState({});
  const [loading, setLoading] = useState(false);

  const API_BASE_URL = 'http://43.203.226.243:8080';

  const runTest = async (testName, endpoint, method = 'GET', body = null) => {
    setLoading(true);
    try {
      const options = {
        method,
        headers: {
          'Content-Type': 'application/json',
        }
      };

      if (body) {
        options.body = JSON.stringify(body);
      }

      const response = await fetch(`${API_BASE_URL}${endpoint}`, options);
      const data = await response.json();
      
      setTestResults(prev => ({
        ...prev,
        [testName]: {
          status: response.status,
          success: response.ok,
          data: data,
          timestamp: new Date().toLocaleTimeString()
        }
      }));
    } catch (error) {
      setTestResults(prev => ({
        ...prev,
        [testName]: {
          status: 'ERROR',
          success: false,
          error: error.message,
          timestamp: new Date().toLocaleTimeString()
        }
      }));
    } finally {
      setLoading(false);
    }
  };

  const tests = [
    {
      name: '사용자 목록 조회',
      endpoint: '/api/users',
      method: 'GET'
    },
    {
      name: 'AI 분석 결과 조회',
      endpoint: '/api/ai/range',
      method: 'GET'
    },
    {
      name: '뱃지 목록 조회',
      endpoint: '/api/badges',
      method: 'GET'
    },
    {
      name: '활동 이력 조회',
      endpoint: '/api/activity/user/1',
      method: 'GET'
    }
  ];

  const runAllTests = () => {
    tests.forEach(test => {
      setTimeout(() => runTest(test.name, test.endpoint, test.method), 100);
    });
  };

  return (
    <div className="test-page-container">
      <h1 className="page-title">🧪 API 테스트 페이지</h1>
      <p className="page-subtitle">백엔드 API 연결 상태를 테스트해보세요</p>
      
      <div className="test-controls">
        <button 
          className="btn btn-primary"
          onClick={runAllTests}
          disabled={loading}
        >
          {loading ? '테스트 중...' : '전체 테스트 실행'}
        </button>
      </div>

      <div className="tests-grid">
        {tests.map((test) => (
          <div key={test.name} className="test-card">
            <h3 className="test-name">{test.name}</h3>
            <p className="test-endpoint">{test.method} {test.endpoint}</p>
            <button 
              className="btn btn-secondary"
              onClick={() => runTest(test.name, test.endpoint, test.method)}
              disabled={loading}
            >
              개별 테스트
            </button>
            
            {testResults[test.name] && (
              <div className={`test-result ${testResults[test.name].success ? 'success' : 'error'}`}>
                <div className="result-header">
                  <span className="status">{testResults[test.name].success ? '✅ 성공' : '❌ 실패'}</span>
                  <span className="timestamp">{testResults[test.name].timestamp}</span>
                </div>
                <div className="result-details">
                  <p>상태 코드: {testResults[test.name].status}</p>
                  {testResults[test.name].error && (
                    <p className="error-message">오류: {testResults[test.name].error}</p>
                  )}
                </div>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}

export default TestPage;
