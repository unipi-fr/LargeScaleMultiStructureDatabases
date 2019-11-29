package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.*;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.CommentManagerDatabaseInterface;

public class CommentManager implements CommentManagerDatabaseInterface {


    private static CommentManager commentManager;

    public static CommentManager getIstance() {
        if (commentManager == null) {
            commentManager = new CommentManager();
        }
        return commentManager;
    }

    private CommentManager() {
        throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
    }

    @Override
    public void createFilmComment(String text, User user, Film film) {
        Comment comment = new Comment();
        comment.setText(text);
        comment.setTimestamp(new Date());

        user.getCommentSet().add(comment);
        film.getCommentSet().add(comment);
        Set<Film> filmSet = new LinkedHashSet<>();
        filmSet.add(film);
        comment.setFilmSet(filmSet);
        comment.setUser(user);

        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in creating the comment!");
        } finally {
            
        }
    }

    @Override
    public void createCinemaComment(String text, User user, Cinema cinema) {
        Comment comment = new Comment();
        comment.setText(text);
        comment.setTimestamp(new Date());

        user.getCommentSet().add(comment);
        cinema.getCommentSet().add(comment);
        Set<Cinema> cinemaSet = new LinkedHashSet<>();
        cinemaSet.add(cinema);
        comment.setCinemaSet(cinemaSet);
        comment.setUser(user);

        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in creating the comment!");
        } finally {

        }
    }

    @Override
    public void update(Comment comment, String text) {
        comment.setText(text);
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating the Comment!");
        } finally {

        }
    }

    @Override
    public void delete(int idComment) {
        // code to delete a cinema
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in removing the Comment!");
        } finally {

        }
    }

    @Override
    public Comment getById(int commentId) {
        Comment comment = null;
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retriving a comment!");
        } finally {
            
        }

        return comment;
    }

}
