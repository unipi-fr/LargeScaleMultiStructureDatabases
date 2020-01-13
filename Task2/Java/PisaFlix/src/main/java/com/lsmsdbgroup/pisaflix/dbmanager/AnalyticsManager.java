/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.AnalyticsClasses.BaseResult;
import com.lsmsdbgroup.pisaflix.Entities.Entity;
import com.lsmsdbgroup.pisaflix.Entities.exceptions.NonConvertibleDocumentException;
import com.mongodb.client.MongoCollection;
import java.util.Date;
import org.bson.Document;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.AnalyticsManagerDatabaseInterface;
import com.mongodb.client.AggregateIterable;
import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.ne;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import org.bson.BsonNull;

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
    public Set<BaseResult> ratingAnalytics(Date startdate, Date endDate, RatingType typeOfRating) {
        
        AggregateIterable<Document> result = FilmCollection.aggregate(Arrays.asList(
                match(and(gte("PublicationDate", startdate), lt("PublicationDate", endDate),ne("Genre", new BsonNull()), ne("Rating", new BsonNull()))), 
                group("$Genre", avg("avg_rating", "$Rating"))));

        Set<BaseResult> res = new LinkedHashSet<>();
        for (Document dbObject : result)
        {
            res.add(createBRfromDocument(dbObject));
        }
        return res;
    }

    @Override
    public Object rankingAnalytics(Date startdate, Date endDate, RankingType typeOfRanking) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private BaseResult createBRfromDocument(Document dbObject){
        if(dbObject.containsKey("_id") && dbObject.containsKey("avg_rating")){
            Double aDouble = dbObject.getDouble("avg_rating");
            String aString = dbObject.get("_id").toString();
            return new BaseResult(aString, aDouble);
        }else{
            try {
                throw new NonConvertibleDocumentException("Document not-convertible in BaseResult");
            } catch (NonConvertibleDocumentException ex) {
                System.out.println(ex.getMessage());
            }
        }  
        return new BaseResult("can't be converted", 0.0);
        // non è bellissimo, se c'è tempo si aggiusta
        //return null;
    }
    
}
