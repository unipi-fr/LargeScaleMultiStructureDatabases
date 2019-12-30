package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class UserViewController implements Initializable {

    private User user;

    private ChangeListener<Film> filmListener;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label firstnameLabel;

    @FXML
    private Label lastnameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label favoriteCounterLabel;

    @FXML
    private Label commentCounterLabel;

    @FXML
    private ListView favoriteList;

    @FXML
    private ImageView userImage;

    @FXML
    private Button filmButton;

    @FXML
    private Button cinemaButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            user = PisaFlixServices.authenticationService.getLoggedUser();

            if (user != null) {
                usernameLabel.setText(user.getUsername());
                firstnameLabel.setText(user.getFirstName());
                lastnameLabel.setText(user.getLastName());
                emailLabel.setText(user.getEmail());

                commentCounterLabel.setText("Comments: " + PisaFlixServices.commentService.count(user));
            }

            Random random = new Random();
            int img = random.nextInt(3) + 1;

            File file = new File("src/main/resources/img/user" + img + ".png");

            Image image = new Image(file.toURI().toString());
            userImage.setImage(image);

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
            
            if(user != null)
                showFavoriteFilms();
        } catch (Exception ex) {
            App.printErrorDialog("User Details", "An error occurred loading the user's details", ex.toString() + "\n" + ex.getMessage());
        }
    }

    private boolean canUpdateOrdDeleteProfile() {
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

            if (!canUpdateOrdDeleteProfile()) {
                deleteButton.setDisable(true);
            }

            if (!this.user.equals(PisaFlixServices.authenticationService.getLoggedUser())) {
                updateButton.setDisable(true);
            }

            commentCounterLabel.setText("Comments: " + PisaFlixServices.commentService.count(user));
            showFavoriteFilms();
        } catch (Exception ex) {
            App.printErrorDialog("Users", "An error occurred loading the users", ex.toString() + "\n" + ex.getMessage());
        }
    }

    private void showFavoriteFilms() {
        try {
            favoriteCounterLabel.setText("Favourites: " + PisaFlixServices.engageService.count(user, Entity.EntityType.FAVOURITE));
            PisaFlixServices.userService.getFavourites(user);
            Set<Film> films = user.getFilmSet();

            ObservableList<Film> observableFilms = FXCollections.observableArrayList(films);
            favoriteList.setItems(observableFilms);

            favoriteList.getSelectionModel().selectedItemProperty().addListener(filmListener);
        } catch (Exception ex) {
            App.printErrorDialog("Favorite Films", "An error occurred loading the favorite films", ex.toString() + "\n" + ex.getMessage());
        }
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

}
