package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Comment;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.PisaFlixServices;
import java.io.IOException;
import javafx.beans.property.StringProperty;
import java.net.URL;
import java.util.Set;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class FilmCardController implements Initializable {

    private final StringProperty titleProperty = new SimpleStringProperty();

    private final int filmId;

    public FilmCardController(String title, int id) {
        titleProperty.set(title);
        filmId = id;
    }

    @FXML
    private Label titleLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        titleLabel.setText(titleProperty.get());
    }

    @FXML
    private void showFilm() {
        Film film = PisaFlixServices.FilmManager.getById(filmId);
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
    }

}
