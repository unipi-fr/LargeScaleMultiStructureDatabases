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
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author alessandromadonna
 */
@Embeddable
public class CinemaCommentPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @Basic(optional = false)
    @Column(name = "idUser")
    private int idUser;
    @Basic(optional = false)
    @Column(name = "idCinema")
    private int idCinema;

    public CinemaCommentPK() {
    }

    public CinemaCommentPK(Date timestamp, int idUser, int idCinema) {
        this.timestamp = timestamp;
        this.idUser = idUser;
        this.idCinema = idCinema;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
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
        hash += (timestamp != null ? timestamp.hashCode() : 0);
        hash += (int) idUser;
        hash += (int) idCinema;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CinemaCommentPK)) {
            return false;
        }
        CinemaCommentPK other = (CinemaCommentPK) object;
        if ((this.timestamp == null && other.timestamp != null) || (this.timestamp != null && !this.timestamp.equals(other.timestamp))) {
            return false;
        }
        if (this.idUser != other.idUser) {
            return false;
        }
        if (this.idCinema != other.idCinema) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PisaFlix.CinemaCommentPK[ timestamp=" + timestamp + ", idUser=" + idUser + ", idCinema=" + idCinema + " ]";
    }
    
}
