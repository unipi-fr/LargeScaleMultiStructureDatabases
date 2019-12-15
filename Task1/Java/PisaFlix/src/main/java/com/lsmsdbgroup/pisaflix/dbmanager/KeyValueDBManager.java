package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflix.Entities.Comment;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.Entities.Projection;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBException;
import static org.iq80.leveldb.impl.Iq80DBFactory.factory;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;
import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;

public class KeyValueDBManager {

    private static DB KeyValueDB;
    private static final Options options = new Options();
    
    DateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy HH.mm.ss");

    
    public static DB getKVFactory(){
        
        if(KeyValueDB == null){
            start();
        }
        return KeyValueDB;
    }
    
    
    public static void start() { 
        try {
            KeyValueDB = factory.open(new File("KeyValueDB"), options);
        } catch (IOException ex) {
            System.out.println("Errore non si è aperto il keyValueDB");
            Logger.getLogger(KeyValueDBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static void stop() {
            try {
                KeyValueDB.close();
            } catch (IOException ex) {
                Logger.getLogger(KeyValueDBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    
    protected void settings() {
        String value = get("settingsPresents");
        if (value == null || "false".equals(value)) {
            put("settingsPresents", "true");
            put("setting:lastCommentKey", "0");
            put("setting:lastProjectionKey", "0");
        }
    }

    
    protected void put(String key, String value) {
        getKVFactory().put(bytes(key), bytes(value));
    }

    
    protected void delete(String key) {
        getKVFactory().delete(bytes(key));
    }

    
    protected String get(String key) {
        byte[] value = getKVFactory().get(bytes(key));
        if (value != null) {
            return Iq80DBFactory.asString(value);
        } else {
            //System.out.println("Key not found");
            return null;
        }
    }

    
    // parte projection 

    public void createProjection(Date dateTime, int room, Cinema cinema, Film film){
        
        if(dateTime == null || cinema == null || film == null){
            System.err.println("Error: the data given to createProjection is invalid");
            return;
        }
        
        String s_room = String.valueOf(room);
        String s_date = dateFormat.format(dateTime);
        String s_cinema = cinema.getIdCinema().toString();
        String s_film = film.getIdFilm().toString();
        
        
        int idProjection = Integer.parseInt(get("setting:lastProjectionKey")) + 1;
        put("projection:" + idProjection + ":dateTime", s_date);
        put("projection:" + idProjection + ":room", s_room);
        put("projection:" + idProjection + ":cinema", s_cinema);
        put("projection:" + idProjection + ":film", s_film);
        
        
        String listaProjection = get("cinema:" + s_cinema + ":projections");
        
        if(listaProjection == null){// me la creo
            put("cinema:" + s_cinema + ":projections", String.valueOf(idProjection));
            put("setting:lastProjectionKey", String.valueOf(idProjection));
            return;
        }
        
        listaProjection = listaProjection.concat(":" + String.valueOf(idProjection));
        
        
        put("cinema:" + s_cinema + ":projections", listaProjection);
        put("setting:lastProjectionKey", String.valueOf(idProjection));
    }
    
    
    public void updateProjection(int projectionId, Date dateTime, int room){
        
        String oldRoom = get("projection:" + projectionId + ":room");
        String oldDate = get("projection:" + projectionId + ":dateTime");
        
        if(oldRoom == null || oldDate == null || dateTime == null){
            System.err.println("Error: the data given to updateProjection is invalid");
            return;
        }
        
        put("projection:" + projectionId + ":room", String.valueOf(room));
        put("projection:" + projectionId + ":dateTime", dateFormat.format(dateTime));
        
    }
    
    
    public void deleteProjection(int projectionId){
        
        String cinemaId = get("projection:" + projectionId + ":cinema");
        
        if(cinemaId == null){
            System.err.println("Error: impossible to delete projection: " + projectionId);
            return;
        }
        
         try{
            delete("projection:" + projectionId + ":dateTime");
            delete("projection:" + projectionId + ":room");
            delete("projection:" + projectionId + ":cinema");
            delete("projection:" + projectionId + ":film");
        }catch (DBException ex){
            System.err.println("Error: impossible to delete one or more tuple of "
                    + "projection: " + projectionId);
            return;
        }
        // prendo la lista di projection associata a quel cinema
        
        String oldProjectionString = get("cinema:" + cinemaId + ":projections");
        
        if(oldProjectionString == null){
        
            System.err.println("Error: impossible to retreive the list of projection "
                    + "of cinema: " + cinemaId);
            return;
        }
        // faccio la split
        
        String[] projectionArray = oldProjectionString.split(":");
        
        // controllo se ha lunghezza 1 -> butto via tutto 
        if(projectionArray.length == 1){
            delete("cinema:" + cinemaId + ":projections");
            return;
        }
        // altrimenti converto in una lista di stringhe, rimuovo l'id in questione, ricorstuisco la stringa da salvare
        
        List<String> listaProjections = new ArrayList<>(Arrays.asList(projectionArray));
        listaProjections.remove(String.valueOf(projectionId));
        
        String newProjectionString = "";
        for(int i=0; i<listaProjections.size();i++){
        
            if(i==0) newProjectionString = listaProjections.get(i);
            else newProjectionString = newProjectionString.concat(listaProjections.get(i));
        }        
                
        // aggiorno la lista.
        put("cinema:" + cinemaId + ":projections", newProjectionString);
    }
    

    public Projection getProjectionById(int projectionId){
        
        String s_room = get("projection:" + projectionId + ":room");
        String s_date = get("projection:" + projectionId + ":dateTime");
        String s_cinema = get("projection:" + projectionId + ":cinema");
        String s_film = get("projection:" + projectionId + ":film");
        
        if(s_room == null || s_date == null || s_cinema == null || s_film == null){
            System.err.println("Error: impossible to retreive projection");
            return null;
        }
        
        // recupero oggetto film, oggetto cinema
        Film film = PisaFlixServices.filmService.getById(Integer.parseInt(s_film));
        Cinema cinema = PisaFlixServices.cinemaService.getById(Integer.parseInt(s_cinema));
        Date date;
        try{
            date = dateFormat.parse(s_date);
        }catch(ParseException ex){ex.printStackTrace(System.out); return null;}
        
        // li controllo
        if(film == null || cinema == null){
            System.err.println("Error: impossible to retreive data");
            return null;
        }
        
        return new Projection(projectionId, date, Integer.parseInt(s_room), cinema, film);
    }
    
    // piccola funzioncina per mostrare la vera utilità della lista delle projection
    public String getProjectionsOfCinema(int cinemaId){
    
        return get("cinema:" + cinemaId + ":projections");
    }
    
    
    
}
