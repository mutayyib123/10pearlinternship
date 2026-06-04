-- =====================================================
-- Step 2: Insert Sample Data
-- =====================================================

USE contact_management_db;
GO

-- =====================================================
-- INSERT SAMPLE USERS
-- =====================================================

INSERT INTO app_users (name, email, phone, password) VALUES
('Ahmed Ali', 'ahmed@example.com', '+923001234567', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/KFm'),
('Fatima Khan', 'fatima@example.com', '+923009876543', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/KFm'),
('Hassan Raza', 'hassan@example.com', '+923005555555', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/KFm');

-- =====================================================
-- INSERT SAMPLE CONTACTS
-- =====================================================

-- Contacts for User 1 (Ahmed Ali)
INSERT INTO contacts (first_name, last_name, title, user_id) VALUES
('Ali', 'Khan', 'Software Engineer', 1),
('Sara', 'Ahmed', 'Project Manager', 1),
('Omar', 'Hassan', 'Business Analyst', 1);

-- Contacts for User 2 (Fatima Khan)
INSERT INTO contacts (first_name, last_name, title, user_id) VALUES
('Ayesha', 'Ali', 'Designer', 2),
('Zain', 'Malik', 'Developer', 2);

-- =====================================================
-- INSERT SAMPLE CONTACT DETAILS
-- =====================================================

-- Details for Contact 1 (Ali Khan)
INSERT INTO contact_details (contact_id, type, value, category) VALUES
(1, 'WORK', 'ali.khan@company.com', 'EMAIL'),
(1, 'WORK', '+923001111111', 'PHONE'),
(1, 'PERSONAL', 'ali.personal@gmail.com', 'EMAIL');

-- Details for Contact 2 (Sara Ahmed)
INSERT INTO contact_details (contact_id, type, value, category) VALUES
(2, 'WORK', 'sara.ahmed@company.com', 'EMAIL'),
(2, 'WORK', '+923002222222', 'PHONE'),
(2, 'HOME', '+923332222222', 'PHONE');

-- Details for Contact 3 (Omar Hassan)
INSERT INTO contact_details (contact_id, type, value, category) VALUES
(3, 'WORK', 'omar.hassan@company.com', 'EMAIL'),
(3, 'WORK', '+923003333333', 'PHONE');

-- Details for Contact 4 (Ayesha Ali)
INSERT INTO contact_details (contact_id, type, value, category) VALUES
(4, 'WORK', 'ayesha.ali@company.com', 'EMAIL'),
(4, 'WORK', '+923004444444', 'PHONE');

-- Details for Contact 5 (Zain Malik)
INSERT INTO contact_details (contact_id, type, value, category) VALUES
(5, 'WORK', 'zain.malik@company.com', 'EMAIL'),
(5, 'HOME', '+923005555555', 'PHONE');

PRINT 'Sample data inserted successfully!';
