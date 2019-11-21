package com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.InvalidPrivilegeLevelException;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.UserNotLoggedException;

public interface CommentServiceInterface {

    void addFilmComment(String comment, User user, Film film);

    void addCinemaComment(String comment, User user, Cinema cinema);

    void update(Comment comment) throws UserNotLoggedException, InvalidPrivilegeLevelException;

    Comment getById(int id);

    void delete(Comment comment) throws UserNotLoggedException, InvalidPrivilegeLevelException;
}
