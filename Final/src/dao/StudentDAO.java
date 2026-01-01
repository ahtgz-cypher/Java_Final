package dao;

import config.DBConnection;
import model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDAO {
    
    // ===== TÌM KIẾM SINH VIÊN THEO MÃ HOẶC TÊN =====
    public static List<Student> searchStudents(String keyword) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE student_code LIKE ? OR full_name LIKE ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Student student = new Student(
                    rs.getInt("student_id"),
                    rs.getInt("user_id"),
                    rs.getString("full_name"),
                    rs.getString("student_code"),
                    rs.getDate("dob")
                );
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("✗ Lỗi tìm kiếm sinh viên: " + e.getMessage());
            e.printStackTrace();
        }
        return students;
    }
    
    // ===== LẤY THÔNG TIN SINH VIÊN THEO ID =====
    public static Optional<Student> getStudentById(int studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Student student = new Student(
                    rs.getInt("student_id"),
                    rs.getInt("user_id"),
                    rs.getString("full_name"),
                    rs.getString("student_code"),
                    rs.getDate("dob")
                );
                return Optional.of(student);
            }
        } catch (Exception e) {
            System.out.println("✗ Lỗi lấy thông tin sinh viên: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }
    
    // ===== LẤY THÔNG TIN SINH VIÊN THEO MÃ SINH VIÊN =====
    public static Optional<Student> getStudentByCode(String studentCode) {
        String sql = "SELECT * FROM students WHERE student_code = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, studentCode);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Student student = new Student(
                    rs.getInt("student_id"),
                    rs.getInt("user_id"),
                    rs.getString("full_name"),
                    rs.getString("student_code"),
                    rs.getDate("dob")
                );
                return Optional.of(student);
            }
        } catch (Exception e) {
            System.out.println("✗ Lỗi lấy thông tin sinh viên: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }
    
    // ===== LẤY TẤT CẢ SINH VIÊN =====
    public static List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY student_code";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Student student = new Student(
                    rs.getInt("student_id"),
                    rs.getInt("user_id"),
                    rs.getString("full_name"),
                    rs.getString("student_code"),
                    rs.getDate("dob")
                );
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("✗ Lỗi lấy danh sách sinh viên: " + e.getMessage());
            e.printStackTrace();
        }
        return students;
    }
    
    // ===== THÊM SINH VIÊN MỚI =====
    public static boolean addStudent(int userId, String fullName, String studentCode, Date dob) {
        String sql = "INSERT INTO students (user_id, full_name, student_code, dob) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, fullName);
            pstmt.setString(3, studentCode);
            pstmt.setDate(4, dob);
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("✗ Lỗi thêm sinh viên: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ===== CẬP NHẬT THÔNG TIN SINH VIÊN =====
    public static boolean updateStudent(int studentId, String fullName, String studentCode, Date dob) {
        String sql = "UPDATE students SET full_name = ?, student_code = ?, dob = ? WHERE student_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, fullName);
            pstmt.setString(2, studentCode);
            pstmt.setDate(3, dob);
            pstmt.setInt(4, studentId);
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("✗ Lỗi cập nhật sinh viên: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
