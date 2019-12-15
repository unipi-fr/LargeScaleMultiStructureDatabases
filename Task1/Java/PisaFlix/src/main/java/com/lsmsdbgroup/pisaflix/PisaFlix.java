package com.lsmsdbgroup.pisaflix;

import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflix.dbmanager.KeyValueDBManager;
import com.lsmsdbgroup.pisaflixg.App;
import java.text.ParseException;
import java.util.logging.*;


public class PisaFlix {

    public static void main(String[] args) throws ParseException {
        LogManager.getLogManager().getLogger("").setLevel(Level.OFF);
        
        App.main(args);
      
        DBManager.stop();
        KeyValueDBManager.stop();
    }
}
