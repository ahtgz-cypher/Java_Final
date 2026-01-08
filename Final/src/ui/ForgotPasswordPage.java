package ui;

import dao.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ForgotPasswordPage extends JFrame {
	private JTextField txtUsername;
	private JPasswordField txtNewPassword, txtConfirmPassword;
	private JButton btnReset, btnBack;
	private JLabel lblMessage;

	public ForgotPasswordPage() {
		initUI();
	}

	private void initUI() {
		setTitle("Quên Mật Khẩu - Hệ Thống Quản Lý Điểm Sinh Viên");
		setSize(500, 580);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		GradientPanel mainPanel = new GradientPanel();
		mainPanel.setLayout(new BorderLayout());
		
		// Header panel
		JPanel headerPanel = new JPanel();
		headerPanel.setOpaque(false);
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
		headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
		
		JLabel lblTitle = new JLabel("ĐẶT LẠI MẬT KHẨU");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setAlignmentX(CENTER_ALIGNMENT);
		headerPanel.add(lblTitle);
		
		JLabel lblSubtitle = new JLabel("Nhập username và mật khẩu mới");
		lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblSubtitle.setForeground(new Color(255, 255, 255, 200));
		lblSubtitle.setAlignmentX(CENTER_ALIGNMENT);
		headerPanel.add(lblSubtitle);
		
		mainPanel.add(headerPanel, BorderLayout.NORTH);

		// Form panel
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setOpaque(false);
		formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(12, 0, 12, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Username row
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.25;
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lblUsername.setForeground(Color.WHITE);
		formPanel.add(lblUsername, gbc);
		
		gbc.gridx = 1;
		gbc.weightx = 0.75;
		txtUsername = createStyledTextField();
		formPanel.add(txtUsername, gbc);

		// New Password row
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.25;
		JLabel lblNewPassword = new JLabel("Mật khẩu mới:");
		lblNewPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lblNewPassword.setForeground(Color.WHITE);
		formPanel.add(lblNewPassword, gbc);
		
		gbc.gridx = 1;
		gbc.weightx = 0.75;
		txtNewPassword = createStyledPasswordField();
		formPanel.add(txtNewPassword, gbc);

		// Confirm Password row
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.25;
		JLabel lblConfirmPassword = new JLabel("Xác nhận:");
		lblConfirmPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lblConfirmPassword.setForeground(Color.WHITE);
		formPanel.add(lblConfirmPassword, gbc);
		
		gbc.gridx = 1;
		gbc.weightx = 0.75;
		txtConfirmPassword = createStyledPasswordField();
		formPanel.add(txtConfirmPassword, gbc);

		// Message label
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(20, 0, 10, 0);
		lblMessage = new JLabel("");
		lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblMessage.setForeground(Color.RED);
		lblMessage.setHorizontalAlignment(JLabel.CENTER);
		formPanel.add(lblMessage, gbc);

		mainPanel.add(formPanel, BorderLayout.CENTER);

		// Bottom panel with buttons
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		bottomPanel.setOpaque(false);
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));

		btnReset = createStyledButton("Đặt lại", new Color(230, 126, 34), new Color(211, 84, 0));
		btnReset.addActionListener(e -> handleReset());
		bottomPanel.add(btnReset);

		btnBack = createStyledButton("Quay lại", new Color(149, 165, 166), new Color(127, 140, 141));
		btnBack.addActionListener(e -> backToLogin());
		bottomPanel.add(btnBack);

		mainPanel.add(bottomPanel, BorderLayout.SOUTH);
		
		add(mainPanel);
		setVisible(true);
	}

	private JTextField createStyledTextField() {
		JTextField field = new JTextField();
		field.setPreferredSize(new Dimension(250, 38));
		field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		field.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 2),
			BorderFactory.createEmptyBorder(8, 12, 8, 12)
		));
		field.setBackground(new Color(255, 255, 255, 245));
		field.setOpaque(true);
		field.setCaretColor(Color.BLACK);
		return field;
	}

	private JPasswordField createStyledPasswordField() {
		JPasswordField field = new JPasswordField();
		field.setPreferredSize(new Dimension(250, 38));
		field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		field.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 2),
			BorderFactory.createEmptyBorder(8, 12, 8, 12)
		));
		field.setBackground(new Color(255, 255, 255, 245));
		field.setOpaque(true);
		field.setCaretColor(Color.BLACK);
		return field;
	}

	private JButton createStyledButton(String text, Color bgColor, Color hoverColor) {
		JButton btn = new JButton(text);
		btn.setPreferredSize(new Dimension(130, 42));
		btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btn.setForeground(Color.WHITE);
		btn.setBackground(bgColor);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setOpaque(true);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btn.setBackground(hoverColor);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btn.setBackground(bgColor);
			}
		});

		return btn;
	}

	private void handleReset() {
		String username = txtUsername.getText().trim();
		String newPassword = new String(txtNewPassword.getPassword());
		String confirmPassword = new String(txtConfirmPassword.getPassword());

		if (username.isEmpty()) {
			showError("✗ Vui lòng nhập username!");
			return;
		}

		if (!UserDAO.usernameExists(username)) {
			showError("✗ Username không tồn tại!");
			return;
		}

		if (!newPassword.equals(confirmPassword)) {
			showError("✗ Mật khẩu không khớp!");
			return;
		}

		if (newPassword.length() < 6) {
			showError("✗ Mật khẩu tối thiểu 6 ký tự!");
			return;
		}

		if (UserDAO.resetPassword(username, newPassword)) {
			lblMessage.setText("✓ Đặt lại mật khẩu thành công!");
			lblMessage.setForeground(new Color(46, 204, 113));
			
			JOptionPane.showMessageDialog(this, 
				"Mật khẩu đã được đặt lại thành công!\n\nVui lòng đăng nhập lại.",
				"Thành công", 
				JOptionPane.INFORMATION_MESSAGE);
			
			clearForm();
			backToLogin();
		} else {
			showError("✗ Đặt lại mật khẩu thất bại! Vui lòng thử lại.");
		}
	}

	private void showError(String message) {
		lblMessage.setText(message);
		lblMessage.setForeground(new Color(231, 76, 60));
	}

	private void clearForm() {
		txtUsername.setText("");
		txtNewPassword.setText("");
		txtConfirmPassword.setText("");
		lblMessage.setText("");
	}

	private void backToLogin() {
		new login();
		dispose();
	}

	private static class GradientPanel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			
			Color color1 = new Color(230, 126, 34);
			Color color2 = new Color(211, 84, 0);
			GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
			g2d.setPaint(gradient);
			g2d.fillRect(0, 0, getWidth(), getHeight());
			g2d.dispose();
		}
	}
}