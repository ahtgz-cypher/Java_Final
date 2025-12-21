package model;

public class User {
	private int userId;
	private String username;
	private String password;
	private int roleId;
	private String roleName;

	public User(int userId, String username, String password, int roleId, String roleName) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.roleId = roleId;
		this.roleName = roleName;
	}

	// Getters
	public int getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public int getRoleId() {
		return roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	// Setters
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User{" + "userId=" + userId + ", username='" + username + '\'' + ", roleId=" + roleId + ", roleName='"
				+ roleName + '\'' + '}';
	}
}