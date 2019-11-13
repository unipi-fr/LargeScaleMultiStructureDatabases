package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.*;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBManager {
    private static EntityManagerFactory factory; 
    
    public static void start(){
        factory = Persistence.createEntityManagerFactory("PisaFlix");
    }
    
    public static void stop(){
        factory.close();
    }
    
    public static EntityManagerFactory getEntityManagerFactory(){
        if(factory == null){
            start();
        }
        return factory;
    }
    
    public static ICinemaManagerDB cm;
    public static IUserManagerDB um;
    public static IFilmManagerDB fm;
    public static IProjectionManagerDB pm;
    public static ICommentManagerDB com;
    
    static{
        cm = CinemaManager.getIstance();
        um = UserManager.getIstance();
        fm = FilmManager.getIstance();
        pm = ProjectionManager.getIstance();
        com = CommentManager.getIstance();
    }
    
}
