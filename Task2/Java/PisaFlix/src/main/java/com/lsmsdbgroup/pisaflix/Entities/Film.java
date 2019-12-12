package com.lsmsdbgroup.pisaflix.Entities;

import com.lsmsdbgroup.pisaflix.Entities.exceptions.NonConvertibleDocumentException;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;

public class Film extends Entity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idFilm;
    private String title;
    private Date publicationDate;
    private String description;
    
    private int favoriteCounter;

    private Set<Comment> commentSet = new LinkedHashSet<>();
    private Set<Projection> projectionSet = new LinkedHashSet<>();

    public Film() {
    }

    public Film(String idFilm) {
        this.idFilm = idFilm;
    }

    public Film(String idFilm, String title, Date publicationDate) {
        this.idFilm = idFilm;
        this.title = title;
        this.publicationDate = publicationDate;
    }
    
    public Film(Document filmDocument) {
        if(filmDocument.containsKey("_id") && filmDocument.containsKey("Title") &&filmDocument.containsKey("PublicationDate") ){
            this.idFilm = filmDocument.get("_id").toString();
            this.title = filmDocument.getString("Title");
            this.publicationDate = filmDocument.getDate("PublicationDate");
            this.description = filmDocument.getString("Description");
            this.favoriteCounter = filmDocument.getInteger("FavoriteCounter");
        }else{
            try {
                throw new NonConvertibleDocumentException("Document not-convertible in film");
            } catch (NonConvertibleDocumentException ex) {
                System.out.println(ex.getMessage());
            }
        }      
    }

    @Override
    public String getId() {
        return idFilm;
    }

    public void setIdFilm(String idFilm) {
        this.idFilm = idFilm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        hash += (idFilm != null ? idFilm.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Film)) {
            return false;
        }
        Film other = (Film) object;
        return !((this.idFilm == null && other.idFilm != null) || (this.idFilm != null && !this.idFilm.equals(other.idFilm)));
    }

    @Override
    public String toString() {
        return title;
    }

}
