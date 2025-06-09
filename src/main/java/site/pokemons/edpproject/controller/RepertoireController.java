package site.pokemons.edpproject.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import lombok.Setter;
import site.pokemons.edpproject.model.dbDto.ScreeningDTO;
import site.pokemons.edpproject.service.serviceSingleton.ScreeningServiceSingleton;
import site.pokemons.edpproject.session.SessionContext;

import java.util.List;
import java.util.Map;

public class RepertoireController {
    @Setter
    private Scene scene;
    @Setter
    private Map<String, Parent> views;
    @Setter
    private Map<String, Object> controllers;

    @FXML private DatePicker datePicker;
    @FXML private ListView<ScreeningDTO> repertoireList;


    public void loadRepertoire(MouseEvent mouseEvent) {
        List<ScreeningDTO> screenings = ScreeningServiceSingleton.getInstance().findScreenings(datePicker.getValue());
        repertoireList.getItems().setAll(screenings);

        repertoireList.setCellFactory(listCell -> new RepertoireListCell(scene, views, controllers));
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
