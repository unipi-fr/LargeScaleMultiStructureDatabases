/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.net.URL;
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
    private Cinema cinema;
    
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
        if(cinema == null){
            nameTextField.setText("");
            addressTextField.setText("");
            addCinemaButton.setText("Add cinema");
        }else{
            nameTextField.setText(cinema.getName());
            addressTextField.setText(cinema.getAddress());
            addCinemaButton.setText("Modify cinema");
        }
              
    }
    
    private void errorLabel(String s){       
        successLabel.setTextFill(Color.RED);
        successLabel.setText(s);
        successLabel.setManaged(true);
        successLabel.setVisible(true);
    }
    
    private void addCinema(){
        try {
            PisaFlixServices.cinemaService.addCinema(nameTextField.getText(), addressTextField.getText());
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
    
    private void modifyCinema(){
        this.cinema.setName(nameTextField.getText());
        this.cinema.setAddress(addressTextField.getText());
        
        try {
            PisaFlixServices.cinemaService.updateCinema(cinema);
        } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
            System.out.println(ex.getMessage());
            return;
        }
        
        successLabel.setTextFill(Color.GREEN);
        successLabel.setText("Cinema modify succesfully!");
        successLabel.setManaged(true);
        successLabel.setVisible(true);
        resetFields();
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
        
        if(cinema == null){
            addCinema();
        }else{
            modifyCinema();
        }
    }

    void SetCinema(Cinema cinema) {
        this.cinema = cinema;
        resetFields();
    }
    
}
