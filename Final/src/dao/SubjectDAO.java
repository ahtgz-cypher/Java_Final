package dao;

import config.DBConnection;
import model.Subject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SubjectDAO {
    
    // ===== LẤY TẤT CẢ MÔN HỌC =====
    public static List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM subjects ORDER BY subject_name";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Subject subject = new Subject(
                    rs.getInt("subject_id"),
                    rs.getString("subject_name"),
                    rs.getInt("teacher_id"),
                    rs.getInt("credit")
                );
                subjects.add(subject);
            }
        } catch (SQLException e) {
            System.err.println("✗ Lỗi lấy danh sách môn học: " + e.getMessage());
            e.printStackTrace();
        }
        return subjects;
    }
    
    // ===== LẤY MÔN HỌC THEO ID =====
    public static Optional<Subject> getSubjectById(int subjectId) {
        String sql = "SELECT * FROM subjects WHERE subject_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, subjectId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Subject subject = new Subject(
                    rs.getInt("subject_id"),
                    rs.getString("subject_name"),
                    rs.getInt("teacher_id"),
                    rs.getInt("credit")
                );
                return Optional.of(subject);
            }
        } catch (SQLException e) {
            System.err.println("✗ Lỗi lấy thông tin môn học: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }
    
    // ===== LẤY MÔN HỌC CỦA GIÁO VIÊN =====
    public static List<Subject> getSubjectsByTeacher(int teacherId) {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM subjects WHERE teacher_id = ? ORDER BY subject_name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, teacherId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Subject subject = new Subject(
                    rs.getInt("subject_id"),
                    rs.getString("subject_name"),
                    rs.getInt("teacher_id"),
                    rs.getInt("credit")
                );
                subjects.add(subject);
            }
        } catch (SQLException e) {
            System.err.println("✗ Lỗi lấy danh sách môn học của giáo viên: " + e.getMessage());
            e.printStackTrace();
        }
        return subjects;
    }
    
    // ===== THÊM MÔN HỌC =====
    public static boolean addSubject(String subjectName, int teacherId, int credit) {
        String sql = "INSERT INTO subjects (subject_name, teacher_id, credit) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, subjectName);
            pstmt.setInt(2, teacherId);
            pstmt.setInt(3, credit);
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("✗ Lỗi thêm môn học: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
