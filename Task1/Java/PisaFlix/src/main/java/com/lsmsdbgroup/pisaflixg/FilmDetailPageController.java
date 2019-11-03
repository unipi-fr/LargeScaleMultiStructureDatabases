package com.lsmsdbgroup.pisaflixg;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

public class FilmDetailPageController implements Initializable {

    @FXML
    private Label titleLabel;
    
    @FXML
    private ImageView moviePosterImageView;
    
    @FXML
    private Label publishDateLabel;
    
    @FXML
    private TextArea descriptionArea;
    
    @FXML
    private ScrollPane commentScrollPane;
    
    @FXML
    private VBox commentVBox;
    
    @FXML
    private TextArea commentArea;
    
    @FXML
    private Button commentButton;
    
    @FXML
    private HBox preferiteHBox;
    
    @FXML
    private Button preferiteButton;
    
    @FXML
    private Label preferiteLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    public void setTitleLabel(String title) {
        titleLabel.setText(title);
    }

    public void setMoviePosterImageView(ImageView moviePosterImageView) {
        this.moviePosterImageView = moviePosterImageView;
    }

    public void setPublishDate(String publishDate) {
        publishDateLabel.setText(publishDate);
    }

    public void setDescription(String Description) {
        descriptionArea.setText(Description);
    }

    public void addComment(String comment) {
        TextArea filmComment = new TextArea();
        filmComment.setText(comment);
        
        commentVBox.getChildren().add(filmComment);
    }
    
}

