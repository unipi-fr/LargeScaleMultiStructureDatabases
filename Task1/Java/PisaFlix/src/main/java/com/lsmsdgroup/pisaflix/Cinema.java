/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdgroup.pisaflix;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author alessandromadonna
 */
@Entity
@Table(name = "Cinema")
@NamedQueries({
    @NamedQuery(name = "Cinema.findAll", query = "SELECT c FROM Cinema c"),
    @NamedQuery(name = "Cinema.findByIdCinema", query = "SELECT c FROM Cinema c WHERE c.idCinema = :idCinema"),
    @NamedQuery(name = "Cinema.findByName", query = "SELECT c FROM Cinema c WHERE c.name = :name"),
    @NamedQuery(name = "Cinema.findByAddress", query = "SELECT c FROM Cinema c WHERE c.address = :address")})
public class Cinema implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idCinema")
    private Integer idCinema;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "address")
    private String address;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cinema")
    private Collection<Projection> projectionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cinema")
    private Collection<CinemaComment> cinemaCommentCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cinema")
    private Collection<CinemaRating> cinemaRatingCollection;

    public Cinema() {
    }

    public Cinema(Integer idCinema) {
        this.idCinema = idCinema;
    }

    public Cinema(Integer idCinema, String name, String address) {
        this.idCinema = idCinema;
        this.name = name;
        this.address = address;
    }

    public Integer getIdCinema() {
        return idCinema;
    }

    public void setIdCinema(Integer idCinema) {
        this.idCinema = idCinema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Collection<Projection> getProjectionCollection() {
        return projectionCollection;
    }

    public void setProjectionCollection(Collection<Projection> projectionCollection) {
        this.projectionCollection = projectionCollection;
    }

    public Collection<CinemaComment> getCinemaCommentCollection() {
        return cinemaCommentCollection;
    }

    public void setCinemaCommentCollection(Collection<CinemaComment> cinemaCommentCollection) {
        this.cinemaCommentCollection = cinemaCommentCollection;
    }

    public Collection<CinemaRating> getCinemaRatingCollection() {
        return cinemaRatingCollection;
    }

    public void setCinemaRatingCollection(Collection<CinemaRating> cinemaRatingCollection) {
        this.cinemaRatingCollection = cinemaRatingCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCinema != null ? idCinema.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cinema)) {
            return false;
        }
        Cinema other = (Cinema) object;
        if ((this.idCinema == null && other.idCinema != null) || (this.idCinema != null && !this.idCinema.equals(other.idCinema))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PisaFlix.Cinema[ idCinema=" + idCinema + " ]";
    }
    
}
