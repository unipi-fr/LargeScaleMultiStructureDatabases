package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.PisaFlixServices;
import java.io.File;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UserViewController implements Initializable {

    private User user;
    
    @FXML
    private Label usernameLabel;
    
    @FXML
    private Label firstnameLabel;
    
    @FXML
    private Label lastnameLabel;
    
    @FXML
    private Label emailLabel;
    
    @FXML
    private Label favoriteCounterLabel;
    
    @FXML
    private Label commentCounterLabel;
    
    @FXML
    private Button updateButton;
    
    @FXML
    private ListView favoriteFilmList;
    
    @FXML
    private ImageView userImage;
    
    @FXML
    private Button filmButton;
    
    @FXML
    private Button cinemaButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        user = PisaFlixServices.Authentication.getLoggedUser();
        
        usernameLabel.setText(user.getUsername());
        firstnameLabel.setText(user.getFirstName());
        lastnameLabel.setText(user.getLastName());
        emailLabel.setText(user.getEmail());
        
        Random random = new Random();
        int img = random.nextInt(3) + 1;
        
        System.out.println(img);
        
        File file = new File("src/main/resources/img/user" + img + ".png");
        
        System.out.println(file.toURI().toString());
        
        Image image = new Image(file.toURI().toString());
        userImage.setImage(image);
        
        commentCounterLabel.setText("(" + user.getCommentSet().size() + ")");
    }
    
    @FXML
    private void showFavoriteFilms(){
        favoriteCounterLabel.setText("(" + user.getFilmSet().size() + ")");
    }
    
    @FXML
    private void showFavoriteCinema(){
        favoriteCounterLabel.setText("(" + user.getCinemaSet().size() + ")");
    }
    
}
