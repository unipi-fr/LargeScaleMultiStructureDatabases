package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflix.pisaflixservices.UserPrivileges;
import java.io.IOException;
import java.util.Set;
import javafx.fxml.FXMLLoader;

public class UserBrowserController extends BrowserController implements Initializable {

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        try {
            super.initialize();
            filterTextField.setPromptText("Name filter");
            searchUsers(null);
        } catch (Exception ex) {
            App.printErrorDialog("Users", "An error occurred loading the page", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @Override
    public Pane createCardPane(String name, String privilege, String id) {
        Pane pane = new Pane();
        try {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("UserCard.fxml"));
                UserCardController fcc = new UserCardController(name, privilege, id);
                loader.setController(fcc);
                pane = loader.load();
            } catch (IOException ex) {
                App.printErrorDialog("Users", "An error occurred loading the user card", ex.toString() + "\n" + ex.getMessage());
            }
        } catch (Exception ex) {
            App.printErrorDialog("Users", "An error occurred loading the user card", ex.toString() + "\n" + ex.getMessage());
        }

        return pane;
    }

    public void populateScrollPane(Set<User> users) {
        try {
            tilePane.getChildren().clear();
            String username;
            String privilege;
            int level;
            Pane pane;

            for (User user : users) {
                username = user.getUsername();
                level = user.getPrivilegeLevel();
                privilege = UserPrivileges.valueOf(level);
                pane = createCardPane(username, privilege, user.getId());
                tilePane.getChildren().add(pane);
            }
        } catch (Exception ex) {
            App.printErrorDialog("Users", "An error occurred loading the users", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    @Override
    public void filter() {
        try {
            String usernameFilter = filterTextField.getText();

            searchUsers(usernameFilter);
        } catch (Exception ex) {
            App.printErrorDialog("Users", "An error occurred loading the users", ex.toString() + "\n" + ex.getMessage());
        }
    }

    private void searchUsers(String usernameFilter) {
        try {
            Set<User> users = PisaFlixServices.userService.getFiltered(usernameFilter);

            populateScrollPane(users);
        } catch (Exception ex) {
            App.printErrorDialog("Users", "An error occurred loading the users", ex.toString() + "\n" + ex.getMessage());
        }
    }
    
    @FXML
    @Override
    public void add(){
        App.setMainPageReturnsController("registration");
    }
}

