package com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.util.*;

public interface FilmServiceInterface {

    Set<Film> getFilmsFiltered(String titleFilter, Date startDateFilter, Date endDateFilter, int limit);

    Set<Film> getAll();

    Film getById(Long id);

    boolean addFilm(String title, Date publicationDate) throws UserNotLoggedException, InvalidPrivilegeLevelException;

    void updateFilm(Film film) throws UserNotLoggedException, InvalidPrivilegeLevelException;

    void deleteFilm(Long idFilm) throws UserNotLoggedException, InvalidPrivilegeLevelException;
    
    void follow(Film film, User user);

    boolean isFollowing(Film film, User user);

    void unfollow(Film film, User user);
    
    long countFollowers(Film film);
    
    Set<User> getFollowers(Film film);
    
}
