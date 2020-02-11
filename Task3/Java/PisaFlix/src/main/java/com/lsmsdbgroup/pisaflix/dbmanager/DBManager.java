package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.*;

public class DBManager {

    public static void start() {
    }

    public static void stop() {
    }

    public static void getMongoClient() {
    }

    public static void getDB() {
        /*if (MongoDatabase == null) {
            start();
        }
        return MongoDatabase;*/
    }

    public static UserManagerDatabaseInterface userManager;
    public static FilmManagerDatabaseInterface filmManager;

    static {
        filmManager = FilmManager.getIstance();
        userManager = UserManager.getIstance();
    }

}
