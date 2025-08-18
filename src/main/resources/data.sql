-- 사용자 초기 데이터
INSERT INTO users (username, email, pw_hash, nickname, campus, level, points_total, membership_level, rank, role) VALUES
('admin', 'admin@hackathon.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '관리자', 'Seoul', 'ADMIN', 1000, 'GOLD', 1, 'ADMIN'),
('user1', 'user1@hackathon.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '설호', 'Seoul', 'USER', 750, 'SILVER', 2, 'USER'),
('user2', 'user2@hackathon.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '김지수', 'Seoul', 'USER', 500, 'SILVER', 3, 'USER'),
('user3', 'user3@hackathon.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '이가은', 'Seoul', 'USER', 300, 'BRONZE', 4, 'USER'),
('user4', 'user4@hackathon.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '이호승', 'Seoul', 'USER', 200, 'BRONZE', 5, 'USER'),
('user5', 'user5@hackathon.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '안예영', 'Seoul', 'USER', 100, 'BRONZE', 6, 'USER');

-- 뱃지 초기 데이터
INSERT INTO badges (name, description, image_url, required_points, category) VALUES
('Recycling Master', 'First recycling achievement', '/images/badges/recycling-master.png', 100, 'RECYCLING'),
('Point Collector', 'Earned 500 points', '/images/badges/point-collector.png', 500, 'ACHIEVEMENT'),
('Waste Warrior', 'Recycled 50 items', '/images/badges/waste-warrior.png', 1000, 'RECYCLING'),
('Eco Hero', 'Environmental champion', '/images/badges/eco-hero.png', 2000, 'ACHIEVEMENT');

-- 쓰레기 분리 기록 초기 데이터
INSERT INTO waste_records (user_id, waste_type, earned_points, accumulated_points, recorded_at, status) VALUES
(2, 'PET', 10, 10, DATEADD('DAY', -1, CURRENT_TIMESTAMP), 'SUCCESS'),
(2, 'CAN', 15, 25, DATEADD('DAY', -2, CURRENT_TIMESTAMP), 'SUCCESS'),
(2, 'PAPER', 5, 30, DATEADD('DAY', -3, CURRENT_TIMESTAMP), 'SUCCESS'),
(3, 'GLASS', 20, 20, DATEADD('DAY', -1, CURRENT_TIMESTAMP), 'SUCCESS'),
(3, 'PET', 10, 30, DATEADD('DAY', -2, CURRENT_TIMESTAMP), 'SUCCESS'),
(4, 'CAN', 15, 15, DATEADD('DAY', -1, CURRENT_TIMESTAMP), 'SUCCESS');
