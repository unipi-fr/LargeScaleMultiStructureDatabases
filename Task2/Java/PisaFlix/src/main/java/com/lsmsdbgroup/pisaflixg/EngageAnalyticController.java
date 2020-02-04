package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.AnalyticsClasses.EngageResult;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class EngageAnalyticController implements Initializable {

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
    private BarChart<String,Number> barChart;
    
    @FXML
    private PieChart pieChart;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        xAxis.setLabel("");       
        yAxis.setLabel("Engage");
        
        ArrayList years = new ArrayList();
        for(int i = 1961; i < 2021; i++)
        {
            years.add(i);
        }
        
        startCombo.getItems().setAll(years);
        endCombo.getItems().setAll(years);
    }
    
    @FXML
    private void calculateEngagement(){
        if(startCombo.getSelectionModel().isEmpty() || endCombo.getSelectionModel().isEmpty()){
            App.printWarningDialog("No Year Selected", "", "You must select both Year");
            return;
        }
        
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
        
        EngageResult engageResult;
        if(engageResults != null)
            engageResult = engageResults.iterator().next();
        else
            engageResult = new EngageResult(film.getId(), 0L, 0L, 0L);
        
        setBarChart(engageResult);
        setPieChart(engageResult);
    }
    
    private void setBarChart(EngageResult engageResult){
        barChart.getData().clear();
        XYChart.Series series = new XYChart.Series();
        
        series.getData().add(new XYChart.Data<>("View",engageResult.getViewCount()));
        series.getData().add(new XYChart.Data<>("Favourite",engageResult.getFavouriteCount()));
        series.getData().add(new XYChart.Data<>("Comment",engageResult.getCommentCount()));
        
        barChart.getData().addAll(series);
    }
    
    private void setPieChart(EngageResult engageResult){
        pieChart.getData().clear();
                
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("View", engageResult.getViewCount()),
                new PieChart.Data("Favourite", engageResult.getFavouriteCount()),
                new PieChart.Data("Comment", engageResult.getCommentCount())
        );
        
        pieChart.setData(pieChartData);
        pieChart.setLabelsVisible(false);
    }
}
