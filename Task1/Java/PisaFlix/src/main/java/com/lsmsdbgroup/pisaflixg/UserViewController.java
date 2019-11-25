package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflix.pisaflixservices.UserPrivileges;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.InvalidPrivilegeLevelException;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.UserNotLoggedException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class UserViewController implements Initializable {

    private User user;
    
    private ChangeListener<Cinema> cinemaListener;
    
    private ChangeListener<Film> filmListener;
    
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
    private ListView favoriteList;
    
    @FXML
    private ImageView userImage;
    
    @FXML
    private Button filmButton;
    
    @FXML
    private Button cinemaButton;
    
    @FXML 
    private Button deleteButton;
    
    @FXML
    private Button updateButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        user = PisaFlixServices.authenticationService.getLoggedUser();
        
        if(user != null)
        {
            usernameLabel.setText(user.getUsername());
            firstnameLabel.setText(user.getFirstName());
            lastnameLabel.setText(user.getLastName());
            emailLabel.setText(user.getEmail());
            
            commentCounterLabel.setText("(" + user.getCommentSet().size() + ")"); 
        }
        
        Random random = new Random();
        int img = random.nextInt(3) + 1;
        
        File file = new File("src/main/resources/img/user" + img + ".png");
        
        Image image = new Image(file.toURI().toString());
        userImage.setImage(image);
        
        cinemaListener = (ObservableValue<? extends Cinema> observable, Cinema oldValue, Cinema newValue) -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CinemaDetailPage.fxml"));
            
            AnchorPane anchorPane = null;
            
            try {
                anchorPane = loader.load();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            
            CinemaDetailPageController cinemaDetailPageController = loader.getController();
            
            cinemaDetailPageController.setCinema(newValue);
            
            App.setMainPane(anchorPane);
        };
        
        filmListener = (ObservableValue<? extends Film> observable, Film oldValue, Film newValue) -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FilmDetailPage.fxml"));
            
            AnchorPane anchorPane = null;
            
            try {
                anchorPane = loader.load();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            
            FilmDetailPageController filmDetailPageController = loader.getController();
            
            filmDetailPageController.setFilm(newValue);
            
            App.setMainPane(anchorPane);
        };
    }
    
    private boolean canUpdateOrdDeleteProfile(){
        try {
                PisaFlixServices.userService.checkUserPrivilegesForOperation(UserPrivileges.ADMIN, "Update/Delete others account");
            } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
                User u = PisaFlixServices.authenticationService.getLoggedUser();
                if(u == null){
                    return false;
                }
                if(u.getIdUser() != this.user.getIdUser()){
                    return false;
                }
            }
            return true;
    }
    
    public void setUser(User user){
        this.user = user;
        
        usernameLabel.setText(user.getUsername());
        firstnameLabel.setText(user.getFirstName());
        lastnameLabel.setText(user.getLastName());
        emailLabel.setText(user.getEmail());
        
        if(!canUpdateOrdDeleteProfile()){
           deleteButton.setDisable(true);
           updateButton.setDisable(true);
        }
        
        
        commentCounterLabel.setText("(" + user.getCommentSet().size() + ")");
    }
    
    @FXML
    private void showFavoriteFilms(){
        favoriteCounterLabel.setText("(" + user.getFilmSet().size() + ")");
        
        favoriteList.getSelectionModel().selectedItemProperty().removeListener(cinemaListener);
        
        Set<Film> films = user.getFilmSet();
        
        ObservableList<Film> observableFilms = FXCollections.observableArrayList(films);
        favoriteList.setItems(observableFilms);
        
        favoriteList.getSelectionModel().selectedItemProperty().addListener(filmListener);
    }
    
    @FXML
    private void showFavoriteCinema(){
        favoriteCounterLabel.setText("(" + user.getCinemaSet().size() + ")");
        
        favoriteList.getSelectionModel().selectedItemProperty().removeListener(filmListener);
        
        Set<Cinema> cinemas = user.getCinemaSet();
        
        ObservableList<Cinema> observableCinemas = FXCollections.observableArrayList(cinemas);
        favoriteList.setItems(observableCinemas);
        
        favoriteList.getSelectionModel().selectedItemProperty().addListener(cinemaListener);
    }
    
    @FXML
    private void updateProfile(){
        UpdateProfileController upc = (UpdateProfileController) App.setMainPageReturnsController("UpdateProfile");
        upc.setUser(this.user);
    }
    
    @FXML
    private void deleteProfile(){
        User u = this.user;
        if(u == null){          
            return;
        }
        if(!App.printConfirmationDialog("Deleting profile", "You're deleting "+u.getUsername()+" profile", "Are you sure do you want continue?")){
            return;
        }
        try { 
            PisaFlixServices.userService.deleteUserAccount(u);
            App.setMainPageReturnsController("Welcome");
            App.resetLogin();
        } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
