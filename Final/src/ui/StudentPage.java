package ui;

import config.DBConnection;

import dao.ScoreDAO;
import dao.StudentDAO;
import dao.UserDAO;
import model.Score;
import model.Student;
import model.User;

import javax.swing.*;
import javax.swing.JFileChooser;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import util.ReportUtil;


public class StudentPage extends JFrame {

    private final int userId;
    private Student currentStudent;
    private User currentUser;

    
    private JLabel lblName;
    private JLabel lblUsername;
    private JLabel lblCode;
    private JLabel lblDob;
    private JLabel lblGpa;
    private JLabel lblTotalCredits;
    private JLabel lblStatus;

    private JComboBox<String> cboFilter;
    private JTextField txtSearch;
    private DefaultTableModel tableModel;
    private JTable tblScores;


    private TableRowSorter<DefaultTableModel> sorter;
    private java.util.List<Score> cachedScores = new java.util.ArrayList<>();
    private JButton btnRefresh;
    private JButton btnExportPdf;
    private JButton btnChangePassword;
    private JButton btnLogout;
    private JLabel lblHeaderTitle;

    
    private JProgressBar pbLoading;
    private JLabel lblLoadingText;

    private final DecimalFormat df = new DecimalFormat("#0.00");

    public StudentPage(int userId) {
        this.userId = userId;

        setTitle("StudentPage");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(920, 600);
        setLocationRelativeTo(null);

        setContentPane(buildRoot());
        bindActions();

        
        refreshAll();

        setVisible(true);
    }

    private JComponent buildRoot() {
        GradientPanel root = new GradientPanel();
        root.setLayout(new BorderLayout());
        root.setBorder(new EmptyBorder(18, 18, 18, 18));

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildCenter(), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);

        return root;
    }

    private JComponent buildHeader() {
        JPanel header = new JPanel(new BorderLayout(14, 0));
        header.setOpaque(false);

    
        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        lblHeaderTitle = new JLabel("...");
        lblHeaderTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblHeaderTitle.setForeground(new Color(255, 255, 255));

        JLabel subtitle = new JLabel("Công Phu Đủ Bền Trái Ngọt Ắt Sinh");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(230, 240, 255));

        titleBox.add(lblHeaderTitle);
        titleBox.add(Box.createVerticalStrut(4));
        titleBox.add(subtitle);

        header.add(titleBox, BorderLayout.WEST);
        return header;
    }

    private JComponent buildCenter() {
        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setOpaque(false);
        center.add(buildInfoCard(), BorderLayout.NORTH);
        center.add(buildScoreCard(), BorderLayout.CENTER);
        return center;
    }

    private JComponent buildInfoCard() {
        RoundedPanel card = new RoundedPanel(18, new Color(255, 255, 255, 235));
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(new EmptyBorder(14, 14, 14, 14));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("Thông tin sinh viên");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 16));
        heading.setForeground(new Color(44, 62, 80));

        lblName = new JLabel("Họ tên: --");
        lblUsername = new JLabel("Tài khoản: --");
        lblCode = new JLabel("Mã SV: --");
        lblDob  = new JLabel("Ngày sinh: --");

        for (JLabel lb : new JLabel[]{lblName, lblCode, lblDob, lblUsername}) {
            lb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lb.setForeground(new Color(52, 73, 94));
            lb.setBorder(new EmptyBorder(2, 0, 2, 0));
        }

        left.add(heading);
        left.add(Box.createVerticalStrut(6));
        left.add(lblName);
        left.add(lblCode);
        left.add(lblDob);
        left.add(lblUsername);

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        JLabel gpaTitle = new JLabel("GPA");
        gpaTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gpaTitle.setForeground(new Color(44, 62, 80));

        lblGpa = new JLabel("--");
        lblTotalCredits = new JLabel("Tổng TC: --");
        lblGpa.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblGpa.setForeground(new Color(39, 174, 96));

        JLabel note = new JLabel("GPA tính tự động từ điểm trong hệ thống");
        note.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        note.setForeground(new Color(127, 140, 141));

        right.add(gpaTitle);
        right.add(Box.createVerticalStrut(6));
        right.add(lblGpa);
        right.add(Box.createVerticalStrut(6));
        right.add(lblTotalCredits);
        right.add(Box.createVerticalStrut(6));
        right.add(note);

        card.add(left, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);

        return card;
    }

    private JComponent buildScoreCard() {
        RoundedPanel card = new RoundedPanel(18, new Color(255, 255, 255, 235));
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(new EmptyBorder(14, 14, 14, 14));

        
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel heading = new JLabel("Bảng điểm");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 16));
        heading.setForeground(new Color(44, 62, 80));

        JPanel filterBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filterBox.setOpaque(false);

        JLabel lblFilter = new JLabel("Lọc:");
        lblFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblFilter.setForeground(new Color(52, 73, 94));

        cboFilter = new JComboBox<>(new String[]{"Tất cả", "A-F", "F-A", "A", "B", "C", "D", "F"});
        cboFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));


        JLabel lblSearch = new JLabel("Tìm:");
        lblSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSearch.setForeground(new Color(52, 73, 94));

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(200, 28));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setToolTipText("Tìm theo Môn học hoặc Giáo viên");
        filterBox.add(lblFilter);
        filterBox.add(cboFilter);
        filterBox.add(lblSearch);
        filterBox.add(txtSearch);
        top.add(heading, BorderLayout.WEST);
        top.add(filterBox, BorderLayout.EAST);

        
        tableModel = new DefaultTableModel(new Object[]{"STT", "TC", "Môn học", "Giáo viên", "Điểm số", "Điểm chữ"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tblScores = new JTable(tableModel) {
            @Override
            public String getToolTipText(java.awt.event.MouseEvent e) {
                int row = rowAtPoint(e.getPoint());
                int col = columnAtPoint(e.getPoint());
                if (row < 0 || col < 0) return null;

                int modelRow = convertRowIndexToModel(row);
                int modelCol = convertColumnIndexToModel(col);

                
                if (modelCol == 5) {
                    Object v = tableModel.getValueAt(modelRow, modelCol);
                    String letter = v == null ? "" : v.toString().trim().toUpperCase();
                    return gradeTooltip(letter);
                }

                
                Object v = tableModel.getValueAt(modelRow, modelCol);
                if (v != null) {
                    String s = v.toString();
                    if (s.length() > 24) return s;
                }
                return null;
            }
        };
        configureTableColumns();
        tblScores.setRowHeight(28);
        tblScores.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblScores.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblScores.getTableHeader().setReorderingAllowed(false);

        
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        tblScores.getColumnModel().getColumn(0).setCellRenderer(center);
        tblScores.getColumnModel().getColumn(1).setCellRenderer(center);
        tblScores.getColumnModel().getColumn(4).setCellRenderer(center);
        tblScores.getColumnModel().getColumn(5).setCellRenderer(center);
        JScrollPane sp = new JScrollPane(tblScores);
        sp.getViewport().addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                applyResponsiveColumnWidths(sp.getViewport().getWidth());
            }
        });
        sp.setBorder(BorderFactory.createLineBorder(new Color(230, 236, 240)));

        
        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(new Color(127, 140, 141));

        card.add(top, BorderLayout.NORTH);
        card.add(sp, BorderLayout.CENTER);
        card.add(lblStatus, BorderLayout.SOUTH);

        return card;
    }

    private JComponent buildFooter() {
    JPanel footer = new JPanel(new BorderLayout(12, 0));
    footer.setOpaque(false);
    footer.setBorder(new EmptyBorder(14, 0, 0, 0));

    JLabel tip = new JLabel("Dương Quang Tại Tiền Hà Tất Ngại Đêm");
    tip.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    tip.setForeground(new Color(230, 240, 255));
    footer.add(tip, BorderLayout.WEST);

    
    JPanel loadingBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    loadingBox.setOpaque(false);
    pbLoading = new JProgressBar();
    pbLoading.setIndeterminate(true);
    pbLoading.setVisible(false);
    pbLoading.setPreferredSize(new Dimension(120, 10));
    lblLoadingText = new JLabel("Đang tải...");
    lblLoadingText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    lblLoadingText.setForeground(new Color(230, 240, 255));
    lblLoadingText.setVisible(false);
    loadingBox.add(pbLoading);
    loadingBox.add(lblLoadingText);
    footer.add(loadingBox, BorderLayout.CENTER);

    JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
    actions.setOpaque(false);

    btnRefresh = makePillButton("⟳ Refresh", new Color(255, 255, 255), new Color(46, 134, 193));
    btnRefresh.setActionCommand("refresh");

    btnExportPdf = makePillButton("⬇ Export PDF", new Color(255, 255, 255), new Color(39, 174, 96));
    btnExportPdf.setActionCommand("exportPdf");

    btnChangePassword = makePillButton("🔒 Đổi MK", new Color(255, 255, 255), new Color(243, 156, 18));
    btnChangePassword.setActionCommand("changePassword");

    btnLogout = makePillButton("↩ Logout", new Color(255, 255, 255), new Color(192, 57, 43));
    btnLogout.setActionCommand("logout");

    actions.add(btnRefresh);
    actions.add(btnExportPdf);
    actions.add(btnChangePassword);
    actions.add(btnLogout);

    footer.add(actions, BorderLayout.EAST);
    return footer;
}

    private void bindActions() {
    if (btnRefresh != null) btnRefresh.addActionListener(e -> refreshAll());
    if (btnExportPdf != null) btnExportPdf.addActionListener(e -> exportPdf());
    if (btnChangePassword != null) btnChangePassword.addActionListener(e -> changePassword());
    if (btnLogout != null) btnLogout.addActionListener(e -> {
        dispose();
        new login().setVisible(true);
    });

    if (cboFilter != null) cboFilter.addActionListener(e -> {
        updateFiltersAndSorting();
        updateStatus();
    });

    if (txtSearch != null) {
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                updateFiltersAndSorting();
                updateStatus();
            }
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
        });
    }
}


private void refreshAll() {
    lblStatus.setText("Đang tải dữ liệu...");
    setLoading(true);

    SwingWorker<FetchResult, Void> worker = new SwingWorker<>() {
        @Override
        protected FetchResult doInBackground() throws Exception {
            
            testDbConnection();

            FetchResult r = new FetchResult();

            
            r.student = StudentDAO.getStudentByUserId(userId)
                    .orElseThrow(() -> new IllegalStateException("Không tìm thấy sinh viên tương ứng với user_id=" + userId));

            
            r.user = UserDAO.getUserById(userId).orElse(null);

            
            r.scores = ScoreDAO.getScoresByStudent(r.student.getStudentId());

            java.util.Optional<Double> gpaOpt = ScoreDAO.calculateGpaByCredits(r.student.getStudentId());
            if (gpaOpt.isEmpty()) gpaOpt = ScoreDAO.calculateGpaSimple(r.student.getStudentId());
            r.gpa = gpaOpt.orElse(null);

            
            int tc = 0;
            for (Score s : r.scores) tc += Math.max(0, s.getCredit());
            r.totalCredits = tc;

            return r;
        }

        @Override
        protected void done() {
            try {
                FetchResult r = get();

                currentStudent = r.student;
                currentUser = r.user;
                cachedScores = r.scores != null ? r.scores : new java.util.ArrayList<>();

                
                lblName.setText("Họ tên: " + safe(currentStudent.getFullName()));
                lblCode.setText("Mã SV: " + safe(currentStudent.getStudentCode()));
                lblDob.setText("Ngày sinh: " + (currentStudent.getDob() == null ? "--" : currentStudent.getDob().toString()));

                if (lblHeaderTitle != null) {
                    lblHeaderTitle.setText(safe(currentStudent.getFullName()) + " – " + safe(currentStudent.getStudentCode()));
                }

                lblUsername.setText("Tài khoản: " + (currentUser == null ? "--" : safe(currentUser.getUsername())));
                lblGpa.setText(r.gpa == null ? "--" : df.format(r.gpa));
                if (lblTotalCredits != null) lblTotalCredits.setText("Tổng TC: " + r.totalCredits);

                // Table
                rebuildTableFromCache();
                updateFiltersAndSorting();
                updateTotals();
                updateStatus();

                lblStatus.setText("Đã đồng bộ dữ liệu thành công.");
            } catch (Exception ex) {
                lblStatus.setText("Có lỗi khi tải dữ liệu. Vui lòng kiểm tra kết nối DB.");
                JOptionPane.showMessageDialog(StudentPage.this,
                        "Không thể tải dữ liệu.\nHãy kiểm tra MySQL đang chạy và cấu hình DBConnection.java.\n\nChi tiết: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            } finally {
                setLoading(false);
            }
        }
    };

    worker.execute();
}

private static class FetchResult {
    Student student;
    User user;
    java.util.List<Score> scores;
    Double gpa;
    int totalCredits;
}

private void setLoading(boolean loading) {
    if (pbLoading != null) pbLoading.setVisible(loading);
    if (lblLoadingText != null) lblLoadingText.setVisible(loading);

    if (btnRefresh != null) btnRefresh.setEnabled(!loading);
    if (btnExportPdf != null) btnExportPdf.setEnabled(!loading);
    if (btnChangePassword != null) btnChangePassword.setEnabled(!loading);
    if (btnLogout != null) btnLogout.setEnabled(!loading);

    if (cboFilter != null) cboFilter.setEnabled(!loading);
    if (txtSearch != null) txtSearch.setEnabled(!loading);
}

private void testDbConnection() throws Exception {
    java.sql.Connection c = null;
    try {
        c = DBConnection.getConnection();
        java.sql.Statement st = c.createStatement();
        st.execute("SELECT 1");
        st.close();
    } finally {
        if (c != null) try { c.close(); } catch (Exception ignored) {}
    }
}

    private void loadStudent() {
        Optional<Student> stOpt = StudentDAO.getStudentByUserId(userId);
        if (stOpt.isEmpty()) {
            throw new IllegalStateException("Không tìm thấy sinh viên tương ứng với user_id=" + userId);
        }
        currentStudent = stOpt.get();

        lblName.setText("Họ tên: " + safe(currentStudent.getFullName()));
        lblCode.setText("Mã SV: " + safe(currentStudent.getStudentCode()));
        lblDob.setText("Ngày sinh: " + (currentStudent.getDob() == null ? "--" : currentStudent.getDob().toString()));
        
        if (lblHeaderTitle != null) {
            lblHeaderTitle.setText(safe(currentStudent.getFullName()) + " – " + safe(currentStudent.getStudentCode()));
        }
    }
private void loadUser() {
    currentUser = UserDAO.getUserById(userId).orElse(null);
    lblUsername.setText("Tài khoản: " + (currentUser == null ? "--" : safe(currentUser.getUsername())));
}


    private void loadScoresAndGpa() {
        if (currentStudent == null) return;

        Optional<Double> gpaOpt = ScoreDAO.calculateGpaByCredits(currentStudent.getStudentId());
        if (gpaOpt.isEmpty()) {
            gpaOpt = ScoreDAO.calculateGpaSimple(currentStudent.getStudentId());
        }
        lblGpa.setText(gpaOpt.map(df::format).orElse("--"));

        applyFilter();
    }

    
    
    
    
private String gradeTooltip(String letter) {
    switch (letter) {
        case "A": return "A: 8.5 – 10.0";
        case "B": return "B: 7.0 – 8.4";
        case "C": return "C: 5.0 – 6.9";
        case "D": return "D: 4.0 – 4.9";
        case "F": return "F: < 4.0";
        default: return null;
    }
}

private void configureTableColumns() {
    if (tblScores == null) return;
    tblScores.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    
    applyResponsiveColumnWidths(860);
}

private void applyResponsiveColumnWidths(int viewportWidth) {
    if (tblScores == null) return;
    if (viewportWidth <= 0) viewportWidth = 860;

    
    int w = Math.max(600, viewportWidth);

    
    int wStt = 48;
    int wTc  = 60;
    int wScore  = 110;
    int wLetter = 110;
    int wTeacher = 180;

    int fixed = wStt + wTc + wTeacher + wScore + wLetter;
    int wSubject = Math.max(240, w - fixed);

    
    int total = wStt + wTc + wSubject + wTeacher + wScore + wLetter;

    
    int diff = w - total;
    wLetter += diff;

    
    tblScores.getColumnModel().getColumn(0).setPreferredWidth(wStt);
    tblScores.getColumnModel().getColumn(1).setPreferredWidth(wTc);
    tblScores.getColumnModel().getColumn(2).setPreferredWidth(wSubject);
    tblScores.getColumnModel().getColumn(3).setPreferredWidth(wTeacher);
    tblScores.getColumnModel().getColumn(4).setPreferredWidth(wScore);
    tblScores.getColumnModel().getColumn(5).setPreferredWidth(wLetter);

    tblScores.doLayout();
}

private String toLetter(double score) {
        
        if (score >= 8.5) return "A";
        if (score >= 7.0) return "B";
        if (score >= 5.0) return "C";
        if (score >= 4.0) return "D";
        return "F";
    }

private void applyFilter() {
    if (currentStudent == null) return;

    cachedScores = ScoreDAO.getScoresByStudent(currentStudent.getStudentId());
    rebuildTableFromCache();
    updateFiltersAndSorting();
    updateTotals();
    updateStatus();
}

private void rebuildTableFromCache() {
    tableModel.setRowCount(0);
    int stt = 1;
    for (Score s : cachedScores) {
        String letter = toLetter(s.getScore());
        int credit = s.getCredit();

        tableModel.addRow(new Object[]{
                stt++,
                credit,
                safe(s.getSubjectName()),
                s.getTeacherName() == null ? "" : safe(s.getTeacherName()),
                df.format(s.getScore()),
                letter
        });
    }
}

private void updateTotals() {
    if (lblTotalCredits == null) return;
    int totalCredits = 0;
    for (Score s : cachedScores) totalCredits += Math.max(0, s.getCredit());
    lblTotalCredits.setText("Tổng TC: " + (totalCredits == 0 ? "--" : String.valueOf(totalCredits)));
}

private void updateFiltersAndSorting() {
    if (sorter == null) {
        sorter = new TableRowSorter<>(tableModel);

        
        sorter.setComparator(5, (o1, o2) -> {
            String a = o1 == null ? "" : o1.toString();
            String b = o2 == null ? "" : o2.toString();
            return Integer.compare(letterRank(a), letterRank(b));
        });

        
        sorter.setComparator(4, (o1, o2) -> {
            double a = parseDoubleSafe(o1);
            double b = parseDoubleSafe(o2);
            return Double.compare(a, b);
        });

        tblScores.setRowSorter(sorter);
    }

    String mode = (String) cboFilter.getSelectedItem();
    String q = (txtSearch == null) ? "" : txtSearch.getText().trim().toLowerCase();

    java.util.List<RowFilter<Object,Object>> filters = new java.util.ArrayList<>();

    
    if (mode != null && mode.matches("^[ABCDF]$")) {
        filters.add(new RowFilter<Object, Object>() {
            @Override
            public boolean include(Entry<?, ?> entry) {
                Object val = entry.getValue(5);
                return val != null && val.toString().equalsIgnoreCase(mode);
            }
        });
    }

    
    if (q != null && !q.isEmpty()) {
        filters.add(new RowFilter<Object, Object>() {
            @Override
            public boolean include(Entry<?, ?> entry) {
                String subject = entry.getValue(2) == null ? "" : entry.getValue(2).toString().toLowerCase();
                String teacher = entry.getValue(3) == null ? "" : entry.getValue(3).toString().toLowerCase();
                return subject.contains(q) || teacher.contains(q);
            }
        });
    }

    if (filters.isEmpty()) sorter.setRowFilter(null);
    else sorter.setRowFilter(RowFilter.andFilter(filters));


    if ("A-F".equals(mode)) {
        sorter.setSortKeys(java.util.List.of(new javax.swing.RowSorter.SortKey(5, javax.swing.SortOrder.ASCENDING)));
    } else if ("F-A".equals(mode)) {
        sorter.setSortKeys(java.util.List.of(new javax.swing.RowSorter.SortKey(5, javax.swing.SortOrder.DESCENDING)));
    } else {
        sorter.setSortKeys(null);
    }
}

private void updateStatus() {
    String mode = (String) cboFilter.getSelectedItem();
    String q = (txtSearch == null) ? "" : txtSearch.getText().trim();
    int total = tableModel.getRowCount();
    int visible = tblScores.getRowCount();

    String extra = (q == null || q.isBlank()) ? "" : (" • Tìm: " + q);
    lblStatus.setText("Hiển thị " + visible + "/" + total + " dòng • " + mode + extra);
}

private int letterRank(String letter) {
    if (letter == null) return 99;
    switch (letter.trim().toUpperCase()) {
        case "A": return 1;
        case "B": return 2;
        case "C": return 3;
        case "D": return 4;
        case "F": return 5;
        default: return 99;
    }
}

private double parseDoubleSafe(Object o) {
    try {
        if (o == null) return 0;
        return Double.parseDouble(o.toString().replace(",", ".").trim());
    } catch (Exception ex) {
        return 0;
    }
}

    private static String safe(String s) {
        return s == null ? "--" : s;
    }

    

    private static JButton makePillButton(String text, Color fg, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 16, 10, 16));
        b.putClientProperty("bg", bg);
        b.putClientProperty("bgHover", bg.darker());
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { b.putClientProperty("hover", true); b.repaint(); }
            @Override public void mouseExited(java.awt.event.MouseEvent e) { b.putClientProperty("hover", false); b.repaint(); }
        });
        b.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override public void paint(Graphics g, JComponent c) {
                JButton btn = (JButton) c;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean hover = Boolean.TRUE.equals(btn.getClientProperty("hover"));
                Color bgc = (Color) btn.getClientProperty(hover ? "bgHover" : "bg");
                g2.setColor(bgc);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 22, 22);
                g2.dispose();
                super.paint(g, c);
            }
        });
        return b;
    }

    private static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bg;

        public RoundedPanel(int radius, Color bg) {
            this.radius = radius;
            this.bg = bg;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class GradientPanel extends JPanel {
        public GradientPanel() {
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            Color c1 = new Color(52, 152, 219);
            Color c2 = new Color(142, 68, 173);
            GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2);
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

    private void changePassword() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
        JPasswordField oldPw = new JPasswordField();
        JPasswordField newPw = new JPasswordField();
        JPasswordField confirm = new JPasswordField();

        panel.add(new JLabel("Mật khẩu cũ:"));
        panel.add(oldPw);
        panel.add(new JLabel("Mật khẩu mới:"));
        panel.add(newPw);
        panel.add(new JLabel("Nhập lại mật khẩu mới:"));
        panel.add(confirm);

        int res = JOptionPane.showConfirmDialog(this, panel, "Đổi mật khẩu", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        String oldPass = new String(oldPw.getPassword());
        String newPass = new String(newPw.getPassword());
        String cf = new String(confirm.getPassword());

        if (newPass.length() < 6) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới tối thiểu 6 ký tự.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!newPass.equals(cf)) {
            JOptionPane.showMessageDialog(this, "Xác nhận mật khẩu không khớp.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean ok = UserDAO.changePassword(userId, oldPass, newPass);
        if (!ok) {
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thất bại. Kiểm tra lại mật khẩu cũ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!", "OK", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportPdf() {
        if (currentStudent == null) return;

        
        List<Score> scores = ScoreDAO.getScoresByStudent(currentStudent.getStudentId());
        List<String> lines = new ArrayList<>();
        lines.add("Ho ten: " + currentStudent.getFullName());
        lines.add("MSV: " + currentStudent.getStudentCode());
        lines.add("Username: " + (currentUser == null ? "--" : currentUser.getUsername()));
        lines.add("Ngay sinh: " + (currentStudent.getDob() == null ? "--" : currentStudent.getDob().toString()));
        lines.add("GPA: " + lblGpa.getText());
        lines.add("--------------------------------------------");
        lines.add(String.format("%-3s | %-3s | %-22s | %-16s | %-7s | %-6s", "STT", "TC", "Mon hoc", "Giao vien", "Diem", "Chu"));

        int stt = 1;
        for (Score s : scores) {
            String letter = toLetter(s.getScore());
            lines.add(String.format("%-3d | %-3d | %-22s | %-16s | %-7s | %-6s",
                    stt++,
                    s.getCredit(),
                    trimLen(s.getSubjectName(), 22),
                    trimLen(s.getTeacherName() == null ? "" : s.getTeacherName(), 16),
                    df.format(s.getScore()),
                    letter
            ));
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Lưu bảng điểm (PDF)");
        chooser.setSelectedFile(new java.io.File("bang_diem_" + currentStudent.getStudentCode() + ".pdf"));
        int ch = chooser.showSaveDialog(this);
        if (ch != JFileChooser.APPROVE_OPTION) return;

        String path = chooser.getSelectedFile().getAbsolutePath();
        if (!path.toLowerCase().endsWith(".pdf")) path += ".pdf";

        boolean ok = ReportUtil.exportScoresToPDF(path, "Bang diem sinh vien", lines);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Xuất PDF thành công:\n" + path, "OK", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Xuất PDF thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String trimLen(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, Math.max(0, max - 1)) + "…";
    }

    private void loadStudentData() {
        refreshAll();
    }
}