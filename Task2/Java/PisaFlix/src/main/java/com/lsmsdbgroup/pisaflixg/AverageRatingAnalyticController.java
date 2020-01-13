package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.AnalyticsClasses.BaseResult;
import com.lsmsdbgroup.pisaflix.DateConverter;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.AnalyticsManagerDatabaseInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

public class AverageRatingAnalyticController implements Initializable {
    
    @FXML
    private CategoryAxis xAxis;
    
    @FXML
    private NumberAxis yAxis;
    
    @FXML
    private Button showResult;
    
    @FXML
    private DatePicker startDate;
    
    @FXML
    private DatePicker endDate;
    
    
    @FXML
    private BarChart<String,Number> barChart;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.barChart.setTitle("Average rating");
        xAxis.setLabel("Genre");       
        yAxis.setLabel("Rating");
    }    
    
    @FXML
    private void clickShowResult(){  
        LocalDate startDateLD = startDate.getValue(); 
        LocalDate endDateLD = endDate.getValue();
        
        if(startDateLD == null || endDateLD == null){
            App.printErrorDialog("Error", "you must specify both dates", "Please insert two dates and retry");
            return;
        }
        Date sDate = DateConverter.LocalDateToDate(startDateLD);
        Date eDate = DateConverter.LocalDateToDate(endDateLD);
        
        Set<BaseResult> results = PisaFlixServices.analyticService.ratingAnalytics(sDate, eDate, AnalyticsManagerDatabaseInterface.RatingType.GENRE);
        
        setBarChart(results);
        
    }
    
    private void setBarChart(Set<BaseResult> brs){
        this.barChart.getData().clear();
        XYChart.Series avgRatings = new XYChart.Series();
        
        avgRatings.setName("Average Ratings");
        
        brs.forEach((br) -> {
            avgRatings.getData().add(new XYChart.Data(br.getTag(), br.getValue()));
        });
        
        this.barChart.getData().addAll(avgRatings);
    }
}
