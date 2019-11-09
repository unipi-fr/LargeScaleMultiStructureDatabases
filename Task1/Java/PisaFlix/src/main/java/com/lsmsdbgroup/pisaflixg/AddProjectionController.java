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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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
        
        Set<Film> filmSet = PisaFlixServices.FilmManager.getAll();
        Set<Cinema> cinemaSet = PisaFlixServices.CinemaManager.getAll();
        ObservableList observableFilmSet = FXCollections.observableArrayList(filmSet);
        ObservableList observableCinemaSet = FXCollections.observableArrayList(cinemaSet);
        

        filmComboBox.getItems().setAll(observableFilmSet);
        filmComboBox.getItems().add("All");
        
        cinemaComboBox.getItems().setAll(observableCinemaSet);
        cinemaComboBox.getItems().add("All");
        
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
        
        
        Cinema cinema = (Cinema) cinemaComboBox.getValue();
        Film film = (Film) filmComboBox.getValue();
        LocalTime lt = (LocalTime) timeComboBox.getValue();
        LocalDate ld = dateDatePicker.getValue();
        
        LocalDateTime ldt = LocalDateTime.of(ld, lt);
        Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        
        try {
            PisaFlixServices.ProjectionManager.addProjection(cinema, film, date, Integer.parseInt(roomTextField.getText()));
        } catch (PisaFlixServices.UserManager.UserNotLoggedException | PisaFlixServices.UserManager.InvalidPrivilegeLevelException ex) {
            System.out.println(ex.getMessage());
        }
        successLabel.setVisible(false);
        successLabel.setManaged(false);
    }
    
}
