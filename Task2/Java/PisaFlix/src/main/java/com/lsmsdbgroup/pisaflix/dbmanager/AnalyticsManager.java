package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.AnalyticsClasses.AverageRatingResult;
import com.lsmsdbgroup.pisaflix.AnalyticsClasses.EngageResult;
import com.lsmsdbgroup.pisaflix.AnalyticsClasses.RankingResult;
import com.lsmsdbgroup.pisaflix.Entities.Comment;
import com.lsmsdbgroup.pisaflix.Entities.Entity;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.exceptions.NonConvertibleDocumentException;
import com.mongodb.client.MongoCollection;
import java.util.Date;
import org.bson.Document;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.AnalyticsManagerDatabaseInterface;
import com.mongodb.client.AggregateIterable;
import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Accumulators.first;
import static com.mongodb.client.model.Accumulators.sum;
import com.mongodb.client.model.Aggregates;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.lookup;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Aggregates.sort;
import static com.mongodb.client.model.Aggregates.unwind;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Indexes.ascending;
import static com.mongodb.client.model.Indexes.descending;
import com.mongodb.client.model.Projections;
import static com.mongodb.client.model.Projections.computed;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import com.mongodb.client.model.UnwindOptions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.bson.BsonNull;

public class AnalyticsManager implements AnalyticsManagerDatabaseInterface{
    private static AnalyticsManager AnalyticsManager;
    private final MongoCollection<Document> EngageCollection;
    private final MongoCollection<Document> FilmCollection;
    private final MongoCollection<Document> UserCollection;

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
    
    private AggregateIterable<Document> engageAggregate(Date startDate, Date endDate, String film){
        return EngageCollection.aggregate(Arrays.asList(
                match(and(eq("Film", film),
                        gte("Timestamp", startDate), 
                        lt("Timestamp", endDate))),
                Aggregates.project(
                        Projections.fields(Projections.include("Film", "Timestamp"),
                        new Document("View", new Document("$cond", Arrays.<Object>asList(new Document("$eq", Arrays.<Object>asList("$Type", "VIEW")),
                                        1L, 0L))),
                        new Document("Favourite", new Document("$cond", Arrays.<Object>asList(new Document("$eq", Arrays.<Object>asList("$Type", "FAVOURITE")),
                                        1L, 0L))),
                        new Document("Comment", new Document("$cond", Arrays.<Object>asList(new Document("$eq", Arrays.<Object>asList("$Type", "COMMENT")),
                                        1L, 0L))))),
                /*project(
                        fields(include("Film"), 
                        computed("View", eq("$cond", and(eq("if", Arrays.asList("$Type", "VIEW")), eq("then", 1L), eq("else", 0L)))), 
                        computed("Favourite", eq("$cond", and(eq("if", Arrays.asList("$Type", "FAVOURITE")), eq("then", 1L), eq("else", 0L)))), 
                        computed("Comment", eq("$cond", and(eq("if", Arrays.asList("$Type", "COMMENT")), eq("then", 1L), eq("else", 0L)))))),*/ 
                group(eq("$year", "$Timestamp"), sum("ViewCount", "$View"), sum("FavouriteCount", "$Favourite"), sum("CommentCount", "$Comment")),
                sort(ascending("_id"))));
    }
    
    @Override
    public Set<EngageResult> engagementAnalytics(Date startDate, Date endDate, Entity entity) {
        AggregateIterable<Document> result;
        String idFilm = entity.getId();
        Film film = DBManager.filmManager.getById(idFilm);
        DBManager.filmManager.getRecentComments(film);
        
        HashMap<Integer, Integer> commentYear = new HashMap<>();
        
        for(Comment comment: film.getCommentSet()){
            Date date = (Date) comment.getTimestamp();
            
            if(date.getTime() < startDate.getTime() || date.getTime() > endDate.getTime())
                continue;
            
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            
            if(commentYear.containsKey(year))
            {
                int currentCount = commentYear.get(year);
                commentYear.put(year, currentCount + 1);
            } else {
                commentYear.put(year, 1);
            }
        }
        
        result = engageAggregate(startDate, endDate, idFilm);
        
        Set<EngageResult> res = new LinkedHashSet<>();
        
        for (Document dbObject : result){
            EngageResult engageResult = createEngageResultFromDocument(dbObject);
            Integer commentCount = commentYear.get(engageResult.getYear());
            
            if(commentCount != null)
            {
                commentCount += film.getCommentSet().size();
                engageResult.setCommentCount(commentCount.longValue());
            }
            
            res.add(engageResult);
        }
        return res;
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

        List<RankingResult> res = new ArrayList<>();
        
        AggregateIterable<Document> fromEngage;
        AggregateIterable<Document> fromFilm;
                
        switch(typeOfRanking){
            case FILM:
                fromEngage = aggregateEngageCollectionRankingByFilms(startDate,endDate);
                fromFilm = aggregateFilmCollectionRankingByFilms(startDate,endDate);
                break;
            case USER:
                fromEngage = aggregateEngageCollectionRankingByUsers(startDate,endDate);
                fromFilm = aggregateFilmCollectionRankingByUsers(startDate,endDate);
                break;
            default:
                return new LinkedHashSet<>();  
        }
        
        for (Document dbObject : fromEngage)
        {
            RankingResult rr = createRankingResultfromDocument(dbObject);
            res.add(rr);
        }
        for(Document dbObject : fromFilm){
            RankingResult rr = createRankingResultfromDocument(dbObject);
            RankingResult.addOrMerge(res, rr);
        }
        
        Collections.sort(res);
        
        return new LinkedHashSet<>(res);
    }
    
    private AggregateIterable<Document> aggregateEngageCollectionRankingByFilms(Date startDate, Date endDate){

        return EngageCollection.aggregate(Arrays.asList(match(and(
                        gte("Timestamp", startDate), 
                        lt("Timestamp", endDate))), 
                Aggregates.project(
                        Projections.fields(Projections.include("Film"),
                        new Document("isView", new Document("$cond", Arrays.<Object>asList(new Document("$eq", Arrays.<Object>asList("$Type", "VIEW")),
                                        1L, 0L))),
                        new Document("isFavourite", new Document("$cond", Arrays.<Object>asList(new Document("$eq", Arrays.<Object>asList("$Type", "FAVOURITE")),
                                        1L, 0L))),
                        new Document("isComment", new Document("$cond", Arrays.<Object>asList(new Document("$eq", Arrays.<Object>asList("$Type", "COMMENT")),
                                        1L, 0L))))), 
                group(eq("$toObjectId", "$Film"), 
                        sum("commentCount", "$isComment"), 
                        sum("viewCount", "$isView"), 
                        sum("favouriteCount", "$isFavourite")), 
                lookup("FilmCollection", "_id", "_id", "FilmD"), 
                unwind("$FilmD", new UnwindOptions().includeArrayIndex("ArrayPosition").preserveNullAndEmptyArrays(false)), 
                project(fields(
                        computed("title_username", "$FilmD.Title"), 
                        include("commentCount", "favouriteCount", "viewCount")))));
   
    }
    
    private AggregateIterable<Document> aggregateEngageCollectionRankingByUsers(Date startDate, Date endDate){
        return EngageCollection.aggregate(Arrays.asList(
            match(
                and(
                    and(
                        gte("Timestamp", startDate), 
                        lt("Timestamp", endDate)), 
                    ne("User", "anonymous"))), 
            Aggregates.project(
                        Projections.fields(Projections.include("User"),
                        new Document("isView", new Document("$cond", Arrays.<Object>asList(new Document("$eq", Arrays.<Object>asList("$Type", "VIEW")),
                                        1L, 0L))),
                        new Document("isFavourite", new Document("$cond", Arrays.<Object>asList(new Document("$eq", Arrays.<Object>asList("$Type", "FAVOURITE")),
                                        1L, 0L))),
                        new Document("isComment", new Document("$cond", Arrays.<Object>asList(new Document("$eq", Arrays.<Object>asList("$Type", "COMMENT")),
                                        1L, 0L))))), 
            group(eq("$toObjectId", "$User"), 
                    sum("commentCount", "$isComment"), 
                    sum("viewCount", "$isView"), 
                    sum("favouriteCount", "$isFavourite")), 
            lookup("UserCollection", "_id", "_id", "UserD"), 
            unwind("$UserD", new UnwindOptions().includeArrayIndex("positionInArray").preserveNullAndEmptyArrays(false)), 
            project(fields(
                    computed("title_username", "$UserD.Username"), 
                    include("commentCount", "viewCount", "favouriteCount")))));
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
            title_username = dbObject.get("title_username").toString();
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
        //return null;
    }
    
    private EngageResult createEngageResultFromDocument(Document doc){
        EngageResult engageResult = null;
        
        if(doc.containsKey("_id")){
            Integer year = doc.getInteger("_id");
            Long viewCount = doc.getLong("ViewCount");
            Long favouriteCount = doc.getLong("FavouriteCount");
            Long commentCount = doc.getLong("CommentCount");
            
            engageResult = new EngageResult(year, viewCount, favouriteCount, commentCount);
        }else{
            try {
                throw new NonConvertibleDocumentException("Document not-convertible in EngageResult");
            } catch (NonConvertibleDocumentException ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        return engageResult;
    }
    
}
