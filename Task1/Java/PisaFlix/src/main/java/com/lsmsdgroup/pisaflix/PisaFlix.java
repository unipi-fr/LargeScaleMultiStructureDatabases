package com.lsmsdgroup.pisaflix;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class PisaFlix {

    public static void main(String[] args){
        LogManager.getLogManager().getLogger("").setLevel(Level.OFF);
        
        DBManager.setup();
        DBManager.CommentManager.createFilmComment("Blablablablabla", DBManager.UserManager.getById(1), DBManager.FilmManager.getById(1));
        DBManager.CommentManager.createCinemaComment("Blablablablabla", DBManager.UserManager.getById(1), DBManager.CinemaManager.getById(1));
        DBManager.exit();     
    }
}
