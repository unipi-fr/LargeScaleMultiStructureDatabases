package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.Entities.Comment;
import com.lsmsdbgroup.pisaflix.PisaFlixServices;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class CinemaCardController implements Initializable {
    
    private StringProperty nameProperty = new SimpleStringProperty();
    
    private int cinemaId;
    
    public CinemaCardController(String name, int id){
        nameProperty.set(name);
        cinemaId = id;
    }
    
    @FXML
    private Label nameLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameLabel.setText(nameProperty.get());
    }
    
    @FXML
    private void showCinema(){
        Cinema cinema = PisaFlixServices.CinemaManager.getById(cinemaId);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CinemaDetailPage.fxml"));
        
        AnchorPane anchorPane = null;
        
        try {
            anchorPane = loader.load();
        } catch (IOException ex) {
            System.out.println("hello");
            System.out.println(ex.getMessage());
        }
        
        CinemaDetailPageController cdc = loader.getController();
        
        cdc.setCinema(cinema);
        
        cdc.setNameLabel(cinema.getName());
        cdc.setAddress(cinema.getAddress());
        
        Collection<Comment> comments = cinema.getCommentCollection();
        
        for(Comment comment: comments){
            cdc.addComment(comment.getText());
        }
        
        cdc.setPrefCount(cinema.getUserCollection().size());
        
        App.setMainPane(anchorPane);
    }
    
}
