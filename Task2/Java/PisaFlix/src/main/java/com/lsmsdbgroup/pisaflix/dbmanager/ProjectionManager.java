package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.*;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.ProjectionManagerDatabaseInterface;

public class ProjectionManager implements ProjectionManagerDatabaseInterface {

    private static ProjectionManager projectionManager;

    public static ProjectionManager getIstance() {
        if (projectionManager == null) {
            projectionManager = new ProjectionManager();
        }

        return projectionManager;
    }

    private ProjectionManager() {
        throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
    }

    @Override
    public void create(Date dateTime, int room, Film film, Cinema cinema) {
        Projection projection = new Projection();
        projection.setDateTime(dateTime);
        projection.setRoom(room);
        projection.setIdCinema(cinema);
        projection.setIdFilm(film);
        cinema.getProjectionSet().add(projection);
        film.getProjectionSet().add(projection);
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in creating the projection!");
        } finally {

        }
    }

    @Override
    public void delete(int idProjection) {
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
    public void update(int idProjection, Date dateTime, int room) {
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
    public Projection getById(int projectionId) {
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
    public Set<Projection> queryProjection(int cinemaId, int filmId, String date, int room) {
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
    public boolean checkDuplicates(int cinemaId, int filmId, String date, int room) {

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
