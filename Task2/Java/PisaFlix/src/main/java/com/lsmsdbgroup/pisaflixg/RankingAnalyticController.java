package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.AnalyticsClasses.RankingResult;
import com.lsmsdbgroup.pisaflix.pisaflixservices.DateConverter;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.AnalyticsManagerDatabaseInterface.RankingType;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class RankingAnalyticController implements Initializable {

    private ChangeListener<RankingResultProperty> tableListener;

    public class RankingResultProperty {

        private final SimpleStringProperty position;
        private final SimpleStringProperty title_username;
        private final SimpleStringProperty score;
        private final String id;

        public RankingResultProperty(int position, String title_username, Long score, String id) {
            this.position = new SimpleStringProperty(position + "");
            this.title_username = new SimpleStringProperty(title_username);
            this.score = new SimpleStringProperty(score + "");
            this.id = id;
        }

        public RankingResultProperty(int position, RankingResult rr) {
            this.position = new SimpleStringProperty(position + "");
            this.title_username = new SimpleStringProperty(rr.getTitle_username());
            this.score = new SimpleStringProperty(rr.getScore() + " (" + rr.getCommentCount() + "," + rr.getFavouriteCount() + "," + rr.getViewCount() + ")");
            this.id = rr.getId();
        }

        public String getPosition() {
            return position.get();
        }

        public String getTitle_username() {
            return title_username.get();
        }

        public String getScore() {
            return score.get();
        }
        
        public String getId() {
            return id;
        }

        public void setTitle_username(String tu) {
            this.title_username.set(tu);
        }

        public void setPosition(int pos) {
            this.position.set(pos + "");
        }

        public void setPosition(Long score) {
            this.score.set(score + "");
        }
    }

    @FXML
    private ComboBox rankingType;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private Button showResult;

    @FXML
    private TableView<ObservableList> rankingTable;

    @FXML
    private TableColumn<ObservableList, String> positionColumn;

    @FXML
    private TableColumn<ObservableList, String> titleUsernameColumn;

    @FXML
    private TableColumn<ObservableList, String> scoreColumn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rankingType.getItems().add(RankingType.FILM);
        rankingType.getItems().add(RankingType.USER);

        positionColumn.setCellValueFactory(
                new PropertyValueFactory<>("position"));

        titleUsernameColumn.setCellValueFactory(
                new PropertyValueFactory<>("title_username"));

        scoreColumn.setCellValueFactory(
                new PropertyValueFactory<>("score"));
    }

    @FXML
    public void clickItem(MouseEvent event) {
        if (event.getClickCount() == 2) //Checking double click
        {
            RankingResultProperty selectedValue =  (RankingResultProperty) rankingTable.getSelectionModel().getSelectedItem();
            if(rankingType.getValue() == RankingType.USER){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("UserView.fxml"));

                GridPane gridPane = null;

                try {
                    gridPane = loader.load();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }

                UserViewController userViewController = loader.getController();

                userViewController.setUser(PisaFlixServices.userService.getById(selectedValue.getId()));

                App.setMainPane(gridPane);
            }
            if(rankingType.getValue() == RankingType.FILM){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FilmDetailPage.fxml"));

                GridPane gridPane = null;

                try {
                    gridPane = loader.load();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }

                FilmDetailPageController filmDetailPageController = loader.getController();

                filmDetailPageController.setFilm(PisaFlixServices.filmService.getById(selectedValue.getId()));

                App.setMainPane(gridPane);
            }
        }
    }

    @FXML
    private void clickShowResult() {
        LocalDate startDateLD = startDate.getValue();
        LocalDate endDateLD = endDate.getValue();

        RankingType rt = (RankingType) rankingType.getValue();

        if (rt == null) {
            App.printErrorDialog("Error", "you must specify the type (FILM or USER)", "Please insert the ranking type and retry");
            return;
        }

        if (startDateLD == null || endDateLD == null) {
            App.printErrorDialog("Error", "you must specify both dates", "Please insert two dates and retry");
            return;
        }

        Date sDate = DateConverter.LocalDateToDate(startDateLD);
        Date eDate = DateConverter.LocalDateToDate(endDateLD);

        Set<RankingResult> results = PisaFlixServices.analyticService.rankingAnalytics(sDate, eDate, rt);

        popolateTable(results, 20);
    }

    private void popolateTable(Set<RankingResult> results, int limit) {
        ObservableList data = FXCollections.observableArrayList();
        int pos = 1;
        for (var rr : results) {
            if (pos > limit) {
                break;
            }
            data.add(new RankingResultProperty(pos, rr));
            pos++;
        }

        rankingTable.setItems(data);

    }

}
