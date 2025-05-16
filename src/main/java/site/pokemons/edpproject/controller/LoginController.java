package site.pokemons.edpproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import site.pokemons.edpproject.service.UserService;

public class LoginController {
    private final UserService userService;

    @FXML private TextField usernameText;
    @FXML private TextField passwdText;
    @FXML private Label infoLabel;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @FXML
    public void loginHandle(MouseEvent mouseEvent) {
        boolean log = userService.loginUser(usernameText.getText(), passwdText.getText());

        if(log){
            infoLabel.setText("Pomyslnie zalogowano.");
            return;
        }

        infoLabel.setText("Błdne dane logowania.");
    }
}