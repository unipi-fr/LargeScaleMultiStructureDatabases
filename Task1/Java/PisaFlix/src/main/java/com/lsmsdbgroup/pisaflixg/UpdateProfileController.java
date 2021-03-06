package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
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
        try {
            user = PisaFlixServices.authenticationService.getLoggedUser();
            refreshFields();
        } catch (Exception ex) {
            App.printErrorDialog("Update Profile", "An error occurred loading the page", ex.toString() + "\n" + ex.getMessage());
        }
    }

    private void refreshFields() {
        usernameTextField.setText(user.getUsername());
        firstnameTextField.setText(user.getFirstName());
        lastnameTextField.setText(user.getLastName());
        emailTextField.setText(user.getEmail());
    }

    public void setUser(User u) {
        this.user = u;
        refreshFields();
    }

    @FXML
    private void updateProfile() {
        try {
            user.setUsername(usernameTextField.getText());
            user.setFirstName(firstnameTextField.getText());
            user.setLastName(lastnameTextField.getText());
            user.setEmail(emailTextField.getText());

            String newpass = newpassPass.getText();
            String confirmpass = confirmPass.getText();

            if (!newpass.equals("")) {
                if (newpass.equals(confirmpass)) {
                    user.setPassword(newpass);
                } else {
                    if (App.printWarningDialog("Password not match", "The password you insert doen't match", "New password and the confirmation password must match")) {
                        return;
                    }
                }
            }

            if (!App.printConfirmationDialog("Profile updated", "You are changing " + user.getUsername() + " personal data", "Are you sure to continue")) {
                return;
            }

            PisaFlixServices.userService.updateUser(user);

            UserViewController uvc = (UserViewController) App.setMainPageReturnsController("UserView");
            uvc.setUser(user);
        } catch (Exception ex) {
            App.printErrorDialog("Update Profile", "An error occurred updating the profile", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    private void cancelAction() {
        UserViewController uvc = (UserViewController) App.setMainPageReturnsController("UserView");
        uvc.setUser(user);
    }

}
