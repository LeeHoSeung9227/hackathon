import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar.jsx';
import Splash from './pages/Splash.jsx';
import Home from './pages/Home.jsx';
import Login from './pages/Login.jsx';
import Main from './pages/Main.jsx';
import Camera from './pages/Camera.jsx';
import Ranking from './pages/Ranking.jsx';
import MyPage from './pages/MyPage.jsx';
import History from './pages/History.jsx';
import Shop from './pages/Shop.jsx';
import TestPage from './pages/TestPage.jsx';
import IntegratedTestPage from './pages/IntegratedTestPage.jsx';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <Navbar />
        <div className="container">
          <Routes>
            <Route path="/" element={<Splash />} />
            <Route path="/home" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/main" element={<Main />} />
            <Route path="/camera" element={<Camera />} />
            <Route path="/ranking" element={<Ranking />} />
            <Route path="/mypage" element={<MyPage />} />
            <Route path="/history" element={<History />} />
            <Route path="/shop" element={<Shop />} />
            <Route path="/test" element={<TestPage />} />
        <Route path="/integrated-test" element={<IntegratedTestPage />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;
