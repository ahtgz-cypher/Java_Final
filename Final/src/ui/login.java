package ui;

import model.User;
import dao.UserDAO;
import dao.TeacherDAO;
import util.ValidationUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;
import ui.admin.adminPage;

public class login extends JFrame {
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JButton btnLogin, btnRegister, btnForgotPassword;
	private JLabel lblMessage;

	public login() {
		initUI();
	}

	private void initUI() {
		setTitle("Đăng Nhập - Hệ Thống Quản Lý Điểm Sinh Viên");
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
		headerPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 30, 0));
		
		JLabel lblTitle = new JLabel("ĐĂNG NHẬP");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setAlignmentX(CENTER_ALIGNMENT);
		headerPanel.add(lblTitle);
		
		JLabel lblSubtitle = new JLabel("Hệ thống quản lý điểm sinh viên");
		lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblSubtitle.setForeground(new Color(255, 255, 255, 200));
		lblSubtitle.setAlignmentX(CENTER_ALIGNMENT);
		headerPanel.add(lblSubtitle);
		
		mainPanel.add(headerPanel, BorderLayout.NORTH);

		// Form panel
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setOpaque(false);
		formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 0, 15, 0);
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

		// Message label
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(20, 0, 15, 0);
		lblMessage = new JLabel("");
		lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblMessage.setForeground(Color.RED);
		lblMessage.setHorizontalAlignment(JLabel.CENTER);
		formPanel.add(lblMessage, gbc);

		mainPanel.add(formPanel, BorderLayout.CENTER);

		// Bottom panel
		JPanel bottomPanel = new JPanel();
		bottomPanel.setOpaque(false);
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 20, 50));

		// Buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		buttonPanel.setOpaque(false);

		btnLogin = createStyledButton("Đăng Nhập", new Color(52, 152, 219), new Color(41, 128, 185));
		btnLogin.addActionListener(e -> handleLogin());
		buttonPanel.add(btnLogin);

		btnRegister = createStyledButton("Đăng Ký", new Color(46, 204, 113), new Color(39, 174, 96));
		btnRegister.addActionListener(e -> openRegisterPage());
		buttonPanel.add(btnRegister);

		bottomPanel.add(buttonPanel);
		bottomPanel.add(Box.createVerticalStrut(15));

		// Forgot password button
		btnForgotPassword = createLinkButton("Quên mật khẩu?");
		btnForgotPassword.addActionListener(e -> openForgotPasswordPage());
		JPanel forgotPanel = new JPanel();
		forgotPanel.setOpaque(false);
		forgotPanel.add(btnForgotPassword);
		bottomPanel.add(forgotPanel);

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

	private JButton createLinkButton(String text) {
		JButton btn = new JButton(text);
		btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btn.setForeground(new Color(255, 255, 255, 220));
		btn.setBorderPainted(false);
		btn.setContentAreaFilled(false);
		btn.setFocusPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btn.setForeground(Color.WHITE);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btn.setForeground(new Color(255, 255, 255, 220));
			}
		});

		return btn;
	}

	private void handleLogin() {
		String username = txtUsername.getText().trim();
		String password = new String(txtPassword.getPassword());

		String error = ValidationUtil.validateLoginInput(username, password);
		if (error != null) {
			lblMessage.setText(error);
			lblMessage.setForeground(new Color(231, 76, 60));
			return;
		}

		Optional<User> loginResult = UserDAO.login(username, password);
		if (loginResult.isPresent()) {
			User user = loginResult.get();
			lblMessage.setText("✓ Đăng nhập thành công!");
			lblMessage.setForeground(new Color(46, 204, 113));

			SwingUtilities.invokeLater(() -> {
				try {
					Thread.sleep(800);
					openDashboardByRole(user);
					dispose();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		} else {
			lblMessage.setText("✗ Username hoặc mật khẩu sai!");
			lblMessage.setForeground(new Color(231, 76, 60));
			txtPassword.setText("");
		}
	}

	private void openDashboardByRole(User user) {
		switch (user.getRoleId()) {
		case 1: // ADMIN
			adminPage admin = new adminPage();
			admin.setVisible(true);
			break;
		case 2: // TEACHER
			int teacherId = TeacherDAO.getTeacherIdByUserId(user.getUserId());
			String teacherName = TeacherDAO.getTeacherNameByUserId(user.getUserId());
			TeacherPage teacherPage = new TeacherPage(teacherName, teacherId);
			teacherPage.setVisible(true);
			break;
		case 3: // STUDENT
			StudentPage studentPage = new StudentPage(user.getUserId());
			studentPage.setVisible(true);
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
		SwingUtilities.invokeLater(login::new);
	}

	private static class GradientPanel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			
			Color color1 = new Color(52, 152, 219);
			Color color2 = new Color(142, 68, 173);
			GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
			g2d.setPaint(gradient);
			g2d.fillRect(0, 0, getWidth(), getHeight());
			g2d.dispose();
		}
	}
}