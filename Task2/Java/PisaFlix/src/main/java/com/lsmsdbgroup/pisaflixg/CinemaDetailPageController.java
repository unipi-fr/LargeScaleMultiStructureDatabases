package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
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
    private Button deleteCinemaButton;
    
    @FXML
    private Button modifyCinemaButton;

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
        
        try {
            PisaFlixServices.userService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR);
        } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
            deleteCinemaButton.setVisible(false);
            deleteCinemaButton.setManaged(false); 
            
            modifyCinemaButton.setVisible(false);
            modifyCinemaButton.setManaged(false);
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
        
        setFavoriteButton();

        setNameLabel(cinema.getName());
        setAddress(cinema.getAddress());

        Set<Comment> comments = cinema.getCommentSet();

        comments.forEach((comment) -> {
            addComment(comment);
        });

        setFavoriteCount(cinema.getUserSet().size());
        }catch(Exception ex){
            App.printErrorDialog("Cinema", "An error occurred", ex.toString() + "\n" + ex.getMessage());
        }
    }
    
    public void setFavoriteButton(){
        try{
        if(PisaFlixServices.authenticationService.isUserLogged())
        {
            User userLogged = PisaFlixServices.authenticationService.getLoggedUser();
            
            Set<User> users = cinema.getUserSet();
            
            if(users.contains(userLogged))
            {
                favoriteButton.setText("- Favorite");
            }
        }
        }catch(Exception ex){
            App.printErrorDialog("Favourites", "An error occurred", ex.toString() + "\n" + ex.getMessage());
        }
    }

    public void refreshCinema() {
        try{
        String id = cinema.getIdCinema();
        cinema = PisaFlixServices.cinemaService.getById(id);
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
    private void clickDeleteCinemaButton(){
        try{
        if(!App.printConfirmationDialog("Deleting cinema", "You're deleting the cinema", "Are you sure do you want continue?")){
            return;
        }
        try {
            PisaFlixServices.cinemaService.deleteCinema(this.cinema);
            App.setMainPageReturnsController("Cinemas");
        } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
            System.out.println(ex.getMessage());
        }
        }catch(Exception ex){
            App.printErrorDialog("Delete Cinema", "An error occurred deleting the cinema", ex.toString() + "\n" + ex.getMessage());
        }
    }
    
    @FXML
    private void clickModifyCinemaButton(){      
        AddCinemaController addCinemaController = (AddCinemaController) App.setMainPageReturnsController("AddCinema");
        addCinemaController.SetCinema(this.cinema);
    }

    @FXML
    private void addComment() throws IOException {
        try{
        String comment = commentArea.getText();
        User user = PisaFlixServices.authenticationService.getLoggedUser();

        PisaFlixServices.commentService.addCinemaComment(comment, user, cinema);

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

        setFavoriteCount(cinema.getUserSet().size());
        }catch(Exception ex){
            App.printErrorDialog("Favourites", "An error occurred updating favourites", ex.toString() + "\n" + ex.getMessage());
        }
    }
}
