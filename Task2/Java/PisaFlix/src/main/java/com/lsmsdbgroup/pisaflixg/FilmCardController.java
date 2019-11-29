package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import java.io.IOException;
import javafx.beans.property.StringProperty;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class FilmCardController implements Initializable {

    private final StringProperty titleProperty = new SimpleStringProperty();
    private final StringProperty pusblishDateProperty = new SimpleStringProperty();

    private final int filmId;

    public FilmCardController(String title, String publishDate, int id) {
        titleProperty.set(title);
        pusblishDateProperty.set(publishDate);
        filmId = id;
    }

    @FXML
    private Label titleLabel;

    @FXML
    private Label publishLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        titleLabel.setText(titleProperty.get());
        publishLabel.setText(pusblishDateProperty.get());
    }

    @FXML
    private void showFilm() {
        try {
            Film film = PisaFlixServices.filmService.getById(filmId);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FilmDetailPage.fxml"));

            AnchorPane anchorPane = null;

            try {
                anchorPane = loader.load();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

            FilmDetailPageController filmDetailPageController = loader.getController();

            filmDetailPageController.setFilm(film);

            App.setMainPane(anchorPane);
        } catch (Exception ex) {
            App.printErrorDialog("Film Details", "An error occurred loading the film's details", ex.toString() + "\n" + ex.getMessage());
        }
    }

}