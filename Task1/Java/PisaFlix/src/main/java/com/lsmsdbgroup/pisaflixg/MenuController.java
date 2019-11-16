package com.lsmsdbgroup.pisaflixg;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class MenuController implements Initializable {

    final BooleanProperty firstTime = new SimpleBooleanProperty(true);
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private Button filmsButton;
    @FXML
    private Button cinemasButton;
    @FXML
    private Button projectionsButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filmsButton.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                anchorPane.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });
    }  
    
    @FXML
    private void clickFilmsButton(){     
        App.setMainPageReturnsController("Films");
    }
    
    @FXML
    private void clickCinemasButton(){     
        App.setMainPageReturnsController("Cinemas");
    }
    
    @FXML
    private void clickProjectionsButton(){     
        App.setMainPageReturnsController("Projections");
    }
    
}
