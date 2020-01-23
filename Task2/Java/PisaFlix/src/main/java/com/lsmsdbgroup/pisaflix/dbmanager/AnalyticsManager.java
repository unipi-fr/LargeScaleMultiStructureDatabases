/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.AnalyticsClasses.AverageRatingResult;
import com.lsmsdbgroup.pisaflix.Entities.Entity;
import com.lsmsdbgroup.pisaflix.Entities.exceptions.NonConvertibleDocumentException;
import com.mongodb.client.MongoCollection;
import java.util.Date;
import org.bson.Document;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.AnalyticsManagerDatabaseInterface;
import com.mongodb.client.AggregateIterable;
import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Aggregates.sort;
import static com.mongodb.client.model.Aggregates.unwind;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.ne;
import static com.mongodb.client.model.Indexes.descending;
import static com.mongodb.client.model.Projections.*;
import com.mongodb.client.model.UnwindOptions;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.bson.BsonNull;
import org.bson.conversions.Bson;

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
        List<Bson> asList = Arrays.asList(project(fields(include("Film"), 
                computed("View", eq("$cond", and(eq("if", Arrays.asList("$Type", "VIEW")), eq("then", 1L), eq("else", 0L)))), 
                computed("Favourite", eq("$cond", and(eq("if", Arrays.asList("$Type", "FAVOURITE")), eq("then", 1L), eq("else", 0L)))), 
                computed("Comment", eq("$cond", and(eq("if", Arrays.asList("$Type", "COMMENT")), eq("then", 1L), eq("else", 0L)))))), 
                group("$Film", sum("ViewCount", "$View"), sum("FavouriteCount", "$Favourite"), sum("CommentCount", "$Comment")));
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<AverageRatingResult> ratingAnalytics(Date startDate, Date endDate, RatingType typeOfRating) {
        
        AggregateIterable<Document> result;
        switch(typeOfRating){
            case GENRE:
                result = aggregateByGenre(startDate,endDate);
                break;
            case DIRECOR:
                result = aggregateByDirector(startDate,endDate);
                break;
            case ACTOR:
                result = aggregateByActor(startDate,endDate);
                break;
            default:
                return new LinkedHashSet<>();
        }
         
       
        Set<AverageRatingResult> res = new LinkedHashSet<>();
        for (Document dbObject : result)
        {
            res.add(createBRfromDocument(dbObject));
        }
        return res;
    }
    
    private AggregateIterable<Document> aggregateByGenre(Date startDate, Date endDate){
        return FilmCollection.aggregate(Arrays.asList(
                match(and(and(
                        gte("PublicationDate", startDate), 
                        lt("PublicationDate", endDate)), 
                        ne("Genres",  new BsonNull()), 
                        ne("Rating", new BsonNull())))
                , unwind("$Genres", 
                        new UnwindOptions().includeArrayIndex("arrayIndex")), 
                group("$Genres", 
                        avg("avg_rating", "$Rating"), 
                        sum("count", 1L)), sort(descending("count"))));
    }
    
    private AggregateIterable<Document> aggregateByDirector(Date startDate, Date endDate){
        return FilmCollection.aggregate(Arrays.asList(
                match(and(and(
                        gte("PublicationDate", startDate), 
                        lt("PublicationDate", endDate)), 
                        ne("Directors",  new BsonNull()), 
                        ne("Rating", new BsonNull())))
                , unwind("$Directors", 
                        new UnwindOptions().includeArrayIndex("arrayIndex")), 
                group("$Directors", 
                        avg("avg_rating", "$Rating"), 
                        sum("count", 1L)), sort(descending("count"))));
    }
    
    private AggregateIterable<Document> aggregateByActor(Date startDate, Date endDate){
        return FilmCollection.aggregate(Arrays.asList(
                match(and(and(
                        gte("PublicationDate", startDate), 
                        lt("PublicationDate", endDate)), 
                        ne("Cast",  new BsonNull()), 
                        ne("Rating", new BsonNull())))
                , unwind("$Cast", 
                        new UnwindOptions().includeArrayIndex("arrayIndex")), 
                group("$Cast", 
                        avg("avg_rating", "$Rating"), 
                        sum("count", 1L)), sort(descending("count"))));
    }

    @Override
    public Object rankingAnalytics(Date startdate, Date endDate, RankingType typeOfRanking) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private AverageRatingResult createBRfromDocument(Document dbObject){
        if(dbObject.containsKey("_id") && dbObject.containsKey("avg_rating") && dbObject.containsKey("count")){
            Double aDouble = dbObject.getDouble("avg_rating");
            String aString = dbObject.get("_id").toString();
            Long aLong = dbObject.getLong("count");
            return new AverageRatingResult(aString, aDouble,aLong);
        }else{
            try {
                throw new NonConvertibleDocumentException("Document not-convertible in BaseResult");
            } catch (NonConvertibleDocumentException ex) {
                System.out.println(ex.getMessage());
            }
        }  
        
        return new AverageRatingResult("can't be converted", 0.0, 0L);
        // non è bellissimo, se c'è tempo si aggiusta
        //return null;
    }
    
}
