package com.lsmsdbgroup.pisaflixg;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;

/**
 * FXML Controller class
 *
 * @author FraRonk
 */
public class MainPageController implements Initializable {

    @FXML
    private StackPane menuStackPane;
    @FXML
    private StackPane loginStackPane;
    @FXML
    private StackPane mainStackPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.setMainPageController(this);
        setPane(menuStackPane, "Menu");
        setPane(loginStackPane, "Login");
        setPane(mainStackPane, "primary");
    }

    private void setPane(Pane pane, String fxml){
        pane.getChildren().clear();
        Pane newLoadedPane;
        try {
            URL resurce = App.class.getResource(fxml + ".fxml");
            if(resurce != null){
                newLoadedPane = FXMLLoader.load(resurce);
                pane.getChildren().add(newLoadedPane);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
    public void setMainPane(String fxml){
        setPane(mainStackPane,fxml);
    }
    
}
