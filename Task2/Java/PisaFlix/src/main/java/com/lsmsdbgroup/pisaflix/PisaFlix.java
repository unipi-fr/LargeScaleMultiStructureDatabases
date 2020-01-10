package com.lsmsdbgroup.pisaflix;

import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.AnalyticsManagerDatabaseInterface;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.AnalyticsManagerDatabaseInterface.RatingType;
import com.lsmsdbgroup.pisaflixg.App;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class PisaFlix {

    public static void main(String[] args) {
        
            LogManager.getLogManager().getLogger("").setLevel(Level.OFF);
            //App.main(args);
            
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date startDate = format.parse("10/01/1990");
            Date endDate = format.parse("31/12/2020");
            DBManager.analyticsManager.ratingAnalytics(startDate, endDate, RatingType.GENRE);       
        } catch (ParseException ex) {
            Logger.getLogger(PisaFlix.class.getName()).log(Level.SEVERE, null, ex);
        }
        DBManager.stop();
    }
}
