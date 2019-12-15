package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflix.pisaflixservices.UserPrivileges;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.InvalidPrivilegeLevelException;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.UserNotLoggedException;
import java.io.IOException;
import javafx.beans.property.StringProperty;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class FilmCardController implements Initializable {

    private final StringProperty titleProperty = new SimpleStringProperty();
    private final StringProperty pusblishDateProperty = new SimpleStringProperty();

    private final int filmId;
    
    private Film film;

    public FilmCardController(String title, String publishDate, int id) {
        titleProperty.set(title);
        pusblishDateProperty.set(publishDate);
        filmId = id;
    }

    @FXML
    private Label titleLabel;

    @FXML
    private Label publishLabel;
    
    @FXML
    private MenuItem deleteFilmMenuItem;
    
    @FXML
    private MenuItem modifyFilmMenuItem;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        titleLabel.setText(titleProperty.get());
        publishLabel.setText(pusblishDateProperty.get());
        film = PisaFlixServices.filmService.getById(filmId);
        
        if (!PisaFlixServices.authenticationService.isUserLogged() || (PisaFlixServices.authenticationService.getLoggedUser().getPrivilegeLevel() < UserPrivileges.MODERATOR.getValue())) {
            deleteFilmMenuItem.setVisible(false);
            modifyFilmMenuItem.setVisible(false);
        } else {
            deleteFilmMenuItem.setVisible(true);
            modifyFilmMenuItem.setVisible(true);
        }
    }

    @FXML
    private void clickMouse(MouseEvent event) {
        if (event.isSecondaryButtonDown()) {
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FilmDetailPage.fxml"));

            GridPane gridPane = null;

            try {
                gridPane = loader.load();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

            FilmDetailPageController filmDetailPageController = loader.getController();

            filmDetailPageController.setFilm(film);

            App.setMainPane(gridPane);
        } catch (Exception ex) {
            App.printErrorDialog("Film Details", "An error occurred loading the film's details", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    private void deleteFilm() {
        if (!App.printConfirmationDialog("Deleting film", "You're deleting the film", "Are you sure do you want continue?")) {
            return;
        }
        try {
            PisaFlixServices.filmService.deleteFilm(this.film.getIdFilm());
            FilmBrowserController filmBrowserController = new FilmBrowserController();
            App.setMainPageAndController("Browser", filmBrowserController);
        } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
            App.printErrorDialog("Delete Film", "An error occurred deleting the film", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    private void modifyFilm() {
        AddFilmController addFilmController = (AddFilmController) App.setMainPageReturnsController("AddFilm");
        addFilmController.setFilm(this.film);
    }
}
