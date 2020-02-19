package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.*;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.FilmManagerDatabaseInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.DateConverter;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;
import static org.neo4j.driver.v1.Values.parameters;

public class FilmManager implements FilmManagerDatabaseInterface {

    private static FilmManager FilmManager;
    private final Driver driver;

    private final int limit = 27;
    
    public static FilmManager getIstance() {
        if (FilmManager == null) {
            FilmManager = new FilmManager();
        }
        
        return FilmManager;
    }

    private FilmManager() {
        driver = DBManager.getDB();
    }
    
    private Film getFilmFromRecord(Record record){
        Value value = record.get("n");
        Long id = value.asNode().id();

        String title = value.get("Title").asString();
        String publicationDateStr = value.get("PublicationDate").asString();
        String wikiPage = value.get("WikiPage").asString();

        Date publicationDate = DateConverter.StringToDate(publicationDateStr);
        return new Film(id, title, publicationDate, wikiPage);
    }

    @Override
    public Film getById(Long filmId) {
        Film film = null;
        
        try(Session session = driver.session())
        {
            StatementResult result = session.run("MATCH (n:Film) WHERE ID(n) = $id RETURN n", parameters("id", filmId));
            
            while(result.hasNext()){
                Record record = result.next();
                
                film = getFilmFromRecord(record);
            }
        }
        
        return film;
    }

    @Override
    public Set<Film> getAll() {
        Set<Film> filmSet = new LinkedHashSet<>();
        
        try(Session session = driver.session())
        {
            StatementResult result = session.run("MATCH (n:Film) RETURN n LIMIT " + limit);
            
            while(result.hasNext()){
                Record record = result.next();
                
                Film film = getFilmFromRecord(record);
                
                filmSet.add(film);
            }
        }
        
        return filmSet;
    }

    @Override
    public boolean create(String title, Date publicationDate) {
        boolean success = false;
        
        try(Session session = driver.session())
        {
            session.run("CREATE (f: Film {Title: $title, PublicationDate: $publicationDate})", parameters("title", title, "publicationDate", publicationDate.toString()));
            success = true;
        }
        
        return success;
    }

    @Override
    public void update(Long filmId, String title, Date publicationDate) {
        try(Session session = driver.session())
        {
            session.run("MATCH (f:Film) "
                    + "WHERE ID(f) = $id"
                    + "SET f.Title = $title, f.PublicationDate = $publicationDate "
                    + "RETURN f", 
                    parameters("id", filmId,
                                "title", title, 
                                "publicationDate", publicationDate.toString()));   
        }
    }

    @Override
    public void delete(Long filmId) {
        try(Session session = driver.session())
        {
            session.run("MATCH (n:Film) WHERE ID(n) = $id DELETE n", parameters("id", filmId));
        }
    }

    @Override
    public Set<Film> getFiltered(String titleFilter, Date startDateFilter, Date endDateFilter, int limit) {
        Set<Film> filmSet = new LinkedHashSet<>();
        
        int queryLimit = 0;
        if(limit == 0){
            queryLimit = this.limit;
        }else{
            queryLimit = limit;
        }
        
        try(Session session = driver.session()){
            
            StatementResult result = null;
            
            if(startDateFilter == null || endDateFilter == null){
                result = session.run("MATCH (n:Film) "
                                               + "WHERE n.Title =~ '.*"+titleFilter+".*' "
                                               + "RETURN n LIMIT " + queryLimit);
            }else{
                if(titleFilter == null){
                    result = session.run("MATCH (n:Film) "
                                               + "AND n.PublicationDate <= "+endDateFilter+" "
                                               + "AND n.PublicationDate >= "+startDateFilter+" "
                                               + "RETURN n LIMIT " + queryLimit);
                }else{
                    result = session.run("MATCH (n:Film) "
                                               + "WHERE n.Title =~ '.*"+titleFilter+".*' "
                                               + "AND n.PublicationDate <= "+endDateFilter+" "
                                               + "AND n.PublicationDate >= "+startDateFilter+" "
                                               + "RETURN n LIMIT " + queryLimit);
                } 
            }
            
            while(result.hasNext()){
                Record record = result.next();
                
                Film film = getFilmFromRecord(record);
                
                filmSet.add(film);
            }
        }
        
        return filmSet;
    }
    
    @Override
    public void follow(Film film, User user) {
        
        try(Session session = driver.session()){
            session.run("MATCH (u:User),(f:Film) " +
                        "WHERE ID(u) = "+user.getId()+" AND ID(f) = "+film.getId()+" " +
                        "CREATE (u)-[r:FOLLOWS]->(f) " +
                        "RETURN r");

        }
        
    }
}
