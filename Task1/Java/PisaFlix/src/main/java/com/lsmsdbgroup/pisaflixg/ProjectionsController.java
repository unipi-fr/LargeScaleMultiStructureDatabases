package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.Projection;
import com.lsmsdbgroup.pisaflix.PisaFlixServices;
import java.net.URL;
import java.time.*;
import java.util.*;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Set<Film> films = PisaFlixServices.FilmManager.getAll();
        Set<Cinema> cinemas = PisaFlixServices.CinemaManager.getAll();
        
        for(Film film: films){
            filmCombo.getItems().add(film.getIdFilm() + ":" + film.getTitle());
        }
        filmCombo.getItems().add("All");
        
        for(Cinema cinema: cinemas){
            cinemaCombo.getItems().add(cinema.getIdCinema() + ":" + cinema.getName());
        }
        cinemaCombo.getItems().add("All");
    }
    
    @FXML
    public void showSearch(){
        dataCol.setCellValueFactory(new PropertyValueFactory<ProjectionRow, String>("DateTime"));
        roomCol.setCellValueFactory(new PropertyValueFactory<ProjectionRow, String>("Room"));
        cinemaCol.setCellValueFactory(new PropertyValueFactory<ProjectionRow, String>("Cinema"));
        filmCol.setCellValueFactory(new PropertyValueFactory<ProjectionRow, String>("Film"));

        String cinemaStr = (String) cinemaCombo.getValue();
        
        String[] cinemaSplit = cinemaStr.split(":");
        
        int cinemaId;
        
        if(cinemaSplit[0].equals("All"))
        {
            cinemaId = -1;
        } else {
            cinemaId = Integer.parseInt(cinemaSplit[0]);
        }
        
        String filmStr = (String) filmCombo.getValue();
        
        String[] filmSplit = filmStr.split(":");
        
        int filmId;
                
        if(filmSplit[0].equals("All"))
        {
            filmId = -1;
        } else {
            filmId = Integer.parseInt(filmSplit[0]);
        }
        
        projectionTable.getItems().setAll(getItemsToAdd(cinemaId, filmId));
    }
    
    private List<ProjectionRow> getItemsToAdd(int cinemaId, int filmId){
        List<ProjectionRow> projectionRows = new ArrayList<ProjectionRow>();
        
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
