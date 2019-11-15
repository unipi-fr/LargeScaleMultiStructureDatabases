package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.*;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBManager {

    private static EntityManagerFactory factory;

    public static void start() {
        factory = Persistence.createEntityManagerFactory("PisaFlix");
    }

    public static void stop() {
        factory.close();
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (factory == null) {
            start();
        }
        return factory;
    }

    public static CinemaManagerDatabaseInterface cinemaMamager;
    public static UserManagerDatabaseInterface userManager;
    public static FilmManagerDatabaseInterface filmManager;
    public static ProjectionManagerDatabaseInterface projectionManager;
    public static CommentManagerDatabaseInterface commentManager;

    static {
        cinemaMamager = CinemaManager.getIstance();
        userManager = UserManager.getIstance();
        filmManager = FilmManager.getIstance();
        projectionManager = ProjectionManager.getIstance();
        commentManager = CommentManager.getIstance();
    }

}
