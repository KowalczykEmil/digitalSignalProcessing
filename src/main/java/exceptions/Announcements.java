package exceptions;

import javafx.scene.control.Alert;

public class Announcements {
    public static void messageBox(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
