package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Entity;
import com.lsmsdbgroup.pisaflix.Entities.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
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
            searchUsers("");
        } catch (Exception ex) {
            App.printErrorDialog("Users", "An error occurred loading the page", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @Override
    public Pane createCardPane(Entity user) {
        Pane pane = new Pane();
        
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserCard.fxml"));
            UserCardController fcc = new UserCardController((User) user);
            loader.setController(fcc);
            pane = loader.load();
            
        } catch (IOException ex) {
            App.printErrorDialog("Users", "An error occurred loading the user card", ex.toString() + "\n" + ex.getMessage());
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
            Pane pane;
            pane = createCardPane((User) user);
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
                users.stream().map((user) -> new TileWorker(user)).map((tileWorker) -> {
                    tileWorker.setOnSucceeded((succeededEvent) -> {
                        progressIndicator.setProgress(progressIndicator.getProgress() + 1 / Double.valueOf(users.size()));
                        if (!tileWorker.isCancelled()) {
                            if (!tileWorker.isCancelled()) {
                                tilePane.getChildren().add(tileWorker.getValue());
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
                    return tileWorker;
                }).forEachOrdered((tileWorker) -> {
                    executorService.execute(tileWorker);
                });
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
            Set<User> users = PisaFlixServices.userService.getFiltered(usernameFilter, 0);

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
