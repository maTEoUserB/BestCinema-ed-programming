package site.pokemons.edpproject.controller;

import jakarta.mail.MessagingException;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import lombok.Setter;
import site.pokemons.edpproject.model.dbDto.ScreeningDTO;
import site.pokemons.edpproject.service.serviceSingleton.EmailServiceSingleton;
import site.pokemons.edpproject.service.serviceSingleton.YouTubeApiServiceSingleton;
import site.pokemons.edpproject.session.SessionContext;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    @FXML
    private Button trailerButton;

    @Setter
    private Scene scene;
    @Setter
    private Map<String, Parent> views;
    @Setter
    private Map<String, Object> controllers;

    @FXML
    public void showTrailer(MouseEvent mouseEvent) throws IOException, InterruptedException {
        String videoId = YouTubeApiServiceSingleton.getInstance().getMovieTrailerLink(titleLabel.getText());
        showTrailerPanel(videoId);
    }

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

            CinemaHallController controller = (CinemaHallController) controllers.get("hall-controller");
            controller.setHallNumber(screening.getHallId());
            controller.setScreeningId(screening.getScreeningId());

            showHallPanel();
        });

    }

    private void showHallPanel() {
        scene.setRoot(views.get("hall-view"));
    }

    private void showTrailerPanel(String videoId) {
        String content = """
        <html>
        <body style='margin:0; background-color: black; display: flex; justify-content: center; align-items: center; height: 100vh;'>
            <iframe width='640' height='360'
                src='https://www.youtube.com/embed/%s'
                frameborder='0' allowfullscreen>
            </iframe>
        </body>
        </html>
    """.formatted(videoId);

        WebView webView = new WebView();
        webView.getEngine().loadContent(content);

        Parent previousRoot = scene.getRoot();
        Button backButton = new Button("⟵ Wróć");
        backButton.setOnAction(e -> {
            webView.getEngine().loadContent("<html><body></body></html>");

            PauseTransition pause = new PauseTransition(Duration.millis(200));
            pause.setOnFinished(ev -> scene.setRoot(previousRoot));
            pause.play();
        });

        VBox layout = new VBox();
        layout.setStyle("-fx-background-color: black;");
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(backButton, webView);

        scene.setRoot(layout);

    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
