package site.pokemons.edpproject.controller.component;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PersonRow {
    private final Label seatNumber = new Label();
    private final CheckBox isReduced = new CheckBox();
    private final HBox layout = new HBox(10);
    @Setter
    private Long reservationSeatId;

    public PersonRow(Long id, String seatNumber) {
        this.seatNumber.setText(seatNumber);
        this.seatNumber.setStyle("-fx-text-fill: white;");
        isReduced.setText("Czy bilet ulgowy?");
        isReduced.setStyle("-fx-text-fill: white;");
        layout.getChildren().addAll(isReduced);
        reservationSeatId = id;
    }

}
