package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.*;
import java.util.Date;
import java.util.Set;

public interface CommentManagerDatabaseInterface {

    void createComment(String text, User user, Entity entity, Date timestamp);

    void update(Comment comment, String text);

    void delete(Comment comment);
    
    void deleteAllRelated(Entity entity);

    Comment getById(String commentId);
    
    Set<Comment> getAll(Film film, int skip, int limit);
}
