package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.*;
import java.util.*;
import javax.persistence.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.ProjectionManagerDatabaseInterface;

public class ProjectionManager implements ProjectionManagerDatabaseInterface {

    private final EntityManagerFactory factory;
    private EntityManager entityManager;

    private static ProjectionManager projectionManager;

    public static ProjectionManager getIstance() {
        if (projectionManager == null) {
            projectionManager = new ProjectionManager();
        }

        return projectionManager;
    }

    private ProjectionManager() {
        factory = DBManager.getEntityManagerFactory();
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
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(projection);
            entityManager.merge(cinema);
            entityManager.merge(film);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in creating the projection!");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void delete(int idProjection) {
        // code to delete a cinema
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            Projection reference = entityManager.getReference(Projection.class, idProjection);
            reference.getIdCinema().getProjectionSet().remove(reference);
            reference.getIdFilm().getProjectionSet().remove(reference);
            entityManager.remove(reference);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in removing the Projection!");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void update(int idProjection, Date dateTime, int room) {
        Projection projection = new Projection(idProjection);
        projection.setDateTime(dateTime);
        projection.setRoom(room);
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(projection);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating the projection!");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Set<Projection> getAll() {
        Set<Projection> projections = null;
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            projections = new LinkedHashSet<>(entityManager.createQuery("FROM projection").getResultList());
            if (projections == null) {
                System.out.println("Projection is empty!");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retrieve all projections!");
        } finally {
            entityManager.close();
        }
        return projections;
    }

    @Override
    public Projection getById(int projectionId) {
        Projection projection = null;
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            projection = entityManager.find(Projection.class, projectionId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retriving a projection!");
        } finally {
            entityManager.close();
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
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            projections = new LinkedHashSet<>(entityManager.createQuery(query).getResultList());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retriving the projections!");
        } finally {
            entityManager.close();
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
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            projections = new LinkedHashSet<>(entityManager.createQuery(query).getResultList());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in checking duplicates!");
        } finally {
            entityManager.close();
        }

        return !projections.isEmpty();

    }
}
