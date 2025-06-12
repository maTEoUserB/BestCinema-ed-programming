package site.pokemons.edpproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import site.pokemons.edpproject.event.AppEventBus;
import site.pokemons.edpproject.event.ReservationClosedEvent;
import site.pokemons.edpproject.model.dbDto.ScreeningDTO;
import site.pokemons.edpproject.service.ScreeningService;
import site.pokemons.edpproject.session.SessionContext;
import com.google.common.eventbus.Subscribe;

import java.time.LocalDate;
import java.util.List;

public class RepertoireController {
    @FXML private DatePicker datePicker;
    @FXML private ListView<ScreeningDTO> repertoireList;
    @FXML private Button logoutButton;
    @FXML private Button profileButton;

    @FXML
    public void initialize() {
        datePicker.setOnAction(event -> loadRepertoire());
        logoutButton.setOnAction(event -> logoutButtonClick());
        profileButton.setOnAction(event -> goToProfileView());

        AppEventBus.getInstance().register(this);
    }

    @Subscribe
    public void onReservationClosed(ReservationClosedEvent event) {
        loadRepertoireForDate(LocalDate.now());
    }

    public void loadRepertoire() {
        List<ScreeningDTO> screenings = ScreeningService.getInstance().findScreenings(datePicker.getValue());
        repertoireList.getItems().setAll(screenings);

        repertoireList.setCellFactory(listCell -> new RepertoireListCell());
    }

    public void loadRepertoireForDate(LocalDate now) {
        List<ScreeningDTO> screenings = ScreeningService.getInstance().findScreenings(now);
        repertoireList.getItems().setAll(screenings);
        datePicker.setValue(now);

        repertoireList.setCellFactory(listCell -> new RepertoireListCell());
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
