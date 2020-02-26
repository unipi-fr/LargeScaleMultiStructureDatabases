package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.pisaflixservices.WikiScraper;
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

public class FilmCardController implements Initializable {

    private final StringProperty titleProperty = new SimpleStringProperty();
    private final StringProperty pusblishDateProperty = new SimpleStringProperty();

    private final Long filmId;

    private final Film film;

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
    private ImageView poster;

    @FXML
    private Button followButton;

    @FXML
    private VBox card;

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

        setPoster();
        setFollowButton();
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

    @FXML
    private void FollowUnfollow() {
        if (PisaFlixServices.filmService.isFollowing(film, PisaFlixServices.authenticationService.getLoggedUser())) {
            PisaFlixServices.filmService.unfollow(film, PisaFlixServices.authenticationService.getLoggedUser());
            followButton.setText("+ Follow");
        } else {
            PisaFlixServices.filmService.follow(film, PisaFlixServices.authenticationService.getLoggedUser());
            followButton.setText("- Unfollow");
            followButton.getStyleClass().remove("smallest");
            followButton.getStyleClass().remove("smaller");
        }
    }

    public void setFollowButton() {
        try {
            if (PisaFlixServices.authenticationService.isUserLogged()) {
                if (!PisaFlixServices.filmService.isFollowing(film, PisaFlixServices.authenticationService.getLoggedUser())) {
                    
                    if(film.type().equals("SUGGESTED")){
                        followButton.setText("+ Suggested");
                    }
                    
                    if(film.type().equals("VERY SUGGESTED")){
                        followButton.setText("+ Very Suggested");
                        followButton.getStyleClass().add("smaller");
                    }
                    
                    if(film.type().equals("NORMAL")){
                        followButton.setText("+ Follow");
                    }
                    
                    if(film.type().equals("FRIEND COMMENTED")){
                        followButton.setText("Commented by Friend");
                        followButton.getStyleClass().add("smallest");
                    }
                    
                } else {
                    followButton.setText("- Unfollow");
                }
            } else {
                followButton.setVisible(false);
                followButton.setManaged(false);
                poster.setFitHeight(116);
                poster.setFitWidth(116);
            }
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
}
