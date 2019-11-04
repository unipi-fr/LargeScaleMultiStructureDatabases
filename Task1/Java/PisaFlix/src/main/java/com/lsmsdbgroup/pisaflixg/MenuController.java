package com.lsmsdbgroup.pisaflixg;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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
