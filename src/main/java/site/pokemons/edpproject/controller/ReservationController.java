package site.pokemons.edpproject.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class ReservationController {
    @Setter
    private Scene scene;
    @Setter
    private Map<String, Parent> views;
    @Setter
    @Getter
    private Long hallId;

    @FXML
    public void stopBookButton(MouseEvent mouseEvent) {

    }

    @FXML
    public void goToHallView(MouseEvent mouseEvent) {
        loadHallPanel();
    }

    private void loadHallPanel() {
        scene.setRoot(views.get("hall-view"));
    }
}
