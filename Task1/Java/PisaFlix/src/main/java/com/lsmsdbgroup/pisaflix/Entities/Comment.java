package com.lsmsdbgroup.pisaflix.Entities;

import com.lsmsdbgroup.pisaflix.DBManager;
import java.io.Serializable;
import java.util.Set;
import java.util.Date;
import java.util.LinkedHashSet;
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
    private User idUser;

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

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idComment != null ? idComment.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comment)) {
            return false;
        }
        Comment other = (Comment) object;
        if ((this.idComment == null && other.idComment != null) || (this.idComment != null && !this.idComment.equals(other.idComment))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[ idComment=" + idComment + " ]\n"+"timestamp:"+timestamp.toString()+"\ntext:"+text;
    }

    public static void createFilmComment(String text, User user, Film film) {
        DBManager.CommentManager.createFilmComment(text, user, film);
    }

    public static void createCinemaComment(String text, User user, Cinema cinema) {
        DBManager.CommentManager.createCinemaComment(text, user, cinema);
    }

    public static void update(int idComment, String text) {
        DBManager.CommentManager.update(idComment, text);
    }

    public void updateThis(String text) {
        DBManager.CommentManager.update(this.idComment, text);
    }

    public static void delete(int idComment) {
        DBManager.CommentManager.delete(idComment);
    }

    public void deleteThis() {
        DBManager.CommentManager.delete(this.idComment);
    }

    public static Comment getById(int commentId) {
        return DBManager.CommentManager.getById(commentId);
    }

}
