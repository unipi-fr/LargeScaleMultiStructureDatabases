/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.Entity;
import com.mongodb.client.MongoCollection;
import java.util.Date;
import org.bson.Document;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.AnalyticsManagerDatabaseInterface;

/**
 *
 * @author FraRonk
 */
public class AnalyticsManager implements AnalyticsManagerDatabaseInterface{
    private static AnalyticsManager AnalyticsManager;
    private MongoCollection<Document> EngageCollection;
    private MongoCollection<Document> FilmCollection;
    private MongoCollection<Document> UserCollection;

    public static AnalyticsManager getIstance() {
        if (AnalyticsManager == null) {
            AnalyticsManager = new AnalyticsManager();
        }
        return AnalyticsManager;
    }
    
    private AnalyticsManager() {
        EngageCollection = DBManager.getMongoDatabase().getCollection("EngageCollection");
        FilmCollection = DBManager.getMongoDatabase().getCollection("FilmCollection");
        UserCollection = DBManager.getMongoDatabase().getCollection("UserCollection");        
    }

    @Override
    public Object engagementAnalytics(Date startdate, Date endDate, Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object ratingAnalytics(Date startdate, Date endDate, RatingType typeOfRating) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object rankingAnalytics(Date startdate, Date endDate, RankingType typeOfRanking) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
