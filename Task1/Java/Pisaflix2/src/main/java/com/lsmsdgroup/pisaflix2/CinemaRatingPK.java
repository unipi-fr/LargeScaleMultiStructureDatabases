/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdgroup.pisaflix2;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author alessandromadonna
 */
@Embeddable
public class CinemaRatingPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "idUser")
    private int idUser;
    @Basic(optional = false)
    @Column(name = "idCinema")
    private int idCinema;

    public CinemaRatingPK() {
    }

    public CinemaRatingPK(int idUser, int idCinema) {
        this.idUser = idUser;
        this.idCinema = idCinema;
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
        hash += (int) idUser;
        hash += (int) idCinema;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CinemaRatingPK)) {
            return false;
        }
        CinemaRatingPK other = (CinemaRatingPK) object;
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
        return "PisaFlix.CinemaRatingPK[ idUser=" + idUser + ", idCinema=" + idCinema + " ]";
    }
    
}
