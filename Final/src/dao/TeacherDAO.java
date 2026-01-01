package dao;

import config.DBConnection;
import model.User;
import java.sql.*;

public class TeacherDAO {
    
    // ===== LẤY TEACHER_ID TỪ USER_ID =====
    public static int getTeacherIdByUserId(int userId) {
        String sql = "SELECT teacher_id FROM teachers WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("teacher_id");
            }
        } catch (SQLException e) {
            System.err.println("✗ Lỗi lấy teacher_id: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
    
    // ===== LẤY TÊN GIÁO VIÊN TỪ USER_ID =====
    public static String getTeacherNameByUserId(int userId) {
        String sql = "SELECT full_name FROM teachers WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("full_name");
            }
        } catch (SQLException e) {
            System.err.println("✗ Lỗi lấy tên giáo viên: " + e.getMessage());
            e.printStackTrace();
        }
        return "Giáo viên";
    }
}
