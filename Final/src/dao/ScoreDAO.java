package dao;

import config.DBConnection;
import model.Score;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ScoreDAO {
    
    // ===== NHẬP ĐIỂM CHO SINH VIÊN =====
    public static boolean addOrUpdateScore(int studentId, int subjectId, double score) {
        // Kiểm tra xem điểm đã tồn tại chưa
        String checkSql = "SELECT score_id FROM scores WHERE student_id = ? AND subject_id = ?";
        String insertSql = "INSERT INTO scores (student_id, subject_id, score) VALUES (?, ?, ?)";
        String updateSql = "UPDATE scores SET score = ? WHERE student_id = ? AND subject_id = ?";
        
        try (Connection conn = DBConnection.getConnection()) {
            // Kiểm tra điểm đã tồn tại
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, studentId);
                checkStmt.setInt(2, subjectId);
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next()) {
                    // Cập nhật điểm
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setDouble(1, score);
                        updateStmt.setInt(2, studentId);
                        updateStmt.setInt(3, subjectId);
                        return updateStmt.executeUpdate() > 0;
                    }
                } else {
                    // Thêm điểm mới
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, studentId);
                        insertStmt.setInt(2, subjectId);
                        insertStmt.setDouble(3, score);
                        return insertStmt.executeUpdate() > 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Lỗi nhập điểm: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ===== LẤY ĐIỂM CỦA MỘT SINH VIÊN TRONG MỘT MÔN =====
    public static Optional<Score> getScore(int studentId, int subjectId) {
        String sql = "SELECT sc.*, st.full_name, st.student_code, su.subject_name, su.credit, t.full_name AS teacher_name " +
                     "FROM scores sc " +
                     "JOIN students st ON sc.student_id = st.student_id " +
                     "JOIN subjects su ON sc.subject_id = su.subject_id " +
                     "LEFT JOIN teachers t ON su.teacher_id = t.teacher_id " +
                     "WHERE sc.student_id = ? AND sc.subject_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, subjectId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Score score = new Score(
                    rs.getInt("score_id"),
                    rs.getInt("student_id"),
                    rs.getInt("subject_id"),
                    rs.getDouble("score"),
                    rs.getString("full_name"),
                    rs.getString("student_code"),
                    rs.getString("subject_name"),
                    rs.getString("teacher_name")
                );
                score.setCredit(rs.getInt("credit"));
                return Optional.of(score);
            }
        } catch (SQLException e) {
            System.err.println("✗ Lỗi lấy điểm: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }
    
    // ===== LẤY TẤT CẢ ĐIỂM CỦA MỘT SINH VIÊN =====
    public static List<Score> getScoresByStudent(int studentId) {
        List<Score> scores = new ArrayList<>();
        String sql = "SELECT sc.*, st.full_name, st.student_code, su.subject_name, su.credit, t.full_name AS teacher_name " +
                     "FROM scores sc " +
                     "JOIN students st ON sc.student_id = st.student_id " +
                     "JOIN subjects su ON sc.subject_id = su.subject_id " +
                     "LEFT JOIN teachers t ON su.teacher_id = t.teacher_id " +
                     "WHERE sc.student_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Score score = new Score(
                    rs.getInt("score_id"),
                    rs.getInt("student_id"),
                    rs.getInt("subject_id"),
                    rs.getDouble("score"),
                    rs.getString("full_name"),
                    rs.getString("student_code"),
                    rs.getString("subject_name"),
                    rs.getString("teacher_name")
                );
                score.setCredit(rs.getInt("credit"));
                scores.add(score);
            }
        } catch (Exception e) {
            System.out.println("✗ Lỗi lấy danh sách điểm: " + e.getMessage());
            e.printStackTrace();
        }
        return scores;
    }
    
    // ===== LẤY TẤT CẢ ĐIỂM CỦA MỘT MÔN HỌC =====
    public static List<Score> getScoresBySubject(int subjectId) {
        List<Score> scores = new ArrayList<>();
        String sql = "SELECT sc.*, st.full_name, st.student_code, su.subject_name, su.credit, t.full_name AS teacher_name " +
                     "FROM scores sc " +
                     "JOIN students st ON sc.student_id = st.student_id " +
                     "JOIN subjects su ON sc.subject_id = su.subject_id " +
                     "LEFT JOIN teachers t ON su.teacher_id = t.teacher_id " +
                     "WHERE sc.subject_id = ? " +
                     "ORDER BY st.student_code";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, subjectId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Score score = new Score(
                    rs.getInt("score_id"),
                    rs.getInt("student_id"),
                    rs.getInt("subject_id"),
                    rs.getDouble("score"),
                    rs.getString("full_name"),
                    rs.getString("student_code"),
                    rs.getString("subject_name"),
                    rs.getString("teacher_name")
                );
                score.setCredit(rs.getInt("credit"));
                scores.add(score);
            }
        } catch (Exception e) {
            System.out.println("✗ Lỗi lấy danh sách điểm: " + e.getMessage());
            e.printStackTrace();
        }
        return scores;
    }
    
    // ===== LẤY TẤT CẢ ĐIỂM =====
    public static List<Score> getAllScores() {
        List<Score> scores = new ArrayList<>();
        String sql = "SELECT sc.*, st.full_name, st.student_code, su.subject_name, su.credit, t.full_name AS teacher_name " +
                     "FROM scores sc " +
                     "JOIN students st ON sc.student_id = st.student_id " +
                     "JOIN subjects su ON sc.subject_id = su.subject_id " +
                     "LEFT JOIN teachers t ON su.teacher_id = t.teacher_id " +
                     "ORDER BY st.student_code, su.subject_name";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Score score = new Score(
                    rs.getInt("score_id"),
                    rs.getInt("student_id"),
                    rs.getInt("subject_id"),
                    rs.getDouble("score"),
                    rs.getString("full_name"),
                    rs.getString("student_code"),
                    rs.getString("subject_name"),
                    rs.getString("teacher_name")
                );
                score.setCredit(rs.getInt("credit"));
                scores.add(score);
            }
        } catch (SQLException e) {
            System.err.println("✗ Lỗi lấy danh sách điểm: " + e.getMessage());
            e.printStackTrace();
        }
        return scores;
    }
    
    // ===== XÓA ĐIỂM =====
    public static boolean deleteScore(int studentId, int subjectId) {
        String sql = "DELETE FROM scores WHERE student_id = ? AND subject_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, subjectId);
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("✗ Lỗi xóa điểm: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

//GPA
      public static Optional<Double> calculateGpaByCredits(int studentId) {
        String sql = "SELECT SUM(sc.score * su.credit) / SUM(su.credit) AS gpa " +
                    "FROM scores sc " +
                    "JOIN subjects su ON sc.subject_id = su.subject_id " +
                    "LEFT JOIN teachers t ON su.teacher_id = t.teacher_id " +
                    "WHERE sc.student_id = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double gpa = rs.getDouble("gpa");
                if (rs.wasNull()) return Optional.empty();
                return Optional.of(gpa);
            }
        } catch (SQLException e) {
            System.err.println("✗ Lỗi tính GPA theo tín chỉ: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

// tổng tc
     public static int getTotalCredits(int studentId) {
        String sql = "SELECT COALESCE(SUM(su.credit),0) AS total_credit " +
                     "FROM scores sc " +
                     "JOIN subjects su ON sc.subject_id = su.subject_id " +
                     "WHERE sc.student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("total_credit");

        } catch (SQLException e) {
            System.err.println("✗ Lỗi tính tổng tín chỉ: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    
    
}
