package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.Comment;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.User;
import java.util.*;

public interface FilmManagerDatabaseInterface {

    Film getById(String filmId);

    Set<Film> getAll(int limit, int skip);

    void create(String title, Date publicationDate, String description);

    void update(String idFilm, String title, Date publicationDate, String description);

    void delete(String idFilm);

    void addComment(Film film, User user, String text);
    
    void getRecentComments(Film film);
    
    long getCommentPageSize();

    Set<Film> getFiltered(String titleFilter, Date startDateFilter, Date endDateFilter, int limit, int skip);

    void getComments(Film film, int skip, int limit);

    void deleteComment(Comment comment);

    public void updateComment(Comment comment);
}
