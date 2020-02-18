package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.pisaflixservices.WikiScraper;
import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.*;
import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class FilmDetailPageController implements Initializable {

    private Film film;

    @FXML
    private Label titleLabel;

    @FXML
    private Label publishDateLabel;

    @FXML
    private VBox commentVBox;

    @FXML
    private Button FollowButton;

    @FXML
    private Label favoriteLabel;

    @FXML
    private Pagination pagination;

    @FXML
    private ScrollPane scrollableDescription;

    @FXML
    private ImageView moviePosterImageView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            if (PisaFlixServices.authenticationService.isUserLogged())
                FollowButton.setDisable(false);
            
            pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex)
                    -> refreshPosts(newIndex.intValue()));
        } catch (Exception ex) {
            App.printErrorDialog("Film Details", "An error occurred in inizialization", ex.toString() + "\n" + ex.getMessage());
        }

        PisaFlixServices.postService.getById(5380L);
    }

    public void setTitleLabel(String title) {
        titleLabel.setText(title);
    }

    public void setPublishDate(String publishDate) {
        publishDateLabel.setText(publishDate);
    }

    private Pane createPost(String username, String timestamp, String commentStr/*, Post post*/) {
        Pane pane = new Pane();
        
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Comment.fxml"));
            //PostController postController = new PostController(username, timestamp, commentStr, 0);
            //postController.setPost(post);
            //loader.setController(postController);
            pane = loader.load();
            
        } catch (IOException ex) {
                App.printErrorDialog("Film Details", "IOException", ex.toString() + "\n" + ex.getMessage());
        } catch (Exception ex) {
            App.printErrorDialog("Film Details", "An error occurred creating the comment", ex.toString() + "\n" + ex.getMessage());
        }
        
        return pane;
    }

    public void addPost(/*Post post*/) {
        String username = "";
        String timestamp = "";
        
        String commentStr = "";

        commentVBox.getChildren().add(createPost(username, timestamp, commentStr/*, post*/));
    }

    public void setFavoriteCount(long count) {
        favoriteLabel.setText("(" + count + ")");
    }

    public void setFilm(Film film) {
        this.film = film;

        setFollowButton();

        setTitleLabel(film.getTitle());
        setPublishDate(film.getPublicationDate().toString());

        //Set<Post> posts = film.getPostSet();
            
        /*posts.forEach((post) -> {
            addPost(post);
        });*/

        WikiScraper scraper = new WikiScraper(film.getWikiPage());
        String url = scraper.scrapePosterLink();
        if(url != null){
            moviePosterImageView.setImage(new Image("https:" + url));
        }
    }

    public void setFollowButton() {
        
    }

    public void refreshFilm() {
        film = PisaFlixServices.filmService.getById(film.getId());
    }

    public int getCurrentPage() {
        return pagination.getCurrentPageIndex();
    }

    public void refreshPosts(int page) {
        
    }

    @FXML
    private void setFollowUnfollow() {
        
    }

}
