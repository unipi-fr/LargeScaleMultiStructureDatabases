package com.lsmsdbgroup.pisaflix;

import com.lsmsdbgroup.pisaflix.Entities.Comment;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflixg.App;
import java.util.Date;
import java.util.logging.*;

public class PisaFlix {

    public static void main(String[] args) {
        LogManager.getLogManager().getLogger("").setLevel(Level.OFF);
        Film film = DBManager.filmManager.getById("5e04fad39d932978bed82a69");
        DBManager.filmManager.addComment(film, new User("5de93b61322cfc28df972da9"), "commento");
        DBManager.filmManager.getRecentComment(film);
        for(Comment comment : film.getCommentSet()){
            System.out.println(comment);
        }
        App.main(args);
        DBManager.stop();
    }
}
