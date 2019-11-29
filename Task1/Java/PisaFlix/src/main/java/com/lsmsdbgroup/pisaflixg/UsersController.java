package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import java.io.IOException;
import java.util.Set;
import javafx.fxml.FXMLLoader;

public class UsersController implements Initializable {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TilePane tilePane;

    @FXML
    private TextField nameFilterTextField;

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        searchUsers(null);
    }

    private Pane createUserCardPane(String name, String privilege, int id) {
        Pane pane = new Pane();
        try {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("UserCard.fxml"));
                UserCardController fcc = new UserCardController(name, privilege, id);
                loader.setController(fcc);
                pane = loader.load();
            } catch (IOException ex) {
                App.printErrorDialog("User Card", "An error occurred loading the user card", ex.toString() + "\n" + ex.getMessage());
            }
        } catch (Exception ex) {
            App.printErrorDialog("User Card", "An error occurred loading the user card", ex.toString() + "\n" + ex.getMessage());
        }

        return pane;
    }

    private String returnPrivilege(int level) {
        switch (level) {
            case 1:
                return "Social Moderator";
            case 2:
                return "Moderator";
            case 3:
                return "Admin";
            default:
                return "User";
        }
    }

    public void populateScrollPane(Set<User> users) {
        try {
            tilePane.getChildren().clear();
            String username;
            String privilege;
            int level;
            int id;

            Pane pane;
            int i = 0;
            for (User user : users) {
                username = user.getUsername();
                level = user.getPrivilegeLevel();

                privilege = returnPrivilege(level);

                id = user.getIdUser();

                pane = createUserCardPane(username, privilege, id);
                tilePane.getChildren().add(pane);
            }
        } catch (Exception ex) {
            App.printErrorDialog("Users", "An error occurred loading the users", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    private void filterUsers() {
        try {
            String usernameFilter = nameFilterTextField.getText();

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
}
