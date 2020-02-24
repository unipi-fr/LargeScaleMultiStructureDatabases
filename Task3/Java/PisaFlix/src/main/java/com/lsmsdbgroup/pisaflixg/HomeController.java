package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class HomeController implements Initializable {

    @FXML
    private VBox postVBox;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        User user;
            
        user = PisaFlixServices.authenticationService.getLoggedUser();
        
        if(user == null)
            return;
        
        populateHome(user);
    }    
    
    private void populateHome(User user){
        
    }
}
