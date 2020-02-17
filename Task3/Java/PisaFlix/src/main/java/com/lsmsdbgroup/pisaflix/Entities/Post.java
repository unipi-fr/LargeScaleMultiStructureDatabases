package com.lsmsdbgroup.pisaflix.Entities;

import java.io.Serializable;
import java.util.Date;

public class Post implements Serializable {
    private static long serialVersionUID = 1L;
    
    private Long idPost;
    
    private String Text;
    private Date timestamp;
    
    private User user;
    private Film film;
    
    Post(Long idPost, String text, Date timestamp, User user, Film film){
        this.idPost = idPost;
        this.Text = text;
        this.timestamp = timestamp;
        this.user = user;
        this.film = film;
    }
    
    public Long getIdPost() {
        return idPost;
    }

    public void setIdPost(Long idPost) {
        this.idPost = idPost;
    }
    
    public String getText() {
        return Text;
    }

    public void setText(String Text) {
        this.Text = Text;
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

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
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
}
