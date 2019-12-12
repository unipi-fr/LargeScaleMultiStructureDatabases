package com.lsmsdbgroup.pisaflix.Entities;

import com.lsmsdbgroup.pisaflix.Entities.exceptions.NonConvertibleDocumentException;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;

public class Cinema extends Entity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idCinema;
    private String name;
    private String address;
    
    private int favoriteCounter;

    private Set<Comment> commentSet = new LinkedHashSet<>();
    private Set<Projection> projectionSet = new LinkedHashSet<>();

    public Cinema() {
    }

    public Cinema(String idCinema) {
        this.idCinema = idCinema;
    }

    public Cinema(String idCinema, String name, String address) {
        this.idCinema = idCinema;
        this.name = name;
        this.address = address;
    }
    
    public Cinema(Document cinemaDocument) {
        if(cinemaDocument.containsKey("_id") && cinemaDocument.containsKey("_id") &&cinemaDocument.containsKey("_id") ){
            this.idCinema = cinemaDocument.get("_id").toString();
            this.name = cinemaDocument.getString("Name");
            this.address = cinemaDocument.getString("Address");
            this.favoriteCounter = cinemaDocument.getInteger("FavoriteCounter");
        }else{
            try {
                throw new NonConvertibleDocumentException("Document not-convertible in cinema");
            } catch (NonConvertibleDocumentException ex) {
                System.out.println(ex.getMessage());
            }
        }      
    }
    
    public String getId() {
        return idCinema;
    }

    public void setIdCinema(String idCinema) {
        this.idCinema = idCinema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Comment> getCommentSet() {
        return commentSet;
    }

    public void setCommentSet(Set<Comment> commentSet) {
        this.commentSet = commentSet;
    }

    public Set<Projection> getProjectionSet() {
        return projectionSet;
    }

    public void setProjectionSet(Set<Projection> projectionSet) {
        this.projectionSet = projectionSet;
    }

    public int getFavoriteCounter() {
        return favoriteCounter;
    }

    public void setFavoriteCounter(int favoriteCounter) {
        this.favoriteCounter = favoriteCounter;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCinema != null ? idCinema.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cinema)) {
            return false;
        }
        Cinema other = (Cinema) object;
        return !((this.idCinema == null && other.idCinema != null) || (this.idCinema != null && !this.idCinema.equals(other.idCinema)));
    }
    
    @Override
    public String toString() {
        return name;
    }

}
