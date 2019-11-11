package com.lsmsdbgroup.pisaflixg;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

public class CommentController implements Initializable {    
    private final StringProperty usernameProperty = new SimpleStringProperty();
    private final StringProperty timestampProperty = new SimpleStringProperty();
    private final StringProperty commentProperty = new SimpleStringProperty();
    
    public CommentController(String username, String timestamp, String commment){
        usernameProperty.set(username);
        
        String[] timestampSplit = timestamp.split(":");
        String timestampStr = timestampSplit[0] + ":" + timestampSplit[1];
        timestampProperty.set(timestampStr);
        commentProperty.set(commment);
    }
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private Label usernameLabel;
    
    @FXML
    private Label timestampLabel;
    
    @FXML
    private Label commentLabel;
    
    @FXML
    private ContextMenu commentMenu;
    
    @FXML
    private MenuItem updateMenuItem;
    
    @FXML
    private MenuItem deleteMenuItem;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usernameLabel.setText(usernameProperty.get());
        timestampLabel.setText(timestampProperty.get());
        commentLabel.setText(commentProperty.get());
        
        commentLabel.setMinHeight(Region.USE_PREF_SIZE);
        
        anchorPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isSecondaryButtonDown()) {
                    commentMenu.show(anchorPane, event.getScreenX(), event.getScreenY());
                }
            }
        });
    }    
    
    @FXML
    private void updateComment(){
        
    }
    
    @FXML
    private void deleteComment(){
        
    }
}
