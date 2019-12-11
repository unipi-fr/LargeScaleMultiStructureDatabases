package com.lsmsdbgroup.pisaflix;

import com.lsmsdbgroup.pisaflix.Entities.Comment;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflix.dbmanager.KeyValueDBManager;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflixg.App;
import java.util.Set;
import java.util.logging.*;


public class PisaFlix {

    public static void main(String[] args) {
        LogManager.getLogManager().getLogger("").setLevel(Level.OFF);
        App.main(args);
        
        /*
        User u = PisaFlixServices.userService.getUserById(3);
        
        System.out.println(u.toString());
        
        Film f = PisaFlixServices.filmService.getById(2);
        
        PisaFlixServices.commentService.addFilmComment("ARGGGGHHHHH!!!!",u , f);
        
        Set<Film> films = PisaFlixServices.filmService.getAll();
        
        for(Film film: films){
        
            System.out.println(film.toString());
            Set<Comment> commenti = film.getCommentSet();
            for(Comment comment: commenti){
                
                System.out.println(comment.toString());
            }
            
        }
        */
        
        DBManager.stop();
        KeyValueDBManager.stop();
    }
}
