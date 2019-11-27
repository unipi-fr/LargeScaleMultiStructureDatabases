package com.lsmsdbgroup.pisaflixg;

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
        try{
        successLabel.setVisible(false);
        successLabel.setManaged(false);
        if (usernameTextField.getText().isBlank() || !usernameTextField.getText().matches("^[a-zA-Z0-9._-]{3,}$")) {            
            errorLabel("Only valid usernames are accepted");
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
        if (emailTextField.getText().isBlank() || !emailTextField.getText().matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
            errorLabel("Only valid emails are accepted");
            return;
        }
        if (PisaFlixServices.userService.checkDuplicates(usernameTextField.getText(), emailTextField.getText())) {
            errorLabel("Username or Email already exist");
            return;
        }
        if (!firstNameTextField.getText().matches("[a-zA-Z]+") && !firstNameTextField.getText().isEmpty()) {
            errorLabel("Only valid names are accepted");
            return;
        }
        if (!lastNameTextField.getText().matches("[a-zA-Z]+") && !firstNameTextField.getText().isEmpty()) {
            errorLabel("Only valid names are accepted");
            return;
        }
        PisaFlixServices.userService.register(usernameTextField.getText(), passPasswordField.getText(), firstNameTextField.getText(), lastNameTextField.getText(), emailTextField.getText());
        
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
        }catch(Exception ex){
            App.printErrorDialog("Registration", "An error occurred during the registration", ex.toString() + "\n" + ex.getMessage());
        }
    }
    
}
