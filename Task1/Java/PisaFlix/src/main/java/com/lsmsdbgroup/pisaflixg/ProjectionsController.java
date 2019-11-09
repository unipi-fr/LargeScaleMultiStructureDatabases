package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.PisaFlixServices;
import java.net.URL;
import java.time.*;
import java.util.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProjectionsController implements Initializable {

    @FXML 
    private TableView<ProjectionRow> projectionTable;
    
    @FXML 
    private TableColumn<ProjectionRow, String> dataCol;
    
    @FXML 
    private TableColumn<ProjectionRow, String> roomCol;
    
    @FXML 
    private TableColumn<ProjectionRow, String> cinemaCol;
    
    @FXML 
    private TableColumn<ProjectionRow, String> filmCol;
    
    @FXML
    private ComboBox cinemaCombo;
    
    @FXML
    private ComboBox filmCombo;
    
    @FXML
    private DatePicker datePicker;
    
    @FXML
    private Button addProjectionButton;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            PisaFlixServices.UserManager.checkUserPrivilegesForOperation(PisaFlixServices.UserPrivileges.MODERATOR);
        } catch (PisaFlixServices.UserManager.UserNotLoggedException | PisaFlixServices.UserManager.InvalidPrivilegeLevelException ex) {
            addProjectionButton.setVisible(false);
            addProjectionButton.setManaged(false);
        }
        
        Set<Film> films = PisaFlixServices.FilmManager.getAll();
        Set<Cinema> cinemas = PisaFlixServices.CinemaManager.getAll();
        
        for(Film film: films){
            filmCombo.getItems().add(film);
        }
        
        for(Cinema cinema: cinemas){
            cinemaCombo.getItems().add(cinema);
        }
    }
    
    @FXML
    private void clickAddProjectionButton(){
        App.setMainPane("AddProjection");
    }
    
    @FXML
    public void showSearch(){
        dataCol.setCellValueFactory(new PropertyValueFactory<ProjectionRow, String>("DateTime"));
        roomCol.setCellValueFactory(new PropertyValueFactory<ProjectionRow, String>("Room"));
        cinemaCol.setCellValueFactory(new PropertyValueFactory<ProjectionRow, String>("Cinema"));
        filmCol.setCellValueFactory(new PropertyValueFactory<ProjectionRow, String>("Film"));

        Cinema c = (Cinema) cinemaCombo.getValue();
        int cinemaId = -1;
        
        if(c != null){
            cinemaId = c.getIdCinema();
        }
         
        Film f = (Film) filmCombo.getValue();
        int filmId = -1;
        
        if(f != null){
            filmId = f.getIdFilm();
        }  
        
        projectionTable.getItems().setAll(getItemsToAdd(cinemaId, filmId));
    }
    
    private Set<ProjectionRow> getItemsToAdd(int cinemaId, int filmId){
        Set<ProjectionRow> projectionRows = new LinkedHashSet<>();
        
        LocalDate localDate = datePicker.getValue();
        String dateStr;
        
        if(localDate != null) {
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date date = Date.from(instant);
            dateStr = localDate.toString();
        } else
            dateStr = "all";
        
        Set<Projection> projections = PisaFlixServices.ProjectionManager.queryProjections(cinemaId, filmId, dateStr);
        
        if(projections == null)
            return null;
        
        for(Projection projection: projections){
            ProjectionRow pr = new ProjectionRow();
            
            pr.setDateTime(projection.getDateTime().toString());
            pr.setRoom("" + projection.getRoom());
            
            String cinema = projection.getIdCinema().getName();
            pr.setCinema(cinema);
            
            String film = projection.getIdFilm().getTitle();
            pr.setFilm(film);
            
            projectionRows.add(pr);
        }
        
        return projectionRows;
    }
}
