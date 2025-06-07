package site.pokemons.edpproject.controller;

import jakarta.mail.MessagingException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Setter;
import site.pokemons.edpproject.model.dbDto.ScreeningDTO;
import site.pokemons.edpproject.service.EmailService;
import site.pokemons.edpproject.service.ScreeningService;


public class RepertoireListCellController {
    @FXML
    private Label titleLabel;
    @FXML
    private Label overviewLabel;
    @FXML
    private Label startTimeLabel;
    @FXML
    private Button bookSeatsButton;

    @Setter
    private ScreeningService screeningService;


    public void setData(ScreeningDTO screening) {
        titleLabel.setText(screening.getTitle());
        overviewLabel.setText(screening.getDescription());
        startTimeLabel.setText(screening.getStartTime().toString());

        bookSeatsButton.setOnAction(event -> {
            String toEmail = "bobinskimateusz265@gmail.com";
            String subject = "Repertoire";
            String body = "Repertoire: " + screening.getTitle() + " " + screening.getDescription();

            new Thread(() -> {
                try {
                    EmailService emailService = new EmailService(
                            System.getenv("MY_EMAIL"),
                            System.getenv("MY_EMAIL_PASS")
                    );
                    emailService.sendEmail(toEmail, subject, body);
                    Platform.runLater(() -> {
                        showAlert("Wysłano rezerwację na maila.", Alert.AlertType.INFORMATION);
                    });
                } catch (MessagingException e) {
                    Platform.runLater(() -> {
                        showAlert("Nie udało się wysłać rezerwacji na maila.", Alert.AlertType.ERROR);
                    });
                }
            }).start();
        });
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
