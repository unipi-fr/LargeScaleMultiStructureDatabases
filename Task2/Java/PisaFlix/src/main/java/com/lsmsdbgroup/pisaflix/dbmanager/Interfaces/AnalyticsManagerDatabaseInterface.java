/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.AnalyticsClasses.*;
import com.lsmsdbgroup.pisaflix.Entities.Entity;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author FraRonk
 */
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
    //TODO: da decidere il tipo di ritorno
    Object ratingAnalytics(Date startdate, Date endDate, RatingType typeOfRating);
    //TODO: da decidere il tipo di ritorno
    Object rankingAnalytics(Date startdate, Date endDate, RankingType typeOfRanking);
    //TODO: da decidere il tipo di ritorno
    Object engagementAnalytics(Date startdate, Date endDate, Entity entity);
}
