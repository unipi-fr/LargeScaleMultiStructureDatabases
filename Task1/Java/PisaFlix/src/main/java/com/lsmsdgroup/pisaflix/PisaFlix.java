package com.lsmsdgroup.pisaflix;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class PisaFlix {

    public static void main(String[] args){
        LogManager.getLogManager().getLogger("").setLevel(Level.OFF);
        
        DBManager.setup();
        DBManager.FilmManager.delete(9);
        //DBManager.UserManager.getAllUsers();
        DBManager.exit();
        //List<Film> films = DBManager.FilmManager.getAll();
        /*Film film = DBManager.FilmManager.getById(1);
        DBManager.exit();
        
        for(User user: film.getUserCollection())
        {
            System.out.println(user.toString());
        }
        
        /*for(Film film: films)
        {
            System.out.println(film.toString());
        }*/
       
    }
}
