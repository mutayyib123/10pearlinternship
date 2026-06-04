-- =====================================================
-- Step 4: Reset Database (Delete All Data)
-- =====================================================

USE contact_management_db;
GO

-- DELETE ALL DATA (TABLES REMAIN INTACT)
DELETE FROM contact_details;
DELETE FROM contacts;
DELETE FROM app_users;

-- RESET IDENTITY SEEDS
DBCC CHECKIDENT (app_users, RESEED, 0);
DBCC CHECKIDENT (contacts, RESEED, 0);
DBCC CHECKIDENT (contact_details, RESEED, 0);

PRINT 'All data has been deleted. Identity seeds have been reset.';
PRINT 'You can now insert new data using 02-sample-data.sql';
