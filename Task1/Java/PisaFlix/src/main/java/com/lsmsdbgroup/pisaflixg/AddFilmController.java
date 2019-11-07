/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.PisaFlixServices;
import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

/**
 * FXML Controller class
 *
 * @author FraRonk
 */
public class AddFilmController implements Initializable {
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        successLabel.setVisible(false);
        successLabel.setManaged(false);
        resetFields();
    } 
    
    private void resetFields(){
        titleTextField.setText("");
        descriptionTextArea.setText("");
        datePicker.setValue(null);
        
    }
    
    @FXML
    private void clickAddFilmButton(){
        successLabel.setVisible(false);
        successLabel.setManaged(false);
        try {
            PisaFlixServices.Authentication.checkUserPrivilegesForOperation(PisaFlixServices.UserPrivileges.MODERATOR, "add a new film");
        } catch (PisaFlixServices.UserManager.UserNotLoggedException | PisaFlixServices.UserManager.InvalidPrivilegeLevelException ex) {
            System.out.println(ex.getMessage());
            return;
        }
        
        if(titleTextField.getText() == null || titleTextField.getText().isBlank()){
            System.out.println("Il titolo non può essere vuoto");
            return;
        }
        if(datePicker.getValue() == null){
            System.out.println("la data non può essere vuota");
            return;
        }
        Date date = Date.from(datePicker.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        String title = titleTextField.getText();
        String description = descriptionTextArea.getText();
        
        PisaFlixServices.FilmManager.addFilm(title, date, description);
        
        successLabel.setVisible(true);
        successLabel.setManaged(true);
        resetFields();
    }
    
}
