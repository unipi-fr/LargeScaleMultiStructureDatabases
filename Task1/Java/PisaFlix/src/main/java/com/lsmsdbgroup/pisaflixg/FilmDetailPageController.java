package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflix.pisaflixservices.UserPrivileges;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Set;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.*;
import javafx.scene.layout.*;

public class FilmDetailPageController implements Initializable {

    private Film film;

    @FXML
    private Label titleLabel;

    @FXML
    private ImageView moviePosterImageView;

    @FXML
    private Label publishDateLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private ScrollPane commentScrollPane;

    @FXML
    private VBox commentVBox;

    @FXML
    private TextArea commentArea;

    @FXML
    private Button commentButton;

    @FXML
    private HBox favoriteHBox;

    @FXML
    private Button favoriteButton;
    
    @FXML
    private Button deleteFilmButton;
    
    @FXML
    private Button modifyFilmButton;

    @FXML
    private Label favoriteLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (PisaFlixServices.authenticationService.isUserLogged()) {
            commentArea.setPromptText("Write here a comment for the film...");
            commentArea.setEditable(true);
            commentButton.setDisable(false);
            favoriteButton.setDisable(false);
        }
        
        try {
            PisaFlixServices.userService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR);
        } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
            deleteFilmButton.setVisible(false);
            deleteFilmButton.setManaged(false);
            modifyFilmButton.setVisible(false);
            modifyFilmButton.setManaged(false);
        }
        
    }

    public void setTitleLabel(String title) {
        titleLabel.setText(title);
    }

    public void setPublishDate(String publishDate) {
        publishDateLabel.setText(publishDate);
    }

    public void setDescription(String Description) {
        descriptionLabel.setText(Description);
    }

    private Pane createComment(String username, String timestamp, String commentStr, Comment comment){
        Pane pane = new Pane();
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Comment.fxml"));
            CommentController commentController = new CommentController(username, timestamp, commentStr, 0);
            commentController.setComment(comment);
            loader.setController(commentController);
            pane = loader.load();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        
        return pane;
    }
    
    public void addComment(Comment comment) {
        String username = comment.getIdUser().getUsername();
        String timestamp = comment.getTimestamp().toString();
        String commentStr = comment.getText();
        
        commentVBox.getChildren().add(createComment(username, timestamp, commentStr, comment));
        /*TextArea filmComment = new TextArea();

        filmComment.setText(comment);
        filmComment.setEditable(false);

        commentVBox.getChildren().add(filmComment);*/
    }

    public void setFavoriteCount(int count) {
        favoriteLabel.setText("(" + count + ")");
    }

    public void setFilm(Film film) {
        this.film = film;
        
        setFavoriteButton();

        setTitleLabel(film.getTitle());
        setPublishDate(film.getPublicationDate().toString());
        setDescription(film.getDescription());

        Set<Comment> comments = film.getCommentSet();

        comments.forEach((comment) -> {
            addComment(comment);
        });

        setFavoriteCount(film.getUserSet().size());
    }
    
    public void setFavoriteButton(){
        if(PisaFlixServices.authenticationService.isUserLogged())
        {
            User userLogged = PisaFlixServices.authenticationService.getLoggedUser();
            
            Set<User> users = film.getUserSet();
            
            if(users.contains(userLogged))
            {
                favoriteButton.setText("Remove favorite");
            }
        }
    }

    public void refreshFilm() {
        int id = film.getIdFilm();
        film = PisaFlixServices.filmService.getById(id);
    }

    public void refreshComment() {
        commentVBox.getChildren().clear();
        Set<Comment> comments = film.getCommentSet();

        comments.forEach((comment) -> {
            addComment(comment);
        });
    }

    @FXML
    private void clickDeleteFilmButton(){
        
        if(!App.printConfirmationDialog("Deleting film", "You're deleting the film", "Are you sure do you want continue?")){
            return;
        }
        try {
            PisaFlixServices.filmService.deleteFilm(this.film.getIdFilm());
            App.setMainPageReturnsController("Films");
        } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    @FXML
    private void clickModifyFilmButton(){      
        AddFilmController afc = (AddFilmController) App.setMainPageReturnsController("AddFilm");
        afc.setFilm(this.film);
    }
    
    @FXML
    private void addComment() throws IOException {
        String comment = commentArea.getText();
        User user = PisaFlixServices.authenticationService.getLoggedUser();

        PisaFlixServices.commentService.addFilmComment(comment, user, film);

        refreshFilm();
        refreshComment();
    }

    @FXML
    private void favoriteAddRemove() throws IOException {
        if(!PisaFlixServices.authenticationService.isUserLogged())
            return;
        
        User user = PisaFlixServices.authenticationService.getLoggedUser();
        
        if(favoriteButton.getText().equals("Add favorite")){
            PisaFlixServices.filmService.addFavorite(film, user);
            
            favoriteButton.setText("Remove favorite");
        } else {
            PisaFlixServices.filmService.removeFavourite(film, user);
            
            favoriteButton.setText("Add favorite");
        }
        
        refreshFilm();

        setFavoriteCount(film.getUserSet().size());
    }
}
