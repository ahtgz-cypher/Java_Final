USE qlsv_diem;

-- Xem danh sách sinh viên
SELECT * FROM students;

-- Xem điểm từng sinh viên
SELECT s.student_code, s.full_name, sub.subject_name, sc.score
FROM scores sc
JOIN students s ON sc.student_id = s.student_id
JOIN subjects sub ON sc.subject_id = sub.subject_id;

-- Điểm trung bình
SELECT s.student_code, s.full_name, ROUND(AVG(sc.score),2) AS avg_score
FROM scores sc
JOIN students s ON sc.student_id = s.student_id
GROUP BY s.student_id;
