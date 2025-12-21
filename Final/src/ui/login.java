package ui;

import model.User;
import dao.UserDAO;
import util.ValidationUtil;
import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class login extends JFrame {
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JButton btnLogin, btnRegister, btnForgotPassword;
	private JLabel lblMessage;

	public login() {
		initUI();
	}

	private void initUI() {
		setTitle("Hệ Thống Quản Lí Sinh Viên - Đăng Nhập");
		setSize(400, 350);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBackground(new Color(240, 240, 240));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

		// Title
		JLabel lblTitle = new JLabel("ĐĂNG NHẬP");
		lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitle.setForeground(new Color(41, 128, 185));
		lblTitle.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(lblTitle);
		mainPanel.add(Box.createVerticalStrut(20));

		// Username
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Arial", Font.PLAIN, 12));
		mainPanel.add(lblUsername);
		txtUsername = new JTextField();
		txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
		txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
		mainPanel.add(txtUsername);
		mainPanel.add(Box.createVerticalStrut(10));

		// Password
		JLabel lblPassword = new JLabel("Mật khẩu:");
		lblPassword.setFont(new Font("Arial", Font.PLAIN, 12));
		mainPanel.add(lblPassword);
		txtPassword = new JPasswordField();
		txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
		txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
		mainPanel.add(txtPassword);
		mainPanel.add(Box.createVerticalStrut(15));

		// Message
		lblMessage = new JLabel("");
		lblMessage.setFont(new Font("Arial", Font.PLAIN, 11));
		lblMessage.setForeground(Color.RED);
		lblMessage.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(lblMessage);
		mainPanel.add(Box.createVerticalStrut(5));

		// Buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		buttonPanel.setOpaque(false);

		btnLogin = new JButton("Đăng Nhập");
		btnLogin.setPreferredSize(new Dimension(100, 35));
		btnLogin.setFont(new Font("Arial", Font.BOLD, 12));
		btnLogin.setBackground(new Color(41, 128, 185));
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setBorder(BorderFactory.createEmptyBorder());
		btnLogin.addActionListener(e -> handleLogin());
		buttonPanel.add(btnLogin);

		btnRegister = new JButton("Đăng Kí");
		btnRegister.setPreferredSize(new Dimension(100, 35));
		btnRegister.setFont(new Font("Arial", Font.BOLD, 12));
		btnRegister.setBackground(new Color(46, 204, 113));
		btnRegister.setForeground(Color.WHITE);
		btnRegister.setBorder(BorderFactory.createEmptyBorder());
		btnRegister.addActionListener(e -> openRegisterPage());
		buttonPanel.add(btnRegister);

		mainPanel.add(buttonPanel);
		mainPanel.add(Box.createVerticalStrut(10));

		btnForgotPassword = new JButton("Quên mật khẩu?");
		btnForgotPassword.setFont(new Font("Arial", Font.PLAIN, 11));
		btnForgotPassword.setForeground(new Color(41, 128, 185));
		btnForgotPassword.setBorderPainted(false);
		btnForgotPassword.setContentAreaFilled(false);
		btnForgotPassword.setAlignmentX(CENTER_ALIGNMENT);
		btnForgotPassword.addActionListener(e -> openForgotPasswordPage());
		mainPanel.add(btnForgotPassword);

		add(mainPanel);
		setVisible(true);
	}

	private void handleLogin() {
		String username = txtUsername.getText();
		String password = new String(txtPassword.getPassword());

		String error = ValidationUtil.validateLoginInput(username, password);
		if (error != null) {
			lblMessage.setText(error);
			lblMessage.setForeground(Color.RED);
			return;
		}

		Optional<User> loginResult = UserDAO.login(username, password);
		if (loginResult.isPresent()) {
			User user = loginResult.get();
			lblMessage.setText("✓ Đăng nhập thành công!");
			lblMessage.setForeground(Color.GREEN);

			SwingUtilities.invokeLater(() -> {
				openDashboardByRole(user);
				dispose();
			});
		} else {
			lblMessage.setText("✗ Username hoặc mật khẩu sai!");
			lblMessage.setForeground(Color.RED);
			txtPassword.setText("");
		}
	}

	private void openDashboardByRole(User user) {
		switch (user.getRoleId()) {
		case 1: // ADMIN
			new AdminPage(user);
			break;
		case 2: // TEACHER
			new TeacherPage(user);
			break;
		case 3: // STUDENT
			new StudentPage(user);
			break;
		default:
			JOptionPane.showMessageDialog(this, "Role không hợp lệ!");
		}
	}

	private void openRegisterPage() {
		new RegisterPage();
		dispose();
	}

	private void openForgotPasswordPage() {
		new ForgotPasswordPage();
		dispose();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(LoginPage::new);
	}
}