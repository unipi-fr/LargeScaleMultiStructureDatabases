package com.lsmsdbgroup.pisaflix.Entities;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class Post implements Serializable {
    private static long serialVersionUID = 1L;
    
    private Long idPost;
    
    private String text;
    private Date timestamp;
    
    private User user;
    private Set<Film> films = new LinkedHashSet<>();
    
    public Post(Long idPost, String text)
    {
        this.idPost = idPost;
        this.text = text;
    }
    
    public Post(Long idPost, String text, Date timestamp, User user, Set<Film> films){
        this.idPost = idPost;
        this.text = text;
        this.timestamp = timestamp;
        this.user = user;
        this.films = films;
    }

    public Post() {
        
    }
    
    public Long getIdPost() {
        return idPost;
    }

    public void setIdPost(Long idPost) {
        this.idPost = idPost;
    }
    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Film> getFilmSet() {
        return films;
    }

    public void setFilmSet(Set<Film> films) {
        this.films = films;
    }
    
    public void addFilm(Film film){
        films.add(film);
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPost != null ? idPost.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Post)) {
            return false;
        }
        Post other = (Post) object;
        return !((this.idPost == null && other.idPost != null) || (this.idPost != null && !this.idPost.equals(other.idPost)));
    }

    public Object getLastModified() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
