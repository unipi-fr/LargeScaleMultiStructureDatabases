package com.lsmsdbgroup.pisaflix.Entities;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "Comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idComment")
    private Integer idComment;

    @Basic(optional = false)
    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Basic(optional = false)
    @Lob
    @Column(name = "text")
    private String text;

    @JoinTable(name = "Cinema_has_Comment", joinColumns = {
        @JoinColumn(name = "idComment", referencedColumnName = "idComment")}, inverseJoinColumns = {
        @JoinColumn(name = "idCinema", referencedColumnName = "idCinema")})
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Cinema> cinemaSet = new LinkedHashSet<>();

    @JoinTable(name = "Film_has_Comment", joinColumns = {
        @JoinColumn(name = "idComment", referencedColumnName = "idComment")}, inverseJoinColumns = {
        @JoinColumn(name = "idFilm", referencedColumnName = "idFilm")})
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Film> filmSet = new LinkedHashSet<>();

    @JoinColumn(name = "idUser", referencedColumnName = "idUser")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public Comment() {
    }

    public Comment(Integer idComment) {
        this.idComment = idComment;
    }

    public Comment(Integer idComment, Date timestamp, String text) {
        this.idComment = idComment;
        this.timestamp = timestamp;
        this.text = text;
    }

    public Comment(Integer idComment, User user, Film film, String text, Date timestamp) {

        this.idComment = idComment;
        this.timestamp = timestamp;
        this.text = text;
        this.user = user;
        filmSet.add(film);

    }

    public Comment(Integer idComment, User user, Cinema cinema, String text, Date timestamp) {

        this.idComment = idComment;
        this.timestamp = timestamp;
        this.text = text;
        this.user = user;
        cinemaSet.add(cinema);
    }

    public Integer getIdComment() {
        return idComment;
    }

    public void setIdComment(Integer idComment) {
        this.idComment = idComment;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<Cinema> getCinemaSet() {
        return cinemaSet;
    }

    public void setCinemaSet(Set<Cinema> cinemaSet) {
        this.cinemaSet = cinemaSet;
    }

    public Set<Film> getFilmSet() {
        return filmSet;
    }

    public void setFilmSet(Set<Film> filmSet) {
        this.filmSet = filmSet;
    }

    public User getUser() {
        if (user == null) {
            User u = new User();
            u.setIdUser(0);
            u.setUsername("Deleted User");
            return u;
        }
        return user;
    }

    public void setUser(User idUser) {
        this.user = idUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idComment != null ? idComment.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Comment)) {
            return false;
        }
        Comment other = (Comment) object;
        return !((this.idComment == null && other.idComment != null) || (this.idComment != null && !this.idComment.equals(other.idComment)));
    }

    @Override
    public String toString() {

        if (!filmSet.isEmpty()) {
            return "[ idComment= " + idComment + " ]\nuser: " + user.toString()
                    + "\ntimestamp:" + timestamp.toString() + "\ntext:" + text
                    + "\nfilm: " + filmSet.toString();
        }

        return "[ idComment= " + idComment + " ]\nuser: " + user.toString()
                + "\ntimestamp:" + timestamp.toString() + "\ntext:" + text
                + "\ncinema: " + cinemaSet.toString();
    }

}
