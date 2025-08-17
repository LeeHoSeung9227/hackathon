#!/bin/bash
set -e

echo "=== EC2 환경 설정 시작 ==="

# 1. Docker 설치
echo "Docker 설치 중..."
sudo yum update -y
sudo yum install -y docker
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -a -G docker ec2-user

# 2. Docker Compose 설치
echo "Docker Compose 설치 중..."
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# 3. 필요한 디렉토리 생성
echo "디렉토리 생성 중..."
sudo mkdir -p /opt/app
sudo chown ec2-user:ec2-user /opt/app

# 4. docker-compose.yml 생성
echo "docker-compose.yml 생성 중..."
cat > /opt/app/docker-compose.yml << 'EOF'
version: '3.8'
services:
  app:
    image: ${ECR_REGISTRY}/my-api:latest
    container_name: hackathon-backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 40s
EOF

# 5. .env 파일 생성
echo ".env 파일 생성 중..."
cat > /opt/app/.env << 'EOF'
ECR_REGISTRY=345594599315.dkr.ecr.eu-north-1.amazonaws.com
EOF

# 6. curl 설치 (health check용)
echo "curl 설치 중..."
sudo yum install -y curl

# 7. 권한 설정
echo "권한 설정 중..."
sudo chown -R ec2-user:ec2-user /opt/app
chmod 644 /opt/app/docker-compose.yml
chmod 644 /opt/app/.env

# 8. Docker 그룹 적용 (현재 세션)
newgrp docker

echo "=== 환경 설정 완료 ==="
echo "Docker 버전: $(docker --version)"
echo "Docker Compose 버전: $(docker compose version)"
echo "파일 확인:"
ls -la /opt/app/
echo ""
echo "docker-compose.yml 내용:"
cat /opt/app/docker-compose.yml
echo ""
echo ".env 내용:"
cat /opt/app/.env
