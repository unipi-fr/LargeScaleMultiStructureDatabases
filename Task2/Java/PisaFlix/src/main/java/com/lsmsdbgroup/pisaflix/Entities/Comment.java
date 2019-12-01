package com.lsmsdbgroup.pisaflix.Entities;

import com.lsmsdbgroup.pisaflix.Entities.exceptions.NonConvertibleDocumentException;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;

public class Comment extends Entity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idComment;
    private Date timestamp;
    private String text;

    private Film film;
    private Cinema cinema;
    private User user;

    public Comment() {
    }

    public Comment(String idComment) {
        this.idComment = idComment;
    }

    public Comment(String idComment, Date timestamp, String text) {
        this.idComment = idComment;
        this.timestamp = timestamp;
        this.text = text;
    }

    public Comment(String idComment, User user, Film film, String text, Date timestamp) {

        this.idComment = idComment;
        this.timestamp = timestamp;
        this.text = text;
        this.user = user;
        this.film = film;

    }

    public Comment(String idComment, User user, Cinema cinema, String text, Date timestamp) {

        this.idComment = idComment;
        this.timestamp = timestamp;
        this.text = text;
        this.user = user;
        this.cinema = cinema;
    }
    
    public Comment(Document commentDocument) {
        
        if(commentDocument.containsKey("_id") && commentDocument.containsKey("Timestamp") && commentDocument.containsKey("Text") && (commentDocument.containsKey("Film")||commentDocument.containsKey("Cinema")) ){
            this.idComment = commentDocument.get("_id").toString();
            this.timestamp = commentDocument.getDate("Timestamp");
            this.text = commentDocument.getString("Text");
            if(commentDocument.containsKey("Film")){
                this.film = PisaFlixServices.filmService.getById(commentDocument.get("Film").toString());
            }else{
                this.cinema = PisaFlixServices.cinemaService.getById(commentDocument.get("Cinema").toString());
            }
            if(commentDocument.containsKey("User")){
                //this.user = PisaFlixServices.userService.getById(commentDocument.get("User").toString());
            }
        }else{
            try {
                throw new NonConvertibleDocumentException("Document not-convertible in cinema");
            } catch (NonConvertibleDocumentException ex) {
                Logger.getLogger(Cinema.class.getName()).log(Level.SEVERE, null, ex);
            }
        }      
    }

    @Override
    public String getId() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinemaSet(Cinema cinema) {
        this.cinema = cinema;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public User getUser() {
        if (user == null) {
            User u = new User();
            u.setIdUser(null);
            u.setUsername("Deleted User");
            return u;
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idComment != null ? idComment.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Comment)) {
            return false;
        }
        Comment other = (Comment) object;
        return !((this.idComment == null && other.idComment != null) || (this.idComment != null && !this.idComment.equals(other.idComment)));
    }

    @Override
    public String toString() {

        if (film != null ) {
            return "[ idComment= " + idComment + " ]\nuser: " + user.toString()
                    + "\ntimestamp:" + timestamp.toString() + "\ntext:" + text
                    + "\nfilm: " + film;
        }

        return "[ idComment= " + idComment + " ]\nuser: " + user.toString()
                + "\ntimestamp:" + timestamp.toString() + "\ntext:" + text
                + "\ncinema: " + cinema;

    }

}
