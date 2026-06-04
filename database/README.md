# Contact Management System - Database Setup Guide

## 📋 Database Overview

This SQL Server database setup creates a Contact Management System with the following components:

### Tables
1. **app_users** - User accounts
2. **contacts** - Contact information linked to users
3. **contact_details** - Detailed contact information (emails, phone numbers)

---

## 🚀 Setup Instructions

### Option 1: Complete Setup (Recommended)

Run all scripts in order:

```sql
-- Step 1: Create database and tables
01-create-database.sql

-- Step 2: Insert sample data
02-sample-data.sql

-- Step 3: View data (optional)
03-view-data.sql
```

### Option 2: Manual Setup

Execute the main script:
```sql
database
```

---

## 📁 Script Files

| File | Purpose |
|------|---------|
| `database` | Complete setup with everything included |
| `01-create-database.sql` | Create database and schema only |
| `02-sample-data.sql` | Insert sample data for testing |
| `03-view-data.sql` | View and verify data |
| `04-reset-database.sql` | Delete all data (keep tables) |
| `05-drop-database.sql` | Complete cleanup (delete everything) |

---

## 🗂️ Database Schema

### app_users Table
```sql
id          BIGINT (Primary Key)
name        NVARCHAR(100)
email       NVARCHAR(150) UNIQUE
phone       NVARCHAR(20)
password    NVARCHAR(255) - BCrypt encrypted
```

### contacts Table
```sql
id          BIGINT (Primary Key)
first_name  NVARCHAR(100)
last_name   NVARCHAR(100)
title       NVARCHAR(100)
user_id     BIGINT (Foreign Key → app_users)
```

### contact_details Table
```sql
id          BIGINT (Primary Key)
contact_id  BIGINT (Foreign Key → contacts)
type        NVARCHAR(50) - WORK/HOME/PERSONAL
value       NVARCHAR(150) - Email or Phone
category    NVARCHAR(50) - EMAIL/PHONE
```

---

## 📊 Sample Data

### Users
- Ahmed Ali (ahmed@example.com)
- Fatima Khan (fatima@example.com)
- Hassan Raza (hassan@example.com)

### Contacts
- Ahmed has 3 contacts: Ali Khan, Sara Ahmed, Omar Hassan
- Fatima has 2 contacts: Ayesha Ali, Zain Malik

### Contact Details
- Each contact has 1-3 details (emails and phone numbers)

---

## 🔑 Indexes

Created for performance optimization:
- `idx_users_email` - For user login queries
- `idx_contacts_user_id` - For fetching user contacts
- `idx_contacts_name` - For searching contacts by name
- `idx_contact_details_contact_id` - For fetching contact details

---

## ⚙️ Connection Settings

For Spring Boot application:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=contact_management_db;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=YourPassword
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
```

---

## 📝 Notes

1. **Password Hashes**: Sample passwords are BCrypt encoded for "password123"
2. **Foreign Keys**: Cascade delete enabled on user and contact deletions
3. **Email Uniqueness**: User emails are unique at database level
4. **Identity Columns**: All IDs use auto-increment (IDENTITY)

---

## 🔄 Reset Workflow

1. **Keep Tables, Clear Data**:
   ```sql
   04-reset-database.sql
   ```
   Then re-run `02-sample-data.sql`

2. **Complete Cleanup**:
   ```sql
   05-drop-database.sql
   ```
   Then start fresh with `01-create-database.sql`

---

## ✅ Verification Queries

```sql
-- Check users
SELECT COUNT(*) FROM app_users;

-- Check contacts
SELECT COUNT(*) FROM contacts;

-- Check contact details
SELECT COUNT(*) FROM contact_details;

-- Check relationships
SELECT u.name, COUNT(c.id) FROM app_users u 
LEFT JOIN contacts c ON u.id = c.user_id 
GROUP BY u.name;
```

---

## 🆘 Troubleshooting

**Error: "Database already exists"**
- Uncomment and run the DROP statement in step 1, or run `05-drop-database.sql`

**Error: "Foreign key constraint failed"**
- Ensure you run scripts in order: create tables first, then add constraints

**Error: "Identity seed value is negative"**
- Run `04-reset-database.sql` to reset identity values

---

## 📞 Support

For issues or questions about the database setup, refer to the SQL Server documentation or check the Spring Boot application logs.
