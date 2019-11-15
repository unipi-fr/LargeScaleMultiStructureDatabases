package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.CommentManagerDatabaseInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.CommentServiceInterface;

public class CommentService implements CommentServiceInterface {

    private final CommentManagerDatabaseInterface commentManager;

    CommentService(CommentManagerDatabaseInterface commentManager) {
        this.commentManager = commentManager;
    }

    @Override
    public void update(Comment comment) {
        commentManager.update(comment, comment.getText());
    }

    @Override
    public Comment getById(int id) {
        Comment comment;
        comment = commentManager.getById(id);
        return comment;
    }

    @Override
    public void delete(int id) {
        commentManager.delete(id);
    }

    @Override
    public void addFilmComment(String comment, User user, Film film) {
        commentManager.createFilmComment(comment, user, film);
    }

    @Override
    public void addCinemaComment(String comment, User user, Cinema cinema) {
        commentManager.createCinemaComment(comment, user, cinema);
    }
}
