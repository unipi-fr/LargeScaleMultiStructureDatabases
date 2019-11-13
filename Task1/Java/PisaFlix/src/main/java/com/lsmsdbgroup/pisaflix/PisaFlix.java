package com.lsmsdbgroup.pisaflix;

import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflixg.App;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PisaFlix {
    
    
    private static final Scanner S = new Scanner(System.in);
    

    public static void main(String[] args){
        LogManager.getLogManager().getLogger("").setLevel(Level.OFF);
        //mainMenu();
        DBManager.start();
        App.main(args);
        DBManager.stop();
        
       /* 
        KeyValueDBManager db = new KeyValueDBManager();
        db.start();
       
        db.createProjection(new Date(), 0, new Cinema(1), new Film(1));
        db.createProjection(new Date(), 0, new Cinema(1), new Film(2));
        db.createProjection(new Date(), 0, new Cinema(2), new Film(1));
        db.createProjection(new Date(), 0, new Cinema(2), new Film(2));
        
        
        //db.updateProjection(1, new Date(), 69);

        //db.deleteProjection(5);
        //db.deleteProjection(6);
        //db.createProjection(new Date(), 0, new Cinema(1), new Film(1));
        System.out.println(db.getProjectionsOfCinema(1));
        System.out.println(db.getProjectionsOfCinema(2));
        
        Projection p;
        for(int i=1; i<10; i++){
        p = db.getProjectionById(i);
        
        if(p == null) continue;
        System.out.println(p.toString());
        
        }
        
        db.stop();
        */
    }
}
