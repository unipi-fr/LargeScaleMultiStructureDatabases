package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.Entities.exceptions.NonConvertibleDocumentException;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.ProjectionManagerDatabaseInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

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
    
    private Projection getProjectionFromDocument(Document projectionDocument) throws NonConvertibleDocumentException
    {
        Projection projection = null;
        
        if(projectionDocument.containsKey("_id") && projectionDocument.containsKey("DateTime") && projectionDocument.containsKey("Room") && projectionDocument.containsKey("Film") && projectionDocument.containsKey("Cinema")){
            String idProjection = projectionDocument.get("_id").toString();
            Date dateTime = projectionDocument.getDate("DateTime");
            int room = projectionDocument.getInteger("Room");

            String idFilm = projectionDocument.getString("Film");
            Film film = PisaFlixServices.filmService.getById(idFilm);

            String idCinema = projectionDocument.getString("Cinema");
            Cinema cinema = PisaFlixServices.cinemaService.getById(idCinema);

            projection = new Projection(idProjection, dateTime, room, cinema, film);
        }else{
            throw new NonConvertibleDocumentException("Projection Document format error");
        }
        
        return projection;
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
            projectionCollection.deleteOne(eq("_id", new ObjectId(idProjection)));
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
            projectionCollection.updateOne(eq("_id", new ObjectId(idProjection)), new Document("$set", new Document("DateTime", dateTime).append("Room", room)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating the projection!");
        } finally {

        }
    }

    @Override
    public Set<Projection> getAll(int limit, int skip) {
        Set<Projection> projectionsSet = new LinkedHashSet<>();
        try (MongoCursor<Document> cursor = projectionCollection.find().sort(sort).limit(limit).skip(skip).iterator()) {
            while (cursor.hasNext()) {
                Document projectionDocument = cursor.next();
                
                Projection projection = getProjectionFromDocument(projectionDocument);
                
                projectionsSet.add(projection);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retriving the projection!");
        }
        return projectionsSet;
    }

    @Override
    public Projection getById(String projectionId) {
        Projection projection = null;
        try (MongoCursor<Document> cursor = projectionCollection.find(eq("_id", new ObjectId(projectionId))).iterator()) {
            Document projectionDocument = cursor.next();
            
            projection = getProjectionFromDocument(projectionDocument);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());;
            System.out.println("A problem occurred in retriving a projection!");
        } finally {

        }
        
        return projection;
    }

    @Override
    public Set<Projection> queryProjection(String cinemaId, String filmId, String date, int room, int limit, int skip) {
        Set<Projection> projectionsSet = new LinkedHashSet<>();
        List filters = new ArrayList();
        
        if(date.equals("all") && cinemaId.equals("-1") && filmId.equals("-1") && room == -1){
            return getAll(limit, skip);
        }
        
        if(!cinemaId.equals("-1"))
            filters.add(eq("Cinema", cinemaId));
        
        if(!filmId.equals("-1"))
            filters.add(eq("Film", filmId));
        
        if(!date.equals("all"))
            filters.add(and(gte("DateTime", date + "T00:00:00.000+00:00"),lt("DateTime", date + "T23:59:59.000+00:00")));
        
        if(room != -1)
            filters.add(eq("Room", room));

        try (MongoCursor<Document> cursor = projectionCollection.find(and(filters)).sort(sort).limit(limit).skip(skip).iterator()) {
            while (cursor.hasNext()) {
                Document projectionDocument = cursor.next();
                
                Projection projection = getProjectionFromDocument(projectionDocument);
                
                projectionsSet.add(projection);         
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retriving the projections!");
        } finally {

        }

        return projectionsSet;
    }

    @Override
    public boolean checkDuplicates(String cinemaId, String filmId, String date, int room, int limit, int skip) {

        Set<Projection> projections = null;

        /*try() {
            while (cursor.hasNext()) {
                projections.add(new Projection(cursor.next()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in checking duplicates!");
        } finally {

        }*/

        //return !projections.isEmpty();
        
        return false;

    }
}
