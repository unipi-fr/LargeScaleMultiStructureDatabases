/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdgroup.pisaflix2;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author alessandromadonna
 */
@Entity
@Table(name = "Cinema_Rating")
@NamedQueries({
    @NamedQuery(name = "CinemaRating.findAll", query = "SELECT c FROM CinemaRating c"),
    @NamedQuery(name = "CinemaRating.findByIdUser", query = "SELECT c FROM CinemaRating c WHERE c.cinemaRatingPK.idUser = :idUser"),
    @NamedQuery(name = "CinemaRating.findByIdCinema", query = "SELECT c FROM CinemaRating c WHERE c.cinemaRatingPK.idCinema = :idCinema"),
    @NamedQuery(name = "CinemaRating.findByRate", query = "SELECT c FROM CinemaRating c WHERE c.rate = :rate")})
public class CinemaRating implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CinemaRatingPK cinemaRatingPK;
    @Basic(optional = false)
    @Column(name = "rate")
    private short rate;
    @JoinColumn(name = "idCinema", referencedColumnName = "idCinema", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Cinema cinema;
    @JoinColumn(name = "idUser", referencedColumnName = "idUser", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;

    public CinemaRating() {
    }

    public CinemaRating(CinemaRatingPK cinemaRatingPK) {
        this.cinemaRatingPK = cinemaRatingPK;
    }

    public CinemaRating(CinemaRatingPK cinemaRatingPK, short rate) {
        this.cinemaRatingPK = cinemaRatingPK;
        this.rate = rate;
    }

    public CinemaRating(int idUser, int idCinema) {
        this.cinemaRatingPK = new CinemaRatingPK(idUser, idCinema);
    }

    public CinemaRatingPK getCinemaRatingPK() {
        return cinemaRatingPK;
    }

    public void setCinemaRatingPK(CinemaRatingPK cinemaRatingPK) {
        this.cinemaRatingPK = cinemaRatingPK;
    }

    public short getRate() {
        return rate;
    }

    public void setRate(short rate) {
        this.rate = rate;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
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
        hash += (cinemaRatingPK != null ? cinemaRatingPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CinemaRating)) {
            return false;
        }
        CinemaRating other = (CinemaRating) object;
        if ((this.cinemaRatingPK == null && other.cinemaRatingPK != null) || (this.cinemaRatingPK != null && !this.cinemaRatingPK.equals(other.cinemaRatingPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PisaFlix.CinemaRating[ cinemaRatingPK=" + cinemaRatingPK + " ]";
    }
    
}
