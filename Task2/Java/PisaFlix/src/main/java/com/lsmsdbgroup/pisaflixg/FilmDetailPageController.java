package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.Scraping.WikiScraper;
import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.Entities.Engage.EngageType;
import com.lsmsdbgroup.pisaflix.pisaflixservices.*;
import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class FilmDetailPageController implements Initializable {

    private Film film;

    @FXML
    private Label titleLabel;

    @FXML
    private Label publishDateLabel;

    @FXML
    private Text descriptionLabel;

    @FXML
    private VBox commentVBox;

    @FXML
    private TextArea commentArea;

    @FXML
    private Button commentButton;

    @FXML
    private Button favoriteButton;

    @FXML
    private Label favoriteLabel;

    @FXML
    private Pagination pagination;

    @FXML
    private ScrollPane scrollableDescription;

    @FXML
    private Text scrollableDescriptionLabel;

    @FXML
    private ImageView moviePosterImageView;

    private boolean newVisit = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            if (PisaFlixServices.authenticationService.isUserLogged()) {
                commentArea.setPromptText("Write here a comment for the film...");
                commentArea.setEditable(true);
                commentButton.setDisable(false);
                favoriteButton.setDisable(false);
            }
            pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex)
                    -> refreshComments(newIndex.intValue()));
        } catch (Exception ex) {
            App.printErrorDialog("Film Details", "An error occurred in inizialization", ex.toString() + "\n" + ex.getMessage());
        }

    }

    public void setTitleLabel(String title) {
        titleLabel.setText(title);
    }

    public void setPublishDate(String publishDate) {
        publishDateLabel.setText(publishDate);
    }

    public void setDescription(String description) {
        if (description.length() > 1000) {
            scrollableDescriptionLabel.setText(description);
            descriptionLabel.setVisible(false);
            descriptionLabel.setManaged(false);
        } else {
            descriptionLabel.setText(description);
            scrollableDescription.setVisible(false);
            scrollableDescription.setManaged(false);
        }
    }

    private Pane createComment(String username, String timestamp, String commentStr, Comment comment) {
        Pane pane = new Pane();
        try {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Comment.fxml"));
                CommentController commentController = new CommentController(username, timestamp, commentStr, 0);
                commentController.setComment(comment);
                loader.setController(commentController);
                pane = loader.load();
            } catch (IOException ex) {
                App.printErrorDialog("Film Details", "IOException", ex.toString() + "\n" + ex.getMessage());
            }
        } catch (Exception ex) {
            App.printErrorDialog("Film Details", "An error occurred creating the comment", ex.toString() + "\n" + ex.getMessage());
        }
        return pane;
    }

    public void addComment(Comment comment) {
        String username = comment.getUser().getUsername();
        String timestamp;
        if (comment.getLastModified() != null) {
            timestamp = comment.getTimestamp().toString() + ", Last Modified at: " + comment.getLastModified().toString();
        } else {
            timestamp = comment.getTimestamp().toString();
        }
        String commentStr = comment.getText();

        commentVBox.getChildren().add(createComment(username, timestamp, commentStr, comment));
    }

    public void setFavoriteCount(long count) {
        favoriteLabel.setText("(" + count + ")");
    }

    public void setFilm(Film film) {
        this.film = film;
        PisaFlixServices.filmService.getRecentComments(film);

        setFavoriteButton();

        setTitleLabel(film.getTitle());
        setPublishDate(film.getPublicationDate().toString());
        setDescription(film.getDescription());

        Set<Comment> comments = film.getCommentSet();

        comments.forEach((comment) -> {
            addComment(comment);
        });

        setFavoriteCount(PisaFlixServices.engageService.count(film, EngageType.FAVOURITE));

        if (newVisit) {
            newVisit = false;
            if (PisaFlixServices.authenticationService.isUserLogged()) {
                PisaFlixServices.engageService.create(PisaFlixServices.authenticationService.getLoggedUser(), film, EngageType.VIEW);
            } else {
                PisaFlixServices.engageService.create(new User("anonymous"), film, EngageType.VIEW);
            }
        }

        WikiScraper scraper = new WikiScraper(film.getWikiPage());
        moviePosterImageView.setImage(new Image("https:" + scraper.scrapePosterLink()));
        /*scraper.setOnSucceeded((succeededEvent) -> {
            moviePosterImageView.setImage(new Image("https:" + scraper.getValue()));
        });
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(scraper);
        executorService.shutdown();*/
        refreshPageCount();
    }

    public void setFavoriteButton() {
        if (PisaFlixServices.authenticationService.isUserLogged()) {
            User userLogged = PisaFlixServices.authenticationService.getLoggedUser();

            if (PisaFlixServices.engageService.isAlreadyPresent(userLogged, film, EngageType.FAVOURITE)) {
                favoriteButton.setText("- Favorite");
            }
        }
    }

    public void refreshFilm() {
        film = PisaFlixServices.filmService.getById(film.getId());
        PisaFlixServices.filmService.getRecentComments(film);
    }

    public int getCurrentPage() {
        return pagination.getCurrentPageIndex();
    }

    public void refreshComments(int page) {
        PisaFlixServices.filmService.getCommentPage(film, page);
        commentVBox.getChildren().clear();
        Set<Comment> comments = film.getCommentSet();

        comments.forEach((comment) -> {
            addComment(comment);
        });
        refreshPageCount();
    }

    @FXML
    private void addComment() throws IOException {
        try {
            String comment = commentArea.getText();
            User user = PisaFlixServices.authenticationService.getLoggedUser();

            PisaFlixServices.filmService.addComment(film, user, comment);

            refreshFilm();
            refreshComments(getCurrentPage());
        } catch (Exception ex) {
            App.printErrorDialog("Comments", "An error occurred loading the comments", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    private void favoriteAddRemove() {
        try {
            if (!PisaFlixServices.authenticationService.isUserLogged()) {
                return;
            }
            if (favoriteButton.getText().equals("+ Favorite")) {
                PisaFlixServices.engageService.create(PisaFlixServices.authenticationService.getLoggedUser(), film, EngageType.FAVOURITE);
                favoriteButton.setText("- Favorite");
            } else {
                PisaFlixServices.engageService.deleteFiltred(PisaFlixServices.authenticationService.getLoggedUser(), film, EngageType.FAVOURITE);
                favoriteButton.setText("+ Favorite");
            }

            refreshFilm();

            setFavoriteCount(PisaFlixServices.engageService.count(film, EngageType.FAVOURITE));
        } catch (Exception ex) {
            App.printErrorDialog("Favourites", "An error occurred updating favourites", ex.toString() + "\n" + ex.getMessage());
        }
    }

    private void refreshPageCount() {
        long savedComments = PisaFlixServices.engageService.count(film, EngageType.COMMENT);
        if (savedComments == 0) {
            pagination.pageCountProperty().setValue(1);
        } else {
            long pageLenght = PisaFlixServices.filmService.getCommentPageSize();
            long pageNumber = (long) Math.floor((savedComments - 1) / pageLenght) + 2;
            pagination.pageCountProperty().setValue(pageNumber);
        }
    }
}
