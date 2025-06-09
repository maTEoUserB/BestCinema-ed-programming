package site.pokemons.edpproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class CinemaHallController {
    @FXML private Label hallNumber;

    public void setHallNumber(long hallNumber) {
        this.hallNumber.setText(String.valueOf(hallNumber));
    }

    @FXML
    public void chooseSeat(MouseEvent mouseEvent) {
        Button clickedSeat = (Button) mouseEvent.getSource();
        String seat = (String) clickedSeat.getUserData();

        clickedSeat.setStyle("-fx-background-color: red;");
    }

    @FXML
    public void toNextStep(MouseEvent mouseEvent) {

    }

    @FXML
    public void goBack(MouseEvent mouseEvent) {

    }
}
