package site.pokemons.edpproject.controller.component;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import site.pokemons.edpproject.event.ReservationClosedEvent;
import site.pokemons.edpproject.event.AppEventBus;

import java.io.IOException;

public class ReservationSummary extends HBox {

    @FXML private ImageView posterImage;
    @FXML private Label movieTitle;
    @FXML private Label screeningTime;
    @FXML private Label seatsInfo;
    @FXML private Button endButton;

    public ReservationSummary() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/site/pokemons/edpproject/view/reservation-summary.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        endButton.setOnAction(event -> {
            AppEventBus.getInstance().post(new ReservationClosedEvent());
            ((Stage) this.getScene().getWindow()).close();
        });
    }

    public void setData(String title, String time, int seatsCount, Image poster) {
        movieTitle.setText("Film: " + title);
        screeningTime.setText("Godzina: " + time);
        seatsInfo.setText("Liczba miejsc: " + seatsCount);
        posterImage.setImage(poster);
    }

//    @FXML
//    private void handleClose() {
//        AppEventBus.getInstance().post(new ReservationClosedEvent());
//        ((Stage) this.getScene().getWindow()).close();
//    }
}