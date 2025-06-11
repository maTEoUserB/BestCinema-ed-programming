package site.pokemons.edpproject.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import lombok.Setter;
import site.pokemons.edpproject.model.tmdbApiDto.MovieDTO;
import site.pokemons.edpproject.model.tmdbApiDto.NowPlayingResponse;
import site.pokemons.edpproject.service.webApi.TmdbApiService;
import site.pokemons.edpproject.session.SessionContext;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AdminController {
    @FXML private Button loadButton;
    @FXML private Button logoutButton;
    @FXML private ListView<MovieDTO> movieList;

    @Setter
    private Scene scene;
    @Setter
    private Map<String, Parent> views;


    @FXML
    public void initialize() throws IOException, InterruptedException {
        new Thread(() -> {
            NowPlayingResponse response = null;
            try {
                response = TmdbApiService.getInstance().getMovieList();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            List<MovieDTO> movies = response.getResults();
            movieList.getItems().setAll(movies);

            movieList.setCellFactory(listCell -> new MovieListCell());
        }).start();
    }

    public void logoutButtonClick(MouseEvent mouseEvent) {
        SessionContext.clear();
        showLoginPanel();
    }

    public void goToProfileView(MouseEvent mouseEvent) {
        showUserProfilePanel();
    }

    private void showLoginPanel() {
        scene.setRoot(views.get("login-view"));
    }

    private void showUserProfilePanel() {
        scene.setRoot(views.get("profile-view"));
    }
}
