package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
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
    
    @FXML
    private Button writeButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            update();
            errorLabel.setVisible(false);
            errorLabel.setManaged(false);
            showProfileButton.setVisible(false);
            showProfileButton.setManaged(false);
            writeButton.setVisible(false);
            writeButton.setManaged(false);
        } catch (Exception ex) {
            App.printErrorDialog("Login", "There was an inizialization error", ex.toString() + "\n" + ex.getMessage());
        }
    }

    public void update() {
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

        if (logged) {
            this.loginStatusLabel.setText(PisaFlixServices.authenticationService.getInfoString());
        }
        
        this.loginStatusLabel.setVisible(logged);
        this.loginStatusLabel.setManaged(logged);
        this.logoutButton.setVisible(logged);
        this.logoutButton.setManaged(logged);
        this.showProfileButton.setVisible(logged);
        this.showProfileButton.setManaged(logged);
        this.logoutButton.setManaged(logged);
        this.writeButton.setVisible(logged);
        this.writeButton.setManaged(logged);
    }

    @FXML
    private void clickLoginButton() {
        try {
            try {
                String username = this.usernameTextField.getText();
                String password = this.passwordTextField.getText();
                if (username.isBlank()) {
                    errorLabel.setText("Username is empty");
                    errorLabel.setTextFill(Color.RED);
                    errorLabel.setVisible(true);
                    errorLabel.setManaged(true);
                    return;
                }
                if (password.isBlank()) {
                    errorLabel.setText("Password is empty");
                    errorLabel.setTextFill(Color.RED);
                    errorLabel.setVisible(true);
                    errorLabel.setManaged(true);
                    return;
                }

                PisaFlixServices.authenticationService.login(username, password);
                update();
                App.setMainPageReturnsController("WelcomeBack");
            } catch (UserAlredyLoggedException | InvalidCredentialsException ex) {
                System.out.println(ex.getMessage());
                errorLabel.setText("Invalid Credentials");
                errorLabel.setTextFill(Color.RED);
                errorLabel.setVisible(true);
                errorLabel.setManaged(true);
            }
        } catch (Exception ex) {
            App.printErrorDialog("Login", "An error occurred during login", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    private void clickRegisterButton() {
        App.setMainPageReturnsController("Registration");
    }

    @FXML
    private void clickLogoutButton() {
        try {
            PisaFlixServices.authenticationService.logout();
            update();
            App.setMainPageReturnsController("Welcome");
        } catch (Exception ex) {
            App.printErrorDialog("Login", "An error occurred", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    private void showUser() {
        App.setMainPageReturnsController("UserView");
    }
}
