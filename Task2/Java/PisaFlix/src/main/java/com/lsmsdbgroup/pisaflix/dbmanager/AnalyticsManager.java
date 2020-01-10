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
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;
import java.util.Arrays;

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
        
        AggregateIterable<Document> result = FilmCollection.aggregate(Arrays.asList(
                match(and(gte("PublicationDate", startdate), lt("PublicationDate", endDate))), 
                group("$Genre", avg("avg_rating", "$Rating"))));

        // Print for demo
        for (Document dbObject : result)
        {
            System.out.println(dbObject);
        }
    return null;
    }

    @Override
    public Object rankingAnalytics(Date startdate, Date endDate, RankingType typeOfRanking) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
