/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.DBManager;
import com.lsmsdbgroup.pisaflix.PisaFlixServices;
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
public class RegistrationController implements Initializable {

    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passPasswordField;
    @FXML
    private PasswordField repeatPassPasswordField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private Label successLabel;
    
           
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        successLabel.setVisible(false);
        successLabel.setManaged(false);
    }    
    
    private void errorLable(String s){       
        successLabel.setTextFill(Color.RED);
        successLabel.setText(s);
        successLabel.setManaged(true);
        successLabel.setVisible(true);
    }
    
    @FXML
    private void clickRegisterButton(){
        successLabel.setVisible(false);
        successLabel.setManaged(false);
        if(usernameTextField.getText().isBlank()){ 
            errorLable("Username is mandatory");
            return; 
        }
        if(passPasswordField.getText().isBlank()){
             errorLable("Password is mandatory");
             return;
        }       
        if(passPasswordField.getText() == null ? repeatPassPasswordField.getText() != null : !passPasswordField.getText().equals(repeatPassPasswordField.getText())){
             errorLable("Passwords are different");
            return;
        } 
        if(emailTextField.getText().isBlank()){
            errorLable("Email is mandatory");
                        return;
        }
        if(PisaFlixServices.UserManager.checkDuplicates(usernameTextField.getText(), emailTextField.getText())){
            errorLable("Username or Email already exist");
                        return;
        }
        PisaFlixServices.UserManager.register(usernameTextField.getText(), passPasswordField.getText(), firstNameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(), 0);
      
        successLabel.setTextFill(Color.GREEN);
        successLabel.setText("Registration is done!");
        successLabel.setVisible(true);
        successLabel.setManaged(true);
    }
    
}
