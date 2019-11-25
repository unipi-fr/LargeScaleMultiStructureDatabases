package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class UpdateProfileController implements Initializable {

    private User user;
    
    @FXML
    private TextField usernameTextField;
    
    @FXML
    private TextField firstnameTextField;
    
    @FXML
    private TextField lastnameTextField;
    
    @FXML
    private TextField emailTextField;
    
    @FXML
    private PasswordField newpassPass;
    
    @FXML        
    private PasswordField confirmPass;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Button updateButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        user = PisaFlixServices.authenticationService.getLoggedUser();
        
        refreshFields();
    }
    
    private void refreshFields(){
        usernameTextField.setText(user.getUsername());
        firstnameTextField.setText(user.getFirstName());
        lastnameTextField.setText(user.getLastName());
        emailTextField.setText(user.getEmail());
    }
    
    public void setUser(User u){
        this.user = u;
        refreshFields();
    }
    
    @FXML
    private void updateProfile(){
        user.setUsername(usernameTextField.getText());
        user.setFirstName(firstnameTextField.getText());
        user.setLastName(lastnameTextField.getText());
        user.setEmail(emailTextField.getText());
        
        String newpass = newpassPass.getText();
        String confirmpass = confirmPass.getText();
        
        if(!newpass.equals(""))
        {
            if(newpass.equals(confirmpass))
            {
                user.setPassword(newpass);
            } else {
                if (App.printWarningDialog("Password not match", "The password you insert doen't match", "New password and the confirmation password must match")){
                    return;
                }
            }
        }
        
        if (!App.printConfirmationDialog("Profile updated", "You are changing "+user.getUsername()+" personal data", "Are you sure to continue")){
            return;
        }
        
        PisaFlixServices.userService.updateUser(user);
        
        UserViewController uvc = (UserViewController) App.setMainPageReturnsController("UserView");
        uvc.setUser(user);
        
    }
    
    @FXML
    private void cancelAction(){
        UserViewController uvc = (UserViewController) App.setMainPageReturnsController("UserView");
        uvc.setUser(user);
    }
    
}
