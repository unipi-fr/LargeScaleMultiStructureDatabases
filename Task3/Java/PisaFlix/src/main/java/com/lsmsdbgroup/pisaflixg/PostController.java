package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.net.URL;
import java.util.*;
import javafx.beans.property.*;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class PostController implements Initializable {

    private final StringProperty usernameProperty = new SimpleStringProperty();
    private final StringProperty timestampProperty = new SimpleStringProperty();
    private final StringProperty postProperty = new SimpleStringProperty();

    private Post post;

    public PostController(String username, String timestamp, String commment, int type) {
        usernameProperty.set(username);
        timestampProperty.set(timestamp);
        postProperty.set(commment);
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @FXML
    private VBox postVbox;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label timestampLabel;

    @FXML
    private Label postLabel;

    @FXML
    private ContextMenu postMenu;

    @FXML
    private MenuItem updateMenuItem;

    @FXML
    private MenuItem deleteMenuItem;

    @FXML
    private TextArea postTextArea;

    @FXML
    private HBox buttonHbox;

    @FXML
    private Button cancelButton;

    @FXML
    private Button updateButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            usernameLabel.setText(usernameProperty.get());
            timestampLabel.setText(timestampProperty.get());
            postLabel.setText(postProperty.get());

            postLabel.setMinHeight(Region.USE_PREF_SIZE);

            postTextArea.setVisible(false);
            postTextArea.setManaged(false);
            buttonHbox.setVisible(false);
            buttonHbox.setManaged(false);

            if (!PisaFlixServices.authenticationService.isUserLogged() || (PisaFlixServices.authenticationService.getLoggedUser().getPrivilegeLevel() < UserPrivileges.SOCIAL_MODERATOR.getValue() && !post.getUser().equals(PisaFlixServices.authenticationService.getLoggedUser()))) {
                deleteMenuItem.setVisible(false);
            } else {
                deleteMenuItem.setVisible(true);
            }

            if (PisaFlixServices.authenticationService.isUserLogged() && post.getUser().equals(PisaFlixServices.authenticationService.getLoggedUser())) {
                updateMenuItem.setVisible(true);
            } else {
                updateMenuItem.setVisible(false);
            }

        } catch (Exception ex) {
            App.printErrorDialog("Post", "An error occurred loading the post", ex.toString() + "\n" + ex.getMessage());
        }

    }

    private boolean canMenuBeVisible() {
        try {
            User user = PisaFlixServices.authenticationService.getLoggedUser();
            if (user != null) {
                if (Objects.equals(post.getUser().getId(), user.getId())) {
                    return true;
                }
                PisaFlixServices.authenticationService.checkUserPrivilegesForOperation(UserPrivileges.SOCIAL_MODERATOR, "Delete/Update other user post");
                return true;
            }
        } catch (InvalidPrivilegeLevelException | UserNotLoggedException ex) {
            App.printErrorDialog("Post Menu", "An error occurred", ex.getMessage());
        }
        return false;
    }

    private void switchState(boolean state) {
        postTextArea.setVisible(state);
        postTextArea.setManaged(state);

        buttonHbox.setVisible(state);
        buttonHbox.setManaged(state);

        postLabel.setVisible(!state);
        postLabel.setManaged(!state);
    }

    private void refreshPosts() {
        FilmDetailPageController filmDetailPageController = App.getLoader().getController();
        filmDetailPageController.refreshPosts(filmDetailPageController.getCurrentPage());
    }

    @FXML
    private void confirmPost() {
        try {
            if (!App.printConfirmationDialog("Updating a post", "You are updating a post", "Are you sure to continue")) {
                return;
            }

            post.setText(postTextArea.getText());

            try {
                if (post.getUser().equals(PisaFlixServices.authenticationService.getLoggedUser())) {
                    PisaFlixServices.postService.update(post.getIdPost(), post.getText());
                } else {
                    App.printErrorDialog("Updating post", "An error occurred while updating post", "Only the user who's written the commet can update it.");
                }
            } catch (Exception ex) {
                App.printErrorDialog("Updating post", "An error occurred while updating post", ex.getMessage());
            }

            switchState(false);

            refreshPosts();

            postLabel.setText(post.getText());
        } catch (Exception ex) {
            App.printErrorDialog("Post Update", "An error occurred updating the post", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    private void cancelPost() {
        switchState(false);
    }

    @FXML
    private void updatePost() {
        switchState(true);

        postTextArea.setText(postLabel.getText());
    }

    @FXML
    private void deletePost() {
        try {
            if (!App.printConfirmationDialog("Deleting a post", "You are deleting a post", "Are you sure to continue")) {
                return;
            }

            try {
                PisaFlixServices.postService.delete(post.getIdPost());
            } catch (Exception ex) {
                App.printErrorDialog("Deleting post", "An error occurred while deleting post", ex.getMessage());
            }
            refreshPosts();
        } catch (Exception ex) {
            App.printErrorDialog("Delete Post", "An error occurred in deleting the post", ex.toString() + "\n" + ex.getMessage());
        }
    }
}
