package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.net.URL;
import java.text.*;
import java.time.*;
import java.util.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

public class AddProjectionController implements Initializable {

    @FXML
    private ComboBox cinemaComboBox;
    @FXML
    private ComboBox filmComboBox;
    @FXML
    private DatePicker dateDatePicker;
    @FXML
    private ComboBox timeComboBox;
    @FXML
    private TextField roomTextField;
    @FXML
    private Label successLabel;

    DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            successLabel.setVisible(false);
            successLabel.setManaged(false);

            Set<Film> filmSet = PisaFlixServices.filmService.getAll();
            Set<Cinema> cinemaSet = PisaFlixServices.cinemaService.getAll();
            ObservableList observableFilmSet = FXCollections.observableArrayList(filmSet);
            ObservableList observableCinemaSet = FXCollections.observableArrayList(cinemaSet);

            filmComboBox.getItems().setAll(observableFilmSet);

            cinemaComboBox.getItems().setAll(observableCinemaSet);

            LocalTime lt = LocalTime.MIN;
            for (int i = 0; i < 48; ++i) {

                timeComboBox.getItems().add(lt);
                lt = lt.plusMinutes(30);
            }
        } catch (Exception ex) {
            App.printErrorDialog("Add Projection", "An error occurred loading the page", ex.toString() + "\n" + ex.getMessage());
        }
    }

    private void resetFields() {
        cinemaComboBox.setValue(null);
        filmComboBox.setValue(null);
        timeComboBox.setValue(null);
        dateDatePicker.setValue(null);
        roomTextField.setText("");
    }

    private void errorLabel(String s) {
        successLabel.setTextFill(Color.RED);
        successLabel.setText(s);
        successLabel.setManaged(true);
        successLabel.setVisible(true);
    }

    @FXML
    private void clickAddProjectionButton() {
        try {
            try {
                successLabel.setVisible(false);
                successLabel.setManaged(false);
                if (cinemaComboBox.getValue() == null || cinemaComboBox.getValue() == "All") {
                    errorLabel("You must select a cinema");
                    return;
                }
                if (filmComboBox.getValue() == null || filmComboBox.getValue() == "All") {
                    errorLabel("You must select a film");
                    return;
                }
                if (dateDatePicker.getValue() == null) {
                    errorLabel("You must select a day");
                    return;
                }
                if (timeComboBox.getValue() == null) {
                    errorLabel("You must select a time");
                    return;
                }
                if (roomTextField.getText() == null || !roomTextField.getText().matches("\\d+")) {
                    errorLabel("You must select a room");
                    return;
                } else {
                }

                Cinema cinema = (Cinema) cinemaComboBox.getValue();
                Film film = (Film) filmComboBox.getValue();
                LocalTime lt = (LocalTime) timeComboBox.getValue();
                LocalDate ld = dateDatePicker.getValue();
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                LocalDateTime ldt = LocalDateTime.of(ld, lt);
                Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

                int room = Integer.parseInt(roomTextField.getText());

                if (PisaFlixServices.projectionService.checkDuplicates(cinema.getIdCinema(), film.getIdFilm(), dateFormat.format(date), room)) {
                    errorLabel("Projection already scheduled");
                    return;
                }

                try {
                    PisaFlixServices.projectionService.addProjection(cinema, film, date, room);
                } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
                    System.out.println(ex.getMessage());
                }
                successLabel.setTextFill(Color.GREEN);
                successLabel.setText("Projection succesfully scheduled");
                successLabel.setManaged(true);
                successLabel.setVisible(true);
                resetFields();
            } catch (NumberFormatException ex) {
                App.printErrorDialog("Add Projection", "Number Format Error", ex.toString() + "\n" + ex.getMessage());
            }
        } catch (Exception ex) {
            App.printErrorDialog("Add Projection", "An error occurred creating the projection", ex.toString() + "\n" + ex.getMessage());
        }
    }

}
