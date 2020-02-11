package com.lsmsdbgroup.pisaflix.Entities;

import java.io.Serializable;
import java.util.*;

public class Film extends Entity implements Serializable {
    
    private String idFilm;
    private String title;
    private Date publicationDate;
    private String wikiPage;

    private Set<User> userSet = new LinkedHashSet<>();

    public Film() {
    }

    public Film(String idFilm) {
        this.idFilm = idFilm;
    }

    public Film(String idFilm, String title) {
        this.idFilm = idFilm;
        this.title = title;
    }

    public Film(String idFilm, String title, Date publicationDate) {
        this.idFilm = idFilm;
        this.title = title;
        this.publicationDate = publicationDate;
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

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
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
