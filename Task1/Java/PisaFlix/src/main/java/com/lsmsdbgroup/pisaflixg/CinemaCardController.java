package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.Entities.Comment;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class CinemaCardController implements Initializable {

    private final StringProperty nameProperty = new SimpleStringProperty();
    private final StringProperty addressProperty = new SimpleStringProperty();

    private final int cinemaId;

    public CinemaCardController(String name, String address, int id) {
        nameProperty.set(name);
        addressProperty.set(address);
        cinemaId = id;
    }

    @FXML
    private Label nameLabel;
    
    @FXML
    private Label addressLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameLabel.setText(nameProperty.get());
        addressLabel.setText(addressProperty.get());
    }

    @FXML
    private void showCinema() {
        Cinema cinema = PisaFlixServices.cinemaService.getById(cinemaId);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CinemaDetailPage.fxml"));

        AnchorPane anchorPane = null;

        try {
            anchorPane = loader.load();
        } catch (IOException ex) {
            System.out.println("hello");
            System.out.println(ex.getMessage());
        }

        CinemaDetailPageController cinemaDetailPageController = loader.getController();

        cinemaDetailPageController.setCinema(cinema);

        App.setMainPane(anchorPane);
    }

}
