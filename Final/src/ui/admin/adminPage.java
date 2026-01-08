package ui.admin;

import config.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;
import javax.swing.text.MaskFormatter;

/**
 *
 * 
 * Chức năng:
 * - Thêm sinh viên mới vào database
 * - Thêm giáo viên mới vào database
 * - Xem danh sách sinh viên từ database
 * - Xem danh sách giáo viên từ database
 * 
 */
public class adminPage extends JFrame {
    
    private JPanel mainPanel;              // Panel chính chứa các card
    private CardLayout cardLayout;         // Layout để chuyển đổi giữa các panel
    
    // Bảng hiển thị dữ liệu
    private JTable studentTable;            // Bảng hiển thị danh sách sinh viên
    private JTable teacherTable;            // Bảng hiển thị danh sách giáo viên
    private DefaultTableModel studentTableModel;  // Model cho bảng sinh viên
    private DefaultTableModel teacherTableModel;  // Model cho bảng giáo viên
    
    /**
     * Constructor - Khởi tạo cửa sổ Admin
     */
    public adminPage() {
        initializeUI();
    }
    
    /**
     * Khởi tạo giao diện người dùng
     */
    private void initializeUI() {
        setTitle("Trang Quản Trị - Admin");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Đóng cửa sổ này, không thoát ứng dụng
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Layout chính sử dụng CardLayout để chuyển đổi giữa các panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Tạo menu điều hướng bên trái
        JPanel menuPanel = createMenuPanel();
        
        // Tạo các panel chức năng
        JPanel homePanel = createHomePanel();
        JPanel addStudentPanel = createAddStudentPanel();
        JPanel addTeacherPanel = createAddTeacherPanel();
        JPanel viewStudentPanel = createViewStudentPanel();
        JPanel viewTeacherPanel = createViewTeacherPanel();
        
        // Thêm các panel vào CardLayout với tên định danh
        mainPanel.add(homePanel, "HOME");
        mainPanel.add(addStudentPanel, "ADD_STUDENT");
        mainPanel.add(addTeacherPanel, "ADD_TEACHER");
        mainPanel.add(viewStudentPanel, "VIEW_STUDENT");
        mainPanel.add(viewTeacherPanel, "VIEW_TEACHER");
        
        // Layout tổng thể: Menu bên trái, Nội dung bên phải
        setLayout(new BorderLayout());
        add(menuPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Tạo panel menu điều hướng bên trái
     * @return JPanel chứa các nút menu
     */
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Menu"));
        panel.setPreferredSize(new Dimension(200, 0));
        panel.setBackground(new Color(240, 240, 240));
        
        // Tạo các nút menu
        JButton btnHome = createMenuButton("Trang Chủ");
        JButton btnAddStudent = createMenuButton("Thêm Sinh Viên");
        JButton btnAddTeacher = createMenuButton("Thêm Giáo Viên");
        JButton btnViewStudent = createMenuButton("Xem Sinh Viên");
        JButton btnViewTeacher = createMenuButton("Xem Giáo Viên");
        
        // Gán sự kiện cho các nút
        btnHome.addActionListener(e -> cardLayout.show(mainPanel, "HOME"));
        btnAddStudent.addActionListener(e -> cardLayout.show(mainPanel, "ADD_STUDENT"));
        btnAddTeacher.addActionListener(e -> cardLayout.show(mainPanel, "ADD_TEACHER"));
        
        // Khi xem danh sách, cần refresh dữ liệu từ database trước
        btnViewStudent.addActionListener(e -> {
            refreshStudentTable();
            cardLayout.show(mainPanel, "VIEW_STUDENT");
        });
        btnViewTeacher.addActionListener(e -> {
            refreshTeacherTable();
            cardLayout.show(mainPanel, "VIEW_TEACHER");
        });
        
        // Thêm các nút vào panel
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnHome);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnAddStudent);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnAddTeacher);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnViewStudent);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnViewTeacher);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    /**
     * Tạo nút menu với style thống nhất
     * @param text Text hiển thị trên nút
     * @return JButton đã được cấu hình
     */
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setPreferredSize(new Dimension(180, 40));
        return btn;
    }
    
    /**
     * Tạo panel trang chủ hiển thị thống kê
     * @return JPanel trang chủ
     */
    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Chào mừng đến với Trang Quản Trị", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
        
        // Panel hiển thị thống kê
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Lấy số lượng từ database
        int studentCount = getStudentCount();
        int teacherCount = getTeacherCount();
        
        JPanel stat1 = createStatPanel("Tổng Sinh Viên", String.valueOf(studentCount));
        JPanel stat2 = createStatPanel("Tổng Giáo Viên", String.valueOf(teacherCount));
        
        infoPanel.add(stat1);
        infoPanel.add(stat2);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(infoPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Tạo panel hiển thị thống kê
     * @param label Nhãn hiển thị
     * @param value Giá trị hiển thị
     * @return JPanel thống kê
     */
    private JPanel createStatPanel(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createRaisedBevelBorder());
        panel.setBackground(Color.WHITE);
        
        JLabel lbl = new JLabel(label, JLabel.CENTER);
        lbl.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JLabel val = new JLabel(value, JLabel.CENTER);
        val.setFont(new Font("Arial", Font.BOLD, 32));
        val.setForeground(new Color(0, 100, 200));
        
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(val, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(200, 150));
        
        return panel;
    }
    
    /**
     * Tạo panel form thêm sinh viên mới
     * Lưu vào bảng: students và users
     * @return JPanel form thêm sinh viên
     */
    private JPanel createAddStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel title = new JLabel("Thêm Sinh Viên Mới", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Form nhập liệu
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Mã sinh viên (student_code trong database)
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Mã Sinh Viên:"), gbc);
        JTextField txtStudentCode = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtStudentCode, gbc);
        
        // Tên sinh viên (full_name trong database)
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Tên Sinh Viên:"), gbc);
        JTextField txtStudentName = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtStudentName, gbc);
        
        // Ngày sinh (dob trong database)
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Ngày Sinh (YYYY-MM-DD):"), gbc);
        JFormattedTextField txtDob;
        try {
            MaskFormatter dateFormatter = new MaskFormatter("####-##-##");
            dateFormatter.setPlaceholderCharacter('_');
            txtDob = new JFormattedTextField(dateFormatter);
            txtDob.setColumns(20);
        } catch (ParseException e) {
            e.printStackTrace();
            txtDob = new JFormattedTextField();
            txtDob.setColumns(20);
        }
        final JFormattedTextField finalTxtDob = txtDob;
        gbc.gridx = 1;
        formPanel.add(txtDob, gbc);
        
        // Username để đăng nhập (tạo user trong bảng users)
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Username:"), gbc);
        JTextField txtUsername = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtUsername, gbc);
        
        // Password mặc định
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Password:"), gbc);
        JTextField txtPassword = new JTextField(20);
        
        gbc.gridx = 1;
        formPanel.add(txtPassword, gbc);
        
        // Nút thêm
        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Thêm Sinh Viên");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 14));
        btnAdd.setPreferredSize(new Dimension(160, 40));
        
        btnAdd.addActionListener(e -> {
            String studentCode = txtStudentCode.getText().trim();
            String fullName = txtStudentName.getText().trim();
            String dob = finalTxtDob.getText().replace("_", "").trim();
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText();
            
            // Kiểm tra dữ liệu đầu vào
            if (studentCode.isEmpty() || fullName.isEmpty() || dob.isEmpty() || 
                username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Thêm sinh viên vào database
            if (addStudentToDatabase(studentCode, fullName, dob, username, password)) {
                JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công!", 
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
                
                // Xóa form sau khi thêm thành công
                txtStudentCode.setText("");
                txtStudentName.setText("");
                finalTxtDob.setValue(null);
                txtUsername.setText("");
                txtPassword.setText("123456");
            } else {
                JOptionPane.showMessageDialog(this, "Thêm sinh viên thất bại! Kiểm tra lại thông tin.", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(btnAdd);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        return panel;
    }
    
    /**
     * Tạo panel form thêm giáo viên mới
     * Lưu vào bảng: teachers, users và subjects
     * @return JPanel form thêm giáo viên
     */
    private JPanel createAddTeacherPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel title = new JLabel("Thêm Giáo Viên Mới", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Form nhập liệu
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Tên giáo viên (full_name trong database)
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Tên Giáo Viên:"), gbc);
        JTextField txtTeacherName = new JTextField(20);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(txtTeacherName, gbc);
        
        // Username để đăng nhập
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Username:"), gbc);
        JTextField txtUsername = new JTextField(20);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(txtUsername, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Password:"), gbc);
        JTextField txtPassword = new JTextField(20);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(txtPassword, gbc);
        
        // Môn học phụ trách 
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Môn Học Phụ Trách:"), gbc);
        
        // Panel chứa bảng môn học và các nút điều khiển
        JPanel subjectPanel = new JPanel(new BorderLayout());
        
        // Bảng hiển thị danh sách môn học
        String[] subjectColumns = {"Tên Môn Học", "Số Tín Chỉ"};
        DefaultTableModel subjectTableModel = new DefaultTableModel(subjectColumns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) return Integer.class;
                return String.class;
            }
        };
        JTable subjectTable = new JTable(subjectTableModel);
        subjectTable.setRowHeight(25);
        JScrollPane subjectScrollPane = new JScrollPane(subjectTable);
        subjectScrollPane.setPreferredSize(new Dimension(300, 150));
        
        // Panel nhập liệu môn học mới
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblSubjectName = new JLabel("Tên Môn:");
        JTextField txtSubjectName = new JTextField(15);
        JLabel lblCredit = new JLabel("Số Tín Chỉ:");
        JSpinner spnCredit = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
        spnCredit.setPreferredSize(new Dimension(60, 25));
        JButton btnAddSubject = new JButton("Thêm Môn");
        JButton btnRemoveSubject = new JButton("Xóa Môn");
        
        inputPanel.add(lblSubjectName);
        inputPanel.add(txtSubjectName);
        inputPanel.add(lblCredit);
        inputPanel.add(spnCredit);
        inputPanel.add(btnAddSubject);
        inputPanel.add(btnRemoveSubject);
        
        // Sự kiện thêm môn học
        btnAddSubject.addActionListener(e -> {
            String subjectName = txtSubjectName.getText().trim();
            if (subjectName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên môn học!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int credit = (Integer) spnCredit.getValue();
            subjectTableModel.addRow(new Object[]{subjectName, credit});
            txtSubjectName.setText("");
            spnCredit.setValue(3);
        });
        
        // Sự kiện xóa môn học
        btnRemoveSubject.addActionListener(e -> {
            int selectedRow = subjectTable.getSelectedRow();
            if (selectedRow >= 0) {
                subjectTableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn môn học cần xóa!", 
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        subjectPanel.add(subjectScrollPane, BorderLayout.CENTER);
        subjectPanel.add(inputPanel, BorderLayout.SOUTH);
        
        gbc.gridx = 1;
        formPanel.add(subjectPanel, gbc);
        
        // Nút thêm
        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Thêm Giáo Viên");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 14));
        btnAdd.setPreferredSize(new Dimension(160, 40));
        
        btnAdd.addActionListener(e -> {
            String fullName = txtTeacherName.getText().trim();
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText();
            
            // Kiểm tra dữ liệu đầu vào
            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Lấy danh sách môn học từ bảng
            List<SubjectInfo> subjectList = new ArrayList<>();
            for (int i = 0; i < subjectTableModel.getRowCount(); i++) {
                String name = (String) subjectTableModel.getValueAt(i, 0);
                Integer credit = (Integer) subjectTableModel.getValueAt(i, 1);
                if (name != null && !name.trim().isEmpty()) {
                    subjectList.add(new SubjectInfo(name.trim(), credit));
                }
            }
            
            if (subjectList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng thêm ít nhất một môn học!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Thêm giáo viên vào database
            if (addTeacherToDatabase(fullName, username, password, subjectList)) {
                JOptionPane.showMessageDialog(this, "Thêm giáo viên thành công!", 
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
                
                // Xóa form sau khi thêm thành công
                txtTeacherName.setText("");
                txtUsername.setText("");
                txtPassword.setText("123456");
                subjectTableModel.setRowCount(0);
                txtSubjectName.setText("");
                spnCredit.setValue(3);
            } else {
                JOptionPane.showMessageDialog(this, "Thêm giáo viên thất bại! Kiểm tra lại thông tin.", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(btnAdd);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        return panel;
    }
    
    /**
     * Tạo panel hiển thị danh sách sinh viên từ database
     * @return JPanel hiển thị danh sách sinh viên
     */
    private JPanel createViewStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel title = new JLabel("Danh Sách Sinh Viên", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Tạo bảng với các cột phù hợp với database (thêm cột ID ẩn)
        String[] columns = {"Mã SV", "Tên Sinh Viên", "Ngày Sinh", "Username"};
        studentTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa trực tiếp trên bảng
            }
        };
        
        studentTable = new JTable(studentTableModel);
        studentTable.setRowHeight(25);
        studentTable.setFont(new Font("Arial", Font.PLAIN, 12));
        studentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane scrollPane = new JScrollPane(studentTable);
        
        // Panel chứa nút xóa
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnDelete = new JButton("Xóa Sinh Viên");
        btnDelete.setFont(new Font("Arial", Font.BOLD, 14));
        btnDelete.setPreferredSize(new Dimension(150, 35));
        btnDelete.setForeground(Color.RED);
        
        btnDelete.addActionListener(e -> {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên cần xóa!", 
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            String studentCode = (String) studentTableModel.getValueAt(selectedRow, 0);
            String studentName = (String) studentTableModel.getValueAt(selectedRow, 1);
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa sinh viên: " + studentName + " (Mã: " + studentCode + ")?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (deleteStudentFromDatabase(studentCode)) {
                    JOptionPane.showMessageDialog(this, "Xóa sinh viên thành công!", 
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    refreshStudentTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa sinh viên thất bại!", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        buttonPanel.add(btnDelete);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        return panel;
    }
    
    /**
     * Tạo panel hiển thị danh sách giáo viên từ database
     * @return JPanel hiển thị danh sách giáo viên
     */
    private JPanel createViewTeacherPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel title = new JLabel("Danh Sách Giáo Viên", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Tạo bảng với các cột phù hợp với database
        String[] columns = {"Tên Giáo Viên", "Username", "Môn Học Phụ Trách"};
        teacherTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa trực tiếp trên bảng
            }
        };
        
        teacherTable = new JTable(teacherTableModel);
        teacherTable.setRowHeight(25);
        teacherTable.setFont(new Font("Arial", Font.PLAIN, 12));
        teacherTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane scrollPane = new JScrollPane(teacherTable);
        
        // Panel chứa nút xóa
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnDelete = new JButton("Xóa Giáo Viên");
        btnDelete.setFont(new Font("Arial", Font.BOLD, 14));
        btnDelete.setPreferredSize(new Dimension(150, 35));
        btnDelete.setForeground(Color.RED);
        
        btnDelete.addActionListener(e -> {
            int selectedRow = teacherTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn giáo viên cần xóa!", 
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            String teacherName = (String) teacherTableModel.getValueAt(selectedRow, 0);
            String username = (String) teacherTableModel.getValueAt(selectedRow, 1);
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa giáo viên: " + teacherName + " (Username: " + username + ")?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (deleteTeacherFromDatabase(username)) {
                    JOptionPane.showMessageDialog(this, "Xóa giáo viên thành công!", 
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    refreshTeacherTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa giáo viên thất bại!", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        buttonPanel.add(btnDelete);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        return panel;
    }
    
    // ==================== CÁC PHƯƠNG THỨC LÀM VIỆC VỚI DATABASE ====================
    
    /**
     * Lấy số lượng sinh viên từ database
     * @return Số lượng sinh viên
     */
    private int getStudentCount() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM students")) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Lấy số lượng giáo viên từ database
     * @return Số lượng giáo viên
     */
    private int getTeacherCount() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM teachers")) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Thêm sinh viên mới vào database
     * Lưu vào 2 bảng: users (với role_id = 3) và students
     * 
     * @param studentCode Mã sinh viên (student_code)
     * @param fullName Tên đầy đủ (full_name)
     * @param dob Ngày sinh (dob) - format: YYYY-MM-DD
     * @param username Username để đăng nhập
     * @param password Password để đăng nhập
     * @return true nếu thêm thành công, false nếu thất bại
     */
    private boolean addStudentToDatabase(String studentCode, String fullName, String dob, 
                                         String username, String password) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction
            
            // Bước 1: Kiểm tra username đã tồn tại chưa
            String checkUserSql = "SELECT user_id FROM users WHERE username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkUserSql)) {
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Username đã tồn tại!", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            
            // Bước 2: Kiểm tra student_code đã tồn tại chưa
            String checkCodeSql = "SELECT student_id FROM students WHERE student_code = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkCodeSql)) {
                checkStmt.setString(1, studentCode);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Mã sinh viên đã tồn tại!", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            
            // Bước 3: Thêm vào bảng users với role_id = 3 (STUDENT)
            String insertUserSql = "INSERT INTO users (username, password, role_id) VALUES (?, ?, 3)";
            int userId = -1;
            try (PreparedStatement userStmt = conn.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, username);
                userStmt.setString(2, password); // Trong thực tế nên hash password
                userStmt.executeUpdate();
                
                // Lấy user_id vừa tạo
                ResultSet rs = userStmt.getGeneratedKeys();
                if (rs.next()) {
                    userId = rs.getInt(1);
                }
            }
            
            // Bước 4: Thêm vào bảng students với user_id vừa tạo
            String insertStudentSql = "INSERT INTO students (user_id, full_name, student_code, dob) VALUES (?, ?, ?, ?)";
            try (PreparedStatement studentStmt = conn.prepareStatement(insertStudentSql)) {
                studentStmt.setInt(1, userId);
                studentStmt.setString(2, fullName);
                studentStmt.setString(3, studentCode);
                studentStmt.setString(4, dob);
                studentStmt.executeUpdate();
            }
            
            conn.commit(); // Commit transaction
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback nếu có lỗi
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Thêm giáo viên mới vào database
     * Lưu vào 3 bảng: users (với role_id = 2), teachers và subjects
     * 
     * @param fullName Tên đầy đủ giáo viên
     * @param username Username để đăng nhập
     * @param password Password để đăng nhập
     * @param subjectList Danh sách môn học phụ trách
     * @return true nếu thêm thành công, false nếu thất bại
     */
    private boolean addTeacherToDatabase(String fullName, String username, String password, 
                                         List<SubjectInfo> subjectList) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction
            
            // Bước 1: Kiểm tra username đã tồn tại chưa
            String checkUserSql = "SELECT user_id FROM users WHERE username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkUserSql)) {
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Username đã tồn tại!", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            
            // Bước 2: Thêm vào bảng users với role_id = 2 (TEACHER)
            String insertUserSql = "INSERT INTO users (username, password, role_id) VALUES (?, ?, 2)";
            int userId = -1;
            try (PreparedStatement userStmt = conn.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, username);
                userStmt.setString(2, password); // Trong thực tế nên hash password
                userStmt.executeUpdate();
                
                // Lấy user_id vừa tạo
                ResultSet rs = userStmt.getGeneratedKeys();
                if (rs.next()) {
                    userId = rs.getInt(1);
                }
            }
            
            // Bước 3: Thêm vào bảng teachers với user_id vừa tạo
            String insertTeacherSql = "INSERT INTO teachers (user_id, full_name) VALUES (?, ?)";
            int teacherId = -1;
            try (PreparedStatement teacherStmt = conn.prepareStatement(insertTeacherSql, Statement.RETURN_GENERATED_KEYS)) {
                teacherStmt.setInt(1, userId);
                teacherStmt.setString(2, fullName);
                teacherStmt.executeUpdate();
                
                // Lấy teacher_id vừa tạo
                ResultSet rs = teacherStmt.getGeneratedKeys();
                if (rs.next()) {
                    teacherId = rs.getInt(1);
                }
            }
            
            // Bước 4: Thêm các môn học vào bảng subjects
            String insertSubjectSql = "INSERT INTO subjects (subject_name, teacher_id, credit) VALUES (?, ?, ?)";
            try (PreparedStatement subjectStmt = conn.prepareStatement(insertSubjectSql)) {
                for (SubjectInfo subject : subjectList) {
                    subjectStmt.setString(1, subject.name);
                    subjectStmt.setInt(2, teacherId);
                    subjectStmt.setInt(3, subject.credit);
                    subjectStmt.executeUpdate();
                }
            }
            
            conn.commit(); // Commit transaction
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback nếu có lỗi
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Refresh bảng hiển thị danh sách sinh viên từ database
     */
    private void refreshStudentTable() {
        studentTableModel.setRowCount(0); // Xóa dữ liệu cũ
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT s.student_code, s.full_name, s.dob, u.username " +
                 "FROM students s " +
                 "JOIN users u ON s.user_id = u.user_id " +
                 "ORDER BY s.student_code")) {
            
            while (rs.next()) {
                studentTableModel.addRow(new Object[]{
                    rs.getString("student_code"),
                    rs.getString("full_name"),
                    rs.getString("dob"),
                    rs.getString("username")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu sinh viên!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Refresh bảng hiển thị danh sách giáo viên từ database
     */
    private void refreshTeacherTable() {
        teacherTableModel.setRowCount(0); // Xóa dữ liệu cũ
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT t.full_name, u.username, " +
                 "GROUP_CONCAT(s.subject_name SEPARATOR ', ') AS subjects " +
                 "FROM teachers t " +
                 "JOIN users u ON t.user_id = u.user_id " +
                 "LEFT JOIN subjects s ON t.teacher_id = s.teacher_id " +
                 "GROUP BY t.teacher_id, t.full_name, u.username " +
                 "ORDER BY t.full_name")) {
            
            while (rs.next()) {
                String subjects = rs.getString("subjects");
                if (subjects == null) {
                    subjects = "Chưa có môn học";
                }
                
                teacherTableModel.addRow(new Object[]{
                    rs.getString("full_name"),
                    rs.getString("username"),
                    subjects
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu giáo viên!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Xóa sinh viên khỏi database
     * Xóa từ 2 bảng: students và users
     * 
     * @param studentCode Mã sinh viên cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    private boolean deleteStudentFromDatabase(String studentCode) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction
            
            // Bước 1: Lấy user_id từ student_code
            int userId = -1;
            String getUserIdSql = "SELECT user_id FROM students WHERE student_code = ?";
            try (PreparedStatement stmt = conn.prepareStatement(getUserIdSql)) {
                stmt.setString(1, studentCode);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên với mã: " + studentCode, 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                userId = rs.getInt("user_id");
            }
            
            // Bước 2: Xóa từ bảng students
            String deleteStudentSql = "DELETE FROM students WHERE student_code = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteStudentSql)) {
                stmt.setString(1, studentCode);
                stmt.executeUpdate();
            }
            
            // Bước 3: Xóa từ bảng users
            String deleteUserSql = "DELETE FROM users WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteUserSql)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }
            
            conn.commit(); // Commit transaction
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback nếu có lỗi
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Xóa giáo viên khỏi database
     * Xóa từ 3 bảng: subjects, teachers và users
     * 
     * @param username Username của giáo viên cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    private boolean deleteTeacherFromDatabase(String username) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction
            
            // Bước 1: Lấy user_id và teacher_id từ username
            int userId = -1;
            int teacherId = -1;
            String getIdsSql = "SELECT u.user_id, t.teacher_id FROM users u " +
                              "JOIN teachers t ON u.user_id = t.user_id " +
                              "WHERE u.username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(getIdsSql)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy giáo viên với username: " + username, 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                userId = rs.getInt("user_id");
                teacherId = rs.getInt("teacher_id");
            }
            
            // Bước 2: Xóa từ bảng subjects
            String deleteSubjectsSql = "DELETE FROM subjects WHERE teacher_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteSubjectsSql)) {
                stmt.setInt(1, teacherId);
                stmt.executeUpdate();
            }
            
            // Bước 3: Xóa từ bảng teachers
            String deleteTeacherSql = "DELETE FROM teachers WHERE teacher_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteTeacherSql)) {
                stmt.setInt(1, teacherId);
                stmt.executeUpdate();
            }
            
            // Bước 4: Xóa từ bảng users
            String deleteUserSql = "DELETE FROM users WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteUserSql)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }
            
            conn.commit(); // Commit transaction
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback nếu có lỗi
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    // ==================== CÁC LỚP HỖ TRỢ ====================
    
    /**
     * Lớp lưu trữ thông tin môn học
     */
    private static class SubjectInfo {
        String name;   // Tên môn học
        int credit;    // Số tín chỉ
        
        SubjectInfo(String name, int credit) {
            this.name = name;
            this.credit = credit;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            adminPage frame = new adminPage();
            frame.setVisible(true);
        });
    }
}

