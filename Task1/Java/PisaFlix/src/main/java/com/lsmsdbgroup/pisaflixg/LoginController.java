package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.DBManager;
import com.lsmsdbgroup.pisaflix.PisaFlixServices;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;


public class LoginController implements Initializable {

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private Label loginStatusLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private Label errorLabel;
    @FXML
    private Button showProfileButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        update();
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }    
    
    public void update(){
        boolean logged = PisaFlixServices.Authentication.isUserLogged();
        this.usernameTextField.setText("");
        this.usernameTextField.setVisible(!logged);
        this.usernameTextField.setManaged(!logged);
        this.passwordTextField.setText("");
        this.passwordTextField.setVisible(!logged);
        this.passwordTextField.setManaged(!logged);
        this.loginButton.setVisible(!logged);
        this.loginButton.setManaged(!logged);
        this.registerButton.setVisible(!logged);
        this.registerButton.setManaged(!logged);
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        
        if(logged){
            this.loginStatusLabel.setText(PisaFlixServices.Authentication.getInfoString());
        }
        this.loginStatusLabel.setVisible(logged);
        this.loginStatusLabel.setManaged(logged);
        this.logoutButton.setVisible(logged);
        this.logoutButton.setManaged(logged);
        this.showProfileButton.setVisible(logged);
        this.logoutButton.setManaged(logged);
    }
    
    @FXML
    private void clickLoginButton(){
        try {
            String username = this.usernameTextField.getText();
            String password = this.passwordTextField.getText();
            if(username.isBlank()){
                errorLabel.setText("Username is empty");
                errorLabel.setTextFill(Color.RED);
                errorLabel.setVisible(true);
                errorLabel.setManaged(true);
                return;
            }
            if(password.isBlank()){
                errorLabel.setText("Password is empty");
                errorLabel.setTextFill(Color.RED);
                errorLabel.setVisible(true);
                errorLabel.setManaged(true);
                return;
            }
            
            PisaFlixServices.Authentication.Login(username, password);
            update();
            App.setMainPane("WelcomeBack");
        } catch (PisaFlixServices.Authentication.UserAlredyLoggedException | PisaFlixServices.Authentication.InvalidCredentialsException ex) {
            errorLabel.setText("Invalid Credentials");
            errorLabel.setTextFill(Color.RED);
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
        }
    }
    
    @FXML
    private void clickRegisterButton(){
        App.setMainPane("Registration");
    }

    
    @FXML
    private void clickLogoutButton(){       
        PisaFlixServices.Authentication.Logout(); 
        update();
        App.setMainPane("Welcome");
    }
    
    @FXML
    private void showUser()
    {
        App.setMainPane("UserView");
    }
}
