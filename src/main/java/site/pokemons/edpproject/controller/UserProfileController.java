package site.pokemons.edpproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import site.pokemons.edpproject.service.UserService;
import site.pokemons.edpproject.session.SessionContext;
import site.pokemons.edpproject.validator.InputValidator;


public class UserProfileController {
    @FXML
    private TextField emailField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField newPasswordField;
    @FXML
    private TextField secondPasswordField;
    @FXML
    private Button backButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button changePasswordButton;
    @FXML
    private Button changeButton;

    @FXML
    public void initialize() {
        backButton.setOnAction(event -> backToRepertoire());
        logoutButton.setOnAction(event -> logout());
        changePasswordButton.setOnAction(event -> changePassword());
        changeButton.setOnAction(event -> changeInformation());
    }

    public void backToRepertoire() {
        if (SessionContext.getLoggedInUserRole().equals("USER")) {
            ViewManager.getInstance().switchTo("repertoire-view");
//            scene.setRoot(views.get("repertoire-view"));
        } else {
            ViewManager.getInstance().switchTo("admin-view");
//            scene.setRoot(views.get("admin-view"));
        }
    }

    public void logout() {
        SessionContext.clear();
        showLoginPanel();
    }

    public void changeInformation() {
        InputValidator inputValidator = InputValidator.getInstance();
        if (!emailField.getText().isEmpty() && !inputValidator.isValidEmail(emailField.getText())) {
            showAlert("Błędny format email.", Alert.AlertType.WARNING);
            return;
        }

        if (!usernameField.getText().isEmpty() && !inputValidator.isValidUsername(usernameField.getText())) {
            showAlert("Nazwa użytkownika musi zawierać co najmniej 8 znaków, w tym jedną cyfrę.", Alert.AlertType.WARNING);
            return;
        }

        UserService.getInstance().changeProfileInformation(emailField.getText(), usernameField.getText(), nameField.getText(), surnameField.getText());
        showAlert("Zmieniono dane.", Alert.AlertType.INFORMATION);
        clearInformationFields();
    }


    public void changePassword() {
        if (passwordField.getText().isEmpty() || newPasswordField.getText().isEmpty() || secondPasswordField.getText().isEmpty()) {
            showAlert("Podaj hasło.", Alert.AlertType.WARNING);
            return;
        }
        if (!newPasswordField.getText().equals(secondPasswordField.getText())) {
            showAlert("Hasła nie są identyczne.", Alert.AlertType.WARNING);
            return;
        }

        InputValidator inputValidator = InputValidator.getInstance();
        if (!inputValidator.isValidPassword(newPasswordField.getText())) {
            showAlert("Hasło musi zawierać co najmniej 8 znaków, w tym małą i dużą literę, cyfrę oraz znak specjalny.", Alert.AlertType.WARNING);
            return;
        }

        try {
            UserService.getInstance().changePassword(passwordField.getText(), newPasswordField.getText(), secondPasswordField.getText());
            showAlert("Zmieniono hasło.", Alert.AlertType.INFORMATION);
            clearPasswordFields();
        } catch (IllegalArgumentException e) {
            showAlert(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showLoginPanel() {
        ViewManager.getInstance().switchTo("login-view");
//        scene.setRoot(views.get("login-view"));
    }

    private void clearInformationFields() {
        emailField.clear();
        usernameField.clear();
        nameField.clear();
        surnameField.clear();
    }

    private void clearPasswordFields() {
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
