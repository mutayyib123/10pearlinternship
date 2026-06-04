-- =====================================================
-- Step 5: Drop Database (Complete Cleanup)
-- =====================================================

-- USE MASTER DATABASE TO DROP
USE master;
GO

-- DROP DATABASE IF EXISTS
DROP DATABASE IF EXISTS contact_management_db;
GO

PRINT 'Database has been dropped completely.';
