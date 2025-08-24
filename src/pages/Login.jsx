import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function Login() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    nickname: '',
    school: '',
    college: ''
  });
  const [message, setMessage] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  // 통합 로그인/회원가입 처리
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!formData.nickname.trim() || !formData.school.trim() || !formData.college.trim()) {
      setMessage('모든 필드를 입력해주세요.');
      return;
    }

    setIsLoading(true);
    setMessage('');

    try {
      // 먼저 기존 사용자 검색
              const searchResponse = await axios.get(`http://43.203.226.243:8080/api/users/search`, {
        params: {
          nickname: formData.nickname,
          school: formData.school,
          college: formData.college
        }
      });

      if (searchResponse.data.success && searchResponse.data.data) {
        // 기존 사용자 발견 - 로그인 처리
        const userData = searchResponse.data.data;
        setMessage(`${userData.nickname}님 환영합니다! (기존 계정)`);
        
        // 로컬 스토리지에 사용자 정보 저장
        localStorage.setItem('user', JSON.stringify(userData));
        localStorage.setItem('userId', userData.userId);
        
        // 메인 페이지로 이동
        setTimeout(() => {
          navigate('/main');
        }, 1000);
      } else {
        // 새로운 사용자 - 자동 회원가입
        const signupResponse = await axios.post('http://localhost:8082/api/auth/signup/request', {
          nickname: formData.nickname,
          school: formData.school,
          college: formData.college,
          campus: formData.school, // 학교명을 캠퍼스로도 사용
          name: formData.nickname // 닉네임을 이름으로도 사용
        });

        if (signupResponse.data.success) {
          const newUserData = signupResponse.data.data;
          setMessage(`${formData.nickname}님, 새로운 계정이 생성되었습니다! 🎉`);
          
          // 로컬 스토리지에 사용자 정보 저장
          localStorage.setItem('user', JSON.stringify(newUserData));
          localStorage.setItem('userId', newUserData.userId);
          
          // 메인 페이지로 이동
          setTimeout(() => {
            navigate('/main');
          }, 1500);
        } else {
          setMessage('계정 생성 중 오류가 발생했습니다.');
        }
      }
    } catch (error) {
      console.error('API 호출 오류:', error);
      
      if (error.response?.status === 404) {
        // 사용자를 찾을 수 없는 경우 - 새 계정 생성 시도
        try {
          const signupResponse = await axios.post('http://43.203.226.243:8080/api/auth/signup/request', {
            nickname: formData.nickname,
            school: formData.school,
            college: formData.college,
            campus: formData.school,
            name: formData.nickname
          });

          if (signupResponse.data.success) {
            const newUserData = signupResponse.data.data;
            setMessage(`${formData.nickname}님, 새로운 계정이 생성되었습니다! 🎉`);
            
            localStorage.setItem('user', JSON.stringify(newUserData));
            localStorage.setItem('userId', newUserData.userId);
            
            setTimeout(() => {
              navigate('/main');
            }, 1500);
          } else {
            setMessage('계정 생성 중 오류가 발생했습니다.');
          }
        } catch (signupError) {
          console.error('회원가입 오류:', signupError);
          setMessage('서버 연결 오류가 발생했습니다.');
        }
      } else {
        setMessage('서버 연결 오류가 발생했습니다.');
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div style={{
      width: '100vw',
      height: '100vh',
      backgroundColor: 'white',
      padding: '20px',
      display: 'flex',
      flexDirection: 'column'
    }}>
      {/* 상단 제목 */}
      <div style={{
        fontSize: '14px',
        color: '#666',
        marginBottom: '40px'
      }}>
        로그인
      </div>

      {/* 메인 제목 */}
      <div style={{
        fontSize: '32px',
        fontWeight: 'bold',
        color: '#333',
        marginBottom: '20px'
      }}>
        로그인
      </div>

      {/* 환영 메시지 */}
      <div style={{
        marginBottom: '40px',
        lineHeight: '1.6'
      }}>
        <div style={{ fontSize: '18px', color: '#333', marginBottom: '8px' }}>
          안녕하세요.
        </div>
        <div style={{ 
          fontSize: '24px', 
          fontWeight: 'bold', 
          color: '#4CAF50',
          fontFamily: 'cursive, serif',
          marginBottom: '8px'
        }}>
          Pickle 입니다.
        </div>
        <div style={{ fontSize: '16px', color: '#666' }}>
          닉네임, 학교명, 단과대를 입력해주세요.
        </div>
        <div style={{ fontSize: '14px', color: '#999', marginTop: '8px' }}>
          기존 사용자는 로그인, 새로운 사용자는 자동으로 계정이 생성됩니다.
        </div>
      </div>

      {/* 입력 폼 */}
      <form onSubmit={handleSubmit} style={{ flex: 1 }}>
        {/* 닉네임 입력 */}
        <div style={{ marginBottom: '24px' }}>
          <label style={{
            display: 'block',
            fontSize: '16px',
            fontWeight: 'bold',
            color: '#333',
            marginBottom: '8px'
          }}>
            닉네임
          </label>
          <input
            type="text"
            name="nickname"
            value={formData.nickname}
            onChange={handleInputChange}
            placeholder="닉네임을 입력하세요"
            style={{
              width: '100%',
              padding: '16px',
              border: '1px solid #ddd',
              borderRadius: '8px',
              fontSize: '16px',
              outline: 'none'
            }}
          />
        </div>

        {/* 학교명 입력 */}
        <div style={{ marginBottom: '24px' }}>
          <label style={{
            display: 'block',
            fontSize: '16px',
            fontWeight: 'bold',
            color: '#333',
            marginBottom: '8px'
          }}>
            학교명
          </label>
          <input
            type="text"
            name="school"
            value={formData.school}
            onChange={handleInputChange}
            placeholder="학교명을 입력하세요 (예: 한양대학교)"
            style={{
              width: '100%',
              padding: '16px',
              border: '1px solid #ddd',
              borderRadius: '8px',
              fontSize: '16px',
              outline: 'none'
            }}
          />
        </div>

        {/* 단과대 입력 */}
        <div style={{ marginBottom: '40px' }}>
          <label style={{
            display: 'block',
            fontSize: '16px',
            fontWeight: 'bold',
            color: '#333',
            marginBottom: '8px'
          }}>
            단과대
          </label>
          <input
            type="text"
            name="college"
            value={formData.college}
            onChange={handleInputChange}
            placeholder="단과대를 입력하세요 (예: 디자인대학)"
            style={{
              width: '100%',
              padding: '16px',
              border: '1px solid #ddd',
              borderRadius: '8px',
              fontSize: '16px',
              outline: 'none'
            }}
          />
        </div>

        {/* 메시지 표시 */}
        {message && (
          <div style={{
            padding: '12px',
            backgroundColor: message.includes('환영') || message.includes('생성') ? '#d4edda' : '#f8d7da',
            color: message.includes('환영') || message.includes('생성') ? '#155724' : '#721c24',
            borderRadius: '8px',
            marginBottom: '24px',
            textAlign: 'center'
          }}>
            {message}
          </div>
        )}

        {/* 로그인/회원가입 버튼 */}
        <button
          type="submit"
          disabled={isLoading}
          style={{
            width: '100%',
            padding: '18px',
            backgroundColor: isLoading ? '#6c757d' : '#4CAF50',
            color: 'white',
            border: 'none',
            borderRadius: '8px',
            fontSize: '18px',
            fontWeight: 'bold',
            cursor: isLoading ? 'not-allowed' : 'pointer',
            marginTop: 'auto'
          }}
        >
          {isLoading ? '처리 중...' : '시작하기'}
        </button>
      </form>
    </div>
  );
}

export default Login;
