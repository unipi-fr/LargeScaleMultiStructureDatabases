package com.lsmsdgroup.pisaflix;

import java.util.logging.Level;
import java.util.logging.LogManager;

public class PisaFlix {

    public static void main(String[] args){
        LogManager.getLogManager().getLogger("").setLevel(Level.OFF);
        DBManager.setup();
        DBManager.UserManager.getAllUsers();
        DBManager.exit();
    }
}
