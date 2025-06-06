package site.pokemons.edpproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import site.pokemons.edpproject.model.tmdbApiDto.MovieDTO;
import site.pokemons.edpproject.model.tmdbApiDto.NowPlayingResponse;
import site.pokemons.edpproject.service.ScreeningService;
import site.pokemons.edpproject.service.TmdbApiService;

import java.io.IOException;
import java.util.List;

public class AdminController {
    @FXML private Button loadButton;
    @FXML private ListView<MovieDTO> movieList;

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
}
