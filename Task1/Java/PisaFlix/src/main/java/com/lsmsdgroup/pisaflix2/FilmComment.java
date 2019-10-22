/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdgroup.pisaflix2;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author alessandromadonna
 */
@Entity
@Table(name = "Film_Comment")
@NamedQueries({
    @NamedQuery(name = "FilmComment.findAll", query = "SELECT f FROM FilmComment f"),
    @NamedQuery(name = "FilmComment.findByTimestamp", query = "SELECT f FROM FilmComment f WHERE f.filmCommentPK.timestamp = :timestamp"),
    @NamedQuery(name = "FilmComment.findByIdUser", query = "SELECT f FROM FilmComment f WHERE f.filmCommentPK.idUser = :idUser"),
    @NamedQuery(name = "FilmComment.findByIdFilm", query = "SELECT f FROM FilmComment f WHERE f.filmCommentPK.idFilm = :idFilm")})
public class FilmComment implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FilmCommentPK filmCommentPK;
    @Basic(optional = false)
    @Lob
    @Column(name = "text")
    private String text;
    @JoinColumn(name = "idFilm", referencedColumnName = "idFilm", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Film film;
    @JoinColumn(name = "idUser", referencedColumnName = "idUser", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;

    public FilmComment() {
    }

    public FilmComment(FilmCommentPK filmCommentPK) {
        this.filmCommentPK = filmCommentPK;
    }

    public FilmComment(FilmCommentPK filmCommentPK, String text) {
        this.filmCommentPK = filmCommentPK;
        this.text = text;
    }

    public FilmComment(Date timestamp, int idUser, int idFilm) {
        this.filmCommentPK = new FilmCommentPK(timestamp, idUser, idFilm);
    }

    public FilmCommentPK getFilmCommentPK() {
        return filmCommentPK;
    }

    public void setFilmCommentPK(FilmCommentPK filmCommentPK) {
        this.filmCommentPK = filmCommentPK;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (filmCommentPK != null ? filmCommentPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FilmComment)) {
            return false;
        }
        FilmComment other = (FilmComment) object;
        if ((this.filmCommentPK == null && other.filmCommentPK != null) || (this.filmCommentPK != null && !this.filmCommentPK.equals(other.filmCommentPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PisaFlix.FilmComment[ filmCommentPK=" + filmCommentPK + " ]";
    }
    
}
