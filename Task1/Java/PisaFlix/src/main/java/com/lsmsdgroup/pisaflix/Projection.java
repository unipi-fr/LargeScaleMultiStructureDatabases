package com.lsmsdgroup.pisaflix;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "Projection")
@NamedQueries({
    @NamedQuery(name = "Projection.findAll", query = "SELECT p FROM Projection p"),
    @NamedQuery(name = "Projection.findByDate", query = "SELECT p FROM Projection p WHERE p.projectionPK.date = :date"),
    @NamedQuery(name = "Projection.findByRoom", query = "SELECT p FROM Projection p WHERE p.projectionPK.room = :room"),
    @NamedQuery(name = "Projection.findByIdFilm", query = "SELECT p FROM Projection p WHERE p.projectionPK.idFilm = :idFilm"),
    @NamedQuery(name = "Projection.findByIdCinema", query = "SELECT p FROM Projection p WHERE p.projectionPK.idCinema = :idCinema")})
public class Projection implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProjectionPK projectionPK;
    @JoinColumn(name = "idCinema", referencedColumnName = "idCinema", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Cinema cinema;
    @JoinColumn(name = "idFilm", referencedColumnName = "idFilm", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Film film;

    public Projection() {
    }

    public Projection(ProjectionPK projectionPK) {
        this.projectionPK = projectionPK;
    }

    public Projection(Date date, int room, int idFilm, int idCinema) {
        this.projectionPK = new ProjectionPK(date, room, idFilm, idCinema);
    }

    public ProjectionPK getProjectionPK() {
        return projectionPK;
    }

    public void setProjectionPK(ProjectionPK projectionPK) {
        this.projectionPK = projectionPK;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (projectionPK != null ? projectionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Projection)) {
            return false;
        }
        Projection other = (Projection) object;
        if ((this.projectionPK == null && other.projectionPK != null) || (this.projectionPK != null && !this.projectionPK.equals(other.projectionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PisaFlix.Projection[ projectionPK=" + projectionPK + " ]";
    }
    
}
