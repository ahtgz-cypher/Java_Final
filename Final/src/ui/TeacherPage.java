package ui;

import dao.StudentDAO;
import dao.ScoreDAO;
import dao.SubjectDAO;
import model.Student;
import model.Score;
import model.Subject;
import util.ReportUtil;
import util.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class TeacherPage extends JFrame {
    private JTextField searchField;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextArea infoArea;
    private JComboBox<Subject> subjectComboBox;
    private JTextField scoreField;
    private JLabel statsLabel;
    private int currentTeacherId;
    
    public TeacherPage(String teacherName, int teacherId) {
        this.currentTeacherId = teacherId;
        setTitle(Constants.TEACHER_PAGE_TITLE_PREFIX + teacherName);
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        loadAllStudents();
    }
    
    // ===== STYLE BUTTON - MODERN UI =====
    private void styleButton(JButton button, Color bgColor, Color hoverColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }
    
    // ===== STYLE WHITE BUTTON =====
    private void styleWhiteButton(JButton button) {
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(52, 73, 94));
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        Color hoverColor = new Color(236, 240, 241);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });
    }
    
    private void initComponents() {
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // ===== TOP PANEL - TÌM KIẾM =====
        JPanel topPanel = new JPanel(new BorderLayout(15, 10));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(236, 240, 241)),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        // Left panel for search
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setBackground(Color.WHITE);
        
        JLabel searchLabel = new JLabel("Tìm kiếm sinh viên:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchLabel.setForeground(new Color(52, 73, 94));
        
        searchField = new JTextField(30);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        searchField.setPreferredSize(new Dimension(320, 36));
        
        JButton searchButton = new JButton("↩");
        JButton refreshButton = new JButton("↻");
        JButton logoutButton = new JButton("⌂");
        
        // Modern styling
        styleButton(searchButton, new Color(52, 152, 219), new Color(41, 128, 185)); // Blue
        styleButton(refreshButton, new Color(149, 165, 166), new Color(127, 140, 141)); // Gray
        styleButton(logoutButton, new Color(231, 76, 60), new Color(192, 57, 43)); // Red

        searchButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
        // searchButton.setPreferredSize(new Dimension(50, 35));
        refreshButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
        // refreshButton.setPreferredSize(new Dimension(50, 35));
        logoutButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
        // logoutButton.setPreferredSize(new Dimension(50, 35));

        // Set tooltip for logout button
        searchButton.setToolTipText("Tìm kiếm");
        refreshButton.setToolTipText("Làm mới");
        logoutButton.setToolTipText("Đăng xuất");
        
        searchButton.addActionListener(e -> searchStudents());
        refreshButton.addActionListener(e -> loadAllStudents());
        logoutButton.addActionListener(e -> logout());
        
        // Thêm Enter key listener cho search field
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchStudents();
                }
            }
        });
        
        leftPanel.add(searchLabel);
        leftPanel.add(searchField);
        leftPanel.add(searchButton);
        leftPanel.add(refreshButton);
        
        // Right panel for logout
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        logoutPanel.setBackground(Color.WHITE);
        logoutPanel.add(logoutButton);
        
        topPanel.add(leftPanel, BorderLayout.CENTER);
        topPanel.add(logoutPanel, BorderLayout.EAST);
        
        // ===== CENTER PANEL - DANH SÁCH SINH VIÊN =====
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBorder(BorderFactory.createTitledBorder(Constants.WINDOW_TITLE_STUDENT_LIST));
        
        // Table
        String[] columnNames = {"ID", "Mã sinh viên", "Họ tên", "Ngày sinh"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setRowHeight(28);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        studentTable.setSelectionBackground(new Color(52, 152, 219));
        studentTable.setSelectionForeground(Color.WHITE);
        studentTable.setGridColor(new Color(236, 240, 241));
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        
        // Style table header
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        studentTable.getTableHeader().setBackground(new Color(52, 73, 94));
        studentTable.getTableHeader().setForeground(Color.WHITE);
        studentTable.getTableHeader().setOpaque(true);
        studentTable.getTableHeader().setBorder(BorderFactory.createEmptyBorder());
        
        // ===== CUSTOM RENDERER CHO MÀU SẮC =====
        studentTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, 
                    isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    // Alternating row colors
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(236, 240, 241));
                    }
                }
                return c;
            }
        });
        
        // Table selection listener
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && studentTable.getSelectedRow() != -1) {
                showStudentInfo();
            }
        });
        
        // ===== DOUBLE-CLICK LISTENER =====
        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && studentTable.getSelectedRow() != -1) {
                    showStudentInfo();
                }
            }
        });
        
        JScrollPane tableScrollPane = new JScrollPane(studentTable);
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // ===== RIGHT PANEL - THÔNG TIN & CHỨC NĂNG =====
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setPreferredSize(new Dimension(400, 0));
        
        // Thông tin sinh viên
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder(Constants.WINDOW_TITLE_DETAIL));
        
        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        infoArea.setBackground(new Color(250, 250, 250));
        infoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane infoScrollPane = new JScrollPane(infoArea);
        infoPanel.add(infoScrollPane, BorderLayout.CENTER);
        
        // Panel nhập điểm
        JPanel scorePanel = new JPanel(new GridBagLayout());
        scorePanel.setBorder(BorderFactory.createTitledBorder(Constants.WINDOW_TITLE_ENTER_SCORE));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Môn học
        gbc.gridx = 0;
        gbc.gridy = 0;
        scorePanel.add(new JLabel("Môn học:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        subjectComboBox = new JComboBox<>();
        subjectComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subjectComboBox.setBackground(Color.WHITE);
        loadSubjects();
        scorePanel.add(subjectComboBox, gbc);
        
        // Điểm
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        scorePanel.add(new JLabel("Điểm:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        scoreField = new JTextField();
        scoreField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        scoreField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        scorePanel.add(scoreField, gbc);
        
        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton enterScoreButton = new JButton("Nhập điểm");
        JButton deleteScoreButton = new JButton("Xóa điểm");
        
        // Modern styling
        styleButton(enterScoreButton, new Color(46, 204, 113), new Color(39, 174, 96)); // Green
        styleButton(deleteScoreButton, new Color(231, 76, 60), new Color(192, 57, 43)); // Red
        
        enterScoreButton.addActionListener(e -> enterScore());
        deleteScoreButton.addActionListener(e -> deleteScore());
        
        buttonPanel.add(enterScoreButton);
        buttonPanel.add(deleteScoreButton);
        scorePanel.add(buttonPanel, gbc);
        
        // Panel xuất báo cáo
        JPanel reportPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        reportPanel.setBorder(BorderFactory.createTitledBorder(Constants.WINDOW_TITLE_REPORT));
        
        JButton exportAllButton = new JButton("Xuất báo cáo tất cả");
        JButton exportSubjectButton = new JButton("Xuất báo cáo theo môn");
        JButton exportStudentButton = new JButton("Xuất báo cáo sinh viên");
        
        // Modern white styling
        styleWhiteButton(exportAllButton);
        styleWhiteButton(exportSubjectButton);
        styleWhiteButton(exportStudentButton);
        
        exportAllButton.addActionListener(e -> exportAllScores());
        exportSubjectButton.addActionListener(e -> exportScoresBySubject());
        exportStudentButton.addActionListener(e -> exportStudentScores());
        
        reportPanel.add(exportAllButton);
        reportPanel.add(exportSubjectButton);
        reportPanel.add(exportStudentButton);
        
        // Add to right panel
        rightPanel.add(infoPanel, BorderLayout.CENTER);
        
        JPanel bottomRightPanel = new JPanel(new BorderLayout(5, 5));
        bottomRightPanel.add(scorePanel, BorderLayout.NORTH);
        bottomRightPanel.add(reportPanel, BorderLayout.CENTER);
        rightPanel.add(bottomRightPanel, BorderLayout.SOUTH);
        
        // ===== BOTTOM PANEL - THỐNG KÊ =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsLabel = new JLabel("Tổng số sinh viên: 0");
        bottomPanel.add(statsLabel);
        
        // Add all panels to main
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    // ===== TÌM KIẾM SINH VIÊN =====
    private void searchStudents() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                Constants.MSG_KEYWORD_REQUIRED, 
                Constants.TITLE_WARNING, 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validate keyword length
        if (keyword.length() > Constants.MAX_KEYWORD_LENGTH) {
            JOptionPane.showMessageDialog(this, 
                Constants.MSG_KEYWORD_TOO_LONG, 
                Constants.TITLE_WARNING, 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        List<Student> students = StudentDAO.searchStudents(keyword);
        displayStudents(students);
        
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                Constants.MSG_NO_STUDENT_FOUND, 
                Constants.TITLE_INFO, 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
        // ===== ĐĂNG XUẤT =====
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn đăng xuất?",
            "Xác nhận đăng xuất",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Đóng cửa sổ hiện tại
            this.dispose();
            
            // Mở lại login page
            SwingUtilities.invokeLater(() -> {
                ui.login loginPage = new ui.login();
                loginPage.setVisible(true);
            });
        }
    }

    // ===== TẢI TẤT CẢ SINH VIÊN =====
    private void loadAllStudents() {
        List<Student> students = StudentDAO.getAllStudents();
        displayStudents(students);
        searchField.setText("");
        infoArea.setText("");
    }
    
    // ===== HIỂN THỊ DANH SÁCH SINH VIÊN =====
    private void displayStudents(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student student : students) {
            Object[] row = {
                student.getStudentId(),
                student.getStudentCode(),
                student.getFullName(),
                student.getDob()
            };
            tableModel.addRow(row);
        }
        // Update statistics label
        if (statsLabel != null) {
            statsLabel.setText("Tổng số sinh viên: " + students.size());
        }
    }
    
    // ===== HIỂN THỊ THÔNG TIN SINH VIÊN =====
    private void showStudentInfo() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        int studentId = (int) tableModel.getValueAt(selectedRow, 0);
        Student student = StudentDAO.getStudentById(studentId).orElse(null);
        
        if (student != null) {
            List<Score> scores = ScoreDAO.getScoresByStudent(studentId);
            
            StringBuilder info = new StringBuilder();
            info.append("╔═════════════════════════════════╗\n");
            info.append("‖       THÔNG TIN SINH VIÊN       ‖\n");
            info.append("╚═════════════════════════════════╝\n\n");
            info.append("Mã sinh viên: ").append(student.getStudentCode()).append("\n");
            info.append("Họ tên: ").append(student.getFullName()).append("\n");
            info.append("Ngày sinh: ").append(student.getDob()).append("\n\n");
            
            info.append("╔═════════════════════════════════╗\n");
            info.append("‖            BẢNG ĐIỂM            ‖\n");
            info.append("╚═════════════════════════════════╝\n\n");
            
            if (scores.isEmpty()) {
                info.append("Chưa có điểm.\n");
            } else {
                double total = 0;
                for (Score score : scores) {
                    // Add classification indicator
                    String indicator = "";
                    if (score.getScore() >= 8.0) {
                        indicator = " [Giỏi]";
                    } else if (score.getScore() >= 6.5) {
                        indicator = " [Khá]";
                    } else if (score.getScore() >= 5.0) {
                        indicator = " [Trung bình]";
                    } else {
                        indicator = " [Yếu]";
                    }
                    
                    info.append(String.format("%-25s: %.2f%s\n", 
                        score.getSubjectName(), score.getScore(), indicator));
                    total += score.getScore();
                }
                double average = total / scores.size();
                String avgClassification = ReportUtil.getClassification(average);
                info.append("\n").append("─".repeat(35)).append("\n");
                info.append(String.format("Điểm trung bình: %.2f - %s\n", average, avgClassification));
            }
            
            infoArea.setText(info.toString());
        }
    }
    
    // ===== TẢI DANH SÁCH MÔN HỌC =====
    private void loadSubjects() {
        subjectComboBox.removeAllItems();
        // Filter subjects by current teacher ID
        List<Subject> subjects = SubjectDAO.getSubjectsByTeacher(currentTeacherId);
        for (Subject subject : subjects) {
            subjectComboBox.addItem(subject);
        }
    }
    
    // ===== NHẬP ĐIỂM =====
    private void enterScore() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                Constants.MSG_NO_STUDENT_SELECTED, 
                Constants.TITLE_WARNING, 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (subjectComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, 
                Constants.MSG_NO_SUBJECT_SELECTED, 
                Constants.TITLE_WARNING, 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String scoreText = scoreField.getText().trim();
        if (scoreText.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                Constants.MSG_NO_SCORE_INPUT, 
                Constants.TITLE_WARNING, 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            double score = Double.parseDouble(scoreText);
            if (score < Constants.SCORE_MIN || score > Constants.SCORE_MAX) {
                JOptionPane.showMessageDialog(this, 
                    Constants.MSG_INVALID_SCORE_RANGE, 
                    Constants.TITLE_ERROR, 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int studentId = (int) tableModel.getValueAt(selectedRow, 0);
            Subject subject = (Subject) subjectComboBox.getSelectedItem();
            int subjectId = subject.getSubjectId();
            
            boolean success = ScoreDAO.addOrUpdateScore(studentId, subjectId, score);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    Constants.MSG_SCORE_SUCCESS, 
                    Constants.TITLE_SUCCESS, 
                    JOptionPane.INFORMATION_MESSAGE);
                scoreField.setText("");
                showStudentInfo(); // Refresh info
            } else {
                JOptionPane.showMessageDialog(this, 
                    Constants.MSG_SCORE_FAILED, 
                    Constants.TITLE_ERROR, 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                Constants.MSG_INVALID_SCORE_FORMAT, 
                Constants.TITLE_ERROR, 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ===== XÓA ĐIỂM =====
    private void deleteScore() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                Constants.MSG_NO_STUDENT_SELECTED, 
                Constants.TITLE_WARNING, 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (subjectComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, 
                Constants.MSG_NO_SUBJECT_SELECTED, 
                Constants.TITLE_WARNING, 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int studentId = (int) tableModel.getValueAt(selectedRow, 0);
        Subject subject = (Subject) subjectComboBox.getSelectedItem();
        int subjectId = subject.getSubjectId();
        
        // Check if score exists
        Score existingScore = ScoreDAO.getScore(studentId, subjectId).orElse(null);
        if (existingScore == null) {
            JOptionPane.showMessageDialog(this, 
                "Sinh viên này chưa có điểm môn học được chọn!", 
                Constants.TITLE_WARNING, 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa điểm môn " + subject.getSubjectName() + "?\n" +
            "Điểm hiện tại: " + existingScore.getScore(), 
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = ScoreDAO.deleteScore(studentId, subjectId);
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Xóa điểm thành công!", 
                    Constants.TITLE_SUCCESS, 
                    JOptionPane.INFORMATION_MESSAGE);
                scoreField.setText("");
                showStudentInfo(); // Refresh info
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Xóa điểm thất bại!", 
                    Constants.TITLE_ERROR, 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // ===== XUẤT BÁO CÁO TẤT CẢ =====
    private void exportAllScores() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn thư mục lưu báo cáo");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String directory = fileChooser.getSelectedFile().getAbsolutePath();
            
            // Use SwingWorker để không block UI
            new SwingWorker<Boolean, Void>() {
                private String fileName;
                
                @Override
                protected Boolean doInBackground() throws Exception {
                    List<Score> scores = ScoreDAO.getAllScores();
                    
                    if (scores.isEmpty()) {
                        return null; // Signal no data
                    }
                    
                    fileName = ReportUtil.generateReportFileName(Constants.REPORT_PREFIX_ALL);
                    String filePath = directory + File.separator + fileName;
                    
                    return ReportUtil.exportScoresToCSV(scores, filePath);
                }
                
                @Override
                protected void done() {
                    try {
                        Boolean success = get();
                        if (success == null) {
                            JOptionPane.showMessageDialog(TeacherPage.this, 
                                Constants.MSG_NO_DATA_TO_EXPORT, 
                                Constants.TITLE_WARNING, 
                                JOptionPane.WARNING_MESSAGE);
                        } else if (success) {
                            JOptionPane.showMessageDialog(TeacherPage.this, 
                                Constants.MSG_EXPORT_SUCCESS + "\nFile: " + fileName, 
                                Constants.TITLE_SUCCESS, 
                                JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(TeacherPage.this, 
                                Constants.MSG_EXPORT_FAILED, 
                                Constants.TITLE_ERROR, 
                                JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(TeacherPage.this, 
                            Constants.MSG_EXPORT_FAILED + "\n" + e.getMessage(), 
                            Constants.TITLE_ERROR, 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();
        }
    }
    
    // ===== XUẤT BÁO CÁO THEO MÔN =====
    private void exportScoresBySubject() {
        if (subjectComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, 
                Constants.MSG_NO_SUBJECT_SELECTED, 
                Constants.TITLE_WARNING, 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn thư mục lưu báo cáo");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String directory = fileChooser.getSelectedFile().getAbsolutePath();
            Subject subject = (Subject) subjectComboBox.getSelectedItem();
            int subjectId = subject.getSubjectId();
            String subjectName = subject.getSubjectName();
            
            // Use SwingWorker
            new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    List<Score> scores = ScoreDAO.getScoresBySubject(subjectId);
                    
                    if (scores.isEmpty()) {
                        return null; // Signal no data
                    }
                    
                    return ReportUtil.exportScoresBySubject(scores, subjectName, directory);
                }
                
                @Override
                protected void done() {
                    try {
                        Boolean success = get();
                        if (success == null) {
                            JOptionPane.showMessageDialog(TeacherPage.this, 
                                Constants.MSG_NO_SCORE_DATA, 
                                Constants.TITLE_WARNING, 
                                JOptionPane.WARNING_MESSAGE);
                        } else if (success) {
                            JOptionPane.showMessageDialog(TeacherPage.this, 
                                Constants.MSG_EXPORT_SUCCESS, 
                                Constants.TITLE_SUCCESS, 
                                JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(TeacherPage.this, 
                                Constants.MSG_EXPORT_FAILED, 
                                Constants.TITLE_ERROR, 
                                JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(TeacherPage.this, 
                            Constants.MSG_EXPORT_FAILED + "\n" + e.getMessage(), 
                            Constants.TITLE_ERROR, 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();
        }
    }
    
    // ===== XUẤT BÁO CÁO SINH VIÊN =====
    private void exportStudentScores() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                Constants.MSG_NO_STUDENT_SELECTED, 
                Constants.TITLE_WARNING, 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn thư mục lưu báo cáo");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String directory = fileChooser.getSelectedFile().getAbsolutePath();
            int studentId = (int) tableModel.getValueAt(selectedRow, 0);
            String studentCode = (String) tableModel.getValueAt(selectedRow, 1);
            
            // Use SwingWorker
            new SwingWorker<Boolean, Void>() {
                private String fileName;
                
                @Override
                protected Boolean doInBackground() throws Exception {
                    List<Score> scores = ScoreDAO.getScoresByStudent(studentId);
                    
                    if (scores.isEmpty()) {
                        return null; // Signal no data
                    }
                    
                    fileName = ReportUtil.generateReportFileName(Constants.REPORT_PREFIX_SUBJECT + studentCode);
                    String filePath = directory + File.separator + fileName;
                    
                    return ReportUtil.exportScoresToCSV(scores, filePath);
                }
                
                @Override
                protected void done() {
                    try {
                        Boolean success = get();
                        if (success == null) {
                            JOptionPane.showMessageDialog(TeacherPage.this, 
                                Constants.MSG_STUDENT_NO_SCORE, 
                                Constants.TITLE_WARNING, 
                                JOptionPane.WARNING_MESSAGE);
                        } else if (success) {
                            JOptionPane.showMessageDialog(TeacherPage.this, 
                                Constants.MSG_EXPORT_SUCCESS + "\nFile: " + fileName, 
                                Constants.TITLE_SUCCESS, 
                                JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(TeacherPage.this, 
                                Constants.MSG_EXPORT_FAILED, 
                                Constants.TITLE_ERROR, 
                                JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(TeacherPage.this, 
                            Constants.MSG_EXPORT_FAILED + "\n" + e.getMessage(), 
                            Constants.TITLE_ERROR, 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();
        }
    }
    
    // ===== MAIN - CHỈ ĐỂ TEST =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TeacherPage page = new TeacherPage("Giáo viên Test", 1);
            page.setVisible(true);
        });
    }
}
