package site.pokemons.edpproject.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import site.pokemons.edpproject.service.UserService;

import java.io.IOException;

public class RegisterController {
    private Stage primaryStage;
    private final UserService userService;

    @FXML private TextField usernameText;
    @FXML private TextField passwdText;
    @FXML private TextField cfPasswdText;
    @FXML private CheckBox agreeCheck;
    @FXML private Label infoLabel;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void registerHandle(MouseEvent mouseEvent) {

        if(!agreeCheck.isSelected()) {
            infoLabel.setText("Zaznacz zgodę.");
            return;
        }

        if(!passwdText.getText().equals(cfPasswdText.getText())) {
            infoLabel.setText("Hasła nie są identyczne.");
            return;
        }

        boolean reg = userService.registerUser(usernameText.getText(), passwdText.getText());
        if(reg){
            infoLabel.setText("Pomyślnie zarejestrowano.");

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/site/pokemons/edpproject/view/login-view.fxml"));
                fxmlLoader.setControllerFactory(type -> {
                    if (type == LoginController.class) {
                        return new LoginController(userService);
                    } else {
                        try {
                            return type.getDeclaredConstructor().newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
                });
                Scene scene = new Scene(fxmlLoader.load());
                primaryStage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
                infoLabel.setText("Błąd przejścia do widoku logowania.");
            }

            return;
        }

        infoLabel.setText("Konto o takiej nazwie już istnieje.");
    }
}
