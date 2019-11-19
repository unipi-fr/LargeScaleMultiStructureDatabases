package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import java.io.IOException;
import java.util.Set;
import javafx.fxml.FXMLLoader;

public class UsersController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private ScrollPane scrollPane;
    
    @FXML
    private TilePane tilePane;
    
    @FXML
    private TextField titleFilterTextField;
    
    
    @Override
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        searchUsers(null);
    }
    
    private Pane createUserCardPane(String title, String publishDate, int id){
        Pane pane = new Pane();
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FilmCard.fxml"));
            UserCardController fcc = new UserCardController(title, publishDate, id);
            loader.setController(fcc);
            pane = loader.load();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        
        return pane;
    }
    
    public void populateScrollPane(Set<User> users){
        tilePane.getChildren().clear();
        String title;
        String publishDate;
        int id;
        
        Pane pane;
        int i = 0;
        for(User user: users){
            title = user.getTitle();
            publishDate = user.getPublicationDate().toString();
            id = user.getIdFilm();
            
            pane = createFilmCardPane(title, publishDate, id);
            tilePane.getChildren().add(pane);
        }
    }
    @FXML
    private void filterFilms(){
        String usernameFilter = titleFilterTextField.getText();
        
        searchUsers(usernameFilter);
    }

    
    @FXML
    private void searchUsers(String usernameFilter){
        Set<User> users = PisaFlixServices.userService.getusers(usernameFilter);
        populateScrollPane(users);
    }
}
