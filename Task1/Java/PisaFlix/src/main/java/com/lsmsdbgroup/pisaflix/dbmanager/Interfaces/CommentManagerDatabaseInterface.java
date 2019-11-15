package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.*;

public interface CommentManagerDatabaseInterface {

    void createFilmComment(String text, User user, Film film);

    void createCinemaComment(String text, User user, Cinema cinema);

    void update(Comment comment, String text);

    void delete(int idComment);

    Comment getById(int commentId);

}
