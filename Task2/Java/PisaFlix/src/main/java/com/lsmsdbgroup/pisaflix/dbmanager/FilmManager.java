package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.FilmManagerDatabaseInterface;

public class FilmManager implements FilmManagerDatabaseInterface {


    private static FilmManager filmManager;

    public static FilmManager getIstance() {
        if (filmManager == null) {
            filmManager = new FilmManager();
        }

        return filmManager;
    }

    private FilmManager() {
        throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
    }

    @Override
    public Film getById(int filmId) {
        Film film = null;
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retriving a film!");
        } finally {

        }
        return film;
    }

    @Override
    public Set<Film> getAll() {
        Set<Film> films = null;
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retrieve all films!");
        } finally {
            
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
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in creating the film!");
        } finally {

        }
    }

    @Override
    public void update(int idFilm, String title, Date publicationDate, String description) {
        Film film = new Film(idFilm);
        film.setTitle(title);
        film.setDescription(description);
        film.setPublicationDate(publicationDate);
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating the film!");
        } finally {

        }
    }

    @Override
    public void delete(int idFilm) {
        clearUserSet(getById(idFilm));
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in deleting the film!");
        } finally {

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
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retrieve films filtered!");
        } finally {

        }
        return films;
    }

}
