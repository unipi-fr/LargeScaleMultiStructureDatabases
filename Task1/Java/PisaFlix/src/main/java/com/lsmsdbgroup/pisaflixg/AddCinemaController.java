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
    
    @FXML
    private void clickAddCinemaButton(){
        successLabel.setVisible(false);
        successLabel.setManaged(false);
        
        
        if(nameTextField.getText() == null || nameTextField.getText().isBlank()){
            System.out.println("The name can't be empty");
            return;
        }
        if(addressTextField.getText() == null || addressTextField.getText().isBlank()){
            System.out.println("Address can't be empty");
            return;
        }
        
        try {
            PisaFlixServices.CinemaManager.AddCinema(nameTextField.getText(), addressTextField.getText());
        } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
            System.out.println(ex.getMessage());
            return;
        }
        
        successLabel.setVisible(true);
        successLabel.setManaged(true);
        resetFields();
    }
    
}
