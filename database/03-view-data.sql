-- =====================================================
-- Step 3: View and Verify Data
-- =====================================================

USE contact_management_db;
GO

-- =====================================================
-- VIEW 1: All Users
-- =====================================================
PRINT '======= ALL USERS =======';
SELECT 
    id,
    name,
    email,
    phone
FROM app_users
ORDER BY id;

-- =====================================================
-- VIEW 2: All Contacts with User Information
-- =====================================================
PRINT '======= ALL CONTACTS =======';
SELECT 
    c.id,
    c.first_name,
    c.last_name,
    c.title,
    u.name AS user_name,
    u.email AS user_email
FROM contacts c
INNER JOIN app_users u ON c.user_id = u.id
ORDER BY c.user_id, c.id;

-- =====================================================
-- VIEW 3: All Contact Details
-- =====================================================
PRINT '======= ALL CONTACT DETAILS =======';
SELECT 
    cd.id,
    c.first_name + ' ' + c.last_name AS contact_name,
    cd.type,
    cd.value,
    cd.category
FROM contact_details cd
INNER JOIN contacts c ON cd.contact_id = c.id
ORDER BY cd.contact_id;

-- =====================================================
-- VIEW 4: Contact Count per User
-- =====================================================
PRINT '======= CONTACT COUNT BY USER =======';
SELECT 
    u.id,
    u.name,
    u.email,
    COUNT(c.id) AS contact_count
FROM app_users u
LEFT JOIN contacts c ON u.id = c.user_id
GROUP BY u.id, u.name, u.email
ORDER BY u.id;

-- =====================================================
-- VIEW 5: Details Count per Contact
-- =====================================================
PRINT '======= DETAILS COUNT BY CONTACT =======';
SELECT 
    c.id,
    c.first_name + ' ' + c.last_name AS contact_name,
    COUNT(cd.id) AS detail_count
FROM contacts c
LEFT JOIN contact_details cd ON c.id = cd.contact_id
GROUP BY c.id, c.first_name, c.last_name
ORDER BY c.id;
