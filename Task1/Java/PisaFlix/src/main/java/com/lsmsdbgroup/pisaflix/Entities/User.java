package com.lsmsdbgroup.pisaflix.Entities;

//import com.lsmsdbgroup.pisaflix.DBManager;
import java.io.Serializable;
import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "User")
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

    @Column(name = "lastName")
    private String lastName;

    @Basic(optional = false)
    @Column(name = "privilegeLevel")
    private int privilegeLevel;

    @ManyToMany(mappedBy = "userSet", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Cinema> cinemaSet = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "userSet", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Film> filmSet = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idUser", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Comment> commentSet = new LinkedHashSet<>();

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getPrivilegeLevel() {
        return privilegeLevel;
    }

    public void setPrivilegeLevel(int privilegeLevel) {
        this.privilegeLevel = privilegeLevel;
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

    public Set<Comment> getCommentSet() {
        return commentSet;
    }

    public void setCommentSet(Set<Comment> commentSet) {
        this.commentSet = commentSet;
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
        return !((this.idUser == null && other.idUser != null) || (this.idUser != null && !this.idUser.equals(other.idUser)));
    }

    @Override
    public String toString() {
        return "\n User[  idUser=" + idUser + " | Username=" + username + " | First Name=" + firstName + " | Last Name=" + lastName + " | Email=" + email + " | Prvilege Level=" + privilegeLevel + "] ";
    }
    /*
    public static User getById(int userId) {
        return DBManager.UserManager.getById(userId);
    }

    public static void create(String username, String password, String firstName, String lastName, String email, int privilegeLevel) {
        DBManager.UserManager.create(username, password, firstName, lastName, email, privilegeLevel);
    }

    public static void delete(int userId) {
        DBManager.UserManager.delete(userId);
    }

    public void deleteThis() {
        delete(this.idUser);
    }

    public static void update(int userId, String username, String firstName, String lastName, String email, String password, int privilegeLevel) {
        DBManager.UserManager.update(userId, username, firstName, lastName, email, password, privilegeLevel);
    }

    public void updateThis(String username, String firstName, String lastName, String email, String password, int privilegeLevel) {
        DBManager.UserManager.update(this.idUser, username, firstName, lastName, email, password, privilegeLevel);
    }

    public static Set<User> getAll() {
        return DBManager.UserManager.getAll();
    }
    
    public void addFavouriteCinema(Cinema cinema) {
        cinemaSet.add(cinema);
        cinema.getUserSet().add(this);
        DBManager.UserManager.updateFavorites(this);
    }

    public void removeFavouriteCinema(Cinema cinema) {
        cinemaSet.remove(cinema);
        cinema.getUserSet().remove(this);
        DBManager.UserManager.updateFavorites(this);
        DBManager.CinemaManager.updateFavorites(cinema);
    }
    
    public void addFavouriteFilm(Film film) {
        filmSet.add(film);
        film.getUserSet().add(this);
        DBManager.UserManager.updateFavorites(this);
    }

    public void removeFavouriteFilm(Film film) {
        filmSet.remove(film);
        film.getUserSet().remove(this);
        DBManager.UserManager.updateFavorites(this);
        DBManager.FilmManager.updateFavorites(film);
    }
     */

}
