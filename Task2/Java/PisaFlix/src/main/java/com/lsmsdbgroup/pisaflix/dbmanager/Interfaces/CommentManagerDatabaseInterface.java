package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.*;
import java.util.Set;

public interface CommentManagerDatabaseInterface {

    void createComment(String text, User user, Entity entity);

    void update(Comment comment, String text);

    void delete(String idComment);
    
    void deleteAll(Entity entity);

    Comment getById(String commentId);
    
    Set<Comment> getCommentSet(Entity entity);

}
