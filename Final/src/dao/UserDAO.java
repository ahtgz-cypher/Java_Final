package dao;

import config.DBConnection;
import model.User;
import java.sql.*;
import java.util.Optional;

public class UserDAO {

    // ==========================================================
    // 1. CHỨC NĂNG ĐĂNG NHẬP (Đã Fix lỗi khoảng trắng & Debug)
    // ==========================================================
    public static Optional<User> login(String username, String password) {
        System.out.println("------------------------------------------------");
        System.out.println("DEBUG DAO: Bắt đầu xử lý đăng nhập cho: " + username);

        String sql = "SELECT u.user_id, u.username, u.password, u.role_id, r.role_name " 
                   + "FROM users u "
                   + "JOIN roles r ON u.role_id = r.role_id " 
                   + "WHERE u.username = ?";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Lấy mật khẩu gốc từ Database
                    String storedPassword = rs.getString("password");
                    
                    // --- DEBUG QUAN TRỌNG: IN RA MÀN HÌNH ĐỂ SO SÁNH ---
                    System.out.println("DEBUG DAO: Tìm thấy User trong DB!");
                    System.out.println("   > Pass trong DB (Gốc):   [" + storedPassword + "]");
                    System.out.println("   > Pass người dùng nhập:  [" + password + "]");
                    
                    // Xử lý null và cắt khoảng trắng thừa (QUAN TRỌNG)
                    if (storedPassword != null) {
                        storedPassword = storedPassword.trim();
                    }
                    
                    // So sánh
                    if (storedPassword.equals(password)) {
                        System.out.println("DEBUG DAO: ==> MẬT KHẨU TRÙNG KHỚP! Đăng nhập thành công.");
                        
                        User user = new User(
                            rs.getInt("user_id"), 
                            rs.getString("username"), 
                            storedPassword, 
                            rs.getInt("role_id"),
                            rs.getString("role_name")
                        );
                        return Optional.of(user);
                    } else {
                        System.out.println("DEBUG DAO: ==> MẬT KHẨU KHÔNG KHỚP!");
                    }
                } else {
                    System.out.println("DEBUG DAO: Query chạy thành công nhưng KHÔNG tìm thấy dòng nào.");
                    System.out.println("   > Nguyên nhân: Username sai HOẶC User đó chưa có Role hợp lệ trong bảng 'roles'.");
                }
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi SQL khi đăng nhập: " + e.getMessage());
            e.printStackTrace();
        }
        
        return Optional.empty();
    }

    // ==========================================================
    // 2. CHỨC NĂNG ĐĂNG KÝ
    // ==========================================================
    public static boolean register(String username, String password, int roleId) {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("✗ Username không hợp lệ!");
            return false;
        }
        
        username = username.trim();
        
        // Kiểm tra username đã tồn tại chưa
        if (usernameExists(username)) {
            System.out.println("✗ Username '" + username + "' đã tồn tại!");
            return false;
        }

        String sql = "INSERT INTO users (username, password, role_id) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setInt(3, roleId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ Đăng ký thành công user: " + username);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Lỗi SQL khi đăng ký: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // ==========================================================
    // 3. KIỂM TRA USERNAME TỒN TẠI
    // ==========================================================
    public static boolean usernameExists(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        String sql = "SELECT user_id FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("✗ Lỗi kiểm tra username: " + e.getMessage());
            return false;
        }
    }

    // ==========================================================
    // 4. LẤY THÔNG TIN USER THEO ID
    // ==========================================================
    public static Optional<User> getUserById(int userId) {
        String sql = "SELECT u.user_id, u.username, u.password, u.role_id, r.role_name " 
                   + "FROM users u "
                   + "JOIN roles r ON u.role_id = r.role_id " 
                   + "WHERE u.user_id = ?";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                        rs.getInt("user_id"), 
                        rs.getString("username"), 
                        rs.getString("password"),
                        rs.getInt("role_id"), 
                        rs.getString("role_name")
                    );
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi lấy thông tin user: " + e.getMessage());
        }
        return Optional.empty();
    }

    // ==========================================================
    // 5. QUÊN MẬT KHẨU / RESET MẬT KHẨU
    // ==========================================================
    public static boolean resetPassword(String username, String newPassword) {
        if (!usernameExists(username)) {
            return false;
        }

        String sql = "UPDATE users SET password = ? WHERE username = ?";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("✗ Lỗi reset mật khẩu: " + e.getMessage());
            return false;
        }
    }

    // ==========================================================
    // 6. ĐỔI MẬT KHẨU (CHANGE PASSWORD)
    // ==========================================================
    public static boolean changePassword(int userId, String oldPassword, String newPassword) {
        // Kiểm tra mật khẩu cũ có đúng không trước khi đổi
        Optional<User> userOpt = getUserById(userId);
        if (userOpt.isEmpty()) {
            return false;
        }
        
        User user = userOpt.get();
        // So sánh mật khẩu cũ (có trim để an toàn)
        String currentDbPass = user.getPassword() != null ? user.getPassword().trim() : "";
        
        if (!currentDbPass.equals(oldPassword)) {
            return false; // Mật khẩu cũ không khớp
        }

        String sql = "UPDATE users SET password = ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userId);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("✗ Lỗi đổi mật khẩu: " + e.getMessage());
            return false;
        }
    }
}