package com.lsmsdgroup.pisaflix;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class PisaFlix {

    public static void main(String[] args){
        LogManager.getLogManager().getLogger("").setLevel(Level.OFF);
        DBManager.setup();
        List<User> users = DBManager.UserManager.getAllUsers();
        
        for(User user: users)
        {
            System.out.println("Username: " + user.getUsername());
        }
        
        DBManager.exit();
    }
}
