package site.pokemons.edpproject.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import lombok.Setter;
import site.pokemons.edpproject.service.UserService;
import site.pokemons.edpproject.session.SessionContext;

import java.util.Map;

public class LoginController {
    @Setter
    private Scene scene;
    private final UserService userService;
    @Setter
    private Map<String, Parent> views;

    @FXML private TextField usernameText;
    @FXML private TextField passwdText;
    @FXML private Label infoLabel;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @FXML
    public void loginHandle(MouseEvent mouseEvent) {
        boolean log = userService.loginUser(usernameText.getText(), passwdText.getText());
        clearLoginPage();

        if(log){
            infoLabel.setText("Pomyslnie zalogowano.");
            showAlert("Pomyślnie zalogowano.", Alert.AlertType.INFORMATION);

            if(userService.getAccountById(SessionContext.getLoggedInUserId()).getRole().equals("USER")){
                showUserPanel();
            }else if(userService.getAccountById(SessionContext.getLoggedInUserId()).getRole().equals("ADMIN")){
                showAdminPanel();
            }

            return;
        }

        infoLabel.setText("Błdne dane logowania.");
        showAlert("Błdne dane logowania.", Alert.AlertType.ERROR);
    }

    private void clearLoginPage() {
        usernameText.clear();
        passwdText.clear();
        infoLabel.setText("");
    }

    private void showUserPanel() {
        scene.setRoot(views.get("repertoire-view"));
    }

    private void showAdminPanel() {
        scene.setRoot(views.get("admin-view"));
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void registerLoadHandle(MouseEvent mouseEvent) {
        scene.setRoot(views.get("register-view"));
    }
}