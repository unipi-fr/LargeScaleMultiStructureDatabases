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
    private Set<Cinema> idCinema = new LinkedHashSet<>();
    // it is necessary to use a Set to allow Hibernate to map the “cinema_has_comment”                                                      
    //   table, but it always contains at most one value (none if the comment refers to 
    //   a film)


    @JoinTable(name = "Film_has_Comment", joinColumns = {
        @JoinColumn(name = "idComment", referencedColumnName = "idComment")}, inverseJoinColumns = {
        @JoinColumn(name = "idFilm", referencedColumnName = "idFilm")})
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Film> idFilm = new LinkedHashSet<>();
    // it is necessary to use a Set to allow Hibernate to map the “film_has_comment”                                                      
    //   table, but it always contains at most one value (none if the comment refers to 
    //   a cinema)


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
        this.idFilm.add(film);

    }

    public Comment(Integer idComment, User user, Cinema cinema, String text, Date timestamp) {

        this.idComment = idComment;
        this.timestamp = timestamp;
        this.text = text;
        this.user = user;
        this.idCinema.add(cinema);
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

    public Set<Cinema> getIdCinema() {
        return idCinema;
    }

    public void setIdCinema(Set<Cinema> idCinema) {
        this.idCinema = idCinema;
    }

    public Set<Film> getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(Set<Film> idFilm) {
        this.idFilm = idFilm;
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

        if (!idFilm.isEmpty()) {
            return "[ idComment= " + idComment + " ]\nuser: " + user.toString()
                    + "\ntimestamp:" + timestamp.toString() + "\ntext:" + text
                    + "\nfilm: " + idFilm.toString();
        }

        return "[ idComment= " + idComment + " ]\nuser: " + user.toString()
                + "\ntimestamp:" + timestamp.toString() + "\ntext:" + text
                + "\ncinema: " + idCinema.toString();

    }

}
