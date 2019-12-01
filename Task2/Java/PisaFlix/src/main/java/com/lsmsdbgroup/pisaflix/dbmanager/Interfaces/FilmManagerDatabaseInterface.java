package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import java.util.*;

public interface FilmManagerDatabaseInterface {

    Film getById(String filmId);

    Set<Film> getAll();

    void create(String title, Date publicationDate, String description);

    void update(String idFilm, String title, Date publicationDate, String description);

    void delete(String idFilm);

    void clearUserSet(Film film);

    void updateFavorites(Film film);

    Set<Film> getFiltered(String titleFilter, Date startDateFilter, Date endDateFilter);
}
