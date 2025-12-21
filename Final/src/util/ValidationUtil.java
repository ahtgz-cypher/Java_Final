package util;

import java.util.regex.Pattern;

public class ValidationUtil {
	private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]{3,20}$";
	private static final String PASSWORD_PATTERN = "^.{6,}$";

	public static boolean isValidUsername(String username) {
		return username != null && Pattern.matches(USERNAME_PATTERN, username);
	}

	public static boolean isValidPassword(String password) {
		return password != null && Pattern.matches(PASSWORD_PATTERN, password);
	}

	public static String validateLoginInput(String username, String password) {
		if (username == null || username.trim().isEmpty()) {
			return "Vui lòng nhập username!";
		}
		if (password == null || password.trim().isEmpty()) {
			return "Vui lòng nhập mật khẩu!";
		}
		if (username.length() < 3) {
			return "Username tối thiểu 3 ký tự!";
		}
		if (password.length() < 6) {
			return "Mật khẩu tối thiểu 6 ký tự!";
		}
		return null;
	}

	public static String validateRegisterInput(String username, String password, String confirmPassword) {
		if (!isValidUsername(username)) {
			return "Username không hợp lệ (3-20 ký tự)!";
		}
		if (!isValidPassword(password)) {
			return "Mật khẩu tối thiểu 6 ký tự!";
		}
		if (!password.equals(confirmPassword)) {
			return "Mật khẩu xác nhận không khớp!";
		}
		return null;
	}
}
