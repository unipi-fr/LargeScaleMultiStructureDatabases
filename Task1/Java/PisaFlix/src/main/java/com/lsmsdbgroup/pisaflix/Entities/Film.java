package com.lsmsdbgroup.pisaflix.Entities;

//import com.lsmsdbgroup.pisaflix.DBManager;
import java.io.Serializable;
import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "Film")
public class Film implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idFilm")
    private Integer idFilm;

    @Basic(optional = false)
    @Column(name = "title")
    private String title;

    @Basic(optional = false)
    @Column(name = "publicationDate")
    @Temporal(TemporalType.DATE)
    private Date publicationDate;

    @Lob
    @Column(name = "description")
    private String description;

    @JoinTable(name = "Favorite_Film", joinColumns = {
        @JoinColumn(name = "idFilm", referencedColumnName = "idFilm")}, inverseJoinColumns = {
        @JoinColumn(name = "idUser", referencedColumnName = "idUser")})
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> userSet = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "filmSet", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Comment> commentSet = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idFilm", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Projection> projectionSet = new LinkedHashSet<>();

    public Film() {
    }

    public Film(Integer idFilm) {
        this.idFilm = idFilm;
    }

    public Film(Integer idFilm, String title, Date publicationDate) {
        this.idFilm = idFilm;
        this.title = title;
        this.publicationDate = publicationDate;
    }

    public Integer getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(Integer idFilm) {
        this.idFilm = idFilm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        hash += (idFilm != null ? idFilm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Film)) {
            return false;
        }
        Film other = (Film) object;
        return !((this.idFilm == null && other.idFilm != null) || (this.idFilm != null && !this.idFilm.equals(other.idFilm)));
    }

    @Override
    public String toString() {
        return title;
    }
    /*
    public static Film getById(int filmId) {
        return DBManager.FilmManager.getById(filmId);
    }

    public static Set<Film> getAll() {
        return DBManager.FilmManager.getAll();
    }

    public static void create(String title, Date publicationDate, String description) {
        DBManager.FilmManager.create(title, publicationDate, description);
    }

    public static void update(int idFilm, String title, Date publicationDate, String description) {
        DBManager.FilmManager.update(idFilm, title, publicationDate, description);
    }

    public void updateThis(String title, Date publicationDate, String description) {
        DBManager.FilmManager.update(this.idFilm, title, publicationDate, description);
    }

    public static void delete(int idFilm) {
        DBManager.FilmManager.delete(idFilm);
    }

    public void deleteThis() {
        DBManager.FilmManager.delete(this.idFilm);
    }
     */
}
