/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdgroup.pisaflix;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author alessandromadonna
 */
@Embeddable
public class FilmRatingPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "idUser")
    private int idUser;
    @Basic(optional = false)
    @Column(name = "idFilm")
    private int idFilm;

    public FilmRatingPK() {
    }

    public FilmRatingPK(int idUser, int idFilm) {
        this.idUser = idUser;
        this.idFilm = idFilm;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idUser;
        hash += (int) idFilm;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FilmRatingPK)) {
            return false;
        }
        FilmRatingPK other = (FilmRatingPK) object;
        if (this.idUser != other.idUser) {
            return false;
        }
        if (this.idFilm != other.idFilm) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PisaFlix.FilmRatingPK[ idUser=" + idUser + ", idFilm=" + idFilm + " ]";
    }
    
}
