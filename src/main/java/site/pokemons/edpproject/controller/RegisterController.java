package site.pokemons.edpproject.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import lombok.Setter;
import site.pokemons.edpproject.service.UserService;
import site.pokemons.edpproject.service.serviceSingleton.UserServiceSingleton;

import java.util.Map;

public class RegisterController {
    @Setter
    private Scene scene;
    @Setter
    private Map<String, Parent> views;

    @FXML
    private TextField usernameText;
    @FXML
    private TextField passwdText;
    @FXML
    private TextField emailText;
    @FXML
    private TextField nameText;
    @FXML
    private TextField surnameText;
    @FXML
    private TextField cfPasswdText;
    @FXML
    private CheckBox agreeCheck;
    @FXML
    private Label infoLabel;

    @FXML
    public void registerHandle(MouseEvent mouseEvent) {

        if (!agreeCheck.isSelected()) {
            infoLabel.setText("Zaznacz zgodę.");
            showAlert("Zaznacz zgodę.", Alert.AlertType.ERROR);
            return;
        }

        if (!passwdText.getText().equals(cfPasswdText.getText())) {
            infoLabel.setText("Hasła nie są identyczne.");
            showAlert("Hasła nie są identyczne.", Alert.AlertType.ERROR);
            return;
        }

        boolean reg = UserServiceSingleton.getInstance().registerUser(usernameText.getText(), passwdText.getText(), emailText.getText(), nameText.getText(), surnameText.getText());
        if (reg) {
            infoLabel.setText("Pomyślnie zarejestrowano.");
            showAlert("Pomyślnie zarejestrowano.", Alert.AlertType.INFORMATION);
            clearRegisterPage();

            scene.setRoot(views.get("repertoire-view"));

            return;
        }

        infoLabel.setText("Konto o takiej nazwie już istnieje.");
        showAlert("Konto o takiej nazwie już istnieje.", Alert.AlertType.ERROR);
    }

    @FXML
    public void loginLoadHandle(MouseEvent mouseEvent) {
        scene.setRoot(views.get("login-view"));
    }

    private void clearRegisterPage() {
        usernameText.clear();
        passwdText.clear();
        emailText.clear();
        nameText.clear();
        surnameText.clear();
        cfPasswdText.clear();
        agreeCheck.setSelected(false);
        infoLabel.setText("");
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
