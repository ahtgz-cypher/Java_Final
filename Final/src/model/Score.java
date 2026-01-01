package model;

public class Score {
    private int scoreId;
    private int studentId;
    private int subjectId;
    private double score;
    
    // Thông tin mở rộng để hiển thị
    private String studentName;
    private String studentCode;
    private String subjectName;
    
    public Score(int scoreId, int studentId, int subjectId, double score) {
        this.scoreId = scoreId;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.score = score;
    }
    
    public Score(int scoreId, int studentId, int subjectId, double score, 
                 String studentName, String studentCode, String subjectName) {
        this.scoreId = scoreId;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.score = score;
        this.studentName = studentName;
        this.studentCode = studentCode;
        this.subjectName = subjectName;
    }
    
    // Getters
    public int getScoreId() {
        return scoreId;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public int getSubjectId() {
        return subjectId;
    }
    
    public double getScore() {
        return score;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public String getStudentCode() {
        return studentCode;
    }
    
    public String getSubjectName() {
        return subjectName;
    }
    
    // Setters
    public void setScore(double score) {
        this.score = score;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }
    
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    
    @Override
    public String toString() {
        return "Score{" +
                "scoreId=" + scoreId +
                ", studentId=" + studentId +
                ", subjectId=" + subjectId +
                ", score=" + score +
                ", studentName='" + studentName + '\'' +
                ", studentCode='" + studentCode + '\'' +
                ", subjectName='" + subjectName + '\'' +
                '}';
    }
}
