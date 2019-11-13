/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public interface IFilmManagerDB {
    Film getById(int filmId);

    Set<Film> getAll();

    void create(String title, Date publicationDate, String description);

    void update(int idFilm, String title, Date publicationDate, String description);

    void delete(int idFilm);

    void clearUserSet(Film film);

    void updateFavorites(Film film);

    Set<Film> getFiltered(String titleFilter, Date startDateFilter, Date endDateFilter);
}
