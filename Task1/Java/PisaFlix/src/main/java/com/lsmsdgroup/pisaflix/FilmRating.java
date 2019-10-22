package com.lsmsdgroup.pisaflix;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "Film_Rating")
@NamedQueries({
    @NamedQuery(name = "FilmRating.findAll", query = "SELECT f FROM FilmRating f"),
    @NamedQuery(name = "FilmRating.findByIdUser", query = "SELECT f FROM FilmRating f WHERE f.filmRatingPK.idUser = :idUser"),
    @NamedQuery(name = "FilmRating.findByIdFilm", query = "SELECT f FROM FilmRating f WHERE f.filmRatingPK.idFilm = :idFilm"),
    @NamedQuery(name = "FilmRating.findByRate", query = "SELECT f FROM FilmRating f WHERE f.rate = :rate")})
public class FilmRating implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FilmRatingPK filmRatingPK;
    @Basic(optional = false)
    @Column(name = "rate")
    private short rate;
    @JoinColumn(name = "idFilm", referencedColumnName = "idFilm", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Film film;
    @JoinColumn(name = "idUser", referencedColumnName = "idUser", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;

    public FilmRating() {
    }

    public FilmRating(FilmRatingPK filmRatingPK) {
        this.filmRatingPK = filmRatingPK;
    }

    public FilmRating(FilmRatingPK filmRatingPK, short rate) {
        this.filmRatingPK = filmRatingPK;
        this.rate = rate;
    }

    public FilmRating(int idUser, int idFilm) {
        this.filmRatingPK = new FilmRatingPK(idUser, idFilm);
    }

    public FilmRatingPK getFilmRatingPK() {
        return filmRatingPK;
    }

    public void setFilmRatingPK(FilmRatingPK filmRatingPK) {
        this.filmRatingPK = filmRatingPK;
    }

    public short getRate() {
        return rate;
    }

    public void setRate(short rate) {
        this.rate = rate;
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
        hash += (filmRatingPK != null ? filmRatingPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FilmRating)) {
            return false;
        }
        FilmRating other = (FilmRating) object;
        if ((this.filmRatingPK == null && other.filmRatingPK != null) || (this.filmRatingPK != null && !this.filmRatingPK.equals(other.filmRatingPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PisaFlix.FilmRating[ filmRatingPK=" + filmRatingPK + " ]";
    }
    
}
