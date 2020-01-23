package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.Scraping.WikiScraper;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class FilmCardController implements Initializable {

    private final StringProperty titleProperty = new SimpleStringProperty();
    private final StringProperty pusblishDateProperty = new SimpleStringProperty();

    private final String filmId;

    private Film film;

    public FilmCardController(Film film) {
        this.film = film;
        titleProperty.set(film.getTitle());
        pusblishDateProperty.set(film.getPublicationDate().toString().split(" ")[5]);
        filmId = film.getId();
    }

    @FXML
    private Label titleLabel;

    @FXML
    private Label publishLabel;

    @FXML
    private MenuItem deleteFilmMenuItem;

    @FXML
    private MenuItem modifyFilmMenuItem;

    @FXML
    private Tooltip titleTool;

    @FXML
    private Tooltip publishTool;

    @FXML
    private Text tags;

    @FXML
    private Label suggestedLabel;

    @FXML
    private ImageView poster;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int maxTagLength = 32;
        titleLabel.setText(titleProperty.get());
        publishLabel.setText(pusblishDateProperty.get());

        titleTool.setText(titleProperty.get());

        if (!PisaFlixServices.authenticationService.isUserLogged() || (PisaFlixServices.authenticationService.getLoggedUser().getPrivilegeLevel() < UserPrivileges.MODERATOR.getValue())) {
            deleteFilmMenuItem.setVisible(false);
            modifyFilmMenuItem.setVisible(false);
        } else {
            deleteFilmMenuItem.setVisible(true);
            modifyFilmMenuItem.setVisible(true);
        }

        if (film.getFilmType() == null) {
            suggestedLabel.setVisible(false);
            suggestedLabel.setManaged(false);
        } else {
            maxTagLength = 17;
            if (film.getFilmType() == Film.FilmType.Recent) {
                suggestedLabel.setText("Recent");
            }

            if (film.getFilmType() == Film.FilmType.Suggested) {
                suggestedLabel.setText("*** Suggested ***");
            }

            if (film.getFilmType() == Film.FilmType.Favourite) {
                suggestedLabel.setTextFill(Color.VIOLET);
                suggestedLabel.setText("Favourite");
            }
        }

        if (film.getTags().isEmpty()) {
            tags.setManaged(false);
            tags.setVisible(false);
        } else {
            tags.setText("");
            for (String tag : film.getTags()) {
                if ((tags.getText() + " #" + tag).length() > maxTagLength) {
                    break;
                }
                tags.setText(tags.getText() + " #" + tag);
            }
        }
        setPoster();
    }

    @FXML
    private void clickMouse(MouseEvent event) {
        if (event.isSecondaryButtonDown()) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FilmDetailPage.fxml"));
            App.setLoader(loader);
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
            PisaFlixServices.filmService.deleteFilm(this.film.getId());
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

    private void setPoster() {
        try {
            WikiScraper scraper = new WikiScraper(film.getWikiPage());
            if (!scraper.scrapePosterLink().isBlank()) {
                poster.setImage(new Image("https:" + scraper.scrapePosterLink()));
            }
        } catch (Exception ex) {
            App.printErrorDialog("Film Poster", "An error occurred loading the film's poster", ex.toString() + "\n" + ex.getMessage());
        }
    }
}
