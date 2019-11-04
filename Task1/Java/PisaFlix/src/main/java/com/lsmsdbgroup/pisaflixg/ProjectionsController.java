package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.Projection;
import com.lsmsdbgroup.pisaflix.PisaFlixServices;
import java.net.URL;
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
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Film> films = PisaFlixServices.FilmManager.getAll();
        List<Cinema> cinemas = PisaFlixServices.CinemaManager.getAll();
        
        for(Film film: films){
            filmCombo.getItems().add(film.getIdFilm() + ":" + film.getTitle());
        }
        filmCombo.getItems().add("All");
        
        for(Cinema cinema: cinemas){
            cinemaCombo.getItems().add(cinema.getIdCinema() + ":" + cinema.getName());
        }
        cinemaCombo.getItems().add("All");
    }
    
    public void fun(){
        dataCol.setCellValueFactory(new PropertyValueFactory<ProjectionRow, String>("DataTime"));
        roomCol.setCellValueFactory(new PropertyValueFactory<ProjectionRow, String>("Room"));
        cinemaCol.setCellValueFactory(new PropertyValueFactory<ProjectionRow, String>("Cinema"));
        filmCol.setCellValueFactory(new PropertyValueFactory<ProjectionRow, String>("Film"));

        String cinemaStr = (String) cinemaCombo.getValue();
        
        String[] cinemaSplit = cinemaStr.split(":");
        
        int cinemaId = Integer.getInteger(cinemaSplit[0]);
        
        String filmStr = (String) filmCombo.getValue();
        
        String[] filmSplit = filmStr.split(":");
        
        int filmId = Integer.getInteger(filmSplit[0]);
        
        projectionTable.getItems().setAll(getItemsToAdd(cinemaId, filmId, cinemaSplit[1], filmSplit[1]));
    }
    
    private List<ProjectionRow> getItemsToAdd(int cinemaId, int filmId, String cinema, String film){
        List<ProjectionRow> projectionRows = new ArrayList<ProjectionRow>();
        
        List<Projection> projections = PisaFlixServices.ProjectionManager.queryProjections(cinemaId, filmId);
        
        for(Projection projection: projections){
            ProjectionRow pr = new ProjectionRow();
            
            pr.setDateTime(projection.getDateTime().toString());
            pr.setRoom("" + projection.getRoom());
            pr.setCinema(cinema);
            pr.setFilm(film);
            
            projectionRows.add(pr);
        }
        
        return projectionRows;
    }
}
