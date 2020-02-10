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

    public static UserManagerDatabaseInterface userManager;
    public static FilmManagerDatabaseInterface filmManager;
    public static CommentManagerDatabaseInterface commentManager;
    public static EngageManagerDatabaseInterface engageManager;
    public static AnalyticsManagerDatabaseInterface analyticsManager;

    static {
        filmManager = FilmManager.getIstance();
        commentManager = CommentManager.getIstance();
        userManager = UserManager.getIstance();
        engageManager = EngageManager.getIstance();
        analyticsManager = AnalyticsManager.getIstance();
    }

}
