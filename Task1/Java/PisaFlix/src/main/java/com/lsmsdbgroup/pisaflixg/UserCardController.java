package com.lsmsdbgroup.pisaflixg;

import javafx.beans.property.StringProperty;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

public class UserCardController implements Initializable {

    private final StringProperty userProperty = new SimpleStringProperty();
    private final StringProperty privilegeProperty = new SimpleStringProperty();

    private final int userId;

    public UserCardController(String username, String privilege, int id) {
        userProperty.set(username);
        privilegeProperty.set(privilege);
        userId = id;
    }

    @FXML
    private ImageView userImageView;
    
    @FXML
    private Label usernameLabel;
    
    @FXML
    private Label privilegeLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usernameLabel.setText(userProperty.get());
        privilegeLabel.setText(privilegeProperty.get());
    }
    
}
