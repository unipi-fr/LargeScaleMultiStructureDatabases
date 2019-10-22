/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdgroup.pisaflix;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author alessandromadonna
 */
@Embeddable
public class ProjectionPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Basic(optional = false)
    @Column(name = "room")
    private int room;
    @Basic(optional = false)
    @Column(name = "idFilm")
    private int idFilm;
    @Basic(optional = false)
    @Column(name = "idCinema")
    private int idCinema;

    public ProjectionPK() {
    }

    public ProjectionPK(Date date, int room, int idFilm, int idCinema) {
        this.date = date;
        this.room = room;
        this.idFilm = idFilm;
        this.idCinema = idCinema;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }

    public int getIdCinema() {
        return idCinema;
    }

    public void setIdCinema(int idCinema) {
        this.idCinema = idCinema;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (date != null ? date.hashCode() : 0);
        hash += (int) room;
        hash += (int) idFilm;
        hash += (int) idCinema;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProjectionPK)) {
            return false;
        }
        ProjectionPK other = (ProjectionPK) object;
        if ((this.date == null && other.date != null) || (this.date != null && !this.date.equals(other.date))) {
            return false;
        }
        if (this.room != other.room) {
            return false;
        }
        if (this.idFilm != other.idFilm) {
            return false;
        }
        if (this.idCinema != other.idCinema) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PisaFlix.ProjectionPK[ date=" + date + ", room=" + room + ", idFilm=" + idFilm + ", idCinema=" + idCinema + " ]";
    }
    
}
