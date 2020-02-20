package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.User;
import java.util.*;
import org.neo4j.driver.v1.Record;

public interface FilmManagerDatabaseInterface {

    Film getById(Long filmId);

    Set<Film> getAll();

    boolean create(String title, Date publicationDate);

    void update(Long idFilm, String title, Date publicationDate);

    void delete(Long idFilm);

    Set<Film> getFiltered(String titleFilter, Date startDateFilter, Date endDateFilter, int limit);
    
    void follow(Film film, User user);

    boolean isFollowing(Film film, User user);

    void unfollow(Film film, User user);
    
    long countFollowers(Film film);
    
    Film getFilmFromRecord(Record record);
    
    Set<User> getFollowers(Film film);
    
}
