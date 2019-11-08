/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.PisaFlixServices;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

/**
 * FXML Controller class
 *
 * @author FraRonk
 */
public class AddProjectionController implements Initializable {
    
    @FXML
    private ComboBox cinemaComboBox;
    @FXML
    private ComboBox filmComboBox;
    @FXML
    private DatePicker dateDatePicker;
    @FXML
    private ComboBox timeComboBox;             
                    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Set<Film> films = PisaFlixServices.FilmManager.getAll();
        Set<Cinema> cinemas = PisaFlixServices.CinemaManager.getAll();
        
        for(Film film: films){
            filmComboBox.getItems().add(film.getIdFilm() + ":" + film.getTitle());
        }
        
        for(Cinema cinema: cinemas){
            cinemaComboBox.getItems().add(cinema.getIdCinema() + ":" + cinema.getName());
        }
        
        LocalTime lt = LocalTime.MIN;
        for(int i = 0; i < 48; ++i){
            
            timeComboBox.getItems().add(lt.toString());
            lt=lt.plusMinutes(30);
        }
    }    
    
}
