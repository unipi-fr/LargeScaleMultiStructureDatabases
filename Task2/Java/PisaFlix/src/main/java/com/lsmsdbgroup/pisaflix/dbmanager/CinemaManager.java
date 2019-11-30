package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.CinemaManagerDatabaseInterface;
import com.mongodb.client.*;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

public class CinemaManager implements CinemaManagerDatabaseInterface {

    private static CinemaManager cinemaManager;
    private static MongoCollection<Document> CinemaCollection;

    public static CinemaManager getIstance() {
        if (cinemaManager == null) {
            cinemaManager = new CinemaManager();
        }
        return cinemaManager;
    }

    private CinemaManager() {
        CinemaCollection = DBManager.getMongoDatabase().getCollection("CinemaCollection");
    }

    @Override
    public void create(String name, String address) {
        Document cinemaDocument = new Document();
        cinemaDocument.put("Name", name);
        cinemaDocument.put("Address", address);
        //Upsert insert if documnet does't already exists
        UpdateOptions options = new UpdateOptions().upsert(true); 
        try {
            CinemaCollection.updateOne(and(cinemaDocument),new Document("$set",cinemaDocument),options);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in creating the cinema!");
        }
    }

    @Override
    public Cinema getById(String cinemaId) {
        Cinema cinema = null;
        try (MongoCursor<Document> cursor = CinemaCollection.find(eq("_id",new ObjectId(cinemaId))).iterator()) {
            cinema = new Cinema(cursor.next());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retriving the cinema!");
        }      
        return cinema;
    }

    @Override
    public Set<Cinema> getFiltered(String nameFilter, String addressFilter) {
        Set<Cinema> cinemas = null;
        String name = "";
        String address = "";
        if (nameFilter != null) {
            name = nameFilter;
        }
        if (addressFilter != null) {
            address = addressFilter;
        }

        String query = "SELECT c "
                + "FROM Cinema c "
                + "WHERE ('" + name + "'='' OR c.name LIKE '%" + name + "%') "
                + "AND ('" + address + "'='' OR c.address LIKE '%" + address + "%') ";

        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retrieve cinemas filtered!");
        } finally {
            
        }
        return cinemas;
    }

    @Override
    public void delete(String idCinema) {
        //clearUserSet(getById(idCinema));
        try {
            CinemaCollection.deleteOne(eq("_id",new ObjectId(idCinema)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in removing a Cinema!");
        }
    }

    @Override
    public void clearUserSet(Cinema cinema) {
        cinema.setUserSet(new LinkedHashSet<>());
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred clearing the cinema's userset!");
        } finally {

        }
    }

    @Override
    public void update(String idCinema, String name, String address) {
        Document cinemaDocument = new Document();
        cinemaDocument.put("Name", name);
        cinemaDocument.put("Address", address);
        try {
            CinemaCollection.updateOne(eq("_id", new ObjectId(idCinema)),new Document("$set",cinemaDocument));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating the film!");
        } finally {

        }
    }

    @Override
    public Set<Cinema> getAll() {
        Set<Cinema> cinemas = null;
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retrieve all cinemas!");
        } finally {

        }
        return cinemas;
    }

    @Override
    public void updateFavorites(Cinema cinema) {
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred updating favorite cinemas!");
        } finally {

        }
    }

}
