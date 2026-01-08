package ui;

import dao.UserDAO;
import util.ValidationUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterPage extends JFrame {
	private JTextField txtUsername;
	private JPasswordField txtPassword, txtConfirmPassword;
	private JButton btnRegister, btnBack;
	private JLabel lblMessage;

	public RegisterPage() {
		initUI();
	}

	private void initUI() {
		setTitle("Đăng Ký - Hệ Thống Quản Lý Điểm Sinh Viên");
		setSize(500, 550);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		// Main panel with gradient background
		GradientPanel mainPanel = new GradientPanel();
		mainPanel.setLayout(new BorderLayout());
		
		// Header panel
		JPanel headerPanel = new JPanel();
		headerPanel.setOpaque(false);
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
		headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
		
		JLabel lblTitle = new JLabel("ĐĂNG KÝ TÀI KHOẢN");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setAlignmentX(CENTER_ALIGNMENT);
		headerPanel.add(lblTitle);
		
		JLabel lblSubtitle = new JLabel("Chỉ dành cho sinh viên");
		lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblSubtitle.setForeground(new Color(255, 255, 255, 200));
		lblSubtitle.setAlignmentX(CENTER_ALIGNMENT);
		headerPanel.add(lblSubtitle);
		
		mainPanel.add(headerPanel, BorderLayout.NORTH);

		// Form panel with GridBagLayout
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

		// Password row
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.25;
		JLabel lblPassword = new JLabel("Mật khẩu:");
		lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lblPassword.setForeground(Color.WHITE);
		formPanel.add(lblPassword, gbc);
		
		gbc.gridx = 1;
		gbc.weightx = 0.75;
		txtPassword = createStyledPasswordField();
		formPanel.add(txtPassword, gbc);

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

		btnRegister = createStyledButton("Đăng Ký", new Color(46, 204, 113), new Color(39, 174, 96));
		btnRegister.addActionListener(e -> handleRegister());
		bottomPanel.add(btnRegister);

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
		btn.setPreferredSize(new Dimension(140, 42));
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

	private void handleRegister() {
		String username = txtUsername.getText().trim();
		String password = new String(txtPassword.getPassword());
		String confirmPassword = new String(txtConfirmPassword.getPassword());
		
		int roleId = 3; // STUDENT role

		// Kiểm tra username không rỗng
		if (username.isEmpty()) {
			showError("✗ Vui lòng nhập username!");
			return;
		}

		// Validate input
		String error = ValidationUtil.validateRegisterInput(username, password, confirmPassword);
		if (error != null) {
			showError(error);
			return;
		}

		System.out.println("=== BẮT ĐẦU QUÁ TRÌNH ĐĂNG KÝ ===");
		System.out.println("Username: " + username);
		System.out.println("Password length: " + password.length());
		System.out.println("Role ID: " + roleId);

		// Thử đăng ký (UserDAO sẽ kiểm tra username có tồn tại không)
		boolean success = UserDAO.register(username, password, roleId);
		
		if (success) {
			System.out.println("=== ĐĂNG KÝ THÀNH CÔNG ===");
			lblMessage.setText("✓ Đăng ký thành công!");
			lblMessage.setForeground(new Color(46, 204, 113));
			
			JOptionPane.showMessageDialog(this, 
				"Tài khoản sinh viên đã được tạo thành công!\n\n" +
				"Username: " + username + "\n\n" +
				"Lưu ý: Bạn cần liên hệ Admin để được thêm thông tin chi tiết vào hệ thống.",
				"Đăng ký thành công", 
				JOptionPane.INFORMATION_MESSAGE);
			
			clearForm();
			backToLogin();
		} else {
			System.out.println("=== ĐĂNG KÝ THẤT BẠI ===");
			// Kiểm tra xem username đã tồn tại không
			if (UserDAO.usernameExists(username)) {
				showError("✗ Username '" + username + "' đã tồn tại!");
			} else {
				showError("✗ Đăng ký thất bại! Vui lòng thử lại.");
			}
		}
	}

	private void showError(String message) {
		lblMessage.setText(message);
		lblMessage.setForeground(new Color(231, 76, 60));
	}

	private void clearForm() {
		txtUsername.setText("");
		txtPassword.setText("");
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
			
			Color color1 = new Color(46, 204, 113);
			Color color2 = new Color(39, 174, 96);
			GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
			g2d.setPaint(gradient);
			g2d.fillRect(0, 0, getWidth(), getHeight());
			g2d.dispose();
		}
	}
}