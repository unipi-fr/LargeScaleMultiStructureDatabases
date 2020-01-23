package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Entity;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;

public abstract class BrowserController {

    @FXML
    protected ScrollPane scrollPane;
    
    @FXML
    protected Label safeSearchLabel;
    
    @FXML
    protected Slider safeSearch;
    
    @FXML
    protected Button searchButton;
    
    @FXML
    protected ProgressIndicator progressIndicator;

    @FXML
    protected TilePane tilePane;

    @FXML
    protected TextField filterTextField;

    @FXML
    private Button addButton;

    public void initialize(){
        try {
            PisaFlixServices.authenticationService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR);
        } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
            addButton.setVisible(false);
            addButton.setManaged(false);
        }
    }

    public abstract Pane createCardPane(Entity entity);

    @FXML
    public abstract void filter();

    @FXML
    public abstract void add();
    
}
