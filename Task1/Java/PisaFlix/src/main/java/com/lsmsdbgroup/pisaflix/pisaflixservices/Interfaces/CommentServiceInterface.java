package com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.*;

public interface CommentServiceInterface {

    void addFilmComment(String comment, User user, Film film);

    void addCinemaComment(String comment, User user, Cinema cinema);

    void update(Comment comment);

    Comment getById(int id);

    void delete(int id);
}
