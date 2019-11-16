package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflix.pisaflixservices.UserPrivileges;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

public class AddFilmController implements Initializable {
    private Film film;
    
    @FXML
    private TextField titleTextField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Label successLabel;

    @FXML
    private Button addFilmButton;
    
    
    void setFilm(Film film) {
        this.film = film;
        resetFields();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        successLabel.setVisible(false);
        successLabel.setManaged(false);
        resetFields();
    } 
    
    private void resetFields(){
        if(film == null){
            titleTextField.setText("");
            descriptionTextArea.setText("");
            datePicker.setValue(null);
            addFilmButton.setText("Add film");
        }else{
            titleTextField.setText(film.getTitle());
            descriptionTextArea.setText(film.getDescription());
            Date d =film.getPublicationDate();
            long time = d.getTime();
            LocalDate toLocalDate = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDate();
            datePicker.setValue(toLocalDate);
            addFilmButton.setText("Modify film");
        } 
    }
    
    private void errorLabel(String s){       
        successLabel.setTextFill(Color.RED);
        successLabel.setText(s);
        successLabel.setManaged(true);
        successLabel.setVisible(true);
    }
    
    private void addFilm(){
        
        Date date = Date.from(datePicker.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        String title = titleTextField.getText();
        String description = descriptionTextArea.getText();
        
        try {
            PisaFlixServices.filmService.addFilm(title, date, description);
        } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
            System.out.println(ex.getMessage());
            return;
        }
        
        successLabel.setTextFill(Color.GREEN);
        successLabel.setText("Film successfully added!");
        successLabel.setVisible(true);
        successLabel.setManaged(true);
        resetFields();
    }
    
    private void modifyFilm(){
        Date date = Date.from(datePicker.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        String title = titleTextField.getText();
        String description = descriptionTextArea.getText();
        
        film.setTitle(title);
        film.setDescription(description);
        film.setPublicationDate(date);
        
        try {
            PisaFlixServices.filmService.updateFilm(film);
        } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
            System.out.println(ex.getMessage());
            return;
        }
        
        successLabel.setTextFill(Color.GREEN);
        successLabel.setText("Film modify successfully!");
        successLabel.setVisible(true);
        successLabel.setManaged(true);
        resetFields();
    }
    
    @FXML
    private void clickAddFilmButton(){
        
        successLabel.setVisible(false);
        successLabel.setManaged(false);
        
        if(titleTextField.getText() == null || titleTextField.getText().isBlank()){
            errorLabel("Title is mandatory");
            return;
        }
        if(datePicker.getValue() == null){
            errorLabel("Date is mandatory");
            return;
        }
        
        if(film == null){
            addFilm();
        }else{
            modifyFilm();
        }
    }
    
}
