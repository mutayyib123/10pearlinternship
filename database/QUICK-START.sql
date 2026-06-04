-- =====================================================
-- QUICK START: Complete Database Setup
-- =====================================================
-- Run this script to set up everything at once
-- (Equivalent to running 01 + 02 in sequence)

-- =====================================================
-- 1. CREATE DATABASE AND TABLES
-- =====================================================

CREATE DATABASE contact_management_db;
GO

USE contact_management_db;
GO

-- Table: app_users
CREATE TABLE app_users (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL,
    email NVARCHAR(150) NOT NULL UNIQUE,
    phone NVARCHAR(20) NOT NULL,
    password NVARCHAR(255) NOT NULL
);

-- Table: contacts
CREATE TABLE contacts (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    first_name NVARCHAR(100) NOT NULL,
    last_name NVARCHAR(100) NOT NULL,
    title NVARCHAR(100),
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users(id) ON DELETE CASCADE
);

-- Table: contact_details
CREATE TABLE contact_details (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    contact_id BIGINT NOT NULL,
    type NVARCHAR(50) NOT NULL,
    value NVARCHAR(150) NOT NULL,
    category NVARCHAR(50) NOT NULL,
    FOREIGN KEY (contact_id) REFERENCES contacts(id) ON DELETE CASCADE
);

-- =====================================================
-- 2. CREATE INDEXES
-- =====================================================

CREATE INDEX idx_users_email ON app_users(email);
CREATE INDEX idx_contacts_user_id ON contacts(user_id);
CREATE INDEX idx_contacts_name ON contacts(first_name, last_name);
CREATE INDEX idx_contact_details_contact_id ON contact_details(contact_id);

-- =====================================================
-- 3. INSERT SAMPLE DATA
-- =====================================================

-- Insert Users
INSERT INTO app_users (name, email, phone, password) VALUES
('Ahmed Ali', 'ahmed@example.com', '+923001234567', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/KFm'),
('Fatima Khan', 'fatima@example.com', '+923009876543', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/KFm'),
('Hassan Raza', 'hassan@example.com', '+923005555555', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/KFm');

-- Insert Contacts
INSERT INTO contacts (first_name, last_name, title, user_id) VALUES
('Ali', 'Khan', 'Software Engineer', 1),
('Sara', 'Ahmed', 'Project Manager', 1),
('Omar', 'Hassan', 'Business Analyst', 1),
('Ayesha', 'Ali', 'Designer', 2),
('Zain', 'Malik', 'Developer', 2);

-- Insert Contact Details
INSERT INTO contact_details (contact_id, type, value, category) VALUES
(1, 'WORK', 'ali.khan@company.com', 'EMAIL'),
(1, 'WORK', '+923001111111', 'PHONE'),
(1, 'PERSONAL', 'ali.personal@gmail.com', 'EMAIL'),
(2, 'WORK', 'sara.ahmed@company.com', 'EMAIL'),
(2, 'WORK', '+923002222222', 'PHONE'),
(2, 'HOME', '+923332222222', 'PHONE'),
(3, 'WORK', 'omar.hassan@company.com', 'EMAIL'),
(3, 'WORK', '+923003333333', 'PHONE'),
(4, 'WORK', 'ayesha.ali@company.com', 'EMAIL'),
(4, 'WORK', '+923004444444', 'PHONE'),
(5, 'WORK', 'zain.malik@company.com', 'EMAIL'),
(5, 'HOME', '+923005555555', 'PHONE');

-- =====================================================
-- 4. VERIFY SETUP
-- =====================================================

PRINT '========================================';
PRINT 'DATABASE SETUP COMPLETE!';
PRINT '========================================';
PRINT '';
PRINT 'Users created:';
SELECT COUNT(*) AS user_count FROM app_users;
PRINT 'Contacts created:';
SELECT COUNT(*) AS contact_count FROM contacts;
PRINT 'Contact details created:';
SELECT COUNT(*) AS detail_count FROM contact_details;
PRINT '';
PRINT 'Database is ready for the Spring Boot application!';
PRINT '========================================';
