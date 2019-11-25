package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflix.pisaflixservices.UserPrivileges;
import com.lsmsdbgroup.pisaflix.pisaflixservices.UserService;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.InvalidPrivilegeLevelException;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.UserNotLoggedException;
import java.io.File;
import java.io.IOException;
import javafx.beans.property.StringProperty;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class UserCardController implements Initializable {

    private User user;
            
    private final StringProperty userProperty = new SimpleStringProperty();
    private final StringProperty privilegeProperty = new SimpleStringProperty();

    private final int userId;

    public UserCardController(String username, String privilege, int id) {
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
        user = PisaFlixServices.userService.getUserById(userId);
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
    }
    
    @FXML
    private void updatePrivilege(){
        //cardVbox.getStyleClass().clear();
        //cardVbox.getStyleClass().add("card-user");
        
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
    }
    
    private void reefreshUserCard(){
        usernameLabel.setText(user.getUsername());
        int level = user.getPrivilegeLevel();
        privilegeLabel.setText(UserPrivileges.valueOf(level));
    }
    
    @FXML
    private void modifyPrivilege(){
        if (!App.printConfirmationDialog("Updating Privilege ", "You are updating a privileges", "Are you sure to continue")) {
            return;
        }
        
        int level = UserPrivileges.getLevel(privilegeCombo.getValue().toString());
        
        UserPrivileges userPrivilege = (UserPrivileges) privilegeCombo.getValue();
        try {
            PisaFlixServices.userService.changeUserPrivileges(user, userPrivilege);
        } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
            System.out.println(ex.getMessage());
        }
        
        user = PisaFlixServices.userService.getUserById(userId);
        
        reefreshUserCard();
        
        userImageView.setVisible(true);
        userImageView.setManaged(true);
        
        privilegeCombo.setVisible(false);
        privilegeCombo.setManaged(false);
        
        privilegeLabel.setVisible(true);
        privilegeLabel.setManaged(true);
        
        updatePrivilegeButton.setVisible(false);
        updatePrivilegeButton.setManaged(false);
    }
    
    @FXML
    private void mouseClicked(MouseEvent event) {
        if (event.isSecondaryButtonDown()) {
            privilegeMenu.hide();
            User loggedUser = PisaFlixServices.authenticationService.getLoggedUser();
            
            if(loggedUser != null){
                try {
                    PisaFlixServices.userService.checkUserPrivilegesForOperation(UserPrivileges.ADMIN);
                    privilegeMenu.show(cardVbox, event.getScreenX(), event.getScreenY());
                } catch (UserNotLoggedException ex) {
                    Logger.getLogger(UserCardController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidPrivilegeLevelException ex) {
                    Logger.getLogger(UserCardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            return;
        }
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserView.fxml"));

        AnchorPane anchorPane = null;

        try {
            anchorPane = loader.load();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        UserViewController userViewController = loader.getController();

        userViewController.setUser(user);

        App.setMainPane(anchorPane);
    }
    
}
