package ui;

import dao.UserDAO;
import util.ValidationUtil;
import javax.swing.*;
import java.awt.*;

public class RegisterPage extends JFrame {
	private JTextField txtUsername;
	private JPasswordField txtPassword, txtConfirmPassword;
	private JComboBox<String> cmbRole;
	private JButton btnRegister, btnBack;
	private JLabel lblMessage;

	public RegisterPage() {
		initUI();
	}

	private void initUI() {
		setTitle("Hệ Thống Quản Lí Sinh Viên - Đăng Kí");
		setSize(400, 450);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBackground(new Color(240, 240, 240));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

		JLabel lblTitle = new JLabel("ĐĂNG KÍ TÀI KHOẢN");
		lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitle.setForeground(new Color(46, 204, 113));
		lblTitle.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(lblTitle);
		mainPanel.add(Box.createVerticalStrut(20));

		JLabel lblUsername = new JLabel("Username:");
		mainPanel.add(lblUsername);
		txtUsername = new JTextField();
		txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
		txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
		mainPanel.add(txtUsername);
		mainPanel.add(Box.createVerticalStrut(10));

		JLabel lblRole = new JLabel("Loại tài khoản:");
		mainPanel.add(lblRole);
		cmbRole = new JComboBox<>(new String[] { "STUDENT", "TEACHER", "ADMIN" });
		cmbRole.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
		cmbRole.setFont(new Font("Arial", Font.PLAIN, 14));
		mainPanel.add(cmbRole);
		mainPanel.add(Box.createVerticalStrut(10));

		JLabel lblPassword = new JLabel("Mật khẩu:");
		mainPanel.add(lblPassword);
		txtPassword = new JPasswordField();
		txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
		txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
		mainPanel.add(txtPassword);
		mainPanel.add(Box.createVerticalStrut(10));

		JLabel lblConfirmPassword = new JLabel("Xác nhận mật khẩu:");
		mainPanel.add(lblConfirmPassword);
		txtConfirmPassword = new JPasswordField();
		txtConfirmPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
		txtConfirmPassword.setFont(new Font("Arial", Font.PLAIN, 14));
		mainPanel.add(txtConfirmPassword);
		mainPanel.add(Box.createVerticalStrut(15));

		lblMessage = new JLabel("");
		lblMessage.setFont(new Font("Arial", Font.PLAIN, 11));
		lblMessage.setForeground(Color.RED);
		lblMessage.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(lblMessage);
		mainPanel.add(Box.createVerticalStrut(5));

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		buttonPanel.setOpaque(false);

		btnRegister = new JButton("Đăng Kí");
		btnRegister.setPreferredSize(new Dimension(100, 35));
		btnRegister.setFont(new Font("Arial", Font.BOLD, 12));
		btnRegister.setBackground(new Color(46, 204, 113));
		btnRegister.setForeground(Color.WHITE);
		btnRegister.setBorder(BorderFactory.createEmptyBorder());
		btnRegister.addActionListener(e -> handleRegister());
		buttonPanel.add(btnRegister);

		btnBack = new JButton("Quay lại");
		btnBack.setPreferredSize(new Dimension(100, 35));
		btnBack.setFont(new Font("Arial", Font.BOLD, 12));
		btnBack.setBackground(new Color(149, 165, 166));
		btnBack.setForeground(Color.WHITE);
		btnBack.setBorder(BorderFactory.createEmptyBorder());
		btnBack.addActionListener(e -> backToLogin());
		buttonPanel.add(btnBack);

		mainPanel.add(buttonPanel);
		add(mainPanel);
		setVisible(true);
	}

	private void handleRegister() {
		String username = txtUsername.getText();
		String password = new String(txtPassword.getPassword());
		String confirmPassword = new String(txtConfirmPassword.getPassword());
		int roleId = cmbRole.getSelectedIndex() + 3; // 3=STUDENT, 2=TEACHER, 1=ADMIN

		String error = ValidationUtil.validateRegisterInput(username, password, confirmPassword);
		if (error != null) {
			lblMessage.setText(error);
			lblMessage.setForeground(Color.RED);
			return;
		}

		if (UserDAO.register(username, password, roleId)) {
			lblMessage.setText("✓ Đăng kí thành công!");
			lblMessage.setForeground(Color.GREEN);
			JOptionPane.showMessageDialog(this, "Tài khoản đã được tạo!\nVui lòng đăng nhập.");
			backToLogin();
		} else {
			lblMessage.setText("✗ Username đã tồn tại!");
			lblMessage.setForeground(Color.RED);
		}
	}

	private void backToLogin() {
		new LoginPage();
		dispose();
	}
}