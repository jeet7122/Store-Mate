package org.storemate.pos.utils;

import javafx.scene.control.Alert;

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

    public void showSuccessAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(alertTitle);
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }
    public void showErrorAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(alertTitle);
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }
}
