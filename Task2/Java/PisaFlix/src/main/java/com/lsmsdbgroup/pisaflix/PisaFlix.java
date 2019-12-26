package com.lsmsdbgroup.pisaflix;

import com.lsmsdbgroup.pisaflix.Entities.Comment;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflixg.App;
import java.util.Date;
import java.util.logging.*;

public class PisaFlix {

    public static void main(String[] args) {
        LogManager.getLogManager().getLogger("").setLevel(Level.OFF);
        DBManager.filmManager.addComment(new Film("5e0493c29d932978bebdff39"), new Comment("5de7a035a6a93710e6a6fabc"));
        App.main(args);
        DBManager.stop();
    }
}
