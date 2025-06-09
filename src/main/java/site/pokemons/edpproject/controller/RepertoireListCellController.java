package site.pokemons.edpproject.controller;

import jakarta.mail.MessagingException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Setter;
import site.pokemons.edpproject.model.Reservation;
import site.pokemons.edpproject.model.dbDto.ScreeningDTO;
import site.pokemons.edpproject.service.EmailService;
import site.pokemons.edpproject.service.ReservationService;
import site.pokemons.edpproject.service.ScreeningService;
import site.pokemons.edpproject.service.serviceSingleton.EmailServiceSingleton;
import site.pokemons.edpproject.service.serviceSingleton.ReservationServiceSingleton;
import site.pokemons.edpproject.session.SessionContext;

import java.util.Map;


public class RepertoireListCellController {
    @FXML
    private Label titleLabel;
    @FXML
    private Label overviewLabel;
    @FXML
    private Label startTimeLabel;
    @FXML
    private ImageView image;
    @FXML
    private Button bookSeatsButton;

    @Setter
    private Scene scene;
    @Setter
    private Map<String, Parent> views;
    @Setter
    private Map<String, Object> controllers;



    public void setData(ScreeningDTO screening) {
        titleLabel.setText(screening.getTitle());
        overviewLabel.setText(screening.getDescription());
        startTimeLabel.setText(screening.getStartTime().toString());
        image.setImage(new Image(screening.getImageUrl(), true));

        bookSeatsButton.setOnAction(event -> {
            String toEmail = SessionContext.getLoggedInUserEmail();
            String subject = "Rezerwacja BestCinema.";
            String body = "Rozpocząłeś rezerwację na film " + screening.getTitle() + ".";

            new Thread(() -> {
                try {
                    EmailServiceSingleton.getInstance().sendEmail(toEmail, subject, body);
                    Platform.runLater(() -> {
                        showAlert("Wysłano rezerwację na maila.", Alert.AlertType.INFORMATION);
                    });
                } catch (MessagingException e) {
                    Platform.runLater(() -> {
                        showAlert("Nie udało się wysłać rezerwacji na maila.", Alert.AlertType.ERROR);
                    });
                }
            }).start();

            ReservationController controller = (ReservationController) controllers.get("reservation-controller");
            controller.setHallId(screening.getHallId());

            showReservationPanel();

//            CinemaHallController controller = (CinemaHallController) controllers.get("hall-controller");
//            controller.setHallNumber(screening.getHallId());

//            showHallPanel();
        });

    }

    private void showReservationPanel() {
        scene.setRoot(views.get("reservation-view"));
    }

//    private void showHallPanel() {
//        scene.setRoot(views.get("hall-view"));
//    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
