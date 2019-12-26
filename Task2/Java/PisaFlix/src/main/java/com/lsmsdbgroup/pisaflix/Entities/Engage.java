package com.lsmsdbgroup.pisaflix.Entities;

import com.lsmsdbgroup.pisaflix.Entities.exceptions.NonConvertibleDocumentException;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import java.io.Serializable;
import java.util.Date;
import org.bson.Document;

public class Engage extends Entity implements Serializable{
    
    private static final long serialVersionUID = 1L;

    protected String idEngage;
    protected Date timestamp;
    protected Film film;
    protected User user;
    protected Type type;

    public Engage() {
    }

    public Engage(String idEngage) {
        this.idEngage = idEngage;
    }

    public Engage(String idComment, Date timestamp, String text) {
        this.idEngage = idComment;
        this.timestamp = timestamp;
    }

    public Engage(String idEngage, User user, Film film, Date timestamp, Type type) {

        this.idEngage = idEngage;
        this.timestamp = timestamp;
        this.user = user;
        this.film = film;
        this.type = type;
    }

    public Engage(String idComment, User user, Date timestamp) {

        this.idEngage = idComment;
        this.timestamp = timestamp;
        this.user = user;
    }
    
    public Engage(Document engageDocument) {
        
        if(engageDocument.containsKey("_id") && engageDocument.containsKey("Timestamp") && engageDocument.containsKey("Film") && engageDocument.containsKey("User") ){
            this.idEngage = engageDocument.get("_id").toString();          
            this.user = PisaFlixServices.userService.getById(engageDocument.get("User").toString());           
            this.film = PisaFlixServices.filmService.getById(engageDocument.get("Film").toString());            
            if(engageDocument.containsKey(Type.COMMENT + "Timestamp")){
                this.timestamp = engageDocument.getDate(Type.COMMENT + "Timestamp");
                this.type = Type.COMMENT;
            }
            if(engageDocument.containsKey(Type.VIEW + "Timestamp")){
                this.timestamp = engageDocument.getDate(Type.VIEW + "Timestamp");
                this.type = Type.FAVOURITE;
            }
            if(engageDocument.containsKey(Type.FAVOURITE + "Timestamp")){
                this.timestamp = engageDocument.getDate(Type.COMMENT + "Timestamp");
                this.type = Type.FAVOURITE;
            }
        }else{
            try {
                throw new NonConvertibleDocumentException("Document not-convertible in engage");
            } catch (NonConvertibleDocumentException ex) {
                System.out.println(ex.getMessage());
            }
        }      
    }

    @Override
    public String getId() {
        return idEngage;
    }
    
    public Type getType() {
        return type;
    }
   
    public Type setType(Type type) {
        return type;
    }

    public void setIdComment(String idEngage) {
        this.idEngage = idEngage;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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
        hash += (idEngage != null ? idEngage.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Comment)) {
            return false;
        }
        Comment other = (Comment) object;
        return !((this.idEngage == null && other.idEngage != null) || (this.idEngage != null && !this.idEngage.equals(other.idEngage)));
    }

    @Override
    public String toString() {

        if (film != null ) {
            return "[ idEngage= " + idEngage + " ]\nuser: " + user.toString()
                    + "\ntimestamp:" + timestamp.toString()
                    + "\nfilm: " + film;
        }

        return "[ idEngage= " + idEngage + " ]\nuser: " + user.toString()
                + "\ntimestamp:" + timestamp.toString();

    }

}
