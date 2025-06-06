package site.pokemons.edpproject.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Setter;
import site.pokemons.edpproject.service.UserService;

import java.util.Map;

public class RegisterController {
    @Setter
    private Stage primaryStage;
    private final UserService userService;
    private final Map<String, Parent> views;

    @FXML
    private TextField usernameText;
    @FXML
    private TextField passwdText;
    @FXML
    private TextField cfPasswdText;
    @FXML
    private CheckBox agreeCheck;
    @FXML
    private Label infoLabel;

    public RegisterController(UserService userService, Map<String, Parent> views) {
        this.userService = userService;
        this.views = views;
    }

    @FXML
    public void registerHandle(MouseEvent mouseEvent) {

        if (!agreeCheck.isSelected()) {
            infoLabel.setText("Zaznacz zgodę.");
            return;
        }

        if (!passwdText.getText().equals(cfPasswdText.getText())) {
            infoLabel.setText("Hasła nie są identyczne.");
            return;
        }

        boolean reg = userService.registerUser(usernameText.getText(), passwdText.getText());
        if (reg) {
            infoLabel.setText("Pomyślnie zarejestrowano.");

            Scene scene = new Scene(views.get("login-view"));
            primaryStage.setScene(scene);

            return;
        }

        infoLabel.setText("Konto o takiej nazwie już istnieje.");
    }

    @FXML
    public void loginLoadHandle(MouseEvent mouseEvent) {
        Scene scene = new Scene(views.get("login-view"));
        primaryStage.setScene(scene);
    }
}
