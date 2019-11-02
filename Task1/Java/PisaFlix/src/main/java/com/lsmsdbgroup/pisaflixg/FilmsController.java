package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import com.lsmsdbgroup.pisaflix.PisaFlixServices;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXMLLoader;

public class FilmsController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private ScrollPane scrollPane;
    
    @FXML
    private TilePane tilePane;
    
    @Override
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        List<Film> films = PisaFlixServices.FilmManager.getAll();
        
        populateScrollPane(films);
    }
    
    private Pane createFilmCardPane(String title){
        Pane pane = new Pane();
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FilmCard.fxml"));
            FilmCardController fcc = new FilmCardController(title);
            loader.setController(fcc);
            pane = loader.load();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        
        return pane;
    }
    
    public void populateScrollPane(List<Film> films){
        Pane pane = new Pane();
        String title;
        
        int i = 0;
        for(Film film: films){
            title = film.getTitle();
            pane = createFilmCardPane(title);
            tilePane.getChildren().add(pane);
        }
    }
    
}
