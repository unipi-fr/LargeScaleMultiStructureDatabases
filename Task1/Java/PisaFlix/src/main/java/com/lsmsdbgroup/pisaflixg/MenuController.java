package com.lsmsdbgroup.pisaflixg;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

/**
 * FXML Controller class
 *
 * @author FraRonk
 */
public class MenuController implements Initializable {

    @FXML
    private Button filmsButton;
    @FXML
    private Button cinemasButton;
    @FXML
    private Button projectionsButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
    
    @FXML
    private void clickFilmsButton(){     
        App.setMainPane("Films");
    }
    
    @FXML
    private void clickCinemasButton(){     
        App.setMainPane("Cinemas");
    }
    
    @FXML
    private void clickProjectionsButton(){     
        App.setMainPane("Projections");
    }
    
}
