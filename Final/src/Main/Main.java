package Main;

import ui.login; // Import đúng class login
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new login(); // Khởi tạo class login
        });
    }
}