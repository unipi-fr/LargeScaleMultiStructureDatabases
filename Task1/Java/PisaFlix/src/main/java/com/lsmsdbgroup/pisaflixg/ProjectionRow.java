package com.lsmsdbgroup.pisaflixg;

import javafx.beans.property.SimpleStringProperty;


public class ProjectionRow {
    private SimpleStringProperty dateTimeProperty = new SimpleStringProperty("");
    private SimpleStringProperty roomProperty = new SimpleStringProperty("");
    private SimpleStringProperty cinemaProperty = new SimpleStringProperty("");
    private SimpleStringProperty filmProperty = new SimpleStringProperty("");
    
    public ProjectionRow()
    {
        this("", "", "", "");
    }
    
    public ProjectionRow(String dateTime, String room, String cinema, String film){
        dateTimeProperty.set(dateTime);
        roomProperty.set(room);
        cinemaProperty.set(cinema);
        filmProperty.set(film);
    }

    public String getDateTime() {
        return dateTimeProperty.get();
    }

    public void setDateTime(String dateTime) {
        dateTimeProperty.set(dateTime);
    }

    public String getRoom() {
        return roomProperty.get();
    }

    public void setRoom(String room) {
        roomProperty.set(room);
    }

    public String getCinema() {
        return cinemaProperty.get();
    }

    public void setCinema(String cinema) {
        cinemaProperty.set(cinema);
    }

    public String getFilm() {
        return filmProperty.get();
    }

    public void setFilm(String film) {
        filmProperty.set(film);
    }
    
    
}
