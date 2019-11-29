package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.net.URL;
import java.time.*;
import java.util.*;
import java.util.ResourceBundle;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProjectionsController implements Initializable {

    @FXML
    private TableView<ObservableList> projectionTable;

    @FXML
    private TableColumn<ObservableList, String> dataCol;

    @FXML
    private TableColumn<ObservableList, String> roomCol;

    @FXML
    private TableColumn<ObservableList, String> cinemaCol;

    @FXML
    private TableColumn<ObservableList, String> filmCol;

    @FXML
    private ComboBox cinemaCombo;

    @FXML
    private ComboBox filmCombo;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button addProjectionButton;

    @FXML
    private Button removeProjectionButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {

            try {
                PisaFlixServices.userService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR);
            } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
                addProjectionButton.setVisible(false);
                addProjectionButton.setManaged(false);
                removeProjectionButton.setVisible(false);
                removeProjectionButton.setManaged(false);
            }

            Set<Film> filmSet = PisaFlixServices.filmService.getAll();
            Set<Cinema> cinemaSet = PisaFlixServices.cinemaService.getAll();
            ObservableList observableFilmSet = FXCollections.observableArrayList(filmSet);
            ObservableList observableCinemaSet = FXCollections.observableArrayList(cinemaSet);

            filmCombo.getItems().setAll(observableFilmSet);
            filmCombo.getItems().add("All");

            cinemaCombo.getItems().setAll(observableCinemaSet);
            cinemaCombo.getItems().add("All");
        } catch (Exception ex) {
            App.printErrorDialog("Projections", "An error occurred loading the page", ex.toString() + "\n" + ex.getMessage());
        }

    }

    @FXML
    private void clickAddProjectionButton() {
        App.setMainPageReturnsController("AddProjection");
    }

    @FXML
    private void clickRemoveProjectionButton() {
        try {
            if (projectionTable.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Deleting Projection");
                alert.setHeaderText("warning");
                alert.setContentText("You must select a projection");
                alert.showAndWait();
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deleting Projection");
            alert.setHeaderText("You're deleting the projection");
            alert.setContentText("Are you sure do you want continue?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() != ButtonType.OK) {
                return;
            }

            Projection projection = (Projection) projectionTable.getSelectionModel().getSelectedItem();
            PisaFlixServices.projectionService.removeProjection(projection.getIdProjection());
            showSearch();
        } catch (Exception ex) {
            App.printErrorDialog("Remove Projection", "An error occurred removing the projection", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    public void showSearch() {
        try {
            dataCol.setCellValueFactory(new PropertyValueFactory<>("DateTime"));
            roomCol.setCellValueFactory(new PropertyValueFactory<>("Room"));
            cinemaCol.setCellValueFactory(new PropertyValueFactory<>("Cinema"));
            filmCol.setCellValueFactory(new PropertyValueFactory<>("Film"));

            int cinemaId;
            int filmId;

            if (cinemaCombo.getValue() == null || "All".equals(cinemaCombo.getValue().toString())) {
                cinemaId = -1;
            } else {
                Cinema cinema = (Cinema) cinemaCombo.getValue();
                cinemaId = cinema.getIdCinema();
            }

            if (filmCombo.getValue() == null || "All".equals(filmCombo.getValue().toString())) {
                filmId = -1;
            } else {
                Film film = (Film) filmCombo.getValue();
                filmId = film.getIdFilm();
            }
            projectionTable.getItems().setAll(getItemsToAdd(cinemaId, filmId));
        } catch (Exception ex) {
            App.printErrorDialog("Projection Search", "An error occurred loading the projections", ex.toString() + "\n" + ex.getMessage());
        }
    }

    private ObservableList getItemsToAdd(int cinemaId, int filmId) {

        LocalDate localDate = datePicker.getValue();
        String dateStr;

        if (localDate != null) {
            dateStr = localDate.toString();
        } else {
            dateStr = "all";
        }

        Set<Projection> projectionSet = PisaFlixServices.projectionService.queryProjections(cinemaId, filmId, dateStr, -1);
        ObservableList observableProjectionSet = FXCollections.observableArrayList(projectionSet);

        if (projectionSet == null) {
            return null;
        }

        return observableProjectionSet;
    }
}
