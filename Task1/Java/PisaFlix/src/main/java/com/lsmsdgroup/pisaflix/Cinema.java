package com.lsmsdgroup.pisaflix;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;

@Entity
@Table(name = "Cinema")
public class Cinema implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idCinema")
    private Integer idCinema;

    @Basic(optional = false)
    @Column(name = "name")
    private String name;

    @Basic(optional = false)
    @Column(name = "address")
    private String address;

    @JoinTable(name = "Favorite_Cinema", joinColumns = {
        @JoinColumn(name = "idCinema", referencedColumnName = "idCinema")}, inverseJoinColumns = {
        @JoinColumn(name = "idUser", referencedColumnName = "idUser")})
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<User> userCollection;

    @JoinTable(name = "Cinema_has_Comment", joinColumns = {
        @JoinColumn(name = "idCinema", referencedColumnName = "idCinema")}, inverseJoinColumns = {
        @JoinColumn(name = "idComment", referencedColumnName = "idComment")})
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Comment> commentCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCinema", fetch = FetchType.EAGER)
    private Collection<Projection> projectionCollection;

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

    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    public Collection<Comment> getCommentCollection() {
        return commentCollection;
    }

    public void setCommentCollection(Collection<Comment> commentCollection) {
        this.commentCollection = commentCollection;
    }

    public Collection<Projection> getProjectionCollection() {
        return projectionCollection;
    }

    public void setProjectionCollection(Collection<Projection> projectionCollection) {
        this.projectionCollection = projectionCollection;
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
        return "com.lsmsdgroup.pisaflix.Cinema[ idCinema=" + idCinema + " ]";
    }

}
