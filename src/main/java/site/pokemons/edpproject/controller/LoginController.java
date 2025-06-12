package site.pokemons.edpproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import site.pokemons.edpproject.service.UserService;
import site.pokemons.edpproject.session.SessionContext;

import java.time.LocalDate;

public class LoginController {
    @FXML private Button loginButton;
    @FXML private TextField usernameText;
    @FXML private TextField passwdText;
    @FXML private Label infoLabel;

    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> loginHandle());
    }

    @FXML
    public void loginHandle() {
        UserService userService = UserService.getInstance();
        boolean log = userService.loginUser(usernameText.getText(), passwdText.getText());
        clearLoginPage();

        if(log){

            if(userService.getAccountById(SessionContext.getLoggedInUserId()).getRole().equals("USER")){
                showUserPanel();
            }else if(userService.getAccountById(SessionContext.getLoggedInUserId()).getRole().equals("ADMIN")){
                showAdminPanel();
            }

            return;
        }

        infoLabel.setText("Błędne dane logowania.");
        showAlert();
    }

    private void clearLoginPage() {
        usernameText.clear();
        passwdText.clear();
        infoLabel.setText("");
    }

    private void showUserPanel() {
        RepertoireController repertoireController = ViewManager.getInstance().getController("repertoire-controller", RepertoireController.class);
        if (repertoireController != null) {
            new Thread(() -> repertoireController.loadRepertoireForDate(LocalDate.now())).start();
        }
        ViewManager.getInstance().switchTo("repertoire-view");
    }

    private void showAdminPanel() {
        ViewManager.getInstance().switchTo("admin-view");
    }

    public void registerLoadHandle(MouseEvent mouseEvent) {
        ViewManager.getInstance().switchTo("register-view");
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Błędne dane logowania.");
        alert.showAndWait();
    }
}