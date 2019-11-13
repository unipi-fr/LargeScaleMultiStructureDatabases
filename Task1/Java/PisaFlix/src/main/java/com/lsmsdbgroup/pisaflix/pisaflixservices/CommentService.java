package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.Entities.Comment;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.ICommentManagerDB;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.ICommentService;

public class CommentService implements ICommentService{
    private ICommentManagerDB cm;
    
    CommentService(ICommentManagerDB commentManager){
        cm = commentManager;
    }
    
    @Override
    public void update(Comment comment){
        cm.update(comment, comment.getText());
    }

    @Override
    public Comment getById(int id){
        Comment comment;

        comment = cm.getById(id);

        return comment;
    }

    @Override
    public void delete(int id){
        cm.delete(id);
    }
}
