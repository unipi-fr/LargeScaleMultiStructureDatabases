package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class TagCardController implements Initializable {

    private Film film;
    private int type;
    
    private CreatePostController createPostController;
    
    public TagCardController(Film film, int type, CreatePostController createPostController){
        this.film = film;
        this.type = type;
        this.createPostController = createPostController;
    }
    
    @FXML
    private Label titleTextField;
    
    @FXML
    private Label dateTextField;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        titleTextField.setText(film.getTitle());
        Date date = film.getPublicationDate();
        dateTextField.setText(film.getPublicationDate().toString());
    }
    
    @FXML
    private void addTag(){
        if(type == 0)
            return;
        
        createPostController.addTag(film);
    }
}
