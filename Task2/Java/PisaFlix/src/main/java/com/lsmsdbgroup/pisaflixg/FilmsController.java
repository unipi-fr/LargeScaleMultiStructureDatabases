package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.io.IOException;
import java.util.*;
import javafx.fxml.FXMLLoader;

public class FilmsController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TilePane tilePane;

    @FXML
    private TextField titleFilterTextField;

    @FXML
    private Button addFilmButton;

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        try {
            try {
                PisaFlixServices.userService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR);
            } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
                addFilmButton.setVisible(false);
                addFilmButton.setManaged(false);
            }
            searchFilms(null, null);
        } catch (Exception ex) {
            App.printErrorDialog("Films", "There was an inizialization error", ex.toString() + "\n" + ex.getMessage());

        }
    }

    private Pane createFilmCardPane(String title, String publishDate, int id) {
        Pane pane = new Pane();
        try {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FilmCard.fxml"));
                FilmCardController fcc = new FilmCardController(title, publishDate, id);
                loader.setController(fcc);
                pane = loader.load();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (Exception ex) {
            App.printErrorDialog("Films", "An error occurred creating the film card", ex.toString() + "\n" + ex.getMessage());
        }
        return pane;
    }

    public void populateScrollPane(Set<Film> films) {
        tilePane.getChildren().clear();
        String title;
        String publishDate;
        int id;

        Pane pane;
        int i = 0;
        for (Film film : films) {
            title = film.getTitle();
            publishDate = film.getPublicationDate().toString();
            id = film.getIdFilm();

            pane = createFilmCardPane(title, publishDate, id);
            tilePane.getChildren().add(pane);
        }
    }

    @FXML
    private void filterFilms() {
        try {
            String titleFilter = titleFilterTextField.getText();

            searchFilms(titleFilter, null);
        } catch (Exception ex) {
            App.printErrorDialog("Films", "An error occurred searching the films", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    private void addFilm() {
        try {
            try {
                PisaFlixServices.userService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "add a new film");
            } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
                System.out.println(ex.getMessage());
                return;
            }
            App.setMainPageReturnsController("AddFilm");
        } catch (Exception ex) {
            App.printErrorDialog("Films", "An error occurred", ex.toString() + "\n" + ex.getMessage());

        }
    }

    @FXML
    private void searchFilms(String titleFilter, Date dateFilter) {
        try {
            Set<Film> films = PisaFlixServices.filmService.getFilmsFiltered(titleFilter, dateFilter, dateFilter);
            populateScrollPane(films);
        } catch (Exception ex) {
            App.printErrorDialog("Films", "An error occurred searching the films", ex.toString() + "\n" + ex.getMessage());
        }
    }
}
