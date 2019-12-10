package com.lsmsdbgroup.pisaflix.Entities;

import com.lsmsdbgroup.pisaflix.Entities.exceptions.NonConvertibleDocumentException;
import java.io.Serializable;
import java.util.*;
import org.bson.Document;

public class Projection extends Entity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String idProjection;
    private Date dateTime;
    private int room;
    private Cinema cinema;
    private Film film;

    public Projection() {
    }

    public Projection(String idProjection) {
        this.idProjection = idProjection;
    }

    public Projection(String idProjection, Date dateTime, int room) {
        this.idProjection = idProjection;
        this.dateTime = dateTime;
        this.room = room;
    }

    public Projection(String idProjection, Date dateTime, int room, Cinema cinema, Film film) {
        this(idProjection, dateTime, room);
        this.cinema = cinema;
        this.film = film;
    }

    @Override
    public String getId() {
        return idProjection;
    }

    public void setIdProjection(String idProjection) {
        this.idProjection = idProjection;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public Cinema getIdCinema() {
        return cinema;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setIdCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public Film getIdFilm() {
        return film;
    }

    public Film getFilm() {
        return film;
    }

    public void setIdFilm(Film film) {
        this.film = film;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProjection != null ? idProjection.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Projection)) {
            return false;
        }
        Projection other = (Projection) object;
        return !((this.idProjection == null && other.idProjection != null) || (this.idProjection != null && !this.idProjection.equals(other.idProjection)));
    }

    @Override
    public String toString() {
        return "idProjection=" + idProjection + " ]\ndateTime:"
                + dateTime.toString() + "\nroom:" + room + "\ncinema:"
                + cinema.toString() + "\nfilm:" + film.toString();
    }

}
