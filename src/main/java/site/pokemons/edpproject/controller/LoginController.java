package site.pokemons.edpproject.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Setter;
import site.pokemons.edpproject.service.UserService;
import site.pokemons.edpproject.session.SessionContext;

import java.io.IOException;
import java.util.Map;

public class LoginController {
    @Setter
    private Stage primaryStage;
    private final UserService userService;
    private final Map<String, Parent> views;

    @FXML private TextField usernameText;
    @FXML private TextField passwdText;
    @FXML private Label infoLabel;

    public LoginController(UserService userService, Map<String, Parent> views) {
        this.userService = userService;
        this.views = views;
    }

    @FXML
    public void loginHandle(MouseEvent mouseEvent) {
        boolean log = userService.loginUser(usernameText.getText(), passwdText.getText());

        System.out.println("===============================loginHandle() - log: " + log + "===============================");
        if(log){
            infoLabel.setText("Pomyslnie zalogowano.");

            if(userService.getAccountById(SessionContext.getLoggedInUserId()).getRole().equals("USER")){
                System.out.println("===============================loginHandle() if USER===============================");
                showUserPanel();
            }else if(userService.getAccountById(SessionContext.getLoggedInUserId()).getRole().equals("ADMIN")){
                System.out.println("===============================loginHandle() if ADMIN===============================");
                showAdminPanel();
            }

            return;
        }

        infoLabel.setText("Błdne dane logowania.");
    }

    private void showUserPanel() {
        System.out.println("===============================USER PANEL===============================");
        Scene scene = new Scene(views.get("repertoire-view"));
        primaryStage.setScene(scene);
    }

    private void showAdminPanel() {
        System.out.println("===============================ADMIN PANEL===============================");
        Scene scene = new Scene(views.get("admin-view"));
        primaryStage.setScene(scene);
    }
}