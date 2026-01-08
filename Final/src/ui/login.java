package ui;

import model.User;
import dao.UserDAO;
import dao.TeacherDAO;
import util.ValidationUtil;
import ui.admin.adminPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        setTitle("Đăng Nhập - Hệ Thống Quản Lý Điểm Sinh Viên");
        setSize(500, 600); // Tăng chiều cao một chút cho thoáng
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Sử dụng Panel Gradient làm nền chính
        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());

        // --- PHẦN HEADER (TIÊU ĐỀ) ---
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 30, 0));

        JLabel lblTitle = new JLabel("ĐĂNG NHẬP");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(lblTitle);

        JLabel lblSubtitle = new JLabel("Hệ thống quản lý điểm sinh viên");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(255, 255, 255, 200));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(lblSubtitle);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // --- PHẦN FORM NHẬP LIỆU ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsername.setForeground(Color.WHITE);
        formPanel.add(lblUsername, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtUsername = createStyledTextField();
        formPanel.add(txtUsername, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPassword.setForeground(Color.WHITE);
        formPanel.add(lblPassword, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtPassword = createStyledPasswordField();
        // Thêm tính năng nhấn Enter để đăng nhập
        txtPassword.addActionListener(e -> handleLogin()); 
        formPanel.add(txtPassword, gbc);

        // Thông báo lỗi/thành công
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        lblMessage = new JLabel(" "); // Để khoảng trắng để giữ chỗ
        lblMessage.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMessage.setHorizontalAlignment(JLabel.CENTER);
        formPanel.add(lblMessage, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // --- PHẦN BUTTON (NÚT BẤM) ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 30, 50));

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

        // Quên mật khẩu
        btnForgotPassword = createLinkButton("Quên mật khẩu?");
        btnForgotPassword.addActionListener(e -> openForgotPasswordPage());
        btnForgotPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(btnForgotPassword);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    // --- XỬ LÝ LOGIC ---

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        // === PHẦN DEBUG QUAN TRỌNG: Kiểm tra dữ liệu tại Console ===
        System.out.println("----------------------------------------");
        System.out.println("DEBUG: Đang xử lý đăng nhập...");
        System.out.println("Input Username: [" + username + "]");
        System.out.println("Input Password: [" + password + "]");
        
        // Validate cơ bản
        String error = ValidationUtil.validateLoginInput(username, password);
        if (error != null) {
            lblMessage.setText(error);
            lblMessage.setForeground(new Color(255, 200, 200)); // Màu hồng nhạt cho dễ nhìn trên nền tối
            return;
        }

        try {
            // Gọi xuống Database
            Optional<User> loginResult = UserDAO.login(username, password);
            
            // Debug kết quả từ DAO
            if (loginResult.isPresent()) {
                User user = loginResult.get();
                System.out.println("DEBUG: Đăng nhập THÀNH CÔNG!");
                System.out.println("User ID: " + user.getUserId());
                System.out.println("Role ID: " + user.getRoleId());
                
                lblMessage.setText("✓ Đăng nhập thành công!");
                lblMessage.setForeground(new Color(144, 238, 144)); // Màu xanh lá nhạt

                // Chuyển trang sau 1 giây
                Timer timer = new Timer(800, e -> {
                    openDashboardByRole(user);
                    dispose();
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                System.out.println("DEBUG: Đăng nhập THẤT BẠI - Không tìm thấy user hoặc sai pass");
                lblMessage.setText("✗ Username hoặc mật khẩu sai!");
                lblMessage.setForeground(new Color(255, 100, 100)); // Màu đỏ
                txtPassword.selectAll();
                txtPassword.requestFocus();
            }
        } catch (Exception e) {
            System.out.println("DEBUG: LỖI KẾT NỐI DATABASE HOẶC CODE!");
            e.printStackTrace();
            lblMessage.setText("Lỗi hệ thống! Vui lòng kiểm tra log.");
            lblMessage.setForeground(Color.YELLOW);
        }
    }

    private void openDashboardByRole(User user) {
        try {
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
                    JOptionPane.showMessageDialog(this, "Quyền truy cập không hợp lệ (Role ID: " + user.getRoleId() + ")");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể mở Dashboard: " + e.getMessage());
            e.printStackTrace();
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

    // --- CÁC HÀM UI CUSTOM (GIỮ NGUYÊN STYLE CỦA BẠN) ---

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(250, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setBackground(new Color(255, 255, 255, 240));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setPreferredSize(new Dimension(250, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setBackground(new Color(255, 255, 255, 240));
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor, Color hoverColor) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(140, 40));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hoverColor); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bgColor); }
        });
        return btn;
    }

    private JButton createLinkButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        btn.setForeground(new Color(255, 255, 255, 200));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(Color.WHITE); }
            public void mouseExited(MouseEvent e) { btn.setForeground(new Color(255, 255, 255, 200)); }
        });
        return btn;
    }

    // Class vẽ nền Gradient
    private static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int w = getWidth(), h = getHeight();
            Color color1 = new Color(52, 152, 219);
            Color color2 = new Color(142, 68, 173);
            GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(login::new);
    }
}