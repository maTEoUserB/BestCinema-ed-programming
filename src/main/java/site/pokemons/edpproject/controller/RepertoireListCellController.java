package site.pokemons.edpproject.controller;

import jakarta.mail.MessagingException;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import site.pokemons.edpproject.model.dbDto.ScreeningDTO;
import site.pokemons.edpproject.service.EmailService;
import site.pokemons.edpproject.service.webApi.YouTubeApiService;
import site.pokemons.edpproject.session.SessionContext;

import java.io.IOException;
import java.time.format.DateTimeFormatter;


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

    @FXML
    public void initialize() {
        trailerButton.setOnAction(event -> {
            try {
                showTrailer();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void showTrailer() throws IOException, InterruptedException {
        String videoId = YouTubeApiService.getInstance().getMovieTrailerLink(titleLabel.getText());
        showTrailerPanel(videoId);
    }

    public void setData(ScreeningDTO screening) {
        titleLabel.setText(screening.getTitle());
        overviewLabel.setText(screening.getDescription());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        startTimeLabel.setText(screening.getStartTime().format(formatter));
        image.setImage(new Image(screening.getImageUrl(), true));

        bookSeatsButton.setOnAction(event -> {
            //Wysłanie maila z informacją o rozpoczęciu rezerwacji
            String toEmail = SessionContext.getLoggedInUserEmail();
            String subject = "Rozpoczęcie rezerwacji w BestCinema.";
            String body = "Rozpocząłeś rezerwację na film " + screening.getTitle() + ".";
            sendEmail(toEmail, subject, body);

            CinemaHallController controller = ViewManager.getInstance().getController("hall-controller", CinemaHallController.class);
            controller.setHallNumber(screening.getHallId());
            controller.setScreeningId(screening.getScreeningId());

            showHallPanel();
        });

    }

    private void showHallPanel() {
        ViewManager.getInstance().switchTo("hall-view");
    }

    private void showTrailerPanel(String videoId) {
        String content = """
        <html>
        <body style='margin:0; background-color: black; display: flex; justify-content: center; align-items: center; height: 100vh;'>
            <iframe width='640' height='360'
                src='https://www.youtube.com/embed/%s?rel=0'
                frameborder='0' allowfullscreen>
            </iframe>
        </body>
        </html>
    """.formatted(videoId);

        WebView webView = new WebView();
        webView.getEngine().loadContent(content);

        Parent previousRoot = ViewManager.getInstance().getScene().getRoot();
        Button backButton = new Button("Wróć");
        backButton.setOnAction(e -> {
            webView.getEngine().loadContent("<html><body></body></html>");

            PauseTransition pause = new PauseTransition(Duration.millis(200));
            pause.setOnFinished(ev -> ViewManager.getInstance().getScene().setRoot(previousRoot));
            pause.play();
        });

        VBox layout = new VBox();
        layout.setStyle("-fx-background-color: black;");
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(backButton, webView);

        ViewManager.getInstance().getScene().setRoot(layout);

    }

    private void sendEmail(String toEmail, String subject, String body) {
        new Thread(() -> {
            try {
                EmailService.getInstance().sendEmail(toEmail, subject, body);
            } catch (MessagingException e) {
                Platform.runLater(this::showAlert);
            }
        }).start();
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Nie udało się wysłać rezerwacji na maila.");
        alert.showAndWait();
    }
}
