import javax.swing.SwingUtilities;
import vista.LoginVentana;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginVentana().setVisible(true);
        });
    }
}   