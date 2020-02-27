package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class UserViewController implements Initializable {

    private User user;
    
    private ChangeListener<Film> filmListener;
    
    private ChangeListener<User> userListener;
    
    @FXML
    private Button FollowButton;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label firstnameLabel;

    @FXML
    private Label lastnameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label followingCount;
    
    @FXML
    private Label followerCount;

    @FXML
    private Label postCounterLabel;

    @FXML
    private ListView list;
    
    @FXML
    private Text listText;

    @FXML
    private ImageView userImage;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            user = PisaFlixServices.authenticationService.getLoggedUser();
            
            if (PisaFlixServices.authenticationService.isUserLogged() && user != PisaFlixServices.authenticationService.getLoggedUser()) {
                FollowButton.setDisable(false);
                setFollowButton();
            }else{
                FollowButton.setVisible(false);
                FollowButton.setManaged(false);
            }

            if (user != null) {
                usernameLabel.setText(user.getUsername());
                firstnameLabel.setText(user.getFirstName());
                lastnameLabel.setText(user.getLastName());
                emailLabel.setText(user.getEmail());
            }

            Random random = new Random();
            int img = random.nextInt(3) + 1;

            File file = new File("src/main/resources/img/user" + img + ".png");

            Image image = new Image(file.toURI().toString());
            userImage.setImage(image);
            
            if(user != null)
                showPosts();
            
            filmListener = (ObservableValue<? extends Film> observable, Film oldValue, Film newValue) -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FilmDetailPage.fxml"));

                GridPane gridPane = null;

                try {
                    gridPane = loader.load();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }

                FilmDetailPageController filmDetailPageController = loader.getController();

                filmDetailPageController.setFilm(newValue);

                App.setMainPane(gridPane);
            };
            
            userListener = (ObservableValue<? extends User> observable, User oldValue, User newValue) -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("UserView.fxml"));

                GridPane gridPane = null;

                try {
                    gridPane = loader.load();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }

                UserViewController userViewController = loader.getController();

                userViewController.setUser(newValue);

                App.setMainPane(gridPane);
            };
           
        } catch (Exception ex) {
            App.printErrorDialog("User Details", "An error occurred loading the user's details", ex.toString() + "\n" + ex.getMessage());
        }
    }

    private boolean canUpdateOrDeleteProfile() {
        try {
            PisaFlixServices.authenticationService.checkUserPrivilegesForOperation(UserPrivileges.ADMIN, "Update/Delete others account");
        } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
            User loggedUser = PisaFlixServices.authenticationService.getLoggedUser();
            if (loggedUser == null) {
                return false;
            }
            if (!Objects.equals(loggedUser.getId(), this.user.getId())) {
                return false;
            }
        }
        return true;
    }

    public void setUser(User user) {
        try {
            this.user = user;

            usernameLabel.setText(user.getUsername());
            firstnameLabel.setText(user.getFirstName());
            lastnameLabel.setText(user.getLastName());
            emailLabel.setText(user.getEmail());

            if (!canUpdateOrDeleteProfile()) {
                deleteButton.setVisible(false);
                deleteButton.setManaged(false);
                
            }

            if (!this.user.equals(PisaFlixServices.authenticationService.getLoggedUser())) {
                updateButton.setVisible(false);
                updateButton.setManaged(false);
            }
            
            if (PisaFlixServices.authenticationService.isUserLogged() && !this.user.equals(PisaFlixServices.authenticationService.getLoggedUser())) {
                FollowButton.setDisable(false);
                setFollowButton();
            }else{
                FollowButton.setVisible(false);
                FollowButton.setManaged(false);
            }

            postCounterLabel.setText("Post: " + PisaFlixServices.postService.count(user));
            followingCount.setText("Following: " + PisaFlixServices.userService.countTotalFollowing(user));
            followerCount.setText("Follower: " + PisaFlixServices.userService.countFollowers(user));
            
            addTags();
            
            showPosts();
        } catch (Exception ex) {
            App.printErrorDialog("Users", "An error occurred loading the users", ex.toString() + "\n" + ex.getMessage());
        }
    }

    private void showPosts() {
        
    }

    @FXML
    private void updateProfile() {
        try {
            UpdateProfileController updateProfileController = (UpdateProfileController) App.setMainPageReturnsController("UpdateProfile");
            updateProfileController.setUser(this.user);
        } catch (Exception ex) {
            App.printErrorDialog("Update Profile", "An error occurred loading update profile page", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    private void deleteProfile() {
        try {
            User u = this.user;
            if (u == null) {
                return;
            }
            if (!App.printConfirmationDialog("Deleting profile", "You're deleting " + u.getUsername() + " profile", "Are you sure do you want continue?")) {
                return;
            }
            try {
                PisaFlixServices.userService.deleteUserAccount(u);
                App.setMainPageReturnsController("Welcome");
                App.resetLogin();
            } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (Exception ex) {
            App.printErrorDialog("Delete User", "An error occurred deleting the user", ex.toString() + "\n" + ex.getMessage());
        }
    }
    
    @FXML
    private void FollowUnfollow() {
        if (PisaFlixServices.userService.isFollowing(PisaFlixServices.authenticationService.getLoggedUser(), user)) {
            PisaFlixServices.userService.unfollow(PisaFlixServices.authenticationService.getLoggedUser(), user);
            FollowButton.setText("+ Follow");
        } else {
            PisaFlixServices.userService.follow(PisaFlixServices.authenticationService.getLoggedUser(), user);
            FollowButton.setText("- Unfollow");
        }
    }
    
    public void setFollowButton() {
        if (PisaFlixServices.authenticationService.isUserLogged()) {
            if (!PisaFlixServices.userService.isFollowing(PisaFlixServices.authenticationService.getLoggedUser(), user)) {
                FollowButton.setText("+ Follow");
            } else {
                FollowButton.setText("- Unfollow");
            }
        }
    }
    
    @FXML
    private void showFollowers() {
        
        try {
            
            Set<User> userSet = PisaFlixServices.userService.getFollowers(user);
            listText.setText("Followers List:"); 
            
            if(userSet.isEmpty()){
                list.getItems().clear();
                return;
            }

            ObservableList<User> observableUsers = FXCollections.observableArrayList(userSet);
            list.setItems(observableUsers);

            list.getSelectionModel().selectedItemProperty().removeListener(filmListener);
            list.getSelectionModel().selectedItemProperty().addListener(userListener);
            
        } catch (Exception ex) {
            App.printErrorDialog("Favorite Films", "An error occurred loading followers", ex.toString() + "\n" + ex.getMessage());
        }
    }
    
    @FXML
    private void showFollowingFilms() {
        try {
            listText.setText("Following Films List:"); 
            Set<Film> filmSet = PisaFlixServices.userService.getFollowingFilms(user);
            
            if(filmSet.isEmpty()){
                list.getItems().clear();
                return;
            }

            ObservableList<Film> observableFilms = FXCollections.observableArrayList(filmSet);
            list.setItems(observableFilms);

            list.getSelectionModel().selectedItemProperty().removeListener(userListener);
            list.getSelectionModel().selectedItemProperty().addListener(filmListener);
            
        } catch (Exception ex) {
            App.printErrorDialog("Favorite Films", "An error occurred loading following films", ex.toString() + "\n" + ex.getMessage());
        }
    }
    
    @FXML
    private void showFollowingUsers() {
        try {
            listText.setText("Following Users List:"); 
            Set<User> userSet = PisaFlixServices.userService.getFollowingUsers(user);

            if(userSet.isEmpty()){
                list.getItems().clear();
                return;
            }
            
            ObservableList<User> observableUsers = FXCollections.observableArrayList(userSet);
            list.setItems(observableUsers);
            
            list.getSelectionModel().selectedItemProperty().removeListener(filmListener);
            list.getSelectionModel().selectedItemProperty().addListener(userListener);
            
        } catch (Exception ex) {
            App.printErrorDialog("Favorite Films", "An error occurred loading following users", ex.toString() + "\n" + ex.getMessage());
        }
    }

    private void addTags() {
        
    }
    

}
