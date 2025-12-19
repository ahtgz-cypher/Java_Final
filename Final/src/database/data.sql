USE qlsv_diem;

INSERT INTO roles VALUES
(1,'ADMIN'),
(2,'TEACHER'),
(3,'STUDENT');

INSERT INTO users (username,password,role_id) VALUES
('admin1','123456',1),
('admin2','123456',1),

('teacher1','123456',2),
('teacher2','123456',2),
('teacher3','123456',2),
('teacher4','123456',2),
('teacher5','123456',2),

('sv01','123456',3),
('sv02','123456',3),
('sv03','123456',3),
('sv04','123456',3),
('sv05','123456',3),
('sv06','123456',3),
('sv07','123456',3),
('sv08','123456',3),
('sv09','123456',3),
('sv10','123456',3),
('sv11','123456',3),
('sv12','123456',3),
('sv13','123456',3),
('sv14','123456',3),
('sv15','123456',3);

INSERT INTO teachers (user_id, full_name) VALUES
(3,'Nguyen Van T1'),
(4,'Tran Van T2'),
(5,'Le Van T3'),
(6,'Pham Van T4'),
(7,'Hoang Van T5');

INSERT INTO students (user_id, full_name, student_code, dob) VALUES
(8,'Nguyen Van A','SV001','2003-01-10'),
(9,'Tran Thi B','SV002','2003-02-15'),
(10,'Le Van C','SV003','2003-03-20'),
(11,'Pham Thi D','SV004','2003-04-25'),
(12,'Hoang Van E','SV005','2003-05-30'),
(13,'Do Thi F','SV006','2003-06-12'),
(14,'Bui Van G','SV007','2003-07-18'),
(15,'Vu Thi H','SV008','2003-08-22'),
(16,'Dang Van I','SV009','2003-09-05'),
(17,'Ngo Thi K','SV010','2003-10-11'),
(18,'Ly Van L','SV011','2003-11-03'),
(19,'Mai Thi M','SV012','2003-12-19'),
(20,'Pham Van N','SV013','2004-01-08'),
(21,'Nguyen Thi O','SV014','2004-02-14'),
(22,'Tran Van P','SV015','2004-03-21');

INSERT INTO subjects (subject_name, teacher_id, credit) VALUES
('Lap trinh Java',1,3),
('Co so du lieu',2,3),
('Mang may tinh',3,3),
('Cau truc du lieu',4,4),
('He dieu hanh',5,3);

INSERT INTO scores (student_id, subject_id, score)
SELECT s.student_id, sub.subject_id, ROUND(RAND()*4+6,1)
FROM students s
CROSS JOIN subjects sub;