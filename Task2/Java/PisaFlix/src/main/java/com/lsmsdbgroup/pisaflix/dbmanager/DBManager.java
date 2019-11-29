package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.*;

public class DBManager {

    public static void start() {
        throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
    }

    public static void stop() {
        throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
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
