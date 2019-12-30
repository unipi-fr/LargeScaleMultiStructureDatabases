package com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;

public interface CommentServiceInterface {

    void addComment(String text, User user, Film film);

    void update(Comment comment) throws UserNotLoggedException, InvalidPrivilegeLevelException;

    Comment getById(String id);

    void delete(Comment comment) throws UserNotLoggedException, InvalidPrivilegeLevelException;

    long count(Entity user);
}
