package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.Entity;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflix.pisaflixservices.UserPrivileges;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.InvalidPrivilegeLevelException;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.UserNotLoggedException;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

public class FilmBrowserController extends BrowserController implements Initializable {

    ExecutorService executorService;

    private double adultnessMargin;

    public FilmBrowserController() {
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            super.initialize();
            filterTextField.setPromptText("Title filter");
            if (PisaFlixServices.authenticationService.isUserLogged()) {
                adultnessMargin = PisaFlixServices.authenticationService.getLoggedUser().getAdultnessMargin();
                safeSearch.setValue(Math.round(adultnessMargin * 100));
                try {
                    adultnessMargin = (safeSearch.getValue() / 100);
                    if (PisaFlixServices.authenticationService.isUserLogged()) {
                        PisaFlixServices.authenticationService.getLoggedUser().setAdultnessMargin(adultnessMargin);
                        PisaFlixServices.userService.updateSafeSearchSettings(PisaFlixServices.authenticationService.getLoggedUser(), adultnessMargin);
                    }
                    Set<Film> films = PisaFlixServices.filmService.getSuggestedFilms(PisaFlixServices.authenticationService.getLoggedUser(), adultnessMargin);
                    populateScrollPane(films);
                } catch (Exception ex) {
                    App.printErrorDialog("Films", "An error occurred searching the films", ex.toString() + "\n" + ex.getMessage());
                }
            } else {
                filter();
            }
        } catch (Exception ex) {
            App.printErrorDialog("Films", "There was an inizialization error", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @Override
    public Pane createCardPane(Entity film) {
        Pane pane = new Pane();
        try {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FilmCard.fxml"));
                FilmCardController fcc = new FilmCardController((Film) film);
                loader.setController(fcc);
                pane = loader.load();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (Exception ex) {
            App.printErrorDialog("Films", "An error occurred creating the film card", ex.toString() + "\n" + ex.getMessage());
        }
        return pane;
    }

    @FXML
    @Override
    public void filter() {
        try {
            String titleFilter = filterTextField.getText();
            searchFilms(titleFilter, null);
        } catch (Exception ex) {
            App.printErrorDialog("Films", "An error occurred searching the films", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    @Override
    public void add() {
        try {
            try {
                PisaFlixServices.authenticationService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "add a new film");
            } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
                System.out.println(ex.getMessage());
                return;
            }
            App.setMainPageReturnsController("AddFilm");
        } catch (Exception ex) {
            App.printErrorDialog("Films", "An error occurred", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    public void searchFilms(String titleFilter, Date dateFilter) {
        try {
            adultnessMargin = (safeSearch.getValue() / 100);
            if (PisaFlixServices.authenticationService.isUserLogged()) {
                PisaFlixServices.authenticationService.getLoggedUser().setAdultnessMargin(adultnessMargin);
                PisaFlixServices.userService.updateSafeSearchSettings(PisaFlixServices.authenticationService.getLoggedUser(), adultnessMargin);
            }
            Set<Film> films = PisaFlixServices.filmService.getFilmsFiltered(titleFilter, dateFilter, dateFilter, adultnessMargin);
            populateScrollPane(films);
        } catch (Exception ex) {
            App.printErrorDialog("Films", "An error occurred searching the films", ex.toString() + "\n" + ex.getMessage());
        }
    }

    class TileWorker extends Task<Pane> {

        Film film;

        public TileWorker(Film film) {
            this.film = film;
        }

        @Override
        protected Pane call() throws Exception {
            Pane pane;
            pane = createCardPane(film);
            return pane;
        }

    }

    public void populateScrollPane(Set<Film> filmSet) throws InterruptedException {
        tilePane.getChildren().clear();
        if (filmSet.size() > 0) {
            progressIndicator.setProgress(0);
            executorService = Executors.newFixedThreadPool(filmSet.size(), (Runnable r) -> {
                Thread t = Executors.defaultThreadFactory().newThread(r);
                t.setDaemon(true);
                return t;
            });
            for (Film film : filmSet) {
                TileWorker tileWorker = new TileWorker(film);
                tileWorker.setOnSucceeded((succeededEvent) -> {
                    progressIndicator.setProgress(progressIndicator.getProgress() + 1 / Double.valueOf(filmSet.size()));
                    if (!tileWorker.isCancelled()) {
                        if (!tileWorker.isCancelled()) {
                            tilePane.getChildren().add(tileWorker.getValue());
                        }
                        if (executorService.isTerminated()) {
                            filterTextField.setDisable(false);
                            filterTextField.requestFocus();
                            filterTextField.selectEnd();
                            searchButton.setDisable(false);
                            progressIndicator.setProgress(1);
                        }
                    }
                });
                executorService.execute(tileWorker);
            }
            executorService.shutdown();
            filterTextField.setDisable(true);
            searchButton.setDisable(true);
        }
    }

}
