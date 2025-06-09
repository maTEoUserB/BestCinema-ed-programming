package site.pokemons.edpproject.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import lombok.Setter;
import site.pokemons.edpproject.service.UserService;
import site.pokemons.edpproject.service.serviceSingleton.UserServiceSingleton;
import site.pokemons.edpproject.session.SessionContext;

import java.util.Map;

public class UserProfileController {
    @Setter
    private Scene scene;
    @Setter
    private Map<String, Parent> views;

    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private TextField nameField;
    @FXML private TextField surnameField;
    @FXML private TextField passwordField;
    @FXML private TextField newPasswordField;
    @FXML private TextField secondPasswordField;


    public void backToRepertoire(MouseEvent mouseEvent) {
        scene.setRoot(views.get("repertoire-view"));
    }

    public void logout(MouseEvent mouseEvent) {
        SessionContext.clear();
        showLoginPanel();
    }

    public void changeInformation(MouseEvent mouseEvent) {
        UserServiceSingleton.getInstance().changeProfileInformation(emailField.getText(), usernameField.getText(), nameField.getText(), surnameField.getText());
        showAlert("Zmieniono dane.", Alert.AlertType.INFORMATION);
        clearInformationFields();
    }


    public void changePassword(MouseEvent mouseEvent) {
        try {
            UserServiceSingleton.getInstance().changePassword(passwordField.getText(), newPasswordField.getText(), secondPasswordField.getText());
            showAlert("Zmieniono hasło.", Alert.AlertType.INFORMATION);
            clearPasswordFields();
        } catch (IllegalArgumentException e) {
            showAlert(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showLoginPanel() {
        scene.setRoot(views.get("login-view"));
    }

    private void clearInformationFields() {
        emailField.clear();
        usernameField.clear();
        nameField.clear();
        surnameField.clear();
    }

    private void clearPasswordFields(){
        passwordField.clear();
        newPasswordField.clear();
        secondPasswordField.clear();
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
