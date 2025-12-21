package ui;

import dao.UserDAO;
import javax.swing.*;
import java.awt.*;

public class ForgotPasswordPage extends JFrame {
	private JTextField txtUsername;
	private JPasswordField txtNewPassword, txtConfirmPassword;
	private JButton btnReset, btnBack;
	private JLabel lblMessage;

	public ForgotPasswordPage() {
		initUI();
	}

	private void initUI() {
		setTitle("Hệ Thống Quản Lí Sinh Viên - Quên Mật Khẩu");
		setSize(400, 380);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBackground(new Color(240, 240, 240));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

		JLabel lblTitle = new JLabel("ĐẶT LẠI MẬT KHẨU");
		lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitle.setForeground(new Color(230, 126, 34));
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

		JLabel lblNewPassword = new JLabel("Mật khẩu mới:");
		mainPanel.add(lblNewPassword);
		txtNewPassword = new JPasswordField();
		txtNewPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
		txtNewPassword.setFont(new Font("Arial", Font.PLAIN, 14));
		mainPanel.add(txtNewPassword);
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

		btnReset = new JButton("Đặt lại");
		btnReset.setPreferredSize(new Dimension(100, 35));
		btnReset.setFont(new Font("Arial", Font.BOLD, 12));
		btnReset.setBackground(new Color(230, 126, 34));
		btnReset.setForeground(Color.WHITE);
		btnReset.setBorder(BorderFactory.createEmptyBorder());
		btnReset.addActionListener(e -> handleReset());
		buttonPanel.add(btnReset);

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

	private void handleReset() {
		String username = txtUsername.getText();
		String newPassword = new String(txtNewPassword.getPassword());
		String confirmPassword = new String(txtConfirmPassword.getPassword());

		if (username.isEmpty()) {
			lblMessage.setText("Vui lòng nhập username!");
			lblMessage.setForeground(Color.RED);
			return;
		}
		if (!newPassword.equals(confirmPassword)) {
			lblMessage.setText("Mật khẩu không khớp!");
			lblMessage.setForeground(Color.RED);
			return;
		}
		if (newPassword.length() < 6) {
			lblMessage.setText("Mật khẩu tối thiểu 6 ký tự!");
			lblMessage.setForeground(Color.RED);
			return;
		}

		if (UserDAO.resetPassword(username, newPassword)) {
			lblMessage.setText("✓ Đặt lại mật khẩu thành công!");
			lblMessage.setForeground(Color.GREEN);
			JOptionPane.showMessageDialog(this, "Vui lòng đăng nhập lại.");
			backToLogin();
		} else {
			lblMessage.setText("✗ Username không tồn tại!");
			lblMessage.setForeground(Color.RED);
		}
	}

	private void backToLogin() {
		new login();
		dispose();
	}
}