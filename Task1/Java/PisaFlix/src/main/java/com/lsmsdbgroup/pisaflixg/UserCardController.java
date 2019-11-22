package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflix.pisaflixservices.UserPrivileges;
import java.io.File;
import java.io.IOException;
import javafx.beans.property.StringProperty;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    }
    
    @FXML
    private void modifyPrivilegeEn(){
        cardVbox.getStyleClass().clear();
        cardVbox.getStyleClass().add("card-user");
        privilegeCombo.setVisible(true);
        privilegeCombo.setManaged(true);
        
        privilegeLabel.setVisible(false);
        privilegeLabel.setManaged(false);
        
        int userPrivilege = user.getPrivilegeLevel();
        privilegeCombo.setValue(UserPrivileges.valueOf(userPrivilege));
    }
    
    @FXML
    private void modifyPrivilegeEx(){
        cardVbox.getStyleClass().clear();
        cardVbox.getStyleClass().add("card-film");
        privilegeCombo.setVisible(false);
        privilegeCombo.setManaged(false);
        
        privilegeLabel.setVisible(true);
        privilegeLabel.setManaged(true); 
    }
    
    @FXML
    private void showUser() {
        User user = PisaFlixServices.userService.getUserById(userId);
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
