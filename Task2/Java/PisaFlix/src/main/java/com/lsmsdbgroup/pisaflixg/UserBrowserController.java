package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflix.pisaflixservices.UserPrivileges;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;

public class UserBrowserController extends BrowserController implements Initializable {

    private ExecutorService executorService;

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        try {
            super.initialize();
            filterTextField.setPromptText("Name filter");
            searchUsers(null);
        } catch (Exception ex) {
            App.printErrorDialog("Users", "An error occurred loading the page", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @Override
    public Pane createCardPane(String name, String privilege, String id) {
        Pane pane = new Pane();
        try {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("UserCard.fxml"));
                UserCardController fcc = new UserCardController(name, privilege, id);
                loader.setController(fcc);
                pane = loader.load();
            } catch (IOException ex) {
                App.printErrorDialog("Users", "An error occurred loading the user card", ex.toString() + "\n" + ex.getMessage());
            }
        } catch (Exception ex) {
            App.printErrorDialog("Users", "An error occurred loading the user card", ex.toString() + "\n" + ex.getMessage());
        }

        return pane;
    }

    class TileWorker extends Task<Pane> {

        User user;

        public TileWorker(User user) {
            this.user = user;
        }

        @Override
        protected Pane call() throws Exception {
            String username;
            String privilege;
            int level;
            Pane pane;

            username = user.getUsername();
            level = user.getPrivilegeLevel();
            privilege = UserPrivileges.valueOf(level);
            pane = createCardPane(username, privilege, user.getId());
            return pane;
        }

    }

    public void populateScrollPane(Set<User> users) {
        try {
            tilePane.getChildren().clear();
            if (users.size() > 0) {
                executorService = Executors.newFixedThreadPool(users.size(), (Runnable r) -> {
                    Thread t = Executors.defaultThreadFactory().newThread(r);
                    t.setDaemon(true);
                    return t;
                });
                progressIndicator.setProgress(0);
                for (User user : users) {
                    TileWorker tileWarker = new TileWorker(user);
                    tileWarker.setOnSucceeded((succeededEvent) -> {
                        progressIndicator.setProgress(progressIndicator.getProgress() + 1 / Double.valueOf(users.size()));
                        if (!tileWarker.isCancelled()) {
                            if (!tileWarker.isCancelled()) {
                                tilePane.getChildren().add(tileWarker.getValue());
                            }
                            if (executorService.isTerminated()) {
                                filterTextField.setDisable(false);
                                filterTextField.requestFocus();
                                filterTextField.selectEnd();
                                progressIndicator.setProgress(1);
                                searchButton.setDisable(false);
                            }
                        }
                    });
                    executorService.execute(tileWarker);
                }
                executorService.shutdown();
                filterTextField.setDisable(true);
                searchButton.setDisable(true);
            }
        } catch (Exception ex) {
            App.printErrorDialog("Users", "An error occurred loading the users", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    @Override
    public void filter() {
        try {
            String usernameFilter = filterTextField.getText();

            searchUsers(usernameFilter);
        } catch (Exception ex) {
            App.printErrorDialog("Users", "An error occurred loading the users", ex.toString() + "\n" + ex.getMessage());
        }
    }

    private void searchUsers(String usernameFilter) {
        try {
            Set<User> users = PisaFlixServices.userService.getFiltered(usernameFilter);

            populateScrollPane(users);
        } catch (Exception ex) {
            App.printErrorDialog("Users", "An error occurred loading the users", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    @Override
    public void add() {
        App.setMainPageReturnsController("registration");
    }
}
