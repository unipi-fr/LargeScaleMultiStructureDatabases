package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

public class CreatePostController implements Initializable {

    @FXML
    private TextArea postTextArea;
    
    @FXML
    private HBox tagsHBox;
    
    @FXML
    private TextField filterTextField;
    
    @FXML
    private Button searchButton;
    
    @FXML
    private TilePane tilePane;
    
    @FXML
    private Button postButton;
    
    @FXML
    private Button resetButton;
    
    private Set<Film> tagFilms = new LinkedHashSet<>();
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filter();
    }
    
    public Pane createCardPane(Film film, int type) {
        Pane pane = new Pane();
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TagCard.fxml"));
            TagCardController tcc = new TagCardController(film, type, this);
            loader.setController(tcc);
            pane = loader.load();
        } catch (IOException ex) {
            App.printErrorDialog("Users", "An error occurred loading the user card", ex.toString() + "\n" + ex.getMessage());
        }

        return pane;
    }
 
    private void populate(Set<Film> films){
        tilePane.getChildren().clear();
        
        for(Film film: films){
            Pane pane = createCardPane(film, 1);
            tilePane.getChildren().add(pane);
        }
    }
    
    private void searchFilm(String title){
        Set<Film> films = PisaFlixServices.filmService.getFilmsFiltered(title, null, null, 35, 0);
        
        populate(films);
    }
    
    @FXML
    private void filter(){
        String titleStr = filterTextField.getText();
        searchFilm(titleStr);
    }
    
    public void addTag(Film film){
        if(tagFilms.contains(film))
            return;
        
        Pane pane = createCardPane(film, 0);
        
        tagsHBox.getChildren().add(pane);
        
        tagFilms.add(film);
    }
    
    @FXML
    private void resetTags(){
        tagFilms.clear();
        
        tagsHBox.getChildren().clear();
        
        tagsHBox.getChildren().add(resetButton);
    }
    
    @FXML
    private void createPost(){
        if(tagFilms.isEmpty()){
            App.printErrorDialog("Create Post", "Crete Post Error", "You must enter at least a tag");
            return;
        }
        
        if(!App.printConfirmationDialog("Create Post", "Create Post confirmation", "Are you sure to create this post"))
            return;
        
        String text = postTextArea.getText();
        
        User user = PisaFlixServices.authenticationService.getLoggedUser();
        
        PisaFlixServices.postService.create(text, user, tagFilms);
        
        resetTags();
        
        postTextArea.setText("");
    }
}
