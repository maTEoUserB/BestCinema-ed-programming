package site.pokemons.edpproject.controller;

import jakarta.mail.MessagingException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Setter;
import site.pokemons.edpproject.controller.component.PersonRow;
import site.pokemons.edpproject.model.*;
import site.pokemons.edpproject.service.EmailService;
import site.pokemons.edpproject.service.ReservationService;
import site.pokemons.edpproject.service.SeatService;
import site.pokemons.edpproject.session.SessionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CinemaHallController {
    @FXML private AnchorPane rootPane;
    @FXML private Label hallNumber;
    @FXML private Button backButton;
    @FXML private Button nextStepButton;
    @FXML
    private VBox personList;
    private List<PersonRow> personRows = new ArrayList<>();
    private Reservation reservation;
    private Long screeningId;
    @Setter
    private Scene scene;
    @Setter
    private Map<String, Parent> views;
    private List<Button> clickedSeats = new ArrayList<>();
    private List<String> occupiedSeats = new ArrayList<>();

    @FXML
    public void initialize() {
        nextStepButton.setOnAction(event -> toNextStep());
        backButton.setOnAction(event -> goBack());
    }

    @FXML
    public void chooseSeat(MouseEvent mouseEvent) {
        Button clickedSeat = (Button) mouseEvent.getSource();
        clickedSeat.getStyleClass().remove("seat-button");
        clickedSeat.getStyleClass().add("seat-button-selected");
        clickedSeats.add(clickedSeat);

        Long id = addReservationSeat(clickedSeat.getId());
        if(id != null) addPerson(id, clickedSeat.getId());
    }

    @FXML
    public void toNextStep() {
        unmarkOccupiedSeats();
        for(PersonRow personRow : personRows) {
            ReservationService.getInstance().updateReservationSeat(personRow.getReservationSeatId(), personRow.getIsReduced().isSelected());
        }

        String body = ReservationService.getInstance().getReservationInformation(reservation.getReservationId());
        sendEmail(SessionContext.getLoggedInUserEmail(), body);
        personRows.clear();
        personList.getChildren().clear();
        for(Button button : clickedSeats) {
            button.getStyleClass().remove("seat-button-selected");
            button.getStyleClass().add("seat-button");
        }
        scene.setRoot(views.get("repertoire-view"));
    }

    @FXML
    public void goBack() {
        unmarkOccupiedSeats();
        ReservationService.getInstance().deleteReservation(reservation.getReservationId());
        personRows.clear();
        personList.getChildren().clear();
        for(Button button : clickedSeats) {
            button.getStyleClass().remove("seat-button-selected");
            button.getStyleClass().add("seat-button");
        }
        scene.setRoot(views.get("repertoire-view"));
    }

    private Long addReservationSeat(String seatNumber) {
        Long rsId = ReservationService.getInstance().addReservationSeat(seatNumber, hallNumber.getText(), screeningId, reservation.getReservationId());

        if(rsId == null) showAlert("Miejsce zajęte", Alert.AlertType.WARNING);
        return rsId;
    }

    private void addPerson(Long id, String seatNumber) {
        PersonRow personRow = new PersonRow(id, seatNumber);
        personRows.add(personRow);
        personList.getChildren().add(personRow.getLayout());
    }

    //Setter
    public void setHallNumber(long hallNumber) {
        this.hallNumber.setText(String.valueOf(hallNumber));
    }

    //Setter
    public void setScreeningId(Long screeningId) {
        this.screeningId = screeningId;
        markOccupiedSeats(screeningId);
        reservation = ReservationService.getInstance().addReservation(screeningId);
    }

    private void markOccupiedSeats(Long screeningId) {
        List<String> occupiedSeats = SeatService.getInstance().getOccupiedSeats(screeningId);
        for (String seatId : occupiedSeats) {
            Node node = rootPane.lookup("#" + seatId);
            if (node instanceof Button) {
                Button seatButton = (Button) node;
                seatButton.getStyleClass().remove("seat-button");
                seatButton.getStyleClass().add("seat-button-occupied");
                seatButton.setDisable(true);
            }
        }
        this.occupiedSeats = occupiedSeats;
    }

    private void unmarkOccupiedSeats() {
        for (String seatId : occupiedSeats) {
            Node node = rootPane.lookup("#" + seatId);
            if (node instanceof Button) {
                Button seatButton = (Button) node;
                seatButton.getStyleClass().remove("seat-button-occupied");
                seatButton.getStyleClass().add("seat-button");
                seatButton.setDisable(false);
            }
        }
        this.occupiedSeats.clear();
    }

    private void sendEmail(String toEmail, String body) {
        new Thread(() -> {
            try {
                EmailService.getInstance().sendEmail(toEmail, "BestCinema - rezerwacja.", body);
            } catch (MessagingException e) {
                Platform.runLater(() -> {
                    showAlert("Nie udało się wysłać rezerwacji na maila.", Alert.AlertType.ERROR);
                });
            }
        }).start();
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
