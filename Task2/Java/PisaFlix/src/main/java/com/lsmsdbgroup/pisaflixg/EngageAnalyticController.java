package com.lsmsdbgroup.pisaflixg;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

public class EngageAnalyticController implements Initializable {

    @FXML
    private ComboBox startCombo;
    
    @FXML
    private ComboBox endCombo;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Vector years = new Vector();
        for(int i = 1961; i < 2021; i++)
        {
            years.add(i);
        }
        
        startCombo.getItems().setAll(years);
        endCombo.getItems().setAll(years);
    }    
    
}
