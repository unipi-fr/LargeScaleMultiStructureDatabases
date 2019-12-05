package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.*;
import com.mongodb.client.*;

public class DBManager {
    
    private static final MongoClient MongoClient = MongoClients.create("mongodb+srv://root:root@lsmsdcluster-yeauu.mongodb.net/test?retryWrites=true&w=majority");
    private static MongoDatabase MongoDatabase;

    public static void start() {
        MongoDatabase = MongoClient.getDatabase("PisaFlix");
    }

    public static void stop() {
        MongoClient.close();
    }
    
    public static MongoClient getMongoClient() {
        return MongoClient;
    }
    
    public static MongoDatabase getMongoDatabase() {
        if (MongoDatabase == null) {
            start();
        }
        return MongoDatabase;
    }

    public static CinemaManagerDatabaseInterface cinemaMamager;
    public static UserManagerDatabaseInterface userManager;
    public static FilmManagerDatabaseInterface filmManager;
    public static ProjectionManagerDatabaseInterface projectionManager;
    public static CommentManagerDatabaseInterface commentManager;

    static {
        cinemaMamager = CinemaManager.getIstance();
        filmManager = FilmManager.getIstance();
        commentManager = CommentManager.getIstance();
        userManager = UserManager.getIstance();
        System.out.println("RIMETTERE ROBA NEL DBMANAGER!!!!!!!!!!!!!!");
        // projectionManager = ProjectionManager.getIstance();
    }

}
