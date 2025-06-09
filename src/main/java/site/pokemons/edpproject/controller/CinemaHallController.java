package site.pokemons.edpproject.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import site.pokemons.edpproject.service.serviceSingleton.SeatServiceSingleton;

import java.util.List;

public class CinemaHallController {
    @FXML private AnchorPane rootPane;
    @FXML private Label hallNumber;


    public void setHallNumber(long hallNumber) {
        this.hallNumber.setText(String.valueOf(hallNumber));
    }

    @FXML
    public void initialize() {
        markOccupiedSeats();
    }

    @FXML
    public void chooseSeat(MouseEvent mouseEvent) {
        Button clickedSeat = (Button) mouseEvent.getSource();

        clickedSeat.setStyle("-fx-background-color: red;");
    }

    @FXML
    public void toNextStep(MouseEvent mouseEvent) {

    }

    @FXML
    public void goBack(MouseEvent mouseEvent) {

    }

    private void markOccupiedSeats() {
        List<String> occupiedSeats = SeatServiceSingleton.getInstance().getOccupiedSeats(Long.parseLong(hallNumber.getText()));
        for (String seatId : occupiedSeats) {
            Node node = rootPane.lookup("#" + seatId);
            if (node instanceof Button) {
                Button seatButton = (Button) node;
                seatButton.setStyle("-fx-background-color: gray; -fx-text-fill: white;");
                seatButton.setDisable(true);
            }
        }
    }
}
