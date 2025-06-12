package site.pokemons.edpproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import site.pokemons.edpproject.model.tmdbApiDto.MovieDTO;
import site.pokemons.edpproject.model.tmdbApiDto.NowPlayingResponse;
import site.pokemons.edpproject.service.webApi.TmdbApiService;
import site.pokemons.edpproject.session.SessionContext;

import java.io.IOException;
import java.util.List;

public class AdminController {
    @FXML private Button logoutButton;
    @FXML private Button profileButton;
    @FXML private ListView<MovieDTO> movieList;


    @FXML
    public void initialize() {
        logoutButton.setOnAction(event -> logoutButtonClick());
        profileButton.setOnAction(event -> goToProfileView());

        new Thread(() -> {
            NowPlayingResponse response;
            try {
                response = TmdbApiService.getInstance().getMovieList();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            List<MovieDTO> movies = response.getResults();
            movieList.getItems().setAll(movies);

            movieList.setCellFactory(listCell -> new MovieListCell());
        }).start();
    }

    public void logoutButtonClick() {
        SessionContext.clear();
        showLoginPanel();
    }

    public void goToProfileView() {
        showUserProfilePanel();
    }

    private void showLoginPanel() {
        ViewManager.getInstance().switchTo("login-view");
    }

    private void showUserProfilePanel() {
        ViewManager.getInstance().switchTo("profile-view");
    }
}
