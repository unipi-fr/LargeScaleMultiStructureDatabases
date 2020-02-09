package com.lsmsdbgroup.randomgeneratefavoriteview;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bson.Document;
import org.bson.types.ObjectId;

public class RandomGen {
    private static final MongoClient MongoClient = MongoClients.create("mongodb+srv://root:root@lsmsdcluster-yeauu.mongodb.net/test?retryWrites=true&w=majority");
    private static MongoDatabase MongoDatabase;
    
    private MongoCollection<Document> EngageCollection;
    private MongoCollection<Document> FilmCollection;
    private MongoCollection<Document> UserCollection;
    
    public List<ObjectId> filmSet;
    public List<ObjectId> userSet;
    
    private Date d1;
    private Date d2;
    
    public RandomGen(){
        d1 = new Date();
        d1.setTime(1205061921424L);
        
        d2 = new Date();
        d2.setTime(1579181324178L);
    }
    
    public void start(){
        MongoDatabase = MongoClient.getDatabase("PisaFlix");
        EngageCollection = MongoDatabase.getCollection("EngageCollection");
        FilmCollection = MongoDatabase.getCollection("FilmCollection");
        UserCollection = MongoDatabase.getCollection("UserCollection");  
    }
    
    public void stop(){
        MongoClient.close();
    }
    
    public void getAllFilm() {
        filmSet = new ArrayList<>();
        try (MongoCursor<Document> cursor = FilmCollection.find().projection(Projections.include("_id")).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                ObjectId filmId = doc.getObjectId("_id");
                filmSet.add(filmId);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retrieve all films!");
        }
    }
    
    public void getAllUSer() {
        userSet = new ArrayList<>();
        try (MongoCursor<Document> cursor = UserCollection.find().projection(Projections.include("_id")).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                ObjectId userId = doc.getObjectId("_id");
                userSet.add(userId);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retrieve all users!");
        }
    }
    
    public List<ObjectId> getFilmSet() {
        return filmSet;
    }
    
    public List<ObjectId> getUserSet() {
        return userSet;
    }
    
    public Document generateViewDocument(String film, String user, Date date)
    {
        Document viewDoc = new Document()
                .append("Film", film)
                .append("Timestamp", date)
                .append("Type", "VIEW")
                .append("User", user);
        
        return viewDoc;
    }
    
    public Document generateFavoriteDocument(String film, String user, Date date)
    {
        Document viewDoc = new Document()
                .append("Film", film)
                .append("Timestamp", date)
                .append("Type", "FAVOURITE")
                .append("User", user);
        
        return viewDoc;
    }
    
    public Date generateRandomIsoDate(){
        Date randomDate = new Date(ThreadLocalRandom.current()
                              .nextLong(d1.getTime(), d2.getTime()));
        
        return randomDate;
    }
    
    public void insertMany(List<Document> documents){
        EngageCollection.insertMany(documents);
    }
}
