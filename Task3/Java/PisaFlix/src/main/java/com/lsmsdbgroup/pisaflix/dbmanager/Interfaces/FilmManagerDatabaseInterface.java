package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.Entity;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.User;
import java.util.*;

public interface FilmManagerDatabaseInterface {

    Film getById(String filmId);

    Set<Film> getAll();

    boolean create(String title, Date publicationDate);

    void update(String idFilm, String title, Date publicationDate);

    void delete(String idFilm);

    Set<Film> getFiltered(String titleFilter, Date startDateFilter, Date endDateFilter);
}