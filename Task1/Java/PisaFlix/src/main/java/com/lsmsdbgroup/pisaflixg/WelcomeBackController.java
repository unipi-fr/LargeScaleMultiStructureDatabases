package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.control.Label;

public class WelcomeBackController implements Initializable {

    @FXML
    private Label userLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            String user = PisaFlixServices.authenticationService.getLoggedUser().getUsername();

            userLabel.setText(user.toUpperCase());
        } catch (Exception ex) {
            App.printErrorDialog("Error", "An error occurred in inizialization", ex.toString() + "\n" + ex.getMessage());
        }
    }
}
