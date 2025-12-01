package org.storemate.pos.utils;

import javafx.scene.control.Alert;


/**
 * Utility class for displaying standard JavaFX alert dialogs.
 * <p>
 * Provides convenience methods to show success (confirmation) and error alerts.
 * Can also store alert properties (title, message, type) for potential future use.
 * @author Jeet
 */
public class Alerts {
    private String alertTitle;
    private String alertMessage;
    private Alert.AlertType alertType;

    public  Alerts(String alertTitle, String alertMessage, Alert.AlertType alertType) {
        this.alertTitle = alertTitle;
        this.alertMessage = alertMessage;
        this.alertType = alertType;
    }

    public Alerts() {
    }

    public String getAlertTitle() {
        return alertTitle;
    }

    public void setAlertTitle(String alertTitle) {
        this.alertTitle = alertTitle;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public Alert.AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(Alert.AlertType alertType) {
        this.alertType = alertType;
    }

    /**
     * Shows a confirmation alert with the given title and message.
     *
     * @param title   the alert dialog title
     * @param message the content message to display
     */
    public void showSuccessAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows an error alert with the given title and message.
     *
     * @param title   the alert dialog title
     * @param message the content message to display
     */
    public void showErrorAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
