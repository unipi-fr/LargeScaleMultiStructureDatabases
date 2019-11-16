package com.lsmsdbgroup.pisaflixg;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;

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
        setPane(mainStackPane, "Welcome");
    }

    private Object setPane(Pane pane, String fxml){
        pane.getChildren().clear();
        Pane newLoadedPane;
        try {
            URL resurce = App.class.getResource(fxml + ".fxml");
            if(resurce != null){
                
                FXMLLoader loader = new FXMLLoader(resurce);        
                newLoadedPane = loader.load();
                pane.getChildren().add(newLoadedPane);
                return loader.getController();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }   
        return null;
    }
    
    private void setPane(Pane paneParent, Pane paneChild){
        paneParent.getChildren().clear();
        paneParent.getChildren().add(paneChild);
    }
    
    
    public Object setMainPaneReturnsController(String fxml){
        return setPane(mainStackPane,fxml);
    }

    public void setMainPane(Pane pane)
    {
        setPane(mainStackPane, pane);
    }

    void resetLogin() {
        setPane(loginStackPane, "Login");
    }
    
}
