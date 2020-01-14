
package com.lsmsdbgroup.pisaflixg;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;


public class AnalyticsController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @FXML
    private void showAverageAnalytics(){
        App.setMainPageReturnsController("AverageRatingAnalytic");
    }
    
    @FXML
    private void showEngageAnalytics(){
        App.setMainPageReturnsController("EngageAnalytic");
    }
    
    @FXML
    private void showRankingAnalytics(){
        App.setMainPageReturnsController("RankingAnalytic");
    }
}
