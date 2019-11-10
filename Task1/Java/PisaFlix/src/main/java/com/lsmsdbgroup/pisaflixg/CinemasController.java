package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.PisaFlixServices;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CinemasController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TilePane tilePane;
    
    @FXML
    private Button addCinemaButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            PisaFlixServices.UserManager.checkUserPrivilegesForOperation(PisaFlixServices.UserPrivileges.MODERATOR);
        } catch (PisaFlixServices.UserManager.UserNotLoggedException | PisaFlixServices.UserManager.InvalidPrivilegeLevelException ex) {
            addCinemaButton.setVisible(false);
            addCinemaButton.setManaged(false);
        }
        
        List<Cinema> cinemas = new ArrayList<>(PisaFlixServices.CinemaManager.getAll());

        populateScrollPane(cinemas);
    }

    private Pane createFilmCardPane(String name, String address, int id) {
        Pane pane = new Pane();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CinemaCard.fxml"));
            CinemaCardController ccc = new CinemaCardController(name, address, id);
            loader.setController(ccc);
            pane = loader.load();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return pane;
    }

    public void populateScrollPane(List<Cinema> cinemas) {
        Pane pane = new Pane();
        String name;
        String address;
        int id;

        for (Cinema cinema : cinemas) {
            name = cinema.getName();
            address = cinema.getAddress();
            id = cinema.getIdCinema();
            
            pane = createFilmCardPane(name, address, id);
            tilePane.getChildren().add(pane);
        }
    }
   
    @FXML
    private void addCinema(){
        App.setMainPane("AddCinema");
    }

}
