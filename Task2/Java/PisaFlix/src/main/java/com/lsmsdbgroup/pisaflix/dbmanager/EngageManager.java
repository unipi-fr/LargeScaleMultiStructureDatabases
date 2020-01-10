package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.Entities.Engage.EngageType;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.EngageManagerDatabaseInterface;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.UpdateOptions;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.bson.Document;
import org.bson.types.ObjectId;

public class EngageManager implements EngageManagerDatabaseInterface {
    private static EngageManager EngageManager;
    protected static MongoCollection<Document> EngageCollection;

    static EngageManagerDatabaseInterface getIstance() {
        if (EngageManager == null) {
            EngageManager = new EngageManager();
        }
        return EngageManager;
    }
    //It's equal to sorting by publication date, index not needed
    protected final Document sort = new Document("_id",-1);

    public EngageManager(){
        EngageCollection = DBManager.getMongoDatabase().getCollection("EngageCollection");
    }
    
    @Override
    public void create(User user, Film film, EngageType type) {
        Document engageDocument = new Document()
                .append("User", user.getId())
                .append("Timestamp", new Date())
                .append("Film", film.getId())
                .append("Type", type.toString());

        //Upsert insert if documnet does't already exists
        UpdateOptions options = new UpdateOptions().upsert(true);

        try {
            EngageCollection.updateOne(and(engageDocument), new Document("$set", engageDocument), options);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in creating the engage!");
        }
    }

    //public void update() {
    //}

    @Override
    public void delete(String idEngage) {
        try {
            EngageCollection.deleteOne(eq("_id", new ObjectId(idEngage)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in removing the Engage!");
        }
    }
    
    @Override
    public void deleteAllRelated(Entity entity) {
        try {
            if(entity.getClass().equals(Film.class)){
                EngageCollection.deleteMany(eq("Film", entity.getId()));
            }
            if(entity.getClass().equals(User.class)){
                EngageCollection.deleteMany(eq("User", entity.getId()));
                DBManager.filmManager.deleteReleted((User) entity);
            }  
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in removing the Comment!");
        }
    }

    @Override
    public Engage getById(String engageId) {
        Engage engage = null;
        try (MongoCursor<Document> cursor = EngageCollection.find(eq("_id", new ObjectId(engageId))).iterator()) {
            engage = new Engage(cursor.next());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retriving a comment!");
        } 
        return engage;
    }
    
    @Override
    public Set<Engage> getEngageSet(Entity entity, int limit, int skip, EngageType type ) {
        Set<Engage> engageSet = new LinkedHashSet<>();
        List filters = new ArrayList();
        filters.add(new Document("Type", type.toString()));
        
        if(entity.getClass().equals(Film.class)){
            filters.add(new Document("Film", entity.getId()));
        }
        if(entity.getClass().equals(User.class)){
            filters.add(new Document("User", entity.getId()));
        }

        try (MongoCursor<Document> cursor = EngageCollection.find(and(filters)).sort(sort).limit(limit).skip(skip).iterator()) {
            while (cursor.hasNext()) {
                engageSet.add(new Engage(cursor.next()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retrieve the enages!");
        }

        return engageSet;
    }
    
    @Override
    public void deleteFiltred(User user, Film film, EngageType type){
        try {
            EngageCollection.deleteOne(and(eq("User",user.getId()), eq("Film", film.getId()), eq("Type", type.toString())));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in removing the Comment!");
        }
    }
    
    @Override
    public long count(Entity entity, EngageType type){
        long count = 0;
        try {
            if(entity.getClass() == Film.class){
                count = EngageCollection.countDocuments(and(eq("Film", entity.getId()), eq("Type", type.toString())));
            }
            if(entity.getClass() == User.class){
                count = EngageCollection.countDocuments(and(eq("User", entity.getId()), eq("Type", type.toString())));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in removing the Comment!");
        }
        return count;
    }
    
        @Override
    public long count(User user, Film film, EngageType type){
        long count = 0;
        try {
            count = EngageCollection.countDocuments(and(eq("Film", film.getId()),eq("User", user.getId()), eq("Type", type.toString())));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in removing the Comment!");
        }
        return count;
    }

}
