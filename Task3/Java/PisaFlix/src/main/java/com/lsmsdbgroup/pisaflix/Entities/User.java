package com.lsmsdbgroup.pisaflix.Entities;

import java.io.Serializable;
import java.util.*;

public class User extends Entity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long idUser;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private int privilegeLevel;

    private Set<Film> filmSet = new LinkedHashSet<>();

    public User() {
    }

    public User(Long idUser) {
        this.idUser = idUser;
    }

    public User(Long idUser, String username, String password, int privilegeLevel) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.privilegeLevel = privilegeLevel;
    }
    
    public User(Long idUser, String email, String username, int privilegeLevel, String firstName, String lastName, String password) {
        this.idUser = idUser;
        this.email = email;
        this.username = username;
        this.privilegeLevel = privilegeLevel;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    @Override
    public Long getId() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
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

    public Set<Film> getFilmSet() {
        return filmSet;
    }

    public void setFilmSet(Set<Film> filmSet) {
        this.filmSet = filmSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUser != null ? idUser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        return !((this.idUser == null && other.idUser != null) || (this.idUser != null && !this.idUser.equals(other.idUser)));
    }

    @Override
    public String toString() {
        return "User[  idUser=" + idUser + " | Username=" + username + " | First Name=" + firstName + " | Last Name=" + lastName + " | Email=" + email + " | Privilege Level=" + privilegeLevel + "] ";
    }

}
