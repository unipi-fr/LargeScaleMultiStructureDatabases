package com.lsmsdbgroup.pisaflixg;

import javafx.beans.property.StringProperty;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class FilmCardController implements Initializable {
    private StringProperty titleProperty = new SimpleStringProperty();
    
    public FilmCardController(String title){
        titleProperty.set(title);
    }
    
    @FXML
    private Label titleLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        titleLabel.setText(titleProperty.get());
    }
    
    @FXML
    private void showFilm(){     
        App.setMainPane("Primary");
    }
    
}
