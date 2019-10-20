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
@Table(name = "Cinema_Comment")
@NamedQueries({
    @NamedQuery(name = "CinemaComment.findAll", query = "SELECT c FROM CinemaComment c"),
    @NamedQuery(name = "CinemaComment.findByTimestamp", query = "SELECT c FROM CinemaComment c WHERE c.cinemaCommentPK.timestamp = :timestamp"),
    @NamedQuery(name = "CinemaComment.findByIdUser", query = "SELECT c FROM CinemaComment c WHERE c.cinemaCommentPK.idUser = :idUser"),
    @NamedQuery(name = "CinemaComment.findByIdCinema", query = "SELECT c FROM CinemaComment c WHERE c.cinemaCommentPK.idCinema = :idCinema")})
public class CinemaComment implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CinemaCommentPK cinemaCommentPK;
    @Basic(optional = false)
    @Lob
    @Column(name = "text")
    private String text;
    @JoinColumn(name = "idCinema", referencedColumnName = "idCinema", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Cinema cinema;
    @JoinColumn(name = "idUser", referencedColumnName = "idUser", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;

    public CinemaComment() {
    }

    public CinemaComment(CinemaCommentPK cinemaCommentPK) {
        this.cinemaCommentPK = cinemaCommentPK;
    }

    public CinemaComment(CinemaCommentPK cinemaCommentPK, String text) {
        this.cinemaCommentPK = cinemaCommentPK;
        this.text = text;
    }

    public CinemaComment(Date timestamp, int idUser, int idCinema) {
        this.cinemaCommentPK = new CinemaCommentPK(timestamp, idUser, idCinema);
    }

    public CinemaCommentPK getCinemaCommentPK() {
        return cinemaCommentPK;
    }

    public void setCinemaCommentPK(CinemaCommentPK cinemaCommentPK) {
        this.cinemaCommentPK = cinemaCommentPK;
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
        hash += (cinemaCommentPK != null ? cinemaCommentPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CinemaComment)) {
            return false;
        }
        CinemaComment other = (CinemaComment) object;
        if ((this.cinemaCommentPK == null && other.cinemaCommentPK != null) || (this.cinemaCommentPK != null && !this.cinemaCommentPK.equals(other.cinemaCommentPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PisaFlix.CinemaComment[ cinemaCommentPK=" + cinemaCommentPK + " ]";
    }
    
}
