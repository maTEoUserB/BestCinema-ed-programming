package site.pokemons.edpproject.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import site.pokemons.edpproject.model.dbDto.ScreeningDTO;
import site.pokemons.edpproject.service.ScreeningService;

import java.io.IOException;
import java.util.Map;

public class RepertoireListCell extends ListCell<ScreeningDTO> {
    private FXMLLoader loader;
    private HBox root;
    private RepertoireListCellController controller;
    private final ScreeningService screeningService;

    private Scene scene;
    private Map<String, Parent> views;
    private Map<String, Object> controllers;

    public RepertoireListCell(ScreeningService screeningService, Scene scene, Map<String, Parent> views, Map<String, Object> controllers) {
        this.screeningService = screeningService;
        this.scene = scene;
        this.views = views;
        this.controllers = controllers;
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
            controller.setScene(scene);
            controller.setViews(views);
            controller.setControllers(controllers);
            controller.setData(screening);
            setText(null);
            setGraphic(root);
        }
    }
}
