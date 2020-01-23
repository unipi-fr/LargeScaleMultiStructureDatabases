package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.AnalyticsClasses.AverageRatingResult;
import com.lsmsdbgroup.pisaflix.AnalyticsClasses.RankingResult;
import com.lsmsdbgroup.pisaflix.Entities.Entity;
import com.lsmsdbgroup.pisaflix.Entities.exceptions.NonConvertibleDocumentException;
import com.mongodb.client.MongoCollection;
import java.util.Date;
import org.bson.Document;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.AnalyticsManagerDatabaseInterface;
import com.mongodb.client.AggregateIterable;
import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Accumulators.first;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.addFields;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.lookup;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Aggregates.sort;
import static com.mongodb.client.model.Aggregates.unwind;
import com.mongodb.client.model.Field;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Indexes.descending;
import static com.mongodb.client.model.Projections.computed;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import com.mongodb.client.model.UnwindOptions;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.bson.BsonNull;
import org.bson.conversions.Bson;

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
        
        Set<AverageRatingResult> res = new LinkedHashSet<>();
        
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
                return res;
        }
         
       
        
        for (Document dbObject : result)
        {
            res.add(createAverageRatingResultfromDocument(dbObject));
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
    public Set<RankingResult> rankingAnalytics(Date startDate, Date endDate, RankingType typeOfRanking) {

        Set<RankingResult> res = new LinkedHashSet<>();
        
        AggregateIterable<Document> fromEngage;
        AggregateIterable<Document> fromFilm;
                
        
        switch(typeOfRanking){
            case FILM:
                fromEngage = aggregateEngageCollectionRankingByFilms(startDate,endDate);
                fromFilm = aggregateFilmCollectionRankingByFilms(startDate,endDate);
                break;
            case USERS:
                fromEngage = aggregateEngageCollectionRankingByUsers(startDate,endDate);
                fromFilm = aggregateFilmCollectionRankingByUsers(startDate,endDate);
                break;
            default:
                return res;
            
        }
        for (Document dbObject : fromEngage)
        {
            res.add(createRankingResultfromDocument(dbObject));
        }
        for(Document dbObject : fromFilm){
            RankingResult.addOrMerge(res, createRankingResultfromDocument(dbObject));
        }
        
        TreeSet<RankingResult> orderedSet = new TreeSet<>((RankingResult rr1, RankingResult rr2) -> rr1.compareTo(rr2)); 
        orderedSet.addAll(res);
        
        return orderedSet;
    }
    
    private AggregateIterable<Document> aggregateEngageCollectionRankingByFilms(Date startDate, Date endDate){
        
    return EngageCollection.aggregate(Arrays.asList(
            match(
                    and(gte("Timestamp", startDate), lt("Timestamp", endDate))), 
            group(eq("$toObjectId", "$Film"), 
                    sum("commentCount", 
                            eq("$cond", and(eq("if", Arrays.asList("$Type", "COMMENT")), eq("then", 1L), eq("else", 0L)))), 
                    sum("viewCount", 
                            eq("$cond", and(eq("if", Arrays.asList("$Type", "VIEW")), eq("then", 1L), eq("else", 0L)))), 
                    sum("favouriteCount", 
                            eq("$cond", and(eq("if", Arrays.asList("$Type", "FAVOURITE")), eq("then", 1L), eq("else", 0L))))), 
            lookup("FilmCollection", "_id", "_id", "FilmD"), 
            unwind("$FilmD", new UnwindOptions().includeArrayIndex("ArrayPosition").preserveNullAndEmptyArrays(false)), 
            project(
                    fields(computed("title_username", "$FilmD.Title"), include("commentCount", "favouriteCount", "viewCount")))));
    }
    private AggregateIterable<Document> aggregateEngageCollectionRankingByUsers(Date startDate, Date endDate){
        
    return EngageCollection.aggregate(Arrays.asList(
            match(
                and(
                    and(gte("Timestamp", startDate), lt("Timestamp", endDate)), 
                        ne("User", "anonymous"))), 
            group(eq("$toObjectId", "$User"), 
                   sum("commentCount", 
                           eq("$cond", and(eq("if", Arrays.asList("$Type", "COMMENT")), eq("then", 1L), eq("else", 0L)))), 
                   sum("viewCount", 
                           eq("$cond", and(eq("if", Arrays.asList("$Type", "VIEW")), eq("then", 1L), eq("else", 0L)))), 
                   sum("favouriteCount", eq("$cond", and(eq("if", Arrays.asList("$Type", "FAVOURITE")), 
                           eq("then", 1L), eq("else", 0L))))), lookup("UserCollection", "_id", "_id", "UserD"), 
            unwind("$UserD", 
                    new UnwindOptions().includeArrayIndex("positionInArray").preserveNullAndEmptyArrays(false)), 
            project(fields(computed("title_username", "$UserD.Username"), include("commentCount", "viewCount", "favouriteCount")))));
    }
    
    private AggregateIterable<Document> aggregateFilmCollectionRankingByUsers(Date startDate, Date endDate){
        
    return FilmCollection.aggregate(Arrays.asList(
            unwind("$RecentComments", new UnwindOptions().includeArrayIndex("CommentNumber").preserveNullAndEmptyArrays(false)), 
            match(
                    and(
                            gte("RecentComments.Timestamp", startDate), 
                            lt("RecentComments.Timestamp", endDate))), 
            group(eq("$toObjectId", "$RecentComments.User"), sum("commentCount", 1L)), 
            lookup("UserCollection", "_id", "_id", "UserD"), 
            unwind("$UserD", new UnwindOptions().includeArrayIndex("ArrayPosition").preserveNullAndEmptyArrays(false)), 
            project(fields(computed("title_username", "$UserD.Username"), include("commentCount")))));
    }
    private AggregateIterable<Document> aggregateFilmCollectionRankingByFilms(Date startDate, Date endDate){
        
    return FilmCollection.aggregate(Arrays.asList(
            unwind("$RecentComments", new UnwindOptions().includeArrayIndex("CommentNumber").preserveNullAndEmptyArrays(false)), 
            match(
                and(
                        gte("RecentComments.Timestamp", startDate), 
                        lt("RecentComments.Timestamp", endDate))), 
            group("$_id", 
                    first("title_username", "$Title"), 
                    sum("commentCount", 1L))));
    }
    
    private RankingResult createRankingResultfromDocument(Document dbObject){
        String id = "";
        String title_username = "";
        Long commentCount = 0L;
        Long favouriteCount = 0L;
        Long viewCount = 0L;
        
        if(dbObject.containsKey("_id")) {
            id = dbObject.get("_id").toString();
        }
        
        if(dbObject.containsKey("title_username")) {
            title_username = dbObject.getString("title_username");
        }
        
        if(dbObject.containsKey("commentCount")) {
            commentCount = dbObject.getLong("commentCount");
        }
        
        if(dbObject.containsKey("favouriteCount")) {
            favouriteCount = dbObject.getLong("favouriteCount");
        }
        
        if(dbObject.containsKey("viewCount")) {
            viewCount = dbObject.getLong("viewCount");
        }
            
        
        return new RankingResult(id,title_username,commentCount,favouriteCount,viewCount);
    }
    
    private AverageRatingResult createAverageRatingResultfromDocument(Document dbObject){
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
