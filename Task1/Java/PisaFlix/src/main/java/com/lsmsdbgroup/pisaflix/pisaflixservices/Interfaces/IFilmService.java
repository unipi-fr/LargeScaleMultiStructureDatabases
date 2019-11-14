/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.util.Date;
import java.util.Set;

public interface IFilmService {
        
    Set<Film> getFilmsFiltered(String titleFilter, Date startDateFilter, Date endDateFilter);

    Set<Film> getAll();

    Film getById(int id);

    void addFilm(String title, Date publicationDate, String description);

    void deleteFilm(int idFilm) throws UserNotLoggedException, InvalidPrivilegeLevelException;

    void addFavorite(Film film, User user);

    void removeFavourite(Film film, User user);
}
