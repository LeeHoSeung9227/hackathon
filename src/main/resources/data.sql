-- 사용자 초기 데이터 (User 엔티티 구조에 맞게 수정)
INSERT INTO users (username, email, password, name, campus, level, points_total, college, created_at, updated_at) VALUES
('admin', 'admin@hackathon.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '관리자', 'Seoul', 1, 1000, 'Seoul National University', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user1', 'user1@hackathon.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '설호', 'Seoul', 1, 750, 'Seoul National University', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user2', 'user2@hackathon.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '김지수', 'Seoul', 1, 500, 'Seoul National University', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user3', 'user3@hackathon.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '이가은', 'Seoul', 1, 300, 'Seoul National University', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user4', 'user4@hackathon.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '이호승', 'Seoul', 1, 200, 'Seoul National University', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user5', 'user5@hackathon.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '안예영', 'Seoul', 1, 100, 'Seoul National University', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

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
