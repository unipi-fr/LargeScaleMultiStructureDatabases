package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import java.util.*;
import javax.persistence.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.FilmManagerDatabaseInterface;

public class FilmManager implements FilmManagerDatabaseInterface {

    private final EntityManagerFactory factory;
    private EntityManager entityManager;

    private static FilmManager filmManager;

    public static FilmManager getIstance() {
        if (filmManager == null) {
            filmManager = new FilmManager();
        }

        return filmManager;
    }

    private FilmManager() {
        factory = DBManager.getEntityManagerFactory();
    }

    @Override
    public Film getById(int filmId, boolean retreiveComments) {
        Film film = null;
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            film = entityManager.find(Film.class, filmId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retriving a film!");
        } finally {
            entityManager.close();
        }
        return film;
    }

    @Override
    public Set<Film> getAll() {
        Set<Film> films = null;
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            films = new LinkedHashSet<>(entityManager.createQuery("FROM Film").getResultList());
            if (films == null) {
                System.out.println("Film is empty!");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retrieve all films!");
        } finally {
            entityManager.close();
        }
        return films;
    }

    @Override
    public void create(String title, Date publicationDate, String description) {
        Film film = new Film();
        film.setTitle(title);
        film.setDescription(description);
        film.setPublicationDate(publicationDate);
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(film);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in creating the film!");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void update(int idFilm, String title, Date publicationDate, String description) {
        Film film = new Film(idFilm);
        film.setTitle(title);
        film.setDescription(description);
        film.setPublicationDate(publicationDate);
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(film);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating the film!");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void delete(int idFilm) {
        clearUserSet(getById(idFilm, true));
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            Film reference = entityManager.getReference(Film.class, idFilm);
            entityManager.remove(reference);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in deleting the film!");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void clearUserSet(Film film) {
        film.setUserSet(new LinkedHashSet<>());
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(film);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred clearing the film's userset!");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void updateFavorites(Film film) {
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(film);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred updating favorite films!");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Set<Film> getFiltered(String titleFilter, Date startDateFilter, Date endDateFilter) {
        Set<Film> films = null;
        String title = "";
        String startDate = "0000-01-01";
        String endDate = "9999-12-31";
        if (titleFilter != null) {
            title = titleFilter;
        }
        if (startDateFilter != null) {
            startDate = startDateFilter.toString();
        }
        if (endDateFilter != null) {
            endDate = endDateFilter.toString();
        }

        String query = "SELECT f "
                + "FROM Film f "
                + "WHERE ('" + title + "'='' OR f.title LIKE '%" + title + "%') "
                + "AND (publicationDate between '" + startDate + " 00:00:00' and '" + endDate + " 23:59:59') ";

        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            films = new LinkedHashSet<>(entityManager.createQuery(query).getResultList());
            if (films == null) {
                System.out.println("Films are empty!");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retrieve films filtered!");
        } finally {
            entityManager.close();
        }
        return films;
    }

}
