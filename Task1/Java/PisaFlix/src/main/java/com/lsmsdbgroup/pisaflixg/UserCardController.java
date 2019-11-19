package com.lsmsdbgroup.pisaflixg;

import javafx.beans.property.StringProperty;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class UserCardController implements Initializable {

    private final StringProperty userProperty = new SimpleStringProperty();
    private final StringProperty privilegeProperty = new SimpleStringProperty();

    private final int userId;

    public UserCardController(String title, String publishDate, int id) {
        userProperty.set(title);
        privilegeProperty.set(publishDate);
        userId = id;
    }

    @FXML
    private Label userLabel;
    
    @FXML
    private Label privilegeLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userLabel.setText(userProperty.get());
        privilegeLabel.setText(privilegeProperty.get());
    }
    
}
