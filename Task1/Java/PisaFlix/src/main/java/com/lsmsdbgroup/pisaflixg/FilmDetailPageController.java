package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.PisaFlixServices;
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
    private Label favoriteLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (PisaFlixServices.Authentication.isUserLogged()) {
            commentArea.setPromptText("Write here a comment for the film...");
            commentArea.setEditable(true);
            commentButton.setDisable(false);
            favoriteButton.setDisable(false);
        }
        
        try {
            PisaFlixServices.UserManager.checkUserPrivilegesForOperation(PisaFlixServices.UserPrivileges.MODERATOR);
        } catch (PisaFlixServices.UserManager.UserNotLoggedException | PisaFlixServices.UserManager.InvalidPrivilegeLevelException ex) {
            deleteFilmButton.setVisible(false);
            deleteFilmButton.setManaged(false);
            deleteFilmButton.setDisable(false); 
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

    private Pane createComment(String username, String timestamp, String comment){
        Pane pane = new Pane();
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Comment.fxml"));
            CommentController commentController = new CommentController(username, timestamp, comment);
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
        
        commentVBox.getChildren().add(createComment(username, timestamp, commentStr));
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
        if(PisaFlixServices.Authentication.isUserLogged())
        {
            User userLogged = PisaFlixServices.Authentication.getLoggedUser();
            
            Set<User> users = film.getUserSet();
            
            if(users.contains(userLogged))
            {
                favoriteButton.setText("Remove favorite");
            }
        }
    }

    public void refreshFilm() {
        int id = film.getIdFilm();
        film = PisaFlixServices.FilmManager.getById(id);
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
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Deleting film");
        alert.setHeaderText("You're deleting the film");
        alert.setContentText("Are you sure do you want continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK){
            return;
        }
        try {
            PisaFlixServices.FilmManager.deleteFilm(this.film.getIdFilm());
            App.setMainPane("Films");
        } catch (PisaFlixServices.UserManager.UserNotLoggedException | PisaFlixServices.UserManager.InvalidPrivilegeLevelException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    @FXML
    private void addComment() throws IOException {
        String comment = commentArea.getText();
        User user = PisaFlixServices.Authentication.getLoggedUser();

        PisaFlixServices.FilmManager.addComment(comment, user, film);

        refreshFilm();
        refreshComment();
    }

    @FXML
    private void favoriteAddRemove() throws IOException {
        if(!PisaFlixServices.Authentication.isUserLogged())
            return;
        
        User user = PisaFlixServices.Authentication.getLoggedUser();
        
        if(favoriteButton.getText().equals("Add favorite")){
            PisaFlixServices.FilmManager.addFavorite(film, user);
            
            favoriteButton.setText("Remove favorite");
        } else {
            PisaFlixServices.FilmManager.removeFavourite(film, user);
            
            favoriteButton.setText("Add favorite");
        }
        
        refreshFilm();

        setFavoriteCount(film.getUserSet().size());
    }
}
