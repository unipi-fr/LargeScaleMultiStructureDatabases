package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
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
    private Button updateButton;
    
    @FXML
    private ListView favoriteList;
    
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
        
        File file = new File("src/main/resources/img/user" + img + ".png");
        
        Image image = new Image(file.toURI().toString());
        userImage.setImage(image);
        
        commentCounterLabel.setText("(" + user.getCommentSet().size() + ")");
        
        cinemaListener = new ChangeListener<Cinema>() {
            @Override
            public void changed(ObservableValue<? extends Cinema> observable, Cinema oldValue, Cinema newValue) {
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
            }
        };
        
        filmListener = new ChangeListener<Film>() {
            @Override
            public void changed(ObservableValue<? extends Film> observable, Film oldValue, Film newValue) {
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
            }
        };
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
        App.setMainPane("UpdateProfile");
    }
    
}
