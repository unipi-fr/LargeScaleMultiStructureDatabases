package com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.util.Set;

public interface CommentServiceInterface {

    void addComment(String comment, User user, Entity entity);

    void update(Comment comment) throws UserNotLoggedException, InvalidPrivilegeLevelException;

    Comment getById(String id);

    void delete(Comment comment) throws UserNotLoggedException, InvalidPrivilegeLevelException;
}
