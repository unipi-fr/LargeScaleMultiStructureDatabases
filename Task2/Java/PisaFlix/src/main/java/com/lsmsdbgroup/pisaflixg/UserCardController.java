package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.pisaflixservices.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.io.*;
import javafx.beans.property.StringProperty;
import java.net.URL;
import java.util.*;
import java.util.logging.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class UserCardController implements Initializable {

    private User user;

    private final StringProperty userProperty = new SimpleStringProperty();
    private final StringProperty privilegeProperty = new SimpleStringProperty();

    private final String userId;

    public UserCardController(String username, String privilege, String id) {
        userProperty.set(username);
        privilegeProperty.set(privilege);
        userId = id;
    }

    @FXML
    private VBox cardVbox;

    @FXML
    private ImageView userImageView;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label privilegeLabel;

    @FXML
    private ComboBox privilegeCombo;

    @FXML
    private ContextMenu privilegeMenu;

    @FXML
    private MenuItem updatePrivilegeMenuItem;

    @FXML
    private Button updatePrivilegeButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            user = PisaFlixServices.userService.getById(userId);
            usernameLabel.setText(userProperty.get());
            privilegeLabel.setText(privilegeProperty.get());

            Random random = new Random();
            int img = random.nextInt(3) + 1;

            File file = new File("src/main/resources/img/user" + img + ".png");

            Image image = new Image(file.toURI().toString());
            userImageView.setImage(image);

            Set<UserPrivileges> privilegeSet = new LinkedHashSet<>();

            privilegeSet.add(UserPrivileges.NORMAL_USER);
            privilegeSet.add(UserPrivileges.SOCIAL_MODERATOR);
            privilegeSet.add(UserPrivileges.MODERATOR);
            privilegeSet.add(UserPrivileges.ADMIN);

            privilegeCombo.getItems().setAll(privilegeSet);

            privilegeCombo.setVisible(false);
            privilegeCombo.setManaged(false);

            updatePrivilegeButton.setVisible(false);
            updatePrivilegeButton.setManaged(false);

            if (!PisaFlixServices.authenticationService.isUserLogged() || PisaFlixServices.authenticationService.getLoggedUser().getPrivilegeLevel() <= user.getPrivilegeLevel()) {
                updatePrivilegeMenuItem.setVisible(false);
            } else {
                updatePrivilegeMenuItem.setVisible(true);
            }
        } catch (Exception ex) {
            App.printErrorDialog("User Card", "An error occurred loading the user card", ex.toString() + "\n" + ex.getMessage());
        }

    }

    @FXML
    private void updatePrivilege() {
        try {

            userImageView.setVisible(false);
            userImageView.setManaged(false);

            privilegeCombo.setVisible(true);
            privilegeCombo.setManaged(true);

            privilegeLabel.setVisible(false);
            privilegeLabel.setManaged(false);

            updatePrivilegeButton.setVisible(true);
            updatePrivilegeButton.setManaged(true);

            int userPrivilege = user.getPrivilegeLevel();

            privilegeCombo.getSelectionModel().select(userPrivilege);
        } catch (Exception ex) {
            App.printErrorDialog("Update Privilege", "An error occurred updating the privilege", ex.toString() + "\n" + ex.getMessage());
        }
    }

    private void reefreshUserCard() {
        usernameLabel.setText(user.getUsername());
        int level = user.getPrivilegeLevel();
        privilegeLabel.setText(UserPrivileges.valueOf(level));
    }

    @FXML
    private void modifyPrivilege() {
        try {
            if (!App.printConfirmationDialog("Updating Privilege", "You are updating the privileges", "Are you sure to continue")) {
                return;
            }

            int level = UserPrivileges.getLevel(privilegeCombo.getValue().toString());

            UserPrivileges userPrivilege = (UserPrivileges) privilegeCombo.getValue();
            try {
                PisaFlixServices.userService.changeUserPrivileges(user, userPrivilege);
            } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
                App.printErrorDialog("Updating Privilege", "An error occurred while updating the privileges", ex.getMessage());
            }

            user = PisaFlixServices.userService.getById(userId);

            reefreshUserCard();

            userImageView.setVisible(true);
            userImageView.setManaged(true);

            privilegeCombo.setVisible(false);
            privilegeCombo.setManaged(false);

            privilegeLabel.setVisible(true);
            privilegeLabel.setManaged(true);

            updatePrivilegeButton.setVisible(false);
            updatePrivilegeButton.setManaged(false);
        } catch (Exception ex) {
            App.printErrorDialog("Update Privilege", "An error occurred updating the privilege", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    private void mouseClicked(MouseEvent event) {
        try {
            if (event.isSecondaryButtonDown()) {
                privilegeMenu.hide();
                User loggedUser = PisaFlixServices.authenticationService.getLoggedUser();

                if (loggedUser != null) {
                    try {
                        PisaFlixServices.authenticationService.checkUserPrivilegesForOperation(UserPrivileges.ADMIN);
                        privilegeMenu.show(cardVbox, event.getScreenX(), event.getScreenY());
                    } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
                        Logger.getLogger(UserCardController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserView.fxml"));

            GridPane gridPane = null;

            try {
                gridPane = loader.load();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

            UserViewController userViewController = loader.getController();

            userViewController.setUser(user);

            App.setMainPane(gridPane);
        } catch (Exception ex) {
            App.printErrorDialog("User Card", "An error occurred", ex.toString() + "\n" + ex.getMessage());
        }
    }

}
