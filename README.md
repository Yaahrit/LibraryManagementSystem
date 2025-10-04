# Library Management System – Quick Start Guide

## Overview

This is a **Java Swing Desktop Library Management System** using **MySQL**.  
Supports **Admin** and **User** roles with dashboards to manage books, members, and transactions.

---

## Quick Features

**Admin**  
- Login with `admin/admin123`  
- Manage Books, Members, Users, and Transactions  

**User**  
- Login with any new username → auto-registers  
- Browse books, borrow/return, view transactions  

---

## Dummy Credentials

| Role  | Username | Password   |
|-------|----------|------------|
| Admin | admin    | admin123   |
| User  | Any new username | Any password |

> Note: Users are automatically registered if they don’t exist in the system.

---

## Setup & Run

### 1. Create Database & Tables

Run these commands in MySQL:

```sql
CREATE DATABASE library;

USE library;

-- Books Table
CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    publisher VARCHAR(255),
    isbn VARCHAR(50),
    year INT,
    copies_available INT DEFAULT 0
);

-- Members Table
CREATE TABLE members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20)
);

-- Transactions Table
CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    book_id INT NOT NULL,
    issue_date DATE NOT NULL,
    return_date DATE,
    status ENUM('ISSUED','RETURNED','OVERDUE') NOT NULL,
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

-- Users Table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    role ENUM('ADMIN','USER') NOT NULL
);

2. Insert Dummy Data
  1.50 Books, 20 Members, 20 Transactions (see previous scripts)
  2.Admin user:
       INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'ADMIN');
  3. Update Database Connection
        Edit DBUtil.java with your MySQL username, password, and database name.
         private static final String URL = "jdbc:mysql://localhost:3306/library";
          private static final String USER = "root"; 
          private static final String PASSWORD = "your_mysql_password";
  4. Compile & Run
      From the src folder:
      javac model/*.java dao/*.java db/*.java ui/*.java
      java ui.LoginFrame
          1.Admin login: admin/admin123 → Admin Dashboard
          2.Any new username → auto-registers → User Dashboard

Folder Structure
LibraryManagementSystem/
 ├─ src/
 │   ├─ model/        # Book, Member, Transaction, User
 │   ├─ dao/          # CRUD operations
 │   ├─ db/           # Database connection
 │   └─ ui/           # GUI (LoginFrame, Dashboards)
 └─ README_QUICK.md

Notes
  1.Users are stored in MySQL when logging in.
  2.Borrow/Return transactions update copies_available automatically.
  3.Can be extended with search, reports, and overdue notifications.
