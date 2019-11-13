package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

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
    
    private void errorLabel(String s) {        
        successLabel.setTextFill(Color.RED);
        successLabel.setText(s);
        successLabel.setManaged(true);
        successLabel.setVisible(true);
    }
    
    @FXML
    private void clickRegisterButton() {
        successLabel.setVisible(false);
        successLabel.setManaged(false);
        if (usernameTextField.getText().isBlank()) {            
            errorLabel("Username is mandatory");
            return;            
        }
        if (passPasswordField.getText().isBlank()) {
            errorLabel("Password is mandatory");
            return;
        }        
        if (passPasswordField.getText() == null ? repeatPassPasswordField.getText() != null : !passPasswordField.getText().equals(repeatPassPasswordField.getText())) {
            errorLabel("Passwords are different");
            return;
        }        
        if (emailTextField.getText().isBlank()) {
            errorLabel("Email is mandatory");
            return;
        }
        if (PisaFlixServices.UserManager.checkDuplicates(usernameTextField.getText(), emailTextField.getText())) {
            errorLabel("Username or Email already exist");
            return;
        }
        PisaFlixServices.UserManager.register(usernameTextField.getText(), passPasswordField.getText(), firstNameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(), 0);
        
        User u = new User();
        u.setUsername(usernameTextField.getText());
        u.setPassword(passPasswordField.getText());
        u.setFirstName(firstNameTextField.getText());
        u.setLastName(lastNameTextField.getText());
        u.setEmail(emailTextField.getText());
        
        PisaFlixServices.UserManager.registerUser(u);
        successLabel.setTextFill(Color.GREEN);
        successLabel.setText("Registration is done!");
        successLabel.setVisible(true);
        successLabel.setManaged(true);
        usernameTextField.setText("");
        passPasswordField.setText("");
        firstNameTextField.setText("");
        lastNameTextField.setText("");
        emailTextField.setText("");
        repeatPassPasswordField.setText("");
    }
    
}
