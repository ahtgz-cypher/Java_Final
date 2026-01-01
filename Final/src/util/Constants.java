package util;

/**
 * Class chứa các hằng số sử dụng trong ứng dụng
 */
public class Constants {
    
    // ===== TEACHER PAGE CONSTANTS =====
    public static final String TEACHER_PAGE_TITLE_PREFIX = "Trang Giáo Viên - ";
    public static final String WINDOW_TITLE_SEARCH = "Tìm kiếm sinh viên";
    public static final String WINDOW_TITLE_STUDENT_LIST = "Danh sách sinh viên";
    public static final String WINDOW_TITLE_DETAIL = "Thông tin chi tiết";
    public static final String WINDOW_TITLE_ENTER_SCORE = "Nhập điểm";
    public static final String WINDOW_TITLE_REPORT = "Xuất báo cáo";
    
    // ===== MESSAGES =====
    public static final String MSG_NO_STUDENT_SELECTED = "Vui lòng chọn sinh viên!";
    public static final String MSG_NO_SUBJECT_SELECTED = "Vui lòng chọn môn học!";
    public static final String MSG_NO_SCORE_INPUT = "Vui lòng nhập điểm!";
    public static final String MSG_INVALID_SCORE_RANGE = "Điểm phải từ 0 đến 10!";
    public static final String MSG_INVALID_SCORE_FORMAT = "Điểm phải là số!";
    public static final String MSG_SCORE_SUCCESS = "Nhập điểm thành công!";
    public static final String MSG_SCORE_FAILED = "Nhập điểm thất bại!";
    public static final String MSG_NO_STUDENT_FOUND = "Không tìm thấy sinh viên nào!";
    public static final String MSG_NO_DATA_TO_EXPORT = "Không có dữ liệu để xuất!";
    public static final String MSG_NO_SCORE_DATA = "Không có dữ liệu điểm cho môn này!";
    public static final String MSG_STUDENT_NO_SCORE = "Sinh viên này chưa có điểm!";
    public static final String MSG_EXPORT_SUCCESS = "Xuất báo cáo thành công!";
    public static final String MSG_EXPORT_FAILED = "Xuất báo cáo thất bại!";
    public static final String MSG_KEYWORD_REQUIRED = "Vui lòng nhập từ khóa tìm kiếm!";
    public static final String MSG_KEYWORD_TOO_LONG = "Từ khóa tìm kiếm quá dài (tối đa 100 ký tự)!";
    public static final String MSG_INVALID_SUBJECT = "Môn học không hợp lệ!";
    
    // ===== DIALOG TITLES =====
    public static final String TITLE_SUCCESS = "Thành công";
    public static final String TITLE_ERROR = "Lỗi";
    public static final String TITLE_WARNING = "Thông báo";
    public static final String TITLE_INFO = "Thông tin";
    
    // ===== SCORE CLASSIFICATION =====
    public static final String CLASSIFICATION_EXCELLENT = "Xuất sắc";
    public static final String CLASSIFICATION_GOOD = "Giỏi";
    public static final String CLASSIFICATION_FAIR = "Khá";
    public static final String CLASSIFICATION_AVERAGE = "Trung bình";
    public static final String CLASSIFICATION_WEAK = "Yếu";
    public static final String CLASSIFICATION_POOR = "Kém";
    
    // ===== SCORE RANGES =====
    public static final double SCORE_MIN = 0.0;
    public static final double SCORE_MAX = 10.0;
    public static final double SCORE_EXCELLENT_MIN = 9.0;
    public static final double SCORE_GOOD_MIN = 8.0;
    public static final double SCORE_FAIR_MIN = 7.0;
    public static final double SCORE_AVERAGE_MIN = 5.5;
    public static final double SCORE_WEAK_MIN = 4.0;
    
    // ===== VALIDATION =====
    public static final int MAX_KEYWORD_LENGTH = 100;
    public static final int MAX_NAME_LENGTH = 100;
    
    // ===== FILE EXPORT =====
    public static final String REPORT_PREFIX_ALL = "BaoCaoDiem_TatCa";
    public static final String REPORT_PREFIX_SUBJECT = "BaoCaoDiem_";
    public static final String REPORT_FILE_EXTENSION = ".csv";
    public static final String REPORT_DATE_FORMAT = "yyyyMMdd_HHmmss";
    public static final String REPORT_DISPLAY_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
}
