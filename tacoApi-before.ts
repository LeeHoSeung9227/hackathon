// src/api/tacoApi.ts (수정 전)
import apiClient from "./apiClient";
import axios from "axios";

export async function analyzeImage(file: File): Promise<string> {
  try {
    const formData = new FormData();
    formData.append("image", file);

    const response = await apiClient.post("api/ai/analyze", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });

    return response.data;
  } catch (error: any) {
    if (axios.isAxiosError(error)) {
      if (error.response) {
        throw new Error(error.response.data);
      }
      throw new Error(error.message);
    }
    throw error;
  }
}

// src/api/apiClient.ts
import axios from "axios";

// 개발 단계용 고정 토큰 (나중엔 로그인 인증 결과로 대체)
const ACCESS_TOKEN = "cc25e466-fc6c-4d24-8a63-6f3cba41cbd0";

// 공통 axios 인스턴스
const apiClient = axios.create({
  baseURL: "http://43.203.226.243:8080", // 실제 서버 주소 작성
  headers: {
    Authorization: `Bearer ${ACCESS_TOKEN}`,
  },
});

export default apiClient;

// 사진 촬영 및 백엔드 전송
const capturePhoto = async () => {
  if (!videoRef.current || !canvasRef.current) return;

  setIsLoading(true);

  try {
    const canvas = canvasRef.current;
    const video = videoRef.current;
    const context = canvas.getContext('2d');
    if (!context) return;

    // 비디오 프레임을 캔버스에 그림
    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;
    context.drawImage(video, 0, 0);

    // 캔버스를 Blob으로 변환 후 API 호출
    canvas.toBlob(async (blob) => {
      if (!blob) {
        setIsLoading(false);
        alert("이미지 생성 실패");
        return;
      }

      try {
        const file = new File([blob], 'capture.jpg', { type: 'image/jpeg' });
        const result = await analyzeImage(file);  // ← 이 줄을 수정
        // 모델 분석 결과를 result 변수에 받음

        // 성공 시 result를 화면에 출력하거나 처리 (예제: alert)
        alert("분석 결과:\n" + result);

        // 필요 시 다른 페이지 이동 등 처리 가능
        // navigate('/Success'); 혹은 setResult(result) 등

      } catch (error: any) {
        alert("분석 실패: " + error.message);
      } finally {
        setIsLoading(false);
      }
    }, 'image/jpeg', 0.8);

  } catch (error) {
    setIsLoading(false);
    alert("사진 촬영에 실패했습니다.");
  }
};
