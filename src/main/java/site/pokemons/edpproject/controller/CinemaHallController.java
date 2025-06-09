package site.pokemons.edpproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CinemaHallController {
    @FXML private Label hallNumber;

    public void setHallNumber(long hallNumber) {
        this.hallNumber.setText(String.valueOf(hallNumber));
    }
}
