package model;

import java.sql.Date;

public class Student {
    private int studentId;
    private int userId;
    private String fullName;
    private String studentCode;
    private Date dob;
    
    public Student(int studentId, int userId, String fullName, String studentCode, Date dob) {
        this.studentId = studentId;
        this.userId = userId;
        this.fullName = fullName;
        this.studentCode = studentCode;
        this.dob = dob;
    }
    
    // Getters
    public int getStudentId() {
        return studentId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public String getStudentCode() {
        return studentCode;
    }
    
    public Date getDob() {
        return dob;
    }
    
    // Setters
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }
    
    public void setDob(Date dob) {
        this.dob = dob;
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", fullName='" + fullName + '\'' +
                ", studentCode='" + studentCode + '\'' +
                ", dob=" + dob +
                '}';
    }
}
