package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflix.pisaflixservices.UserPrivileges;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.InvalidPrivilegeLevelException;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.UserNotLoggedException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.*;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class CinemaCardController implements Initializable {

    private final StringProperty nameProperty = new SimpleStringProperty();
    private final StringProperty addressProperty = new SimpleStringProperty();

    private final int cinemaId;
    
    private Cinema cinema;

    public CinemaCardController(String name, String address, int id) {
        nameProperty.set(name);
        addressProperty.set(address);
        cinemaId = id;
    }

    @FXML
    private Label nameLabel;

    @FXML
    private Label addressLabel;
    
    @FXML
    private MenuItem modifyCinemaMenuItem;
    
    @FXML
    private MenuItem deleteCinemaMenuItem;
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cinema = PisaFlixServices.cinemaService.getById(cinemaId);
        nameLabel.setText(nameProperty.get());
        addressLabel.setText(addressProperty.get());
        
        
        if (!PisaFlixServices.authenticationService.isUserLogged() || (PisaFlixServices.authenticationService.getLoggedUser().getPrivilegeLevel() < UserPrivileges.MODERATOR.getValue())) {
            deleteCinemaMenuItem.setVisible(false);
            modifyCinemaMenuItem.setVisible(false);
        } else {
            deleteCinemaMenuItem.setVisible(true);
            modifyCinemaMenuItem.setVisible(true);
        }
    }

    @FXML
    private void clickMouse(MouseEvent event) {
        if (event.isSecondaryButtonDown()) {
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CinemaDetailPage.fxml"));
            GridPane gridPane = null;
            try {
                gridPane = loader.load();
            } catch (IOException ex) {
                App.printErrorDialog("Cinema Card", "An error occurred loading the card", ex.toString() + "\n" + ex.getMessage());
            }
            CinemaDetailPageController cinemaDetailPageController = loader.getController();
            cinemaDetailPageController.setCinema(cinema);
            App.setMainPane(gridPane);
        } catch (Exception ex) {
            App.printErrorDialog("Cinema Card", "An error occurred loading the card", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    private void deleteCinema(){
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
    private void modifyCinema(){      
        AddCinemaController addCinemaController = (AddCinemaController) App.setMainPageReturnsController("AddCinema");
        addCinemaController.SetCinema(this.cinema);
    }
}
