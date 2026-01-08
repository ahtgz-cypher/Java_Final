package ui.admin;

import config.DBConnection;
import ui.login;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.MaskFormatter;

public class adminPage extends JFrame {
    
    private final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private final Color SIDEBAR_COLOR = new Color(44, 62, 80);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color HOVER_COLOR = new Color(52, 73, 94);
    
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    // Các bảng dữ liệu
    private JTable studentTable;
    private JTable teacherTable;
    private JTable pendingTable; // Bảng tài khoản chờ duyệt
    
    private DefaultTableModel studentTableModel;
    private DefaultTableModel teacherTableModel;
    private DefaultTableModel pendingTableModel;
    
    public adminPage() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Hệ Thống Quản Trị Viên");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Color.WHITE);
        
        JPanel sidebarPanel = createSidebarPanel();
        
        // Đăng ký các màn hình con
        mainPanel.add(createHomePanel(), "HOME");
        mainPanel.add(createAddStudentPanel(), "ADD_STUDENT");
        mainPanel.add(createAddTeacherPanel(), "ADD_TEACHER");
        mainPanel.add(createViewStudentPanel(), "VIEW_STUDENT");
        mainPanel.add(createViewTeacherPanel(), "VIEW_TEACHER");
        mainPanel.add(createPendingUsersPanel(), "PENDING_USERS"); // Màn hình mới
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sidebarPanel, BorderLayout.WEST);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }
    
    // ==================== SIDEBAR ====================
    
    private JPanel createSidebarPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(SIDEBAR_COLOR);
        panel.setPreferredSize(new Dimension(240, 0));
        
        JLabel lblBrand = new JLabel("ADMIN CONTROL");
        lblBrand.setForeground(TEXT_COLOR);
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblBrand.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblBrand.setBorder(new EmptyBorder(30, 0, 30, 0));
        panel.add(lblBrand);
        
        panel.add(createMenuButton("Trang Chủ", "HOME"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createMenuButton("Thêm Sinh Viên", "ADD_STUDENT"));
        panel.add(createMenuButton("Thêm Giáo Viên", "ADD_TEACHER"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createMenuButton("Danh Sách SV", "VIEW_STUDENT"));
        panel.add(createMenuButton("Danh Sách GV", "VIEW_TEACHER"));
        
        // --- NÚT MỚI: TÀI KHOẢN MỚI ---
        panel.add(Box.createVerticalStrut(10));
        panel.add(createMenuButton("Tài Khoản Mới", "PENDING_USERS"));
        // -----------------------------
        
        panel.add(Box.createVerticalGlue());
        
        JButton btnLogout = new JButton("Đăng Xuất");
        styleMenuButton(btnLogout);
        btnLogout.setBackground(new Color(192, 57, 43));
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new login().setVisible(true);
                dispose();
            }
        });
        panel.add(btnLogout);
        panel.add(Box.createVerticalStrut(20));
        
        return panel;
    }
    
    private JButton createMenuButton(String text, String cardName) {
        JButton btn = new JButton(text);
        styleMenuButton(btn);
        
        btn.addActionListener(e -> {
            // Logic refresh dữ liệu khi chuyển tab
            if (cardName.equals("VIEW_STUDENT")) refreshStudentTable();
            if (cardName.equals("VIEW_TEACHER")) refreshTeacherTable();
            if (cardName.equals("HOME")) refreshHomeStats();
            if (cardName.equals("PENDING_USERS")) refreshPendingTable(); // Refresh bảng chờ
            
            cardLayout.show(mainPanel, cardName);
        });
        
        return btn;
    }
    
    private void styleMenuButton(JButton btn) {
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setForeground(TEXT_COLOR);
        btn.setBackground(SIDEBAR_COLOR);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!btn.getText().contains("Đăng Xuất"))
                    btn.setBackground(HOVER_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                if (!btn.getText().contains("Đăng Xuất"))
                    btn.setBackground(SIDEBAR_COLOR);
            }
        });
    }

    // ==================== HOME ====================
    
    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Thống Kê Hệ Thống", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(SIDEBAR_COLOR);
        title.setBorder(new EmptyBorder(40, 0, 40, 0));
        
        JPanel statsContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        statsContainer.setBackground(Color.WHITE);
        
        statsContainer.add(createStatCard("Tổng Sinh Viên", getStudentCount(), new Color(46, 204, 113)));
        statsContainer.add(createStatCard("Tổng Giáo Viên", getTeacherCount(), new Color(155, 89, 182)));
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(statsContainer, BorderLayout.CENTER);
        
        return panel;
    }

    private void refreshHomeStats() {
        mainPanel.add(createHomePanel(), "HOME");
    }
    
    private JPanel createStatCard(String title, int value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(250, 160));
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblValue = new JLabel(String.valueOf(value));
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblValue.setForeground(Color.WHITE);
        lblValue.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(Box.createVerticalGlue());
        card.add(lblValue);
        card.add(Box.createVerticalStrut(10));
        card.add(lblTitle);
        card.add(Box.createVerticalGlue());
        
        return card;
    }

    // ==================== FORMS THÊM MỚI ====================

    private void addFormRow(JPanel panel, GridBagConstraints gbc, String labelText, JComponent component) {
        gbc.gridx = 0; 
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(label, gbc);
        
        gbc.gridx = 1;
        if (component instanceof JTextField) {
            component.setPreferredSize(new Dimension(250, 35));
            component.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }
        panel.add(component, gbc);
        gbc.gridy++;
    }

    private JPanel createAddStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("THÊM SINH VIÊN MỚI", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(PRIMARY_COLOR);
        title.setBorder(new EmptyBorder(30, 0, 30, 0));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;

        JTextField txtStudentCode = new JTextField();
        JTextField txtFullName = new JTextField();
        JFormattedTextField txtDob = createDateTextField();
        JTextField txtUsername = new JTextField();
        JPasswordField txtPassword = new JPasswordField("123456");

        addFormRow(formPanel, gbc, "Mã Sinh Viên (SV001):", txtStudentCode);
        addFormRow(formPanel, gbc, "Họ và Tên:", txtFullName);
        addFormRow(formPanel, gbc, "Ngày Sinh (YYYY-MM-DD):", txtDob);
        addFormRow(formPanel, gbc, "Username:", txtUsername);
        addFormRow(formPanel, gbc, "Mật khẩu:", txtPassword);

        JButton btnAdd = new JButton("Lưu Sinh Viên");
        styleActionButton(btnAdd, PRIMARY_COLOR);
        
        btnAdd.addActionListener(e -> {
            String dob = txtDob.getText().replace("_", "").trim();
            if (addStudentToDatabase(txtStudentCode.getText(), txtFullName.getText(), dob, 
                                   txtUsername.getText(), new String(txtPassword.getPassword()))) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                txtStudentCode.setText("");
                txtFullName.setText("");
                txtUsername.setText("");
                txtDob.setValue(null);
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnAdd);

        panel.add(title, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createAddTeacherPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("THÊM GIÁO VIÊN MỚI", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(PRIMARY_COLOR);
        title.setBorder(new EmptyBorder(20, 0, 20, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;

        JTextField txtFullName = new JTextField();
        JTextField txtUsername = new JTextField();
        JPasswordField txtPassword = new JPasswordField("123456");

        addFormRow(formPanel, gbc, "Họ và Tên:", txtFullName);
        addFormRow(formPanel, gbc, "Username:", txtUsername);
        addFormRow(formPanel, gbc, "Mật khẩu:", txtPassword);

        gbc.gridx = 0; gbc.gridwidth = 2;
        JLabel lblSub = new JLabel("Danh sách môn học phụ trách:");
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblSub, gbc);
        gbc.gridy++;

        DefaultTableModel subjectModel = new DefaultTableModel(new String[]{"Tên Môn", "Tín Chỉ"}, 0);
        JTable tblSubject = new JTable(subjectModel);
        styleTable(tblSubject);
        JScrollPane scrollSub = new JScrollPane(tblSubject);
        scrollSub.setPreferredSize(new Dimension(400, 100));
        formPanel.add(scrollSub, gbc);
        gbc.gridy++;

        JPanel inputSubPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputSubPanel.setBackground(Color.WHITE);
        JTextField txtSubName = new JTextField(12);
        JSpinner spnCredit = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
        JButton btnAddSub = new JButton("Thêm Môn");
        JButton btnDelSub = new JButton("Xóa Dòng");
        
        inputSubPanel.add(new JLabel("Tên:")); inputSubPanel.add(txtSubName);
        inputSubPanel.add(new JLabel("TC:")); inputSubPanel.add(spnCredit);
        inputSubPanel.add(btnAddSub); inputSubPanel.add(btnDelSub);
        
        formPanel.add(inputSubPanel, gbc);

        btnAddSub.addActionListener(e -> {
            if(!txtSubName.getText().isEmpty()) {
                subjectModel.addRow(new Object[]{txtSubName.getText(), spnCredit.getValue()});
                txtSubName.setText("");
            }
        });
        btnDelSub.addActionListener(e -> {
            if(tblSubject.getSelectedRow() != -1) subjectModel.removeRow(tblSubject.getSelectedRow());
        });

        JButton btnSave = new JButton("Lưu Giáo Viên & Môn Học");
        styleActionButton(btnSave, PRIMARY_COLOR);
        
        btnSave.addActionListener(e -> {
            List<SubjectInfo> subjects = new ArrayList<>();
            for(int i=0; i<subjectModel.getRowCount(); i++) {
                subjects.add(new SubjectInfo(
                    subjectModel.getValueAt(i, 0).toString(),
                    Integer.parseInt(subjectModel.getValueAt(i, 1).toString())
                ));
            }
            
            if(subjects.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cần ít nhất 1 môn học!");
                return;
            }

            if(addTeacherToDatabase(txtFullName.getText(), txtUsername.getText(), 
                                    new String(txtPassword.getPassword()), subjects)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                txtFullName.setText(""); txtUsername.setText("");
                subjectModel.setRowCount(0);
            }
        });

        JPanel footer = new JPanel();
        footer.setBackground(Color.WHITE);
        footer.add(btnSave);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(footer, BorderLayout.SOUTH);
        
        return panel;
    }

    // ==================== DANH SÁCH (VIEWS) ====================
    
    private JPanel createViewStudentPanel() {
        return createTablePanel("DANH SÁCH SINH VIÊN", 
            new String[]{"Mã SV", "Họ Tên", "Ngày Sinh", "Username"}, 
            true);
    }
    
    private JPanel createViewTeacherPanel() {
        return createTablePanel("DANH SÁCH GIÁO VIÊN", 
            new String[]{"Họ Tên", "Username", "Môn Dạy"}, 
            false);
    }

    private JPanel createTablePanel(String titleStr, String[] columns, boolean isStudent) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel(titleStr, JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(SIDEBAR_COLOR);
        panel.add(title, BorderLayout.NORTH);
        
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        JTable table = new JTable(model);
        styleTable(table);
        
        if (isStudent) {
            studentTable = table;
            studentTableModel = model;
        } else {
            teacherTable = table;
            teacherTableModel = model;
        }
        
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);
        
        JButton btnDelete = new JButton("Xóa Dữ Liệu Chọn");
        styleActionButton(btnDelete, new Color(231, 76, 60));
        
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 dòng để xóa!");
                return;
            }
            
            if (isStudent) {
                String code = table.getValueAt(row, 0).toString();
                String name = table.getValueAt(row, 1).toString();
                if (JOptionPane.showConfirmDialog(this, "Xóa sinh viên " + name + " (và tất cả điểm số)?", "Cảnh báo", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (deleteStudentFromDatabase(code)) refreshStudentTable();
                }
            } else {
                String name = table.getValueAt(row, 0).toString();
                String user = table.getValueAt(row, 1).toString();
                if (JOptionPane.showConfirmDialog(this, "Xóa giáo viên " + name + "?", "Cảnh báo", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (deleteTeacherFromDatabase(user)) refreshTeacherTable();
                }
            }
        });
        
        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);
        bottom.add(btnDelete);
        panel.add(bottom, BorderLayout.SOUTH);
        
        return panel;
    }

    // ==================== PHẦN MỚI: XỬ LÝ TÀI KHOẢN MỚI ĐĂNG KÝ ====================

    private JPanel createPendingUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("DANH SÁCH TÀI KHOẢN MỚI ĐĂNG KÝ", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(230, 126, 34)); // Màu cam
        panel.add(title, BorderLayout.NORTH);

        // Cột: User ID, Username, Trạng thái
        pendingTableModel = new DefaultTableModel(new String[]{"User ID", "Username", "Trạng Thái"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };

        pendingTable = new JTable(pendingTableModel);
        styleTable(pendingTable); 

        JScrollPane scroll = new JScrollPane(pendingTable);
        panel.add(scroll, BorderLayout.CENTER);

        JButton btnUpdateInfo = new JButton("Cập Nhật Thông Tin Hồ Sơ");
        styleActionButton(btnUpdateInfo, new Color(39, 174, 96)); // Màu xanh lá

        btnUpdateInfo.addActionListener(e -> showUpdateStudentDialog());

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);
        bottom.add(btnUpdateInfo);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshPendingTable() {
        if (pendingTableModel == null) return;
        pendingTableModel.setRowCount(0);
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            // Lấy User là role 3 (HS) nhưng chưa có ID trong bảng Students
            String sql = "SELECT u.user_id, u.username " +
                         "FROM users u " +
                         "LEFT JOIN students s ON u.user_id = s.user_id " +
                         "WHERE u.role_id = 3 AND s.student_id IS NULL";
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                pendingTableModel.addRow(new Object[]{
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    "Chưa có hồ sơ"
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
             try { if (conn != null) conn.close(); } catch (Exception ex) {}
        }
    }

    private void showUpdateStudentDialog() {
        int row = pendingTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một tài khoản để cập nhật!");
            return;
        }

        int userId = Integer.parseInt(pendingTable.getValueAt(row, 0).toString());
        String username = pendingTable.getValueAt(row, 1).toString();

        JDialog dialog = new JDialog(this, "Cập nhật hồ sơ cho: " + username, true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        JTextField txtCode = new JTextField(15);
        JTextField txtName = new JTextField(15);
        JFormattedTextField txtDob = createDateTextField(); 

        dialog.add(new JLabel("Mã Sinh Viên:"), gbc); gbc.gridx = 1; dialog.add(txtCode, gbc);
        gbc.gridx = 0; gbc.gridy++;
        dialog.add(new JLabel("Họ và Tên:"), gbc); gbc.gridx = 1; dialog.add(txtName, gbc);
        gbc.gridx = 0; gbc.gridy++;
        dialog.add(new JLabel("Ngày Sinh (YYYY-MM-DD):"), gbc); gbc.gridx = 1; dialog.add(txtDob, gbc);

        JButton btnSave = new JButton("Lưu Hồ Sơ");
        btnSave.setBackground(new Color(39, 174, 96));
        btnSave.setForeground(Color.WHITE);
        
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        dialog.add(btnSave, gbc);

        btnSave.addActionListener(evt -> {
            String dob = txtDob.getText().replace("_", "").trim();
            if (txtCode.getText().isEmpty() || txtName.getText().isEmpty() || dob.length() < 10) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            if (linkUserToStudentProfile(userId, txtCode.getText(), txtName.getText(), dob)) {
                JOptionPane.showMessageDialog(dialog, "Cập nhật thành công! Tài khoản đã có thể sử dụng.");
                dialog.dispose();
                refreshPendingTable(); 
            } else {
                JOptionPane.showMessageDialog(dialog, "Lỗi! Có thể Mã SV đã tồn tại.");
            }
        });

        dialog.setVisible(true);
    }

    private boolean linkUserToStudentProfile(int userId, String code, String name, String dob) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO students (user_id, student_code, full_name, dob) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, code);
            ps.setString(3, name);
            ps.setString(4, dob);
            
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
             try { if (conn != null) conn.close(); } catch (Exception ex) {}
        }
    }

    // ==================== HELPER STYLES ====================

    private void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(232, 246, 254));
        table.setSelectionForeground(Color.BLACK);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(0, 35));
        
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                }
                return c;
            }
        });
    }

    private void styleActionButton(JButton btn, Color bgColor) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setPreferredSize(new Dimension(200, 40));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JFormattedTextField createDateTextField() {
        try {
            MaskFormatter dateMask = new MaskFormatter("####-##-##");
            dateMask.setPlaceholderCharacter('_');
            return new JFormattedTextField(dateMask);
        } catch (ParseException e) {
            return new JFormattedTextField();
        }
    }

    // ==================== DATABASE ====================

    private boolean deleteStudentFromDatabase(String studentCode) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            int studentId = -1;
            int userId = -1;
            try (PreparedStatement stmt = conn.prepareStatement("SELECT student_id, user_id FROM students WHERE student_code = ?")) {
                stmt.setString(1, studentCode);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    studentId = rs.getInt("student_id");
                    userId = rs.getInt("user_id");
                } else return false;
            }

            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM scores WHERE student_id = ?")) {
                stmt.setInt(1, studentId);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM students WHERE student_id = ?")) {
                stmt.setInt(1, studentId);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE user_id = ?")) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (Exception ex) {}
            JOptionPane.showMessageDialog(this, "Lỗi xóa: " + e.getMessage());
            return false;
        }
    }

    private boolean deleteTeacherFromDatabase(String username) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            int teacherId = -1;
            int userId = -1;
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT t.teacher_id, u.user_id FROM users u JOIN teachers t ON u.user_id = t.user_id WHERE u.username = ?")) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    teacherId = rs.getInt("teacher_id");
                    userId = rs.getInt("user_id");
                } else return false;
            }

            String deleteScoresSql = "DELETE sc FROM scores sc JOIN subjects s ON sc.subject_id = s.subject_id WHERE s.teacher_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteScoresSql)) {
                stmt.setInt(1, teacherId);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM subjects WHERE teacher_id = ?")) {
                stmt.setInt(1, teacherId);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM teachers WHERE teacher_id = ?")) {
                stmt.setInt(1, teacherId);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE user_id = ?")) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (Exception ex) {}
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi xóa GV: " + e.getMessage());
            return false;
        }
    }

    private int getStudentCount() {
        return getCount("SELECT COUNT(*) FROM students");
    }
    
    private int getTeacherCount() {
        return getCount("SELECT COUNT(*) FROM teachers");
    }
    
    private int getCount(String sql) {
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {}
        return 0;
    }

    private boolean addStudentToDatabase(String code, String name, String dob, String user, String pass) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            int userId = -1;
            try(PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password, role_id) VALUES (?,?,3)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, user); ps.setString(2, pass);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()) userId = rs.getInt(1);
            }
            
            try(PreparedStatement ps = conn.prepareStatement("INSERT INTO students (user_id, full_name, student_code, dob) VALUES (?,?,?,?)")) {
                ps.setInt(1, userId); ps.setString(2, name); ps.setString(3, code); ps.setString(4, dob);
                ps.executeUpdate();
            }
            
            conn.commit();
            return true;
        } catch (Exception e) {
            try { if(conn!=null) conn.rollback(); } catch(Exception ex){}
            e.printStackTrace();
            return false;
        }
    }

    private boolean addTeacherToDatabase(String name, String user, String pass, List<SubjectInfo> subjects) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            int userId = -1;
            try(PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password, role_id) VALUES (?,?,2)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, user); ps.setString(2, pass);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()) userId = rs.getInt(1);
            }
            
            int tId = -1;
            try(PreparedStatement ps = conn.prepareStatement("INSERT INTO teachers (user_id, full_name) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, userId); ps.setString(2, name);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()) tId = rs.getInt(1);
            }
            
            String sqlSub = "INSERT INTO subjects (subject_name, teacher_id, credit) VALUES (?,?,?)";
            try(PreparedStatement ps = conn.prepareStatement(sqlSub)) {
                for(SubjectInfo s : subjects) {
                    ps.setString(1, s.name); ps.setInt(2, tId); ps.setInt(3, s.credit);
                    ps.executeUpdate();
                }
            }
            
            conn.commit();
            return true;
        } catch (Exception e) {
            try { if(conn!=null) conn.rollback(); } catch(Exception ex){}
            e.printStackTrace();
            return false;
        }
    }

    private void refreshStudentTable() {
        studentTableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT s.student_code, s.full_name, s.dob, u.username FROM students s JOIN users u ON s.user_id = u.user_id");
            while (rs.next()) studentTableModel.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)});
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void refreshTeacherTable() {
        teacherTableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {
            String sql = "SELECT t.full_name, u.username, GROUP_CONCAT(s.subject_name SEPARATOR ', ') FROM teachers t JOIN users u ON t.user_id = u.user_id LEFT JOIN subjects s ON t.teacher_id = s.teacher_id GROUP BY t.teacher_id";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) teacherTableModel.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3)});
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private static class SubjectInfo {
        String name; int credit;
        SubjectInfo(String n, int c) { name=n; credit=c; }
    }
}