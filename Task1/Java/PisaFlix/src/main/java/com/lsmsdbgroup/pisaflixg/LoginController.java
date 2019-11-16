package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.InvalidCredentialsException;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.UserAlredyLoggedException;
import java.net.URL;
import java.util.ResourceBundle;
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
        boolean logged = PisaFlixServices.authenticationService.isUserLogged();
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
            this.loginStatusLabel.setText(PisaFlixServices.authenticationService.getInfoString());
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
            
            PisaFlixServices.authenticationService.Login(username, password); 
            update();
            App.setMainPageReturnsController("WelcomeBack");
        } catch ( UserAlredyLoggedException | InvalidCredentialsException ex) {
            System.out.println(ex.getMessage());
            errorLabel.setText("Invalid Credentials");
            errorLabel.setTextFill(Color.RED);
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
        }
    }
    
    @FXML
    private void clickRegisterButton(){
        App.setMainPageReturnsController("Registration");
    }

    
    @FXML
    private void clickLogoutButton(){       
        PisaFlixServices.authenticationService.Logout(); 
        update();
        App.setMainPageReturnsController("Welcome");
    }
    
    @FXML
    private void showUser()
    {
        App.setMainPageReturnsController("UserView");
    }
}
