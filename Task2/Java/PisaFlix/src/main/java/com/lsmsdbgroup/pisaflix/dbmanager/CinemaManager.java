package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.CinemaManagerDatabaseInterface;
import com.mongodb.client.*;
import org.bson.Document;

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
        CinemaCollection = DBManager.getMongoDatabase().getCollection("cinema");
    }

    @Override
    public void create(String name, String address) {
        Document cinemaDocument = new Document();
        cinemaDocument.put("Name", name);
        cinemaDocument.put("Address", address);
        try {
            CinemaCollection.insertOne(cinemaDocument);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in creating the cinema!");
        }
    }

    @Override
    public Cinema getById(int cinemaId) {
        Cinema cinema = null;
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retriving a film!");
        } finally {
            
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
    public void delete(int idCinema) {
        clearUserSet(getById(idCinema));
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in removing a Cinema!");
        } finally {
            
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
    public void update(int idCinema, String name, String address) {
        Cinema cinema = new Cinema(idCinema);
        cinema.setName(name);
        cinema.setAddress(address);
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
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
