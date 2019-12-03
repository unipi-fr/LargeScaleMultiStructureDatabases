package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.pisaflixservices.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CinemaBrowserController extends BrowserController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            super.initialize();
            filterTextField.setPromptText("Name filter");
            searchCinemas(null, null);
        } catch (Exception ex) {
            App.printErrorDialog("Cinemas", "An error occurred loading the page", ex.toString() + "\n" + ex.getMessage());
        }
    }

    public Pane createCardPane(String name, String address, int id) {
        Pane pane = new Pane();
        try {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("CinemaCard.fxml"));
                CinemaCardController ccc = new CinemaCardController(name, address, id);
                loader.setController(ccc);
                pane = loader.load();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (Exception ex) {
            App.printErrorDialog("Cinemas", "An error occurred creating the card", ex.toString() + "\n" + ex.getMessage());
        }

        return pane;
    }

    public void populateScrollPane(Set<Cinema> cinemas) {
        Pane pane;
        String name;
        String address;
        int id;

        tilePane.getChildren().clear();
        for (Cinema cinema : cinemas) {
            name = cinema.getName();
            address = cinema.getAddress();
            id = cinema.getIdCinema();

            pane = createCardPane(name, address, id);
            tilePane.getChildren().add(pane);
        }
    }

    @FXML
    @Override
    public void filter() {
        try {
            String nameFilter = filterTextField.getText();
            searchCinemas(nameFilter, null);
        } catch (Exception ex) {
            App.printErrorDialog("Search Cinemas", "An error occurred loading the cinemas", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    private void searchCinemas(String titleFilter, String addressFilter) {
        try {
            Set<Cinema> cinemas = PisaFlixServices.cinemaService.getFiltered(titleFilter, addressFilter);
            populateScrollPane(cinemas);
        } catch (Exception ex) {
            App.printErrorDialog("Search Cinemas", "An error occurred loading the cinemas", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    @Override
    public void add() {
        App.setMainPageReturnsController("AddCinema");
    }

}
