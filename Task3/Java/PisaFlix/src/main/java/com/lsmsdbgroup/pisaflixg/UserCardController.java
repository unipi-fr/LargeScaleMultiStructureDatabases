package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.pisaflixservices.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.io.*;
import javafx.beans.property.StringProperty;
import java.net.URL;
import java.util.*;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class UserCardController implements Initializable {

    private final User user;

    private final StringProperty userProperty = new SimpleStringProperty();
    private final StringProperty privilegeProperty = new SimpleStringProperty();

    private int status;
    
    public UserCardController(User user) {
        this.user = user;
        userProperty.set(user.getUsername());
        privilegeProperty.set(UserPrivileges.valueOf(user.getPrivilegeLevel()));
        status = 0;
    }

    @FXML
    private AnchorPane cardPane;
    
    @FXML
    private VBox cardVbox;

    @FXML
    private VBox imageVBox;
    
    @FXML
    private ImageView userImageView;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label privilegeLabel;
    
    @FXML
    private Label suggestLabel;

    @FXML
    private ComboBox privilegeCombo;

    @FXML
    private ContextMenu privilegeMenu;

    @FXML
    private MenuItem updatePrivilegeMenuItem;

    @FXML
    private Button updatePrivilegeButton;

    @FXML
    private Button followButton;
    
    private ParallelTransition parallelTransitionUp;
    
    private ParallelTransition parallelTransitionDown;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            usernameLabel.setText(userProperty.get());
            privilegeLabel.setText(privilegeProperty.get());
            setFollowButton();
            Random random = new Random();
            int img = random.nextInt(3) + 1;

            File file = new File("src/main/resources/img/user" + img + ".png");

            Image image = new Image(file.toURI().toString());
            userImageView.setImage(image);

            Set<UserPrivileges> privilegeSet = new LinkedHashSet<>();

            privilegeSet.add(UserPrivileges.NORMAL_USER);
            privilegeSet.add(UserPrivileges.SOCIAL_MODERATOR);
            privilegeSet.add(UserPrivileges.MODERATOR);
            privilegeSet.add(UserPrivileges.ADMIN);

            privilegeCombo.getItems().setAll(privilegeSet);

            privilegeCombo.setVisible(false);
            privilegeCombo.setManaged(false);

            updatePrivilegeButton.setVisible(false);
            updatePrivilegeButton.setManaged(false);

            if (!PisaFlixServices.authenticationService.isUserLogged() || PisaFlixServices.authenticationService.getLoggedUser().getPrivilegeLevel() <= user.getPrivilegeLevel()) {
                updatePrivilegeMenuItem.setVisible(false);
            } else {
                updatePrivilegeMenuItem.setVisible(true);
            }
            
            if(PisaFlixServices.authenticationService.isUserLogged()){
                initializeAnimation();
                cardVbox.getStyleClass().add("hider");
            }
            
        } catch (Exception ex) {
            App.printErrorDialog("User Card", "An error occurred loading the user card", ex.toString() + "\n" + ex.getMessage());
        }
    }
    
    private void initializeAnimation(){
        ScaleTransition scaleTransitionUp = 
            new ScaleTransition(Duration.millis(250), userImageView);
        scaleTransitionUp.setFromX(1);
        scaleTransitionUp.setFromY(1);
        scaleTransitionUp.setToX(0.8);
        scaleTransitionUp.setToY(0.8);
        
        TranslateTransition translateTransitionVBoxUp =
            new TranslateTransition(Duration.millis(250), cardVbox);
        translateTransitionVBoxUp.setFromY(0);
        translateTransitionVBoxUp.setToY(-25);
        translateTransitionVBoxUp.setCycleCount(1);
        translateTransitionVBoxUp.setAutoReverse(true);
        
        TranslateTransition translateTransitionPosterUp =
            new TranslateTransition(Duration.millis(250), userImageView);
        translateTransitionPosterUp.setFromY(0);
        translateTransitionPosterUp.setToY(-10);
        translateTransitionPosterUp.setCycleCount(1);
        translateTransitionPosterUp.setAutoReverse(true);
        
        parallelTransitionUp = new ParallelTransition();
        
        parallelTransitionUp.getChildren().addAll(
                scaleTransitionUp,
                translateTransitionVBoxUp,
                translateTransitionPosterUp
        );
        
        ScaleTransition scaleTransitionDown = 
            new ScaleTransition(Duration.millis(250), userImageView);
        scaleTransitionDown.setFromX(0.8);
        scaleTransitionDown.setFromY(0.8);
        scaleTransitionDown.setToX(1);
        scaleTransitionDown.setToY(1);
        
        TranslateTransition translateTransitionVBoxDown =
            new TranslateTransition(Duration.millis(250), cardVbox);
        translateTransitionVBoxDown.setFromY(-25);
        translateTransitionVBoxDown.setToY(0);
        translateTransitionVBoxDown.setCycleCount(1);
        translateTransitionVBoxDown.setAutoReverse(true);
        
        TranslateTransition translateTransitionPosterDown =
            new TranslateTransition(Duration.millis(250), userImageView);
        translateTransitionPosterDown.setFromY(-10);
        translateTransitionPosterDown.setToY(0);
        translateTransitionPosterDown.setCycleCount(1);
        translateTransitionPosterDown.setAutoReverse(true);
        
        parallelTransitionDown = new ParallelTransition();
        
        parallelTransitionDown.getChildren().addAll(
                scaleTransitionDown,
                translateTransitionVBoxDown,
                translateTransitionPosterDown
        );
    }

    @FXML
    private void updatePrivilege() {
        try {

            followButton.setVisible(false);
            followButton.setManaged(false);

            imageVBox.setVisible(false);
            imageVBox.setManaged(false);

            privilegeCombo.setVisible(true);
            privilegeCombo.setManaged(true);

            privilegeLabel.setVisible(false);
            privilegeLabel.setManaged(false);

            updatePrivilegeButton.setVisible(true);
            updatePrivilegeButton.setManaged(true);
           
            AnchorPane.setTopAnchor(cardVbox, 5.0);
            status = 1;
            

            int userPrivilege = user.getPrivilegeLevel();

            privilegeCombo.getSelectionModel().select(userPrivilege);
            
            parallelTransitionDown.play();
        } catch (Exception ex) {
            App.printErrorDialog("Update Privilege", "An error occurred updating the privilege", ex.toString() + "\n" + ex.getMessage());
        }
    }

    private void refreshUserCard() {
        usernameLabel.setText(user.getUsername());
        int level = user.getPrivilegeLevel();
        privilegeLabel.setText(UserPrivileges.valueOf(level));
    }

    @FXML
    private void modifyPrivilege() {
        try {
            if (!App.printConfirmationDialog("Updating Privilege", "You are updating the privileges", "Are you sure to continue")) {
                return;
            }

            user.setPrivilegeLevel(UserPrivileges.getLevel(privilegeCombo.getValue().toString()));

            UserPrivileges userPrivilege = (UserPrivileges) privilegeCombo.getValue();
            try {
                PisaFlixServices.userService.changeUserPrivileges(user, userPrivilege);
            } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
                App.printErrorDialog("Updating Privilege", "An error occurred while updating the privileges", ex.getMessage());
            }

            refreshUserCard();

            imageVBox.setVisible(true);
            imageVBox.setManaged(true);

            privilegeCombo.setVisible(false);
            privilegeCombo.setManaged(false);

            privilegeLabel.setVisible(true);
            privilegeLabel.setManaged(true);

            updatePrivilegeButton.setVisible(false);
            updatePrivilegeButton.setManaged(false);

            followButton.setVisible(true);
            followButton.setManaged(true);
            
            AnchorPane.setTopAnchor(cardVbox, 146.0);
            status = 2;
        } catch (Exception ex) {
            App.printErrorDialog("Update Privilege", "An error occurred updating the privilege", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    private void mouseClicked(MouseEvent event) {
        try {
            if (event.isSecondaryButtonDown()) {
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserView.fxml"));

            GridPane gridPane = null;

            try {
                gridPane = loader.load();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

            UserViewController userViewController = loader.getController();

            userViewController.setUser(user);

            App.setMainPane(gridPane);
        } catch (Exception ex) {
            App.printErrorDialog("User Card", "An error occurred", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    private void FollowUnfollow() {
        if (PisaFlixServices.userService.isFollowing(PisaFlixServices.authenticationService.getLoggedUser(), user)) {
            PisaFlixServices.userService.unfollow(PisaFlixServices.authenticationService.getLoggedUser(), user);
            followButton.setText("+ Follow");
        } else {
            PisaFlixServices.userService.follow(PisaFlixServices.authenticationService.getLoggedUser(), user);
            followButton.setText("- Unfollow");
        }
    }

    public void setFollowButton() {
        if (PisaFlixServices.authenticationService.isUserLogged() && !user.equals(PisaFlixServices.authenticationService.getLoggedUser())) {
            if (!PisaFlixServices.userService.isFollowing(PisaFlixServices.authenticationService.getLoggedUser(), user)) {

                if (user.type().equals("SUGGESTED")) {
                    suggestLabel.setText("Suggested");
                }

                if (user.type().equals("VERY SUGGESTED")) {
                    suggestLabel.setText("Very Suggested");
                }

                if (user.type().equals("NORMAL")) {
                    suggestLabel.setText("");
                }

            } else {
                followButton.setText("- Unfollow");
            }
        } else {
            followButton.setVisible(false);
            followButton.setManaged(false);
            suggestLabel.setVisible(false);
            suggestLabel.setManaged(false);
            userImageView.setFitHeight(116);
            userImageView.setFitWidth(116);
        }
    }

    @FXML
    private void animationUp(){
        if(PisaFlixServices.authenticationService.isUserLogged() && status == 0)
            parallelTransitionUp.play();
    }
    
    @FXML
    private void animationDown(){
        if(PisaFlixServices.authenticationService.isUserLogged() && status == 0)
            parallelTransitionDown.play();
        
        if(status == 2)
            status = 0;
    }
}
