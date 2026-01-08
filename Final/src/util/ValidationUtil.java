package util;

public class ValidationUtil {
	
	// ===== VALIDATION ĐĂNG KÝ =====
	public static String validateRegisterInput(String username, String password, String confirmPassword) {
		// Kiểm tra username
		if (username == null || username.trim().isEmpty()) {
			return "✗ Username không được để trống!";
		}
		
		if (username.length() < 3) {
			return "✗ Username phải ít nhất 3 ký tự!";
		}
		
		if (username.length() > 50) {
			return "✗ Username tối đa 50 ký tự!";
		}
		
		// Kiểm tra password
		if (password == null || password.isEmpty()) {
			return "✗ Mật khẩu không được để trống!";
		}
		
		if (password.length() < 6) {
			return "✗ Mật khẩu tối thiểu 6 ký tự!";
		}
		
		if (password.length() > 100) {
			return "✗ Mật khẩu tối đa 100 ký tự!";
		}
		
		// Kiểm tra xác nhận password
		if (!password.equals(confirmPassword)) {
			return "✗ Mật khẩu không khớp!";
		}
		
		return null; // Hợp lệ
	}
	
	// ===== VALIDATION ĐĂNG NHẬP =====
	public static String validateLoginInput(String username, String password) {
		if (username == null || username.trim().isEmpty()) {
			return "✗ Vui lòng nhập username!";
		}
		
		if (password == null || password.isEmpty()) {
			return "✗ Vui lòng nhập mật khẩu!";
		}
		
		return null; // Hợp lệ
	}
	
	// ===== VALIDATION ĐẶT LẠI MẬT KHẨU =====
	public static String validateResetPasswordInput(String username, String newPassword, String confirmPassword) {
		if (username == null || username.trim().isEmpty()) {
			return "✗ Vui lòng nhập username!";
		}
		
		if (newPassword == null || newPassword.isEmpty()) {
			return "✗ Mật khẩu không được để trống!";
		}
		
		if (newPassword.length() < 6) {
			return "✗ Mật khẩu tối thiểu 6 ký tự!";
		}
		
		if (!newPassword.equals(confirmPassword)) {
			return "✗ Mật khẩu không khớp!";
		}
		
		return null; // Hợp lệ
	}
	
	// ===== VALIDATION ĐỔI MẬT KHẨU =====
	public static String validateChangePasswordInput(String oldPassword, String newPassword, String confirmPassword) {
		if (oldPassword == null || oldPassword.isEmpty()) {
			return "✗ Vui lòng nhập mật khẩu cũ!";
		}
		
		if (newPassword == null || newPassword.isEmpty()) {
			return "✗ Mật khẩu mới không được để trống!";
		}
		
		if (newPassword.length() < 6) {
			return "✗ Mật khẩu tối thiểu 6 ký tự!";
		}
		
		if (!newPassword.equals(confirmPassword)) {
			return "✗ Mật khẩu không khớp!";
		}
		
		if (oldPassword.equals(newPassword)) {
			return "✗ Mật khẩu mới phải khác mật khẩu cũ!";
		}
		
		return null; // Hợp lệ
	}
}