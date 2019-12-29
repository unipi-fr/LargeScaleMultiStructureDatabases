package com.lsmsdbgroup.pisaflix.Entities;

import com.lsmsdbgroup.pisaflix.Entities.exceptions.NonConvertibleDocumentException;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import java.io.Serializable;
import java.util.*;
import org.bson.Document;

public class Comment extends Engage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idComment = idEngage;
    private String text;
    private boolean recent = false;
    private Date lastModified;
            
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

    public Comment(String idComment, User user, String text, Date timestamp) {

        this.idComment = idComment;
        this.timestamp = timestamp;
        this.text = text;
        this.user = user;
    }
    
    public Comment(Document commentDocument) {
        
        if(commentDocument.containsKey("_id") && commentDocument.containsKey("Timestamp") && commentDocument.containsKey("Text")){         
            this.timestamp = commentDocument.getDate("Timestamp");
            this.idComment = commentDocument.get("_id").toString();
            this.text = commentDocument.getString("Text");
            if(commentDocument.containsKey("Film")){
                this.film = PisaFlixServices.filmService.getById(commentDocument.get("Film").toString()); 
            }else{
                this.recent = true; //Se non ha il campo film il commento è dentro la sua lista
            }                           
            if(commentDocument.containsKey("User")){
                this.user = PisaFlixServices.userService.getById(commentDocument.get("User").toString());
            }
            if(commentDocument.containsKey("LastModified")){
                this.lastModified = commentDocument.getDate("LastModified");
            }
        }else{
            try {
                throw new NonConvertibleDocumentException("Document not-convertible in comment");
            } catch (NonConvertibleDocumentException ex) {
                System.out.println(ex.getMessage());
            }
        }      
    }

    @Override
    public String getId() {
        return idComment;
    }

    @Override
    public void setIdComment(String idComment) {
        this.idComment = idComment;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Film getFilm() {
        return film;
    }

    @Override
    public void setFilm(Film film) {
        this.film = film;
    }

    @Override
    public User getUser() {
        if (user == null) {
            User u = new User();
            u.setIdUser(null);
            u.setUsername("Deleted User");
            return u;
        }
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
    
    public boolean isRecent(){
        return recent;
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
                + "\ntimestamp:" + timestamp.toString() + "\ntext:" + text;

    }

    public Object getLastModified() {
        return lastModified;
    }

}
