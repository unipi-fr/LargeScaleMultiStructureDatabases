package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.InvalidFieldException;
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
        if (passPasswordField.getText() == null ? repeatPassPasswordField.getText() != null : !passPasswordField.getText().equals(repeatPassPasswordField.getText())) {
            errorLabel("Passwords are different");
            return;
        }      
        
        PisaFlixServices.userService.register(usernameTextField.getText(), passPasswordField.getText(), emailTextField.getText(), firstNameTextField.getText(), lastNameTextField.getText());
        
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
        }catch(InvalidFieldException ex){
            errorLabel(ex.getMessage());
        }
        catch(Exception ex){
            App.printErrorDialog("Registration", "An error occurred during the registration", ex.toString() + "\n" + ex.getMessage());
        }
    }
    
}
