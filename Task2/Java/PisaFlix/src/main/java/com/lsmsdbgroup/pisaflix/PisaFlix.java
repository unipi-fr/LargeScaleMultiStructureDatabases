package com.lsmsdbgroup.pisaflix;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.InvalidPrivilegeLevelException;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.UserNotLoggedException;
import com.lsmsdbgroup.pisaflixg.App;
import java.util.logging.*;

public class PisaFlix {

    public static void main(String[] args) throws UserNotLoggedException, InvalidPrivilegeLevelException {
        LogManager.getLogManager().getLogger("").setLevel(Level.OFF);
        PisaFlixServices.cinemaService.addCinema("Boh", "BohCity, bohStreet, n. 800");
        Cinema cinema = PisaFlixServices.cinemaService.getById("5de29ffba6a93710e6a6deff");
        System.out.println(cinema);
        cinema.setName("Barbabietole");
        PisaFlixServices.cinemaService.updateCinema(cinema);
        System.out.println(PisaFlixServices.cinemaService.getById("5de29ffba6a93710e6a6deff"));
        //PisaFlixServices.cinemaService.deleteCinema(cinema);
        //App.main(args);
        DBManager.stop();
    }
}
