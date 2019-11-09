package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import com.lsmsdbgroup.pisaflix.PisaFlixServices;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;

public class FilmsController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private ScrollPane scrollPane;
    
    @FXML
    private TilePane tilePane;
    
    @FXML
    private TextField titleFilterTextField;
    
    @FXML
    private Button addFilmButton;
    
    @Override
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        try {
            PisaFlixServices.UserManager.checkUserPrivilegesForOperation(PisaFlixServices.UserPrivileges.MODERATOR);
        } catch (PisaFlixServices.UserManager.UserNotLoggedException | PisaFlixServices.UserManager.InvalidPrivilegeLevelException ex) {
            addFilmButton.setVisible(false);
            addFilmButton.setManaged(false); 
        }
        searchFilms(null,null);
    }
    
    private Pane createFilmCardPane(String title, int id){
        Pane pane = new Pane();
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FilmCard.fxml"));
            FilmCardController fcc = new FilmCardController(title, id);
            loader.setController(fcc);
            pane = loader.load();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        
        return pane;
    }
    
    public void populateScrollPane(Set<Film> films){
        tilePane.getChildren().clear();
        String title;
        int id;
        
        Pane pane;
        int i = 0;
        for(Film film: films){
            title = film.getTitle();
            id = film.getIdFilm();
            pane = createFilmCardPane(title, id);
            tilePane.getChildren().add(pane);
        }
    }
    @FXML
    private void filterFilms(){
        String titleFilter = titleFilterTextField.getText();
        
        searchFilms(titleFilter,null);
    }
    
    @FXML
    private void addFilm(){
        try {
            PisaFlixServices.UserManager.checkUserPrivilegesForOperation(PisaFlixServices.UserPrivileges.MODERATOR, "add a new film");
        } catch (PisaFlixServices.UserManager.UserNotLoggedException | PisaFlixServices.UserManager.InvalidPrivilegeLevelException ex) {
            System.out.println(ex.getMessage());
            return;
        }
        App.setMainPane("AddFilm");
    }
    
    @FXML
    private void searchFilms(String titleFilter, Date dateFilter){
        Set<Film> films = PisaFlixServices.FilmManager.getFilmsFiltered(titleFilter, dateFilter, dateFilter);
        //Set<Film> films = PisaFlixServices.FilmManager.getAll();
        populateScrollPane(films);
    }
}
