package model;

public class Subject {
    private int subjectId;
    private String subjectName;
    private int teacherId;
    private int credit;
    
    public Subject(int subjectId, String subjectName, int teacherId, int credit) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.teacherId = teacherId;
        this.credit = credit;
    }
    
    // Getters
    public int getSubjectId() {
        return subjectId;
    }
    
    public String getSubjectName() {
        return subjectName;
    }
    
    public int getTeacherId() {
        return teacherId;
    }
    
    public int getCredit() {
        return credit;
    }
    
    // Setters
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    
    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }
    
    public void setCredit(int credit) {
        this.credit = credit;
    }
    
    @Override
    public String toString() {
        return subjectId + " - " + subjectName;
    }
}
