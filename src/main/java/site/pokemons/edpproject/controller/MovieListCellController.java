package site.pokemons.edpproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Setter;
import site.pokemons.edpproject.model.tmdbApiDto.MovieDTO;
import site.pokemons.edpproject.service.ScreeningService;
import site.pokemons.edpproject.service.serviceSingleton.ScreeningServiceSingleton;

import java.time.LocalDate;

public class MovieListCellController {
    @FXML private Label titleLabel;
    @FXML private Label overviewLabel;
    @FXML private TextField priceField;
    @FXML private TextField hallField;
    @FXML private DatePicker datePicker;
    @FXML private Spinner<Integer> hourSpinner;
    @FXML private Spinner<Integer> minuteSpinner;
    @FXML private Button saveButton;

    @FXML
    public void initialize() {
        hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12));
        minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
    }


    public void setData(MovieDTO movie) {
        titleLabel.setText(movie.getTitle());
        overviewLabel.setText(movie.getOverview());

        datePicker.setValue(LocalDate.now());

        saveButton.setOnAction(event -> {
            String priceText = priceField.getText();
            String hall = hallField.getText();
            LocalDate date = datePicker.getValue();
            int hour = hourSpinner.getValue();
            int minute = minuteSpinner.getValue();

            if (priceText == null || priceText.isEmpty() || hall == null || hall.isEmpty() || date == null) {
                showAlert("Uzupełnij wszystkie pola!", Alert.AlertType.WARNING);
                return;
            }

            try {
                double price = Double.parseDouble(priceText);
                int hallId = Integer.parseInt(hall);

                // zapisz do bazy
                ScreeningServiceSingleton.getInstance().saveScreening(movie, price, hallId, date, hour, minute);

                priceField.clear();
                hallField.clear();
                datePicker.setValue(LocalDate.now());
                showAlert("Film zapisany!", Alert.AlertType.INFORMATION);
            } catch (NumberFormatException e) {
                showAlert("Niepoprawna cena", Alert.AlertType.ERROR);
            }
        });
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
