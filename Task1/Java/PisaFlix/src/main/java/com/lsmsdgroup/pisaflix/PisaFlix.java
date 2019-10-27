package com.lsmsdgroup.pisaflix;

import com.lsmsdgroup.pisaflix.Entities.User;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class PisaFlix {

    public static void main(String[] args){
        LogManager.getLogManager().getLogger("").setLevel(Level.OFF);
        DBManager.setup();
        /*DBManager.FilmManager.create("Prova", new Date(), "blablablabla");
        DBManager.CinemaManager.create("Prova", "blabla street, blablacity, blablaland");
        DBManager.UserManager.create("Prova", "12345", null, null, null, 0);
        DBManager.CommentManager.createFilmComment("Blablablablabla", DBManager.UserManager.getById(1), DBManager.FilmManager.getById(1));
        DBManager.CommentManager.createCinemaComment("Blablablablabla", DBManager.UserManager.getById(1), DBManager.CinemaManager.getById(1));
        DBManager.ProjectionManager.create(new Date(), 2, DBManager.FilmManager.getById(1), DBManager.CinemaManager.getById(1));
        DBManager.UserManager.getById(1).addFavouriteCinema(DBManager.CinemaManager.getById(1));
        DBManager.UserManager.getById(1).addFavouriteFilm(DBManager.FilmManager.getById(1));*/
        //DBManager.UserManager.delete(1);
        //DBManager.FilmManager.delete(1);
        //DBManager.CinemaManager.delete(1);
        //User user = User.getById(1);
        //user.removeFavouriteFilm(DBManager.FilmManager.getById(1));
        try {
            PisaFlixServices.Authentication.Login("fraronk", "wrong");
        } catch (PisaFlixServices.Authentication.UserAlredyLoggedException | PisaFlixServices.Authentication.InvalidCredentialsException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            PisaFlixServices.Authentication.Login("fraronk", "1234");
        } catch (PisaFlixServices.Authentication.UserAlredyLoggedException | PisaFlixServices.Authentication.InvalidCredentialsException ex) {
            System.out.println(ex.getMessage());
        }
        
        System.out.println(PisaFlixServices.Authentication.getInfoString());
        
        DBManager.exit();     
    }
}
