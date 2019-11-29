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

public class CinemasController implements Initializable {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TilePane tilePane;

    @FXML
    private TextField nameFilterTextField;

    @FXML
    private Button addCinemaButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            try {
                PisaFlixServices.userService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR);
            } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
                addCinemaButton.setVisible(false);
                addCinemaButton.setManaged(false);
            }

            Set<Cinema> cinemas = PisaFlixServices.cinemaService.getAll();

            populateScrollPane(cinemas);
        } catch (Exception ex) {
            App.printErrorDialog("Cinemas", "An error occurred loading the page", ex.toString() + "\n" + ex.getMessage());
        }
    }

    private Pane createFilmCardPane(String name, String address, int id) {
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
            App.printErrorDialog("Cinema Card", "An error occurred creating the card", ex.toString() + "\n" + ex.getMessage());
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

            pane = createFilmCardPane(name, address, id);
            tilePane.getChildren().add(pane);
        }
    }

    @FXML
    private void filterCinemas() {
        try {
            String nameFilter = nameFilterTextField.getText();
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
    private void addCinema() {
        App.setMainPageReturnsController("AddCinema");
    }

}
