package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.PisaFlixServices;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class WelcomeBackController implements Initializable{

    @FXML
    private Label userLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String user = PisaFlixServices.Authentication.getLoggedUser().getUsername();
        
        userLabel.setText(user.toUpperCase());
    }
}