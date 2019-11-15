package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import java.util.*;
import javax.persistence.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.CinemaManagerDatabaseInterface;

public class CinemaManager implements CinemaManagerDatabaseInterface {

    private final EntityManagerFactory factory;
    private EntityManager entityManager;

    private static CinemaManager cinemaManager;

    public static CinemaManager getIstance() {
        if (cinemaManager == null) {
            cinemaManager = new CinemaManager();
        }
        return cinemaManager;
    }

    private CinemaManager() {
        factory = DBManager.getEntityManagerFactory();
    }

    @Override
    public void create(String name, String address) {
        Cinema cinema = new Cinema();
        cinema.setName(name);
        cinema.setAddress(address);
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(cinema);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in creating the cinema!");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Cinema getById(int cinemaId) {
        Cinema cinema = null;
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            cinema = entityManager.find(Cinema.class, cinemaId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retriving a film!");
        } finally {
            entityManager.close();
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
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            cinemas = new LinkedHashSet<>(entityManager.createQuery(query).getResultList());
            if (cinemas == null) {
                System.out.println("cinemas are empty!");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retrieve cinemas filtered!");
        } finally {
            entityManager.close();
        }
        return cinemas;
    }

    @Override
    public void delete(int idCinema) {
        clearUserSet(getById(idCinema));
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            Cinema reference = entityManager.getReference(Cinema.class, idCinema);
            entityManager.remove(reference);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in removing a Cinema!");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void clearUserSet(Cinema cinema) {
        cinema.setUserSet(new LinkedHashSet<>());
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(cinema);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred clearing the cinema's userset!");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void update(int idCinema, String name, String address) {
        Cinema cinema = new Cinema(idCinema);
        cinema.setName(name);
        cinema.setAddress(address);
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(cinema);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating the film!");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Set<Cinema> getAll() {
        Set<Cinema> cinemas = null;
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            cinemas = new LinkedHashSet<>(entityManager.createQuery("FROM Cinema").getResultList());
            if (cinemas == null) {
                System.out.println("Cinema is empty!");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retrieve all cinemas!");
        } finally {
            entityManager.close();
        }
        return cinemas;
    }

    @Override
    public void updateFavorites(Cinema cinema) {
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(cinema);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred updating favorite cinemas!");
        } finally {
            entityManager.close();
        }
    }

}
