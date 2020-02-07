package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.AnalyticsClasses.EngageResult;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class EngageAnalyticController implements Initializable {

    SuggestionProvider<Film> provider;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private TextField titleTextField;

    @FXML
    private ComboBox startCombo;

    @FXML
    private ComboBox endCombo;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private PieChart pieChart;
    
    @FXML
    private Label yearLabel;
    
    @FXML
    private Label engageLabel;
    
    private HashMap<String, EngageResult> engageResultIndexedByYear;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        xAxis.setLabel("");
        yAxis.setLabel("Engage");

        Set<Film> autoCompletions = new LinkedHashSet<>();
        provider = SuggestionProvider.create(autoCompletions);
        AutoCompletionTextFieldBinding<Film> autoCompletionTextFieldBinding = new AutoCompletionTextFieldBinding<>(titleTextField, provider);

        ArrayList years = new ArrayList();
        for (int i = 1961; i < 2021; i++) {
            years.add(i);
        }

        startCombo.getItems().setAll(years);
        endCombo.getItems().setAll(years);
        
        barChart.setLegendVisible(false);
    }

    @FXML
    private void suggestions() {
        if (titleTextField.getText() != null) {
            Set<Film> newCompletions = PisaFlixServices.filmService.getFilmsFiltered(titleTextField.getText(), null, null, 0);;
            provider.clearSuggestions();
            provider.addPossibleSuggestions(newCompletions);
        }
    }

    @FXML
    private void calculateEngagement() {
        if (startCombo.getSelectionModel().isEmpty() || endCombo.getSelectionModel().isEmpty()) {
            App.printWarningDialog("No Year Selected", "", "You must select both Year");
            return;
        }

        resetPieChart();
        String filmTitle = titleTextField.getText();
        Set<Film> films = PisaFlixServices.filmService.getFilmsFiltered(filmTitle, null, null, 0);

        Film film = films.iterator().next();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

        Integer startYear = (Integer) startCombo.getValue();
        String startYearStr = startYear.toString();

        Integer endYear = ((Integer) endCombo.getValue()) + 1;
        String endYearStr = endYear.toString();

        Date dStart = null;
        Date dEnd = null;
        try {
            dStart = sdf.parse(startYearStr);
            dEnd = sdf.parse(endYearStr);
        } catch (ParseException ex) {
            Logger.getLogger(EngageAnalyticController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Set<EngageResult> engageResults = PisaFlixServices.analyticService.engagementAnalytics(dStart, dEnd, film);

        engageResultIndexedByYear = new HashMap<>();
        
        for(EngageResult engageResult: engageResults){
            engageResultIndexedByYear.put(engageResult.getYear().toString(), engageResult);
        }
            
        setBarChart(engageResults);
    }

    private void setBarChart(Set<EngageResult> engageResults){
        barChart.getData().clear();
        Series series = new Series();
        
        barChart.getData().addAll(series);
        
        for(EngageResult engageResult: engageResults){
            Data<String, Number> data = new Data<>(engageResult.getYear().toString(), engageResult.getEngage());
            series.getData().add(data);
            data.getNode().setOnMouseClicked(e -> setPieChart(data.getXValue()));
        }
    }

    private void setPieChart(String date) {
        pieChart.getData().clear();

        EngageResult engageResult = engageResultIndexedByYear.get(date);
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("View", engageResult.getViewCount()),
                new PieChart.Data("Favourite", engageResult.getFavouriteCount()),
                new PieChart.Data("Comment", engageResult.getCommentCount())
        );

        pieChart.setData(pieChartData);
        
        pieChartData.forEach(data ->
            data.nameProperty().bind(Bindings.concat(data.getName(), " ", data.pieValueProperty()))
        );
        
        pieChart.setLabelsVisible(false);
        
        yearLabel.setText(date);
        engageLabel.setText("Engage: "+engageResult.getEngage());
    }
    
    private void resetPieChart(){
        pieChart.getData().clear();
        yearLabel.setText("");
        engageLabel.setText("");
    }
}
