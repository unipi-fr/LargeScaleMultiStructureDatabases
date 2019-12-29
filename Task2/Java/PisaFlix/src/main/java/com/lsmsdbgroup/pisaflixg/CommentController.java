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

public class CommentController implements Initializable {

    private final StringProperty usernameProperty = new SimpleStringProperty();
    private final StringProperty timestampProperty = new SimpleStringProperty();
    private final StringProperty commentProperty = new SimpleStringProperty();

    private Comment comment;

    public CommentController(String username, String timestamp, String commment, int type) {
        usernameProperty.set(username);
        timestampProperty.set(timestamp);
        commentProperty.set(commment);
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    @FXML
    private VBox commentVbox;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label timestampLabel;

    @FXML
    private Label commentLabel;

    @FXML
    private ContextMenu commentMenu;

    @FXML
    private MenuItem updateMenuItem;

    @FXML
    private MenuItem deleteMenuItem;

    @FXML
    private TextArea commentTextArea;

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
            commentLabel.setText(commentProperty.get());

            commentLabel.setMinHeight(Region.USE_PREF_SIZE);

            commentTextArea.setVisible(false);
            commentTextArea.setManaged(false);
            buttonHbox.setVisible(false);
            buttonHbox.setManaged(false);

            if (!PisaFlixServices.authenticationService.isUserLogged() || (PisaFlixServices.authenticationService.getLoggedUser().getPrivilegeLevel() < UserPrivileges.SOCIAL_MODERATOR.getValue() && !comment.getUser().equals(PisaFlixServices.authenticationService.getLoggedUser()))) {
                deleteMenuItem.setVisible(false);
            } else {
                deleteMenuItem.setVisible(true);
            }

            if (PisaFlixServices.authenticationService.isUserLogged() && comment.getUser().equals(PisaFlixServices.authenticationService.getLoggedUser())) {
                updateMenuItem.setVisible(true);
            } else {
                updateMenuItem.setVisible(false);
            }

        } catch (Exception ex) {
            App.printErrorDialog("Comment", "An error occurred loading the comment", ex.toString() + "\n" + ex.getMessage());
        }

    }

    private boolean canMenuBeVisible() {
        try {
            User user = PisaFlixServices.authenticationService.getLoggedUser();
            if (user != null) {
                if (Objects.equals(comment.getUser().getId(), user.getId())) {
                    return true;
                }
                PisaFlixServices.authenticationService.checkUserPrivilegesForOperation(UserPrivileges.SOCIAL_MODERATOR, "Delete/Update other user comment");
                return true;
            }
        } catch (InvalidPrivilegeLevelException | UserNotLoggedException ex) {
            App.printErrorDialog("Comment Menu", "An error occurred", ex.getMessage());
        }
        return false;
    }

    private void switchState(boolean state) {
        commentTextArea.setVisible(state);
        commentTextArea.setManaged(state);

        buttonHbox.setVisible(state);
        buttonHbox.setManaged(state);

        commentLabel.setVisible(!state);
        commentLabel.setManaged(!state);
    }

    private void refreshComments() {
        FilmDetailPageController filmDetailPageController = App.getLoader().getController();
        filmDetailPageController.refreshComments(filmDetailPageController.getCurrentPage());
    }

    @FXML
    private void confirmComment() {
        try {
            if (!App.printConfirmationDialog("Updating a comment", "You are updating a comment", "Are you sure to continue")) {
                return;
            }

            comment.setText(commentTextArea.getText());

            try {
                if (comment.getUser().equals(PisaFlixServices.authenticationService.getLoggedUser())) {
                    PisaFlixServices.commentService.update(comment);
                } else {
                    App.printErrorDialog("Updating comment", "An error occurred while updating comment", "Only the user who's written the commet can update it.");
                }
            } catch (InvalidPrivilegeLevelException | UserNotLoggedException ex) {
                App.printErrorDialog("Updating comment", "An error occurred while updating comment", ex.getMessage());
            }

            switchState(false);

            refreshComments();

            commentLabel.setText(comment.getText());
        } catch (Exception ex) {
            App.printErrorDialog("Comment Update", "An error occurred updating the comment", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    private void cancelComment() {
        switchState(false);
    }

    @FXML
    private void updateComment() {
        switchState(true);

        commentTextArea.setText(commentLabel.getText());
    }

    @FXML
    private void deleteComment() {
        try {
            if (!App.printConfirmationDialog("Deleting a comment", "You are deleting a comment", "Are you sure to continue")) {
                return;
            }

            try {
                PisaFlixServices.commentService.delete(comment);
            } catch (InvalidPrivilegeLevelException | UserNotLoggedException ex) {
                App.printErrorDialog("Deleting comment", "An error occurred while deleting comment", ex.getMessage());
            }
            refreshComments();
        } catch (Exception ex) {
            App.printErrorDialog("Delete Comment", "An error occurred in deleting the comment", ex.toString() + "\n" + ex.getMessage());
        }
    }
}
