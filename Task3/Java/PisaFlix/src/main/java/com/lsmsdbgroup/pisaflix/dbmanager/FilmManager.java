package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.*;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.FilmManagerDatabaseInterface;

public class FilmManager implements FilmManagerDatabaseInterface {

    private static FilmManager FilmManager;

    public static FilmManager getIstance() {
        if (FilmManager == null) {
            FilmManager = new FilmManager();
        }
        return FilmManager;
    }

    private FilmManager() {
        
    }

    @Override
    public Film getById(String filmId) {
        Film film = null;
        
        return film;
    }

    @Override
    public Set<Film> getAll() {
        Set<Film> filmSet = new LinkedHashSet<>();
        
        return filmSet;
    }

    @Override
    public boolean create(String title, Date publicationDate) {
        boolean success = false;
        
        return success;
    }

    @Override
    public void update(String idFilm, String title, Date publicationDate) {
        
    }

    @Override
    public void delete(String idFilm) {
        
    }

    @Override
    public Set<Film> getFiltered(String titleFilter, Date startDateFilter, Date endDateFiltern) {
        Set<Film> filmSet = new LinkedHashSet<>();
        
        return filmSet;
    }
}
