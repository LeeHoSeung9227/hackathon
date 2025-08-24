-- 사용자 초기 데이터 (User 엔티티 구조에 맞게 수정)
INSERT INTO users (username, email, password, name, nickname, school, campus, level, points_total, college, created_at, updated_at) VALUES
('admin', 'admin@hackathon.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '관리자', '관리자', 'Seoul National University', 'Seoul', 1, 1000, 'Seoul National University', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user1', 'user1@hackathon.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '설호', '설호', 'Seoul National University', 'Seoul', 1, 750, 'Seoul National University', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user2', 'user2@hackathon.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '김지수', '김지수', 'Seoul National University', 'Seoul', 1, 500, 'Seoul National University', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user3', 'user3@hackathon.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '이가은', '이가은', 'Seoul National University', 'Seoul', 1, 300, 'Seoul National University', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user4', 'user4@hackathon.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '이호승', '이호승', 'Seoul National University', 'Seoul', 1, 200, 'Seoul National University', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user5', 'user5@hackathon.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '안예영', '안예영', 'Seoul National University', 'Seoul', 1, 100, 'Seoul National University', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('하은', '하은@hanyang.ac.kr', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '하은', '하은', '한양대학교', 'Seoul', 1, 200, '디자인대학', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('민수', '민수@hanyang.ac.kr', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '민수', '민수', '한양대학교', 'Seoul', 1, 350, '공과대학', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('지영', '지영@kaist.ac.kr', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '지영', '지영', 'KAIST', 'Daejeon', 1, 450, '전산학부', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('준호', '준호@kaist.ac.kr', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '준호', '준호', 'KAIST', 'Daejeon', 1, 280, '생명화학공학과', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('소연', '소연@yonsei.ac.kr', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '소연', '소연', '연세대학교', 'Seoul', 1, 180, '의과대학', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('현우', '현우@yonsei.ac.kr', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '현우', '현우', '연세대학교', 'Seoul', 1, 320, '경영대학', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('수진', '수진@korea.ac.kr', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '수진', '수진', '고려대학교', 'Seoul', 1, 150, '문과대학', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('동현', '동현@korea.ac.kr', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '동현', '동현', '고려대학교', 'Seoul', 1, 420, '사과대학', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('예은', '예은@skku.edu', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '예은', '예은', '성균관대학교', 'Seoul', 1, 290, '자연과학대학', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('태민', '태민@skku.edu', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '태민', '태민', '성균관대학교', 'Seoul', 1, 380, '공학대학', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('혜진', '혜진@hanyang.ac.kr', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '혜진', '혜진', '한양대학교', 'Seoul', 1, 220, '의과대학', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('성훈', '성훈@hanyang.ac.kr', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '성훈', '성훈', '한양대학교', 'Seoul', 1, 175, '경영대학', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 뱃지 초기 데이터 (Badge 엔티티 구조에 맞게 수정)
INSERT INTO badges (name, description, image_url, points_required, category, created_at, updated_at) VALUES
('Recycling Master', 'First recycling achievement', '/images/badges/recycling-master.png', 100, 'RECYCLING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Point Collector', 'Earned 500 points', '/images/badges/point-collector.png', 500, 'ACHIEVEMENT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Waste Warrior', 'Recycled 50 items', '/images/badges/waste-warrior.png', 1000, 'RECYCLING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Eco Hero', 'Environmental champion', '/images/badges/eco-hero.png', 2000, 'ACHIEVEMENT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 쓰레기 분리 기록 초기 데이터 (WasteRecord 엔티티 구조에 맞게 수정)
INSERT INTO waste_records (user_id, waste_type, points, image_url, created_at, updated_at) VALUES
(2, 'PET', 10, '/images/waste/pet1.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'CAN', 15, '/images/waste/can1.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'PAPER', 5, '/images/waste/paper1.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'GLASS', 20, '/images/waste/glass1.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'PET', 10, '/images/waste/pet2.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'CAN', 15, '/images/waste/can2.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 상품 초기 데이터 (Product 엔티티 구조에 맞게 수정)
INSERT INTO products (name, description, price, points_required, stock_quantity, category, image_url, created_at, updated_at) VALUES
('에코백', '친환경 재활용 에코백', 15000.00, 100, 50, 'LIFESTYLE', '/images/products/ecobag.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('텀블러', '스테인리스 텀블러', 25000.00, 200, 30, 'LIFESTYLE', '/images/products/tumbler.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('재활용 노트', '재활용 종이로 만든 노트', 8000.00, 50, 100, 'STATIONERY', '/images/products/notebook.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('친환경 펜', '재활용 플라스틱 펜', 5000.00, 30, 200, 'STATIONERY', '/images/products/pen.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('에코 화분', '재활용 소재 화분', 35000.00, 300, 20, 'LIFESTYLE', '/images/products/plantpot.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
