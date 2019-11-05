package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.Entities.Comment;
import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.PisaFlixServices;
import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
    private HBox preferiteHBox;
    
    @FXML
    private Button preferiteButton;
    
    @FXML
    private Label preferiteLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(PisaFlixServices.Authentication.isUserLogged()){
            commentArea.setPromptText("Write here a comment for the film...");
            commentArea.setEditable(true);
            commentButton.setDisable(false);
            preferiteButton.setDisable(false);
        }
    }
    
    public void setNameLabel(String name) {
        nameLabel.setText(name);
    }
    
    public void setAddress(String address) {
        addressLabel.setText(address);
    }
    
    public void addComment(String comment) {
        TextArea cinemaComment = new TextArea();

        cinemaComment.setText(comment);
        cinemaComment.setEditable(false);
        
        commentVBox.getChildren().add(cinemaComment);
    }
    
    public void setPrefCount(int count){
        preferiteLabel.setText("("+count+")");
    }
    
    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }
    
    public void refreshCinema(){
        int id = cinema.getIdCinema();
        cinema = PisaFlixServices.CinemaManager.getById(id);
    }
    
    public void refreshComment(){
        commentVBox.getChildren().clear();
        Set<Comment> comments = cinema.getCommentSet();
        
        for(Comment comment: comments){
            addComment(comment.getText());
        }
    }
    
    @FXML
    private void addComment() throws IOException {
        String comment = commentArea.getText();
        User user = PisaFlixServices.Authentication.getLoggedUser();
        
        PisaFlixServices.CinemaManager.addComment(comment, user, cinema);
        
        refreshCinema();
        refreshComment();
    }
    
    @FXML
    private void addPreferite() throws IOException {
        User user = PisaFlixServices.Authentication.getLoggedUser();
        cinema.getUserSet().add(user);
        
        PisaFlixServices.CinemaManager.addPreferite(cinema);
        
        refreshCinema();
        
        int count = cinema.getUserSet().size();
        setPrefCount(count);
    }
}
