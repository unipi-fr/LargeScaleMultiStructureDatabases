package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.*;

public interface CommentManagerDatabaseInterface {

    void createComment(String text, User user, Entity entity);

    void update(Comment comment, String text);

    void delete(String idComment);
    
    void deleteAllRelated(Entity entity);

    Comment getById(String commentId);
}
