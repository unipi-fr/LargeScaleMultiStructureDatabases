/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.PisaFlixServices;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    @FXML
    private TextField roomTextField;
    @FXML
    private Label successLabel;
                    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        successLabel.setVisible(false);
        successLabel.setManaged(false);
        Set<Film> films = PisaFlixServices.FilmManager.getAll();
        Set<Cinema> cinemas = PisaFlixServices.CinemaManager.getAll();
        
        for(Film film: films){
            filmComboBox.getItems().add(film);
        }
        
        for(Cinema cinema: cinemas){
            cinemaComboBox.getItems().add(cinema);
        }
        
        LocalTime lt = LocalTime.MIN;
        for(int i = 0; i < 48; ++i){
            
            timeComboBox.getItems().add(lt);
            lt=lt.plusMinutes(30);
        }
    }    
    
    private void resetFields(){
        cinemaComboBox.setValue(null);
        filmComboBox.setValue(null);
        dateDatePicker.setValue(null);
        timeComboBox.setValue(null);    
        roomTextField.setText("");
    }
    
    @FXML 
    private void clickAddProjectionButton(){
        successLabel.setVisible(false);
        successLabel.setManaged(false);
        if(cinemaComboBox.getValue() == null){
            System.out.println("You must select a cinema");
            return;
        }
        if(filmComboBox.getValue() == null){
            System.out.println("You must select a film");
            return;
        }
        if(dateDatePicker.getValue() == null){
            System.out.println("You must select a day");
            return;
        }
        if(timeComboBox.getValue() == null){
            System.out.println("You must select a time");
            return;
        }
        
        
        Cinema c = (Cinema) cinemaComboBox.getValue();
        Film f = (Film) filmComboBox.getValue();
        LocalTime lt = (LocalTime) timeComboBox.getValue();
        LocalDate ld = dateDatePicker.getValue();
        
        LocalDateTime ldt = LocalDateTime.of(ld, lt);
        Date d = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        
        try {
            PisaFlixServices.ProjectionManager.addProjection(c, f, d, Integer.parseInt(roomTextField.getText()));
        } catch (PisaFlixServices.UserManager.UserNotLoggedException | PisaFlixServices.UserManager.InvalidPrivilegeLevelException ex) {
            System.out.println(ex.getMessage());
        }
        successLabel.setVisible(false);
        successLabel.setManaged(false);
    }
    
}
