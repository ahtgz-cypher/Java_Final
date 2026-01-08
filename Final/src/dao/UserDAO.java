package dao;

import config.DBConnection;
import model.User;
import java.sql.*;
import java.util.Optional;

public class UserDAO {

	// ===== ĐĂNG NHẬP =====
	public static Optional<User> login(String username, String password) {
		String sql = "SELECT u.user_id, u.username, u.password, u.role_id, r.role_name " + "FROM users u "
				+ "JOIN roles r ON u.role_id = r.role_id " + "WHERE u.username = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				String storedPassword = rs.getString("password");

				if (storedPassword.equals(password)) {
					User user = new User(rs.getInt("user_id"), rs.getString("username"), password, rs.getInt("role_id"),
							rs.getString("role_name"));
					return Optional.of(user);
				}
			}
		} catch (SQLException e) {
			System.out.println("✗ Lỗi đăng nhập: " + e.getMessage());
		}
		return Optional.empty();
	}

	// ===== ĐĂNG KÍ =====
	public static boolean register(String username, String password, int roleId) {
		if (username == null || username.trim().isEmpty()) {
			System.err.println("✗ Username không hợp lệ!");
			return false;
		}
		
		// Trim username để tránh khoảng trắng
		username = username.trim();
		
		System.out.println("========================================");
		System.out.println("DEBUG REGISTER: Bắt đầu đăng ký");
		System.out.println("DEBUG: Username: '" + username + "'");
		System.out.println("DEBUG: Role ID: " + roleId);
		System.out.println("========================================");
		
		// Kiểm tra username đã tồn tại chưa
		boolean exists = usernameExists(username);
		System.out.println("DEBUG: Username exists check result: " + exists);
		
		if (exists) {
			System.out.println("✗ Username '" + username + "' đã tồn tại!");
			return false;
		}

		String sql = "INSERT INTO users (username, password, role_id) VALUES (?, ?, ?)";
		System.out.println("DEBUG: Chuẩn bị insert vào database...");

		try (Connection conn = DBConnection.getConnection()) {
			System.out.println("DEBUG: Đã kết nối database thành công");
			
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, username);
				pstmt.setString(2, password);
				pstmt.setInt(3, roleId);
				
				System.out.println("DEBUG: Đang thực thi INSERT...");
				int rowsAffected = pstmt.executeUpdate();
				
				if (rowsAffected > 0) {
					System.out.println("✓ Đăng ký thành công cho username: " + username);
					System.out.println("✓ Rows affected: " + rowsAffected);
					return true;
				} else {
					System.err.println("✗ Không thể insert user (rowsAffected = 0)");
					return false;
				}
			}
		} catch (SQLException e) {
			System.err.println("========================================");
			System.err.println("✗ LỖI SQL KHI ĐĂNG KÝ:");
			System.err.println("✗ Message: " + e.getMessage());
			System.err.println("✗ SQL State: " + e.getSQLState());
			System.err.println("✗ Error Code: " + e.getErrorCode());
			
			// Kiểm tra nếu lỗi do duplicate username
			if (e.getErrorCode() == 1062 || e.getMessage().contains("Duplicate entry") || e.getMessage().contains("Duplicate")) {
				System.err.println("✗ Username '" + username + "' đã tồn tại (từ database constraint)!");
			} else {
				System.err.println("✗ Lỗi đăng kí không xác định!");
			}
			e.printStackTrace();
			System.err.println("========================================");
			return false;
		} catch (Exception e) {
			System.err.println("========================================");
			System.err.println("✗ LỖI KHÔNG XÁC ĐỊNH:");
			System.err.println("✗ Message: " + e.getMessage());
			e.printStackTrace();
			System.err.println("========================================");
			return false;
		}
	}

	// ===== KIỂM TRA USERNAME TỒN TẠI =====
	public static boolean usernameExists(String username) {
		if (username == null || username.trim().isEmpty()) {
			return false;
		}
		
		String trimmedUsername = username.trim();
		// Sử dụng query đơn giản hơn, chỉ so sánh trực tiếp
		String sql = "SELECT user_id FROM users WHERE username = ?";

		try (Connection conn = DBConnection.getConnection(); 
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, trimmedUsername);
			try (ResultSet rs = pstmt.executeQuery()) {
				boolean exists = rs.next();
				System.out.println("DEBUG: Username '" + trimmedUsername + "' exists: " + exists);
				return exists;
			}
		} catch (SQLException e) {
			System.err.println("✗ Lỗi kiểm tra username: " + e.getMessage());
			System.err.println("✗ SQL State: " + e.getSQLState());
			System.err.println("✗ Error Code: " + e.getErrorCode());
			e.printStackTrace();
			// Nếu có lỗi kết nối, return false để cho phép thử insert
			// Lỗi thực sự sẽ được báo khi insert
			return false;
		}
	}

	// ===== QUÊN MẬT KHẨU / ĐẶT LẠI MẬT KHẨU =====
	public static boolean resetPassword(String username, String newPassword) {
		if (!usernameExists(username)) {
			return false;
		}

		String sql = "UPDATE users SET password = ? WHERE username = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, newPassword);
			pstmt.setString(2, username);

			int rows = pstmt.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			System.out.println("✗ Lỗi cập nhật mật khẩu: " + e.getMessage());
			return false;
		}
	}

	// ===== LẤY THÔNG TIN USER =====
	public static Optional<User> getUserById(int userId) {
		String sql = "SELECT u.user_id, u.username, u.password, u.role_id, r.role_name " + "FROM users u "
				+ "JOIN roles r ON u.role_id = r.role_id " + "WHERE u.user_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				User user = new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password"),
						rs.getInt("role_id"), rs.getString("role_name"));
				return Optional.of(user);
			}
		} catch (SQLException e) {
			System.out.println("✗ Lỗi lấy thông tin user: " + e.getMessage());
		}
		return Optional.empty();
	}

	// ===== ĐỔI MẬT KHẨU =====
	public static boolean changePassword(int userId, String oldPassword, String newPassword) {
		Optional<User> user = getUserById(userId);
		if (user.isEmpty()) {
			return false;
		}

		if (!user.get().getPassword().equals(oldPassword)) {
			return false;
		}

		String sql = "UPDATE users SET password = ? WHERE user_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
