package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.*;
import java.util.*;
import javax.persistence.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.CommentManagerDatabaseInterface;

public class CommentManager implements CommentManagerDatabaseInterface {

    private final EntityManagerFactory factory;
    private EntityManager entityManager;

    private static CommentManager commentManager;

    public static CommentManager getIstance() {
        if (commentManager == null) {
            commentManager = new CommentManager();
        }
        return commentManager;
    }

    private CommentManager() {
        factory = DBManager.getEntityManagerFactory();
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
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(comment);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in creating the comment!");
        } finally {
            entityManager.close();
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
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(comment);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in creating the comment!");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void update(Comment comment, String text) {
        comment.setText(text);
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(comment);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating the Comment!");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void delete(int idComment) {
        // code to delete a cinema
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            Comment reference = entityManager.getReference(Comment.class, idComment);
            if (!reference.getCinemaSet().isEmpty()) {
                reference.getCinemaSet().iterator().next().getCommentSet().remove(reference);
            }
            if (!reference.getFilmSet().isEmpty()) {
                reference.getFilmSet().iterator().next().getCommentSet().remove(reference);
            }
            reference.getUser().getCommentSet().remove(reference);
            entityManager.remove(reference);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in removing the Comment!");
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Comment getById(int commentId) {
        Comment comment = null;
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            comment = entityManager.find(Comment.class, commentId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retriving a comment!");
        } finally {
            entityManager.close();
        }

        return comment;
    }

}
