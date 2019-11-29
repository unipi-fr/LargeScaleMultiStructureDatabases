package com.lsmsdbgroup.pisaflix;

import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.InvalidPrivilegeLevelException;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.UserNotLoggedException;
import com.lsmsdbgroup.pisaflixg.App;
import java.util.logging.*;

public class PisaFlix {

    public static void main(String[] args) throws UserNotLoggedException, InvalidPrivilegeLevelException {
        //LogManager.getLogManager().getLogger("").setLevel(Level.OFF);
        PisaFlixServices.cinemaService.addCinema("Boh", "BohCity, bhStreet, n. 800");
        //App.main(args);
        //DBManager.stop();
    }
}
