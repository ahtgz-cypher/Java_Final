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
		if (usernameExists(username)) {
			return false;
		}

		String sql = "INSERT INTO users (username, password, role_id) VALUES (?, ?, ?)";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.setInt(3, roleId);

			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println("✗ Lỗi đăng kí: " + e.getMessage());
			return false;
		}
	}

	// ===== KIỂM TRA USERNAME TỒN TẠI =====
	public static boolean usernameExists(String username) {
		String sql = "SELECT user_id FROM users WHERE username = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			System.out.println("✗ Lỗi kiểm tra username: " + e.getMessage());
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
