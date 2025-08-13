import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function Login() {
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    nickname: '',
    campus: '',
    college: ''
  });
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const handleInputChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage('');

    try {
      if (isLogin) {
        // 로그인 로직 (현재는 시뮬레이션)
        if (formData.username === 'user1' && formData.password === 'password') {
          setMessage('설호님 환영합니다!');
          setTimeout(() => navigate('/main'), 1000);
        } else if (formData.username === 'user2' && formData.password === 'password') {
          setMessage('김지수님 환영합니다!');
          setTimeout(() => navigate('/camera'), 1000);
        } else if (formData.username === 'user3' && formData.password === 'password') {
          setMessage('이가은님 환영합니다!');
          setTimeout(() => navigate('/ranking'), 1000);
        } else if (formData.username === 'user4' && formData.password === 'password') {
          setMessage('이호승님 환영합니다!');
          setTimeout(() => navigate('/mypage'), 1000);
        } else if (formData.username === 'user5' && formData.password === 'password') {
          setMessage('안예영님 환영합니다!');
          setTimeout(() => navigate('/history'), 1000);
        } else {
          setMessage('잘못된 사용자명 또는 비밀번호입니다.');
        }
      } else {
        // 회원가입 로직
        const response = await axios.post('/api/auth/register', formData);
        setMessage('회원가입이 완료되었습니다!');
        setIsLogin(true);
      }
    } catch (error) {
      setMessage('오류가 발생했습니다. 다시 시도해주세요.');
    }
  };

  return (
    <div className="text-center">
      <h1>{isLogin ? '로그인' : '회원가입'}</h1>
      
      <div className="card" style={{ maxWidth: '400px', margin: '0 auto' }}>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <input
              type="text"
              name="username"
              className="form-control"
              placeholder="사용자명"
              value={formData.username}
              onChange={handleInputChange}
              required
            />
          </div>

          {!isLogin && (
            <>
              <div className="form-group">
                <input
                  type="email"
                  name="email"
                  className="form-control"
                  placeholder="이메일"
                  value={formData.email}
                  onChange={handleInputChange}
                  required
                />
              </div>
              <div className="form-group">
                <input
                  type="text"
                  name="nickname"
                  className="form-control"
                  placeholder="닉네임"
                  value={formData.nickname}
                  onChange={handleInputChange}
                  required
                />
              </div>
              <div className="form-group">
                <input
                  type="text"
                  name="campus"
                  className="form-control"
                  placeholder="캠퍼스"
                  value={formData.campus}
                  onChange={handleInputChange}
                  required
                />
              </div>
              <div className="form-group">
                <input
                  type="text"
                  name="college"
                  className="form-control"
                  placeholder="단과대"
                  value={formData.college}
                  onChange={handleInputChange}
                  required
                />
              </div>
            </>
          )}

          <div className="form-group">
            <input
              type="password"
              name="password"
              className="form-control"
              placeholder="비밀번호"
              value={formData.password}
              onChange={handleInputChange}
              required
            />
          </div>

          <button type="submit" className="btn btn-primary" style={{ width: '100%' }}>
            {isLogin ? '로그인' : '회원가입'}
          </button>
        </form>

        {message && (
          <div className="mt-3" style={{ color: message.includes('환영') ? 'green' : 'red' }}>
            {message}
          </div>
        )}

        <div className="mt-3">
          <button
            type="button"
            className="btn btn-success"
            onClick={() => setIsLogin(!isLogin)}
            style={{ width: '100%' }}
          >
            {isLogin ? '회원가입으로 전환' : '로그인으로 전환'}
          </button>
        </div>
      </div>

      <div className="card mt-3" style={{ maxWidth: '400px', margin: '0 auto' }}>
        <h3>데모 계정 정보</h3>
        <p><strong>user1</strong> (설호) - 비밀번호: password</p>
        <p><strong>user2</strong> (김지수) - 비밀번호: password</p>
        <p><strong>user3</strong> (이가은) - 비밀번호: password</p>
        <p><strong>user4</strong> (이호승) - 비밀번호: password</p>
        <p><strong>user5</strong> (안예영) - 비밀번호: password</p>
      </div>
    </div>
  );
}

export default Login;
