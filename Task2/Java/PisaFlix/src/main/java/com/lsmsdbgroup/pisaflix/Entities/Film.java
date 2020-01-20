package com.lsmsdbgroup.pisaflix.Entities;

import com.lsmsdbgroup.pisaflix.Entities.exceptions.NonConvertibleDocumentException;
import java.io.Serializable;
import java.util.*;
import org.bson.Document;

public class Film extends Entity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idFilm;
    private String title;
    private Date publicationDate;
    private String description;
    private String wikiPage;
    private Double adultness = -1.0;

    private Set<User> userSet = new LinkedHashSet<>();
    private Set<Comment> commentSet = new LinkedHashSet<>();

    public Film() {
    }

    public Film(String idFilm) {
        this.idFilm = idFilm;
    }

    public Film(String idFilm, String title, String description) {
        this.idFilm = idFilm;
        this.title = title;
        this.description = description;
    }

    public Film(String idFilm, String title, Date publicationDate) {
        this.idFilm = idFilm;
        this.title = title;
        this.publicationDate = publicationDate;
    }

    public Film(Document filmDocument) {
        if (filmDocument.containsKey("_id")) {
            if (filmDocument.containsKey("Description")) {
                this.description = filmDocument.getString("Description");
            }
            if (filmDocument.containsKey("Title")) {
                this.title = filmDocument.getString("Title");
            }
            if (filmDocument.containsKey("PublicationDate")) {
                this.publicationDate = filmDocument.getDate("PublicationDate");
            }
            if (filmDocument.containsKey("WikiPage")) {
                this.wikiPage = filmDocument.getString("WikiPage");
            }
            if (filmDocument.containsKey("Adultness")) {
                this.adultness = filmDocument.getDouble("Adultness");
            }
            this.idFilm = filmDocument.get("_id").toString();
        } else {
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
    
    public double getAdultness() {
        return adultness;
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

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
    }

    public Set<Comment> getCommentSet() {
        return commentSet;
    }

    public void setCommentSet(Set<Comment> commentSet) {
        this.commentSet = commentSet;
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

    public String getWikiPage() {
        return wikiPage;
    }

}
