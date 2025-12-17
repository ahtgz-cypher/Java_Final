import config.DBConnection;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Kết nối MySQL thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
