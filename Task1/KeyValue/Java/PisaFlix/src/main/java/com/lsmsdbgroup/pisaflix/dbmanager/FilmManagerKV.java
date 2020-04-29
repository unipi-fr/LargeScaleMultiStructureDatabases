
package com.lsmsdbgroup.pisaflix.dbmanager;

import java.util.*;
import javax.persistence.*;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.FilmManagerDatabaseInterface;


// fare operazioni commentate

public class FilmManagerKV extends KeyValueDBManager implements FilmManagerDatabaseInterface {

    private final EntityManagerFactory factory;
    private EntityManager entityManager;

    private static FilmManagerKV filmManager;

    public static FilmManagerKV getIstance() {
        if (filmManager == null) {
            filmManager = new FilmManagerKV();
        }
        return filmManager;
    }

    private FilmManagerKV() {
        factory = DBManager.getEntityManagerFactory();
        super.settings();
    }

    @Override
    public Film getById(int filmId, boolean retreiveComments) {
        Film film = null;
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            film = entityManager.find(Film.class, filmId);
            entityManager.getTransaction().commit();
            
            // ricorda di recuperare i commenti e di settarli nel caso in cui
            // retreiveComments è settato a true (per evitare la ricorsione infinita)
            if(retreiveComments)
                film.setCommentSet(CommentManagerKV.getIstance().getCommentsFilm(film.getIdFilm()));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retriving a film!");
        } finally {
            if(entityManager.isOpen())
                entityManager.close();
        }
        
        return film;
    }

    @Override
    public Set<Film> getAll(){
        Set<Film> films = null;
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            films = new LinkedHashSet<>(entityManager.createQuery("FROM Film f").getResultList());
            // ricorda di recuperare i commenti e di settarli per ogni film in films
            for(Film film : films){
                film.setCommentSet(CommentManagerKV.getIstance().getCommentsFilm(film.getIdFilm()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retrieve all films!");
        } finally {
            if(entityManager.isOpen())
                entityManager.close();
        }
        
        return films;
    }

    @Override
    public void create(String title, Date publicationDate, String description){
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
        clearUserSet(getById(idFilm, false));
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
            if(entityManager.isOpen())
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
            // ricorda di recuperare i commenti e di settarli per ogni film in films
            for(Film film : films){
                film.setCommentSet(CommentManagerKV.getIstance().getCommentsFilm(film.getIdFilm()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retrieve films filtered!");
        } finally {
            if(entityManager.isOpen())
              entityManager.close();
        }
        return films;
    }
    
    
}

