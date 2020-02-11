package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.*;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.FilmManagerDatabaseInterface;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

public class FilmManager implements FilmManagerDatabaseInterface {

    private static FilmManager FilmManager;
    private final Driver driver;

    public static FilmManager getIstance() {
        if (FilmManager == null) {
            FilmManager = new FilmManager();
        }
        
        return FilmManager;
    }

    private FilmManager() {
        driver = DBManager.getDB();
    }

    @Override
    public Film getById(String filmId) {
        Film film = null;
        
        return film;
    }

    @Override
    public Set<Film> getAll() {
        Set<Film> filmSet = new LinkedHashSet<>();
        
        try(Session session = driver.session())
        {
            StatementResult result = session.run("MATCH (n:Film) RETURN n");
            
            while(result.hasNext()){
                Record record = result.next();
                var varrr = record.toString();
                var aaaa= record.asMap();
                var title = record.get(0);
                var bbbb= title.get("Title");
               var cccc = title.get(0); 
                int a = 5;
            }
        }
        return filmSet;
    }

    @Override
    public boolean create(String title, Date publicationDate) {
        boolean success = false;
        
        return success;
    }

    @Override
    public void update(String idFilm, String title, Date publicationDate) {
        
    }

    @Override
    public void delete(String idFilm) {
        
    }

    @Override
    public Set<Film> getFiltered(String titleFilter, Date startDateFilter, Date endDateFiltern) {
        Set<Film> filmSet = new LinkedHashSet<>();
        
        filmSet = getAll();
        
        return filmSet;
    }
}
