package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.FilmManagerDatabaseInterface;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Filters.regex;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

public class FilmManager implements FilmManagerDatabaseInterface {

    private static FilmManager FilmManager;
    private static MongoCollection<Document> FilmCollection;
    private final int limit = 20;
    private final Document sort = new Document("_id",-1);

    public static FilmManager getIstance() {
        if (FilmManager == null) {
            FilmManager = new FilmManager();
        }
        return FilmManager;
    }

    private FilmManager() {
        FilmCollection = DBManager.getMongoDatabase().getCollection("FilmCollection");
    }

    @Override
    public Film getById(String filmId) {
        Film film = null;
        try (MongoCursor<Document> cursor = FilmCollection.find(eq("_id", new ObjectId(filmId))).iterator()) {
            film = new Film(cursor.next());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retriving a film!");
        } 
        return film;
    }

    @Override
    public Set<Film> getAll() {
        Set<Film> filmSet = new LinkedHashSet<>();
        try (MongoCursor<Document> cursor = FilmCollection.find().sort(sort).limit(limit).iterator()) {
            while (cursor.hasNext()) {
                filmSet.add(new Film(cursor.next()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retrieve all films!");
        } finally {
            
        }
        return filmSet;
    }

    @Override
    public void create(String title, Date publicationDate, String description) {
        Document filmDocument = new Document()
                .append("Title", title)
                .append("PublicationDate", publicationDate);
        if(description != null){
            filmDocument.put("Description", description);
        }      
        //Upsert insert if documnet does't already exists
        UpdateOptions options = new UpdateOptions().upsert(true);
        try {
            FilmCollection.updateOne(and(filmDocument), new Document("$set", filmDocument), options);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in creating the film!");
        } finally {

        }
    }

    @Override
    public void update(String idFilm, String title, Date publicationDate, String description) {
        Document filmDocument = new Document()
                .append("Title", title)
                .append("PublicationDate", publicationDate);
        if(description != null){
            filmDocument.put("Description", description);
        }   
        try {
            FilmCollection.updateOne(eq("_id", new ObjectId(idFilm)), new Document("$set", filmDocument));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating the film!");
        } finally {

        }
    }

    @Override
    public void delete(String idFilm) {
        //clearUserSet(getById(idFilm));
        try {
            FilmCollection.deleteOne(eq("_id", new ObjectId(idFilm)));
            DBManager.commentManager.deleteAll(new Film(idFilm));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in deleting the film!");
        }
    }

    @Override
    public void clearUserSet(Film film) {
        film.setUserSet(new LinkedHashSet<>());
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred clearing the film's userset!");
        } finally {

        }
    }

    @Override
    public void updateFavorites(Film film) {
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred updating favorite films!");
        } finally {

        }
    }

    @Override
    public Set<Film> getFiltered(String titleFilter, Date startDateFilter, Date endDateFilter) {
        Set<Film> filmSet = new LinkedHashSet<>();
        List filters = new ArrayList();
        
        if (titleFilter != null) {
            // i flag = case insensitive
            filters.add(regex("Title", ".*" + titleFilter + ".*","i"));
        }      
        if (startDateFilter != null && endDateFilter != null) {
            filters.add(and(gte("PublicationDate", startDateFilter),lt("PublicationDate", endDateFilter)));
        }else{
            if (titleFilter == null){
                return getAll();
            }
        }

        try (MongoCursor<Document> cursor = FilmCollection.find(or(filters)).sort(sort).limit(limit).iterator()) {
            while (cursor.hasNext()) {
                filmSet.add(new Film(cursor.next()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retrieve films filtered!");
        } 
        
        return filmSet;
    }

}
