package util;

import model.Score;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportUtil {
    
    // ===== XUẤT BÁO CÁO ĐIỂM RA FILE CSV =====
    public static boolean exportScoresToCSV(List<Score> scores, String filePath) {
        try (FileWriter writer = new FileWriter(filePath, java.nio.charset.StandardCharsets.UTF_8)) {
            // Ghi BOM cho UTF-8 để Excel đọc đúng tiếng Việt
            writer.write("\uFEFF");
            
            // Ghi header
            writer.append("STT,Mã sinh viên,Tên sinh viên,Môn học,Điểm\n");
            
            // Ghi dữ liệu với escape CSV
            int stt = 1;
            for (Score score : scores) {
                writer.append(String.valueOf(stt++))
                      .append(',')
                      .append(escapeCSV(score.getStudentCode()))
                      .append(',')
                      .append(escapeCSV(score.getStudentName()))
                      .append(',')
                      .append(escapeCSV(score.getSubjectName()))
                      .append(',')
                      .append(String.valueOf(score.getScore()))
                      .append('\n');
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("✗ Lỗi xuất file CSV: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ===== TẠO TÊN FILE BÁO CÁO TỰ ĐỘNG =====
    public static String generateReportFileName(String prefix) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.REPORT_DATE_FORMAT);
        String timestamp = sdf.format(new Date());
        return prefix + "_" + timestamp + Constants.REPORT_FILE_EXTENSION;
    }
    
    // ===== XUẤT BÁO CÁO ĐIỂM THEO MÔN HỌC =====
    public static boolean exportScoresBySubject(List<Score> scores, String subjectName, String directory) {
        String fileName = generateReportFileName(Constants.REPORT_PREFIX_SUBJECT + subjectName.replaceAll(" ", "_"));
        String fullPath = directory + File.separator + fileName;
        
        try (FileWriter writer = new FileWriter(fullPath, java.nio.charset.StandardCharsets.UTF_8)) {
            // Ghi BOM cho UTF-8
            writer.write("\uFEFF");
            
            // Ghi header
            writer.append("BÁO CÁO ĐIỂM MÔN HỌC: " + escapeCSV(subjectName) + "\n");
            writer.append("Ngày xuất: " + new SimpleDateFormat(Constants.REPORT_DISPLAY_DATE_FORMAT).format(new Date()) + "\n\n");
            writer.append("STT,Mã sinh viên,Tên sinh viên,Điểm,Xếp loại\n");
            
            // Ghi dữ liệu với escape
            int stt = 1;
            for (Score score : scores) {
                String classification = getClassification(score.getScore());
                writer.append(String.valueOf(stt++))
                      .append(',')
                      .append(escapeCSV(score.getStudentCode()))
                      .append(',')
                      .append(escapeCSV(score.getStudentName()))
                      .append(',')
                      .append(String.valueOf(score.getScore()))
                      .append(',')
                      .append(escapeCSV(classification))
                      .append('\n');
            }
            
            // Thống kê
            writer.append("\n=== THỐNG KÊ ===\n");
            writer.append("Tổng số sinh viên: " + scores.size() + "\n");
            writer.append("Điểm trung bình: " + String.format("%.2f", calculateAverage(scores)) + "\n");
            writer.append("Điểm cao nhất: " + String.format("%.2f", getMaxScore(scores)) + "\n");
            writer.append("Điểm thấp nhất: " + String.format("%.2f", getMinScore(scores)) + "\n");
            
            return true;
        } catch (IOException e) {
            System.err.println("✗ Lỗi xuất file báo cáo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ===== ESCAPE CSV TO PREVENT INJECTION =====
    private static String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        // Escape nếu có dấu phẩy, nháy kép, hoặc xuống dòng
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    
    // ===== XẾP LOẠI ĐIỂM =====
    public static String getClassification(double score) {
        if (score >= Constants.SCORE_EXCELLENT_MIN) return Constants.CLASSIFICATION_EXCELLENT;
        if (score >= Constants.SCORE_GOOD_MIN) return Constants.CLASSIFICATION_GOOD;
        if (score >= Constants.SCORE_FAIR_MIN) return Constants.CLASSIFICATION_FAIR;
        if (score >= Constants.SCORE_AVERAGE_MIN) return Constants.CLASSIFICATION_AVERAGE;
        if (score >= Constants.SCORE_WEAK_MIN) return Constants.CLASSIFICATION_WEAK;
        return Constants.CLASSIFICATION_POOR;
    }
    
    // ===== TÍNH ĐIỂM TRUNG BÌNH =====
    private static double calculateAverage(List<Score> scores) {
        if (scores.isEmpty()) return 0.0;
        double sum = 0;
        for (Score score : scores) {
            sum += score.getScore();
        }
        return sum / scores.size();
    }
    
    // ===== LẤY ĐIỂM CAO NHẤT =====
    private static double getMaxScore(List<Score> scores) {
        if (scores.isEmpty()) return 0.0;
        double max = scores.get(0).getScore();
        for (Score score : scores) {
            if (score.getScore() > max) {
                max = score.getScore();
            }
        }
        return max;
    }
    
    // ===== LẤY ĐIỂM THẤP NHẤT =====
    private static double getMinScore(List<Score> scores) {
        if (scores.isEmpty()) return 0.0;
        double min = scores.get(0).getScore();
        for (Score score : scores) {
            if (score.getScore() < min) {
                min = score.getScore();
            }
        }
        return min;
    }


    //PDF
    public static boolean exportScoresToPDF(String filePath, String title, List<String> lines) {
    try {
        StringBuilder content = new StringBuilder();
        
        content.append("BT\n");
        content.append("/F1 12 Tf\n");
        int y = 760;
        content.append(String.format("1 0 0 1 50 %d Tm\n", y));
        content.append(escapePdfText(title)).append(" Tj\n");
        content.append("T*\n");
        for (String line : lines) {
            if (line == null) line = "";
            
            content.append(String.format("1 0 0 1 50 %d Tm\n", y -= 14));
            content.append(escapePdfText(line)).append(" Tj\n");
            if (y < 60) break; 
        }
        content.append("ET\n");

        byte[] streamBytes = content.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
        String header = "%PDF-1.4\n";
        String obj1 = "1 0 obj<< /Type /Catalog /Pages 2 0 R >>endobj\n";
        String obj2 = "2 0 obj<< /Type /Pages /Kids [3 0 R] /Count 1 >>endobj\n";
        String obj3 = "3 0 obj<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] "
                + "/Resources<< /Font<< /F1 4 0 R >> >> /Contents 5 0 R >>endobj\n";
        String obj4 = "4 0 obj<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>endobj\n";
        String obj5 = "5 0 obj<< /Length " + streamBytes.length + " >>stream\n";

        
        java.util.List<byte[]> parts = new java.util.ArrayList<>();
        parts.add(header.getBytes(java.nio.charset.StandardCharsets.US_ASCII));
        parts.add(obj1.getBytes(java.nio.charset.StandardCharsets.US_ASCII));
        parts.add(obj2.getBytes(java.nio.charset.StandardCharsets.US_ASCII));
        parts.add(obj3.getBytes(java.nio.charset.StandardCharsets.US_ASCII));
        parts.add(obj4.getBytes(java.nio.charset.StandardCharsets.US_ASCII));
        parts.add(obj5.getBytes(java.nio.charset.StandardCharsets.US_ASCII));
        parts.add(streamBytes);
        parts.add("\nendstream\nendobj\n".getBytes(java.nio.charset.StandardCharsets.US_ASCII));

        int offset = 0;
        java.util.List<Integer> xref = new java.util.ArrayList<>();
        xref.add(0); 
        for (int i = 1; i <= 5; i++) xref.add(0);

        
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        baos.write(parts.get(0)); offset += parts.get(0).length;

        
        for (int obj = 1; obj <= 4; obj++) {
            xref.set(obj, offset);
            byte[] b = parts.get(obj);
            baos.write(b); offset += b.length;
        }
        
        xref.set(5, offset);
        baos.write(parts.get(5)); offset += parts.get(5).length;
        baos.write(parts.get(6)); offset += parts.get(6).length;
        baos.write(parts.get(7)); offset += parts.get(7).length;

        int xrefStart = offset;
        StringBuilder xrefSb = new StringBuilder();
        xrefSb.append("xref\n0 6\n");
        xrefSb.append(String.format("%010d 65535 f \n", 0));
        for (int i = 1; i <= 5; i++) {
            xrefSb.append(String.format("%010d 00000 n \n", xref.get(i)));
        }
        String trailer = "trailer<< /Size 6 /Root 1 0 R >>\nstartxref\n" + xrefStart + "\n%%EOF\n";

        baos.write(xrefSb.toString().getBytes(java.nio.charset.StandardCharsets.US_ASCII));
        baos.write(trailer.getBytes(java.nio.charset.StandardCharsets.US_ASCII));

        java.nio.file.Files.write(java.nio.file.Paths.get(filePath), baos.toByteArray());
        return true;
    } catch (Exception e) {
        System.out.println("✗ Lỗi xuất PDF: " + e.getMessage());
        return false;
    }
}

private static String escapePdfText(String s) {
    if (s == null) return "()";
    
    s = s.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
    return "(" + s + ")";
}

    
}
