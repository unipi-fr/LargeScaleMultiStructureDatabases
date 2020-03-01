package com.lsmsdbgroup.pisaflixg;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MenuController implements Initializable {

    final BooleanProperty firstTime = new SimpleBooleanProperty(true);

    @FXML
    private VBox anchorPane;

    @FXML
    private Button filmsButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button usersButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            filmsButton.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue && firstTime.get()) {
                    anchorPane.requestFocus(); // Delegate the focus to container
                    firstTime.setValue(false); // Variable value changed for future references
                }
            });
        } catch (Exception ex) {
            App.printErrorDialog("Menu", "An error occurred loading the menu", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    private void clickFilmsButton() {
        FilmBrowserController filmBrowserController = new FilmBrowserController();
        App.setMainPageAndController("Browser", filmBrowserController);
    }

    @FXML
    private void clickHomeButton() {
        App.setMainPageReturnsController("PostView");
    }
    
    @FXML
    private void clickUsersButton() {
        UserBrowserController userBrowserController = new UserBrowserController();
        App.setMainPageAndController("Browser", userBrowserController);
    }

}
