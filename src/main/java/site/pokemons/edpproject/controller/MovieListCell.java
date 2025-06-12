package site.pokemons.edpproject.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import site.pokemons.edpproject.model.tmdbApiDto.MovieDTO;

import java.io.IOException;

public class MovieListCell extends ListCell<MovieDTO> {
    private FXMLLoader loader;
    private HBox root;
    private MovieListCellController controller;

    @Override
    protected void updateItem(MovieDTO movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("/site/pokemons/edpproject/view/movie-list-cell.fxml"));
                try {
                    root = loader.load();
                    controller = loader.getController();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            controller.setData(movie);
            setText(null);
            setGraphic(root);
        }
    }
}