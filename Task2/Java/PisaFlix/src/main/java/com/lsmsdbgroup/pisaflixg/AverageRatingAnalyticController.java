package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.AnalyticsClasses.AverageRatingResult;
import com.lsmsdbgroup.pisaflix.DateConverter;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.AnalyticsManagerDatabaseInterface.RatingType;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class AverageRatingAnalyticController implements Initializable {
       
    @FXML
    private CategoryAxis xAxis;
    
    @FXML
    private NumberAxis yAxis;
    
    @FXML
    private ComboBox ratingType;
    
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
        xAxis.setLabel("");       
        yAxis.setLabel("Rating");
        
        ratingType.getItems().add(RatingType.GENRE);
        ratingType.getItems().add(RatingType.DIRECOR);
        ratingType.getItems().add(RatingType.ACTOR);
    }    
    
    @FXML
    private void clickShowResult(){  
        LocalDate startDateLD = startDate.getValue(); 
        LocalDate endDateLD = endDate.getValue();
        
        RatingType rt = (RatingType) ratingType.getValue();
        
        if(rt == null){
            App.printErrorDialog("Error", "you must specify the type (GENRE or DIRECTOR or ACTOR)", "Please insert the rating type and retry");
            return;
        }
        
        if(startDateLD == null || endDateLD == null){
            App.printErrorDialog("Error", "you must specify both dates", "Please insert two dates and retry");
            return;
        }
        
        xAxis.setLabel(rt+"");   
        
        Date sDate = DateConverter.LocalDateToDate(startDateLD);
        Date eDate = DateConverter.LocalDateToDate(endDateLD);
        
        Set<AverageRatingResult> results = PisaFlixServices.analyticService.ratingAnalytics(sDate, eDate, rt);
        
        setBarChart(results,20);
        
    }
    
    private void setBarChart(Set<AverageRatingResult> brs, int limitResults){
        this.barChart.getData().clear();
        XYChart.Series avgRatings = new XYChart.Series();
        
        avgRatings.setName("Average Ratings");
        int finish = 0;
        
        for (AverageRatingResult br : brs){
            if(finish >= limitResults){
                break;
            }
            avgRatings.getData().add(new XYChart.Data(br.getTag()+" ("+br.getCount()+")", br.getAverageRating()));
            finish++;
        }
        
        this.barChart.getData().addAll(avgRatings);
    }
}
