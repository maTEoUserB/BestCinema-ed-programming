package site.pokemons.edpproject.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import site.pokemons.edpproject.controller.component.PersonRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReservationController {
    @FXML
    private VBox personList;
    private List<PersonRow> personRows = new ArrayList<>();
    @Setter
    private Scene scene;
    @Setter
    private Map<String, Parent> views;
    @Setter
    @Getter
    private Long hallId;

    @FXML
    public void addPerson() {
//        PersonRow personRow = new PersonRow();
//        personRows.add(personRow);
//        personList.getChildren().add(personRow.getLayout());
    }

    @FXML
    public void goToHallView(MouseEvent mouseEvent) {

        loadHallPanel();
    }

    private void loadHallPanel() {
        scene.setRoot(views.get("hall-view"));
    }

    @FXML
    public void stopBookButton(MouseEvent mouseEvent) {

    }
}
