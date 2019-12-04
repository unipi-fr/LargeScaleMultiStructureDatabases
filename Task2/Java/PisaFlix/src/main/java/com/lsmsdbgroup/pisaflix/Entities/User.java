package com.lsmsdbgroup.pisaflix.Entities;

import com.lsmsdbgroup.pisaflix.Entities.exceptions.NonConvertibleDocumentException;
import java.io.Serializable;
import java.util.*;
import org.bson.Document;

public class User extends Entity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String idUser;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private int privilegeLevel;

    private Set<Cinema> cinemaSet = new LinkedHashSet<>();
    private Set<Film> filmSet = new LinkedHashSet<>();
    private Set<Comment> commentSet = new LinkedHashSet<>();

    public User() {
    }

    public User(String idUser) {
        this.idUser = idUser;
    }

    public User(String idUser, String username, String password, int privilegeLevel) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.privilegeLevel = privilegeLevel;
    }
    
    public User(Document userDocument) {     
        if(userDocument.containsKey("_id") && userDocument.containsKey("Username") && userDocument.containsKey("Password") && userDocument.containsKey("Email") && userDocument.containsKey("PrivilegeLevel") ){
            this.idUser = userDocument.get("_id").toString();
            this.username = userDocument.getString("Username");
            this.password = userDocument.getString("Password");
            this.email = userDocument.getString("Email");
            this.privilegeLevel = userDocument.getInteger("PrivilegeLevel");
            if(userDocument.containsKey("FirstName")){
                this.firstName = userDocument.getString("FirstName");
            }
            if(userDocument.containsKey("LastName")){
                this.lastName = userDocument.getString("LastName");
            }
        }else{
            try {
                throw new NonConvertibleDocumentException("Document not-convertible in user");
            } catch (NonConvertibleDocumentException ex) {
                System.out.println(ex.getMessage());
            }
        }      
    }

    @Override
    public String getId() {
        return idUser;
    }

    public void setIdUser(String idUser) {
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
        return "\n User[  idUser=" + idUser + " | Username=" + username + " | First Name=" + firstName + " | Last Name=" + lastName + " | Email=" + email + " | Privilege Level=" + privilegeLevel + "] ";
    }

}
