/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.DBManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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
    
    @FXML
    private void clickRegisterButton(){
        successLabel.setVisible(false);
        successLabel.setManaged(false);
        if(usernameTextField.getText().isBlank())
            return;
        if(passPasswordField.getText().isBlank())
            return;
        if(passPasswordField.getText() == null ? repeatPassPasswordField.getText() != null : !passPasswordField.getText().equals(repeatPassPasswordField.getText()))
            return;
        if(firstNameTextField.getText().isBlank())
            return;
        if(lastNameTextField.getText().isBlank())
            return;
        if(emailTextField.getText().isBlank())
            return;
        
        DBManager.UserManager.create(usernameTextField.getText(), passPasswordField.getText(), firstNameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(), 0);
        successLabel.setVisible(true);
        successLabel.setManaged(true);
    }
    
}
