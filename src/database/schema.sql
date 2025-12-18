DROP DATABASE IF EXISTS qlsv_diem;
CREATE DATABASE qlsv_diem;
USE qlsv_diem;

-- ===== ROLES =====
CREATE TABLE roles (
    role_id INT PRIMARY KEY,
    role_name VARCHAR(20) UNIQUE NOT NULL
);

-- ===== USERS =====
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role_id INT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

-- ===== TEACHERS =====
CREATE TABLE teachers (
    teacher_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNIQUE,
    full_name VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- ===== STUDENTS =====
CREATE TABLE students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNIQUE,
    full_name VARCHAR(100),
    student_code VARCHAR(20) UNIQUE,
    dob DATE,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- ===== SUBJECTS =====
CREATE TABLE subjects (
    subject_id INT AUTO_INCREMENT PRIMARY KEY,
    subject_name VARCHAR(100),
    teacher_id INT,
    credit INT,
    FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id)
);

-- ===== SCORES =====
CREATE TABLE scores (
    score_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT,
    subject_id INT,
    score DECIMAL(4,2),
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id),
    UNIQUE (student_id, subject_id)
);