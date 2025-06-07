package site.pokemons.edpproject.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import site.pokemons.edpproject.model.dbDto.ScreeningDTO;
import site.pokemons.edpproject.model.tmdbApiDto.MovieDTO;
import site.pokemons.edpproject.service.ScreeningService;

import java.io.IOException;

public class RepertoireListCell extends ListCell<ScreeningDTO> {
    private FXMLLoader loader;
    private HBox root;
    private RepertoireListCellController controller;
    private ScreeningService screeningService;

    public RepertoireListCell(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    @Override
    protected void updateItem(ScreeningDTO screening, boolean empty) {
        super.updateItem(screening, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("/site/pokemons/edpproject/view/repertoire-list-cell.fxml"));
                try {
                    root = loader.load();
                    controller = loader.getController();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            controller.setScreeningService(screeningService);
            controller.setData(screening);
            setText(null);
            setGraphic(root);
        }
    }
}
