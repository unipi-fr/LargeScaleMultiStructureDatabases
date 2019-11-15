package com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.util.*;

public interface FilmServiceInterface {

    Set<Film> getFilmsFiltered(String titleFilter, Date startDateFilter, Date endDateFilter);

    Set<Film> getAll();

    Film getById(int id);

    void addFilm(String title, Date publicationDate, String description);

    void deleteFilm(int idFilm) throws UserNotLoggedException, InvalidPrivilegeLevelException;

    void addFavorite(Film film, User user);

    void removeFavourite(Film film, User user);
}
