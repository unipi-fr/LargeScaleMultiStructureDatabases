package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class CinemaDetailPageController implements Initializable {

    private Cinema cinema;

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView cinemaImageView;

    @FXML
    private Label addressLabel;

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
        try{
            if (PisaFlixServices.authenticationService.isUserLogged()) {
                commentArea.setPromptText("Write here a comment for the film...");
                commentArea.setEditable(true);
                commentButton.setDisable(false);
                favoriteButton.setDisable(false);
            }
        }catch(Exception ex){
            App.printErrorDialog("Cinema Details", "An error occurred loading the page", ex.toString() + "\n" + ex.getMessage());
        }
    }

    public void setNameLabel(String name) {
        nameLabel.setText(name);
    }

    public void setAddress(String address) {
        addressLabel.setText(address);
    }
    
    private Pane createComment(String username, String timestamp, String commentStr, Comment comment){
        Pane pane = new Pane();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Comment.fxml"));
            CommentController commentController = new CommentController(username, timestamp, commentStr, 1);
            commentController.setComment(comment);
            loader.setController(commentController);
            pane = loader.load();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }    
        return pane;
    }

    public void addComment(Comment comment) {
        try{
        String username = comment.getUser().getUsername();
        String timestamp = comment.getTimestamp().toString();
        String commentStr = comment.getText();
        
        commentVBox.getChildren().add(createComment(username, timestamp, commentStr, comment));
        }catch(Exception ex){
            App.printErrorDialog("Comment", "An error occurred creating the comment", ex.toString() + "\n" + ex.getMessage());
        }
    }

    public void setFavoriteCount(int count) {
        favoriteLabel.setText("(" + count + ")");
    }

    public void setCinema(Cinema cinema) {
        try{
            this.cinema = cinema;
            PisaFlixServices.cinemaService.refreshCommentSet(cinema);
            
            setFavoriteButton();

            setNameLabel(cinema.getName());
            setAddress(cinema.getAddress());

            Set<Comment> comments = cinema.getCommentSet();

            comments.forEach((comment) -> {
                addComment(comment);
            });

            setFavoriteCount(cinema.getFavoriteCounter());
            
        }catch(Exception ex){
            App.printErrorDialog("Cinema", "An error occurred", ex.toString() + "\n" + ex.getMessage());
        }
    }
    
    public void setFavoriteButton(){
        if(PisaFlixServices.authenticationService.isUserLogged())
        {
            User userLogged = PisaFlixServices.authenticationService.getLoggedUser();

            Set<Cinema> cinemas = userLogged.getCinemaSet();

            if(cinemas.contains(cinema))
            {
                favoriteButton.setText("- Favorite");
            }
        }
    }

    public void refreshCinema() {
        try{
        cinema = PisaFlixServices.cinemaService.getById(cinema.getId());
        PisaFlixServices.cinemaService.refreshCommentSet(cinema);
        }catch(Exception ex){
            App.printErrorDialog("Cinema", "An error occurred", ex.toString() + "\n" + ex.getMessage());
        }
    }

    public void refreshComment() {
        try{       
        commentVBox.getChildren().clear();
        Set<Comment> comments = cinema.getCommentSet();

        comments.forEach((comment) -> {
            addComment(comment);
        });
        }catch(Exception ex){
            App.printErrorDialog("Comments", "An error occurred loading the comments", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    private void addComment() throws IOException {
        try{
        String comment = commentArea.getText();
        User user = PisaFlixServices.authenticationService.getLoggedUser();

        PisaFlixServices.commentService.addComment(comment, user, cinema);

        refreshCinema();
        refreshComment();
        }catch(Exception ex){
            App.printErrorDialog("Comment", "An error occurred creating the comment", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    private void favoriteAddRemove() throws IOException {
        try{
        if(!PisaFlixServices.authenticationService.isUserLogged())
            return;
        
        User user = PisaFlixServices.authenticationService.getLoggedUser();
        
        if(favoriteButton.getText().equals("+ Favorite")){
            PisaFlixServices.cinemaService.addFavorite(cinema, user);
            
            favoriteButton.setText("- Favorite");
        } else {
            PisaFlixServices.cinemaService.removeFavourite(cinema, user);
            
            favoriteButton.setText("+ Favorite");
        }
        
        refreshCinema();

        setFavoriteCount(cinema.getFavoriteCounter());
        }catch(Exception ex){
            App.printErrorDialog("Favourites", "An error occurred updating favourites", ex.toString() + "\n" + ex.getMessage());
        }
    }
}
