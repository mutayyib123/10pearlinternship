-- =====================================================
-- Step 1: Create Database and Tables
-- =====================================================

-- 1. DROP EXISTING DATABASE (Optional)
-- DROP DATABASE IF EXISTS contact_management_db;

-- 2. CREATE DATABASE
CREATE DATABASE contact_management_db;
GO

-- 3. USE THE DATABASE
USE contact_management_db;
GO

-- =====================================================
-- 4. CREATE TABLES
-- =====================================================

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
-- 5. CREATE INDEXES
-- =====================================================

CREATE INDEX idx_users_email ON app_users(email);
CREATE INDEX idx_contacts_user_id ON contacts(user_id);
CREATE INDEX idx_contacts_name ON contacts(first_name, last_name);
CREATE INDEX idx_contact_details_contact_id ON contact_details(contact_id);

PRINT 'Database and tables created successfully!';
