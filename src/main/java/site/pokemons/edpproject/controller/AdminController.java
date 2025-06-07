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
import site.pokemons.edpproject.service.ScreeningService;
import site.pokemons.edpproject.service.TmdbApiService;
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
    private final TmdbApiService tmdbApiService;
    private final ScreeningService screeningService;

    public AdminController(TmdbApiService tmdbApiService, ScreeningService screeningService) {
        this.tmdbApiService = tmdbApiService;
        this.screeningService = screeningService;
    }

    @FXML
    public void initialize() throws IOException, InterruptedException {
        NowPlayingResponse response = tmdbApiService.getMovieList();
        List<MovieDTO> movies = response.getResults();
        movieList.getItems().setAll(movies);

        movieList.setCellFactory(listCell -> new MovieListCell(screeningService));

    }

    public void logoutButtonClick(MouseEvent mouseEvent) {
        SessionContext.clear();
        showLoginPanel();
    }

    private void showLoginPanel() {
        scene.setRoot(views.get("login-view"));
    }
}
