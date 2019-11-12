package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Comment;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.PisaFlixServices;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;

public class CommentController implements Initializable {
    /*
        0 = Film
        1 = Cinema
    */
    private int type;
    
    private final StringProperty usernameProperty = new SimpleStringProperty();
    private final StringProperty timestampProperty = new SimpleStringProperty();
    private final StringProperty commentProperty = new SimpleStringProperty();
    
    private Comment comment;
    
    public CommentController(String username, String timestamp, String commment, int type){
        usernameProperty.set(username);
        
        String[] timestampSplit = timestamp.split(":");
        String timestampStr = timestampSplit[0] + ":" + timestampSplit[1];
        timestampProperty.set(timestampStr);
        commentProperty.set(commment);
        
        this.type = type;
    }
    
    public void setComment(Comment comment){
        this.comment = comment;
    }
    
    @FXML
    private VBox commentVbox;
    
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
    
    @FXML
    private TextArea commentTextArea;
    
    @FXML
    private HBox buttonHbox;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Button updateButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usernameLabel.setText(usernameProperty.get());
        timestampLabel.setText(timestampProperty.get());
        commentLabel.setText(commentProperty.get());
        
        commentLabel.setMinHeight(Region.USE_PREF_SIZE);
        
        commentTextArea.setVisible(false);
        commentTextArea.setManaged(false);
        buttonHbox.setVisible(false);
        buttonHbox.setManaged(false);
    }
    
    private void switchState(boolean state){
        commentTextArea.setVisible(state);
        commentTextArea.setManaged(state);
        
        buttonHbox.setVisible(state);
        buttonHbox.setManaged(state);
        
        commentLabel.setVisible(!state);
        commentLabel.setManaged(!state);
    }
    
    private void refreshComment(){
        comment = PisaFlixServices.CommentManager.getById(comment.getIdComment());
    }
    
    @FXML
    private void shoeCommentMenu(MouseEvent event){
        if (event.isSecondaryButtonDown()) {
            commentMenu.show(commentVbox, event.getScreenX(), event.getScreenY());
        }
    }
    
    @FXML
    private void confirmComment(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Updating a comment");
        alert.setHeaderText("You are updating a comment");
        alert.setContentText("Are you sure to continue");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK){
            return;
        }
        
        comment.setText(commentTextArea.getText());
        
        PisaFlixServices.CommentManager.update(comment);
        
        switchState(false);
        
        refreshComment();
        
        commentLabel.setText(comment.getText());
    }
    
    @FXML
    private void cancelComment(){
        switchState(false);
    }
    
    @FXML
    private void updateComment(){
        switchState(true);
        
        commentTextArea.setText(commentLabel.getText());
    }
    
    @FXML
    private void deleteComment(){       
        Film film = comment.getFilmSet().iterator().next();
        
        PisaFlixServices.CommentManager.delete(comment.getIdComment());
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FilmDetailPage.fxml"));
        AnchorPane anchorPane = null;
        try {
            anchorPane = loader.load();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        FilmDetailPageController filmDetailPageController = loader.getController();
        filmDetailPageController.setFilm(film);
        
        App.setMainPane(anchorPane);
        
        filmDetailPageController.refreshFilm();
        filmDetailPageController.refreshComment();
    }
}
