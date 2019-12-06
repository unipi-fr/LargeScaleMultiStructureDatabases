package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.*;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.ProjectionManagerDatabaseInterface;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.and;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

public class ProjectionManager implements ProjectionManagerDatabaseInterface {

    private static ProjectionManager projectionManager;
    private static MongoCollection<Document> projectionCollection;
    //It's equal to sorting by publication date, index not needed
    private final Document sort = new Document("_id",-1);

    public static ProjectionManager getIstance() {
        if (projectionManager == null) {
            projectionManager = new ProjectionManager();
        }

        return projectionManager;
    }

    private ProjectionManager() {
        projectionCollection = DBManager.getMongoDatabase().getCollection("ProjectionCollection");
    }

    @Override
    public void create(Date dateTime, int room, Film film, Cinema cinema) {
        Document projectionDocument = new Document()
                .append("DateTime", dateTime)
                .append("Room", room)
                .append("Cinema", cinema.getId())
                .append("Film", film.getId());

        //Upsert insert if documnet does't already exists
        UpdateOptions options = new UpdateOptions().upsert(true);
        
        try {
            projectionCollection.updateOne(and(projectionDocument), new Document("$set", projectionDocument), options);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in creating the projection!");
        } finally {

        }
    }

    @Override
    public void delete(String idProjection) {
        // code to delete a cinema
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in removing the Projection!");
        } finally {

        }
    }

    @Override
    public void update(String idProjection, Date dateTime, int room) {
        Projection projection = new Projection(idProjection);
        projection.setDateTime(dateTime);
        projection.setRoom(room);
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating the projection!");
        } finally {

        }
    }

    @Override
    public Set<Projection> getAll() {
        Set<Projection> projections = null;
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retrieve all projections!");
        } finally {

        }
        return projections;
    }

    @Override
    public Projection getById(String projectionId) {
        Projection projection = null;
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());;
            System.out.println("A problem occurred in retriving a projection!");
        } finally {

        }
        return projection;
    }

    @Override
    public Set<Projection> queryProjection(String cinemaId, String filmId, String date, int room) {
        Set<Projection> projections = null;

        String query = "SELECT p "
                + "FROM Projection p "
                + "WHERE ((" + cinemaId + " = -1) OR ( " + cinemaId + " = p.idCinema)) "
                + "AND ((" + filmId + " = -1) OR ( " + filmId + " = p.idFilm)) "
                + "AND (('" + date + "' = 'all') OR dateTime between '" + date + " 00:00:00' and '" + date + " 23:59:59') "
                + "AND ((" + room + " = -1) OR ( " + room + " = p.room)) ";

        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retriving the projections!");
        } finally {

        }

        return projections;
    }

    @Override
    public boolean checkDuplicates(String cinemaId, String filmId, String date, int room) {

        Set<Projection> projections = null;

        String query = "SELECT p "
                + "FROM Projection p "
                + "WHERE ((" + cinemaId + " = -1) OR ( " + cinemaId + " = p.idCinema)) "
                + "AND ((" + filmId + " = -1) OR ( " + filmId + " = p.idFilm)) "
                + "AND (('" + date + "' = 'all') OR dateTime = '" + date + "') "
                + "AND ((" + room + " = -1) OR ( " + room + " = p.room)) ";

        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in checking duplicates!");
        } finally {

        }

        return !projections.isEmpty();

    }
}
