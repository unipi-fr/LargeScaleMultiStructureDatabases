package com.lsmsdbgroup.pisaflix.Entities;

import java.io.Serializable;
import java.util.Date;

public class Post implements Serializable {
    private static long serialVersionUID = 1L;
    
    private String Text;
    private Date timestamp;
    
    private User user;
    private Film film;
    
    Post(String text, Date timestamp, User user, Film film){
        this.Text = text;
        this.timestamp = timestamp;
        this.user = user;
        this.film = film;
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
    
}
