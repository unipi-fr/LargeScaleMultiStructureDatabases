package com.lsmsdbgroup.pisaflix.Entities;

import java.io.Serializable;
import java.util.*;

public class Cinema implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idCinema;
    private String name;
    private String address;

    private Set<User> userSet = new LinkedHashSet<>();
    private Set<Comment> commentSet = new LinkedHashSet<>();
    private Set<Projection> projectionSet = new LinkedHashSet<>();

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

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
    }

    public Set<Comment> getCommentSet() {
        return commentSet;
    }

    public void setCommentSet(Set<Comment> commentSet) {
        this.commentSet = commentSet;
    }

    public Set<Projection> getProjectionSet() {
        return projectionSet;
    }

    public void setProjectionSet(Set<Projection> projectionSet) {
        this.projectionSet = projectionSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCinema != null ? idCinema.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cinema)) {
            return false;
        }
        Cinema other = (Cinema) object;
        return !((this.idCinema == null && other.idCinema != null) || (this.idCinema != null && !this.idCinema.equals(other.idCinema)));
    }

    @Override
    public String toString() {
        return name;
    }

}
