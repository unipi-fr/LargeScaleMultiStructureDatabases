package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.PisaFlixServices;
import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    private Label favoriteLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (PisaFlixServices.Authentication.isUserLogged()) {
            commentArea.setPromptText("Write here a comment for the film...");
            commentArea.setEditable(true);
            commentButton.setDisable(false);
            favoriteButton.setDisable(false);
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

    public void addComment(String comment) {
        TextArea filmComment = new TextArea();

        filmComment.setText(comment);
        filmComment.setEditable(false);

        commentVBox.getChildren().add(filmComment);
    }

    public void setFavoriteCount(int count) {
        favoriteLabel.setText("(" + count + ")");
    }

    public void setFilm(Film film) {
        this.film = film;
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
            addComment(comment.getText());
        });
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
