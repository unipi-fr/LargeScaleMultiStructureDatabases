package com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.util.*;

public interface FilmServiceInterface {

    Set<Film> getFilmsFiltered(String titleFilter, Date startDateFilter, Date endDateFilter);

    Set<Film> getAll();

    Film getById(String id);

    void addFilm(String title, Date publicationDate, String description) throws UserNotLoggedException, InvalidPrivilegeLevelException;

    void updateFilm(Film film) throws UserNotLoggedException, InvalidPrivilegeLevelException;

    void deleteFilm(String idFilm) throws UserNotLoggedException, InvalidPrivilegeLevelException;
 
    void getRecentComments(Film film);
    
    void addComment(Film film, User user, String text);
}
