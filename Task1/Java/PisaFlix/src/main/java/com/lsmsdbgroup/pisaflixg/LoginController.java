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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;


/**
 * FXML Controller class
 *
 * @author alessandromadonna
 */
public class LoginController implements Initializable {

    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button loginButton;
    @FXML
    private Label loginStatusLabel;
    @FXML
    private Button logoutButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        update();
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
        
        if(logged){
            this.loginStatusLabel.setText(PisaFlixServices.Authentication.getInfoString());
        }
        this.loginStatusLabel.setVisible(logged);
        this.loginStatusLabel.setManaged(logged);
        this.logoutButton.setVisible(logged);
        this.logoutButton.setManaged(logged);
    }
    
    @FXML
    private void clickLoginButton(){
        try {
            String username = this.usernameTextField.getText();
            String password = this.passwordTextField.getText();
            if(username.isBlank()){
                return;
            }
            if(password.isBlank()){
                return;
            }
            
            PisaFlixServices.Authentication.Login(username, password); 
        } catch (PisaFlixServices.Authentication.UserAlredyLoggedException | PisaFlixServices.Authentication.InvalidCredentialsException ex) {
            System.out.println(ex.getMessage());
        }
        update();
    }
    
    @FXML
    private void clickLogoutButton(){       
        PisaFlixServices.Authentication.Logout(); 
        update();
    }
    
}
