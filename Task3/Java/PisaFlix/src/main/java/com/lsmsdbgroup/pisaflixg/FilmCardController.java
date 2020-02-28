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
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;

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
    private Label suggestLabel;

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
    
    private ParallelTransition parallelTransitionUp;
    
    private ParallelTransition parallelTransitionDown;

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
        
        if(PisaFlixServices.authenticationService.isUserLogged())
        {
            initializeAnimation();
            titleLabel.getStyleClass().remove("card-film-title");
            titleLabel.getStyleClass().add("card-film-title-hider");
            
            publishLabel.getStyleClass().remove("card-film-subtitle");
            publishLabel.getStyleClass().add("card-film-subtitle-hider");
        } 
        
        

        setPoster();
        setFollowButton();
    }

    private void initializeAnimation(){
        ScaleTransition scaleTransitionUp = 
            new ScaleTransition(Duration.millis(250), poster);
        scaleTransitionUp.setFromX(1);
        scaleTransitionUp.setFromY(1);
        scaleTransitionUp.setToX(0.8);
        scaleTransitionUp.setToY(0.8);
        
        TranslateTransition translateTransitionTitleUp =
            new TranslateTransition(Duration.millis(250), titleLabel);
        translateTransitionTitleUp.setFromY(0);
        translateTransitionTitleUp.setToY(-25);
        translateTransitionTitleUp.setCycleCount(1);
        translateTransitionTitleUp.setAutoReverse(true);
        
        TranslateTransition translateTransitionPosterUp =
            new TranslateTransition(Duration.millis(250), poster);
        translateTransitionPosterUp.setFromY(0);
        translateTransitionPosterUp.setToY(-10);
        translateTransitionPosterUp.setCycleCount(1);
        translateTransitionPosterUp.setAutoReverse(true);
        
        TranslateTransition translateTransitionDateUp =
            new TranslateTransition(Duration.millis(250), publishLabel);
        translateTransitionDateUp.setFromY(0);
        translateTransitionDateUp.setToY(-25);
        translateTransitionDateUp.setCycleCount(1);
        translateTransitionDateUp.setAutoReverse(true);
        
        parallelTransitionUp = new ParallelTransition();
        
        parallelTransitionUp.getChildren().addAll(
                scaleTransitionUp,
                translateTransitionTitleUp,
                translateTransitionDateUp,
                translateTransitionPosterUp
        );
        
        ScaleTransition scaleTransitionDown = 
            new ScaleTransition(Duration.millis(250), poster);
        scaleTransitionDown.setFromX(0.8);
        scaleTransitionDown.setFromY(0.8);
        scaleTransitionDown.setToX(1);
        scaleTransitionDown.setToY(1);
        
        TranslateTransition translateTransitionTitleDown =
            new TranslateTransition(Duration.millis(250), titleLabel);
        translateTransitionTitleDown.setFromY(-25);
        translateTransitionTitleDown.setToY(0);
        translateTransitionTitleDown.setCycleCount(1);
        translateTransitionTitleDown.setAutoReverse(true);
        
        TranslateTransition translateTransitionPosterDown =
            new TranslateTransition(Duration.millis(250), poster);
        translateTransitionPosterDown.setFromY(-10);
        translateTransitionPosterDown.setToY(0);
        translateTransitionPosterDown.setCycleCount(1);
        translateTransitionPosterDown.setAutoReverse(true);
        
        TranslateTransition translateTransitionDateDown =
            new TranslateTransition(Duration.millis(250), publishLabel);
        translateTransitionDateDown.setFromY(-25);
        translateTransitionDateDown.setToY(0);
        translateTransitionDateDown.setCycleCount(1);
        translateTransitionDateDown.setAutoReverse(true);
        
        parallelTransitionDown = new ParallelTransition();
        
        parallelTransitionDown.getChildren().addAll(
                scaleTransitionDown,
                translateTransitionTitleDown,
                translateTransitionDateDown,
                translateTransitionPosterDown
        );
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
        }
    }

    public void setFollowButton() {
        try {
            if (PisaFlixServices.authenticationService.isUserLogged()) {
                if (!PisaFlixServices.filmService.isFollowing(film, PisaFlixServices.authenticationService.getLoggedUser())) {
                    
                    if(film.type().equals("SUGGESTED")){
                        suggestLabel.setText("Suggested");
                    }
                    
                    if(film.type().equals("VERY SUGGESTED")){
                        suggestLabel.setText("Very Suggested");
                    }
                    
                    if(film.type().equals("NORMAL")){
                        suggestLabel.setText("");
                    }
                    
                    if(film.type().equals("FRIEND COMMENTED")){
                        suggestLabel.setText("Commented by Friend");
                    }
                    
                } else {
                    followButton.setText("- Unfollow");
                }
            } else {
                followButton.setVisible(false);
                followButton.setManaged(false);
                suggestLabel.setVisible(false);
                suggestLabel.setManaged(false);
                poster.setFitHeight(116);
                poster.setFitWidth(116);
            }
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
    
    @FXML
    private void animationUp(){
        if(PisaFlixServices.authenticationService.isUserLogged())
            parallelTransitionUp.play();
    }
    
    @FXML
    private void animationDown(){
        if(PisaFlixServices.authenticationService.isUserLogged())
            parallelTransitionDown.play(); 
    }
}
