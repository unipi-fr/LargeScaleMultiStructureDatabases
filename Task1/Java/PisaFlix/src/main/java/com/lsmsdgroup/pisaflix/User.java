package com.lsmsdgroup.pisaflix;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;

@Entity
@Table(name = "User")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByIdUser", query = "SELECT u FROM User u WHERE u.idUser = :idUser"),
    @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByFirstName", query = "SELECT u FROM User u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "User.findBySecondName", query = "SELECT u FROM User u WHERE u.secondName = :secondName"),
    @NamedQuery(name = "User.findByPrivilegeLevel", query = "SELECT u FROM User u WHERE u.privilegeLevel = :privilegeLevel")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idUser")
    private Integer idUser;
    @Basic(optional = false)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "secondName")
    private String secondName;
    @Basic(optional = false)
    @Column(name = "privilegeLevel")
    private int privilegeLevel;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<FilmComment> filmCommentCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<FilmRating> filmRatingCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<CinemaComment> cinemaCommentCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<CinemaRating> cinemaRatingCollection;

    public User() {
    }

    public User(Integer idUser) {
        this.idUser = idUser;
    }

    public User(Integer idUser, String username, String password, int privilegeLevel) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.privilegeLevel = privilegeLevel;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public int getPrivilegeLevel() {
        return privilegeLevel;
    }

    public void setPrivilegeLevel(int privilegeLevel) {
        this.privilegeLevel = privilegeLevel;
    }

    public Collection<FilmComment> getFilmCommentCollection() {
        return filmCommentCollection;
    }

    public void setFilmCommentCollection(Collection<FilmComment> filmCommentCollection) {
        this.filmCommentCollection = filmCommentCollection;
    }

    public Collection<FilmRating> getFilmRatingCollection() {
        return filmRatingCollection;
    }

    public void setFilmRatingCollection(Collection<FilmRating> filmRatingCollection) {
        this.filmRatingCollection = filmRatingCollection;
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
        hash += (idUser != null ? idUser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.idUser == null && other.idUser != null) || (this.idUser != null && !this.idUser.equals(other.idUser))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PisaFlix.User[ idUser=" + idUser + " ]";
    }
    
}
