package com.lsmsdgroup.pisaflix;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Embeddable
public class FilmCommentPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @Basic(optional = false)
    @Column(name = "idUser")
    private int idUser;
    @Basic(optional = false)
    @Column(name = "idFilm")
    private int idFilm;

    public FilmCommentPK() {
    }

    public FilmCommentPK(Date timestamp, int idUser, int idFilm) {
        this.timestamp = timestamp;
        this.idUser = idUser;
        this.idFilm = idFilm;
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

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (timestamp != null ? timestamp.hashCode() : 0);
        hash += (int) idUser;
        hash += (int) idFilm;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FilmCommentPK)) {
            return false;
        }
        FilmCommentPK other = (FilmCommentPK) object;
        if ((this.timestamp == null && other.timestamp != null) || (this.timestamp != null && !this.timestamp.equals(other.timestamp))) {
            return false;
        }
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
        return "PisaFlix.FilmCommentPK[ timestamp=" + timestamp + ", idUser=" + idUser + ", idFilm=" + idFilm + " ]";
    }
    
}
