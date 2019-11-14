/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author FraRonk
 */
public class AddCinemaController implements Initializable {
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField addressTextField;

    @FXML
    private Label successLabel;

    @FXML
    private Button addCinemaButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        successLabel.setVisible(false);
        successLabel.setManaged(false);
        resetFields();
    } 
    
    private void resetFields(){
        nameTextField.setText("");
        addressTextField.setText("");        
    }
    
    private void errorLabel(String s){       
        successLabel.setTextFill(Color.RED);
        successLabel.setText(s);
        successLabel.setManaged(true);
        successLabel.setVisible(true);
    }
    
    @FXML
    private void clickAddCinemaButton(){
        successLabel.setVisible(false);
        successLabel.setManaged(false);
        
        
        if(nameTextField.getText() == null || nameTextField.getText().isBlank()){
            errorLabel("Name is mandatory");
            return;
        }
        if(addressTextField.getText() == null || addressTextField.getText().isBlank()){
            errorLabel("Address is mandatory");
            return;
        }
        
        try {
            PisaFlixServices.cinemaService.AddCinema(nameTextField.getText(), addressTextField.getText());
        } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
            System.out.println(ex.getMessage());
            return;
        }
        
        successLabel.setTextFill(Color.GREEN);
        successLabel.setText("Cinema succesfully added!");
        successLabel.setManaged(true);
        successLabel.setVisible(true);
        resetFields();
    }
    
}
