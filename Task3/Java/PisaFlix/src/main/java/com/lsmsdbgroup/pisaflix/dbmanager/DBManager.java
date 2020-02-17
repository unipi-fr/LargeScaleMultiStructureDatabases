package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.*;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

public class DBManager{
    
    private static Driver driver;

    public static void start() {
        driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("root", "root"));
    }

    public static void stop() {
        driver.close();
    }

    public static Driver getDB() {
        if (driver == null) {
            start();
        }
        
        return driver;
    }

    public static UserManagerDatabaseInterface userManager;
    public static FilmManagerDatabaseInterface filmManager;
    public static PostManagerDatabaseInterface postManager;

    static {
        filmManager = FilmManager.getIstance();
        userManager = UserManager.getIstance();
        postManager = PostManager.getIstance();
    }
}
