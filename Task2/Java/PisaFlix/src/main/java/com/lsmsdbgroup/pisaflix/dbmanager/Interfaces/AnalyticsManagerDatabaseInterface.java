package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.AnalyticsClasses.*;
import com.lsmsdbgroup.pisaflix.Entities.Entity;
import java.util.Date;
import java.util.Set;

public interface AnalyticsManagerDatabaseInterface {
    public static enum RatingType {
     GENRE,
     DIRECOR,
     ACTOR
    }  
    public static enum RankingType {
     FILM,
     USERS
    }  
    
    Set<AverageRatingResult>  ratingAnalytics(Date startdate, Date endDate, RatingType typeOfRating);
    //TODO: da decidere il tipo di ritorno
    Set<RankingResult> rankingAnalytics(Date startdate, Date endDate, RankingType typeOfRanking);
    //TODO: da decidere il tipo di ritorno
    Object engagementAnalytics(Date startdate, Date endDate, Entity entity);
}
