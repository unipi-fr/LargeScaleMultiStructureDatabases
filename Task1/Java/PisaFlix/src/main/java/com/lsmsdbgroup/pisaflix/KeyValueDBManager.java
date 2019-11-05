package com.lsmsdbgroup.pisaflix;

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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iq80.leveldb.DB;
import static org.iq80.leveldb.impl.Iq80DBFactory.factory;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;
import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;

public class KeyValueDBManager {

    DB KeyValueDB;
    Options options = new Options();
    
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY HH.mm.ss");

    
    public void start() { //Sono obbligato a metterle non statiche
        try {
            KeyValueDB = factory.open(new File("KeyValueDB"), options);
        } catch (IOException ex) {
            Logger.getLogger(KeyValueDBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.settings();
    }

    
    public void settings() {
        String value = get("settingsPresents");
        if (value == null || "false".equals(value)) {
            put("settingsPresents", "true");
            put("setting:lastCommentKey", "0");
            put("setting:lastProjectionKey", "0");
        }
    }

    
    public void stop() {
        try {
            KeyValueDB.close();
        } catch (IOException ex) {
            Logger.getLogger(KeyValueDBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public void put(String key, String value) {
        KeyValueDB.put(bytes(key), bytes(value));
    }

    
    public void delete(String key) {
        KeyValueDB.delete(bytes(key));
    }

    
    public String get(String key) {
        byte[] value = KeyValueDB.get(bytes(key));
        if (value != null) {
            return Iq80DBFactory.asString(value);
        } else {
            System.out.println("Key not found");
            return null;
        }
    }

    
    public void createFilmComment(String text, User user, Film film) {
        
        // controllo dati inseriti
        if(text == null || user == null || film == null){
            System.err.println("the data given to createFilmComment is not valid");
            return;
        }
        
        int idComment = Integer.parseInt(get("setting:lastCommentKey")) + 1;
       
        // la chiave per accedere ai vari campi è, ad es "comment:1:user"
        put("comment:" + idComment + ":" + "user", user.getIdUser().toString());
        put("comment:" + idComment + ":" + "film", film.getIdFilm().toString());
        put("comment:" + idComment + ":" + "text", text);
        put("comment:" + idComment + ":" + "timestamp", dateFormat.format(new Date()));
        
        String listaIdCommenti = get("film:" + film.getIdFilm().toString() + "comments");
        
        if(listaIdCommenti == null){ // non esiste, me la creo
            put("film:" + film.getIdFilm().toString() + "comments", String.valueOf(idComment));
            return;
        }
        
        // altrimenti mi limito ad appendere ":idComment"
        listaIdCommenti = listaIdCommenti.concat(":" + String.valueOf(idComment));
        delete("film:" + film.getIdFilm().toString() + "comments");
        put("film:" + film.getIdFilm().toString() + "comments", listaIdCommenti);
        
        put("setting:lastCommentKey", String.valueOf(idComment));
    }

    // da fare
    public void createCinemaComment(String text, User user, Cinema cinema) {
        
        
        /*
            int idComment = Integer.parseInt(get("setting:lastCommentKey")) + 1;
            put("comment:" + String.valueOf(idComment), "user:" + user.getIdUser().toString() + ":cinema:" + cinema.getIdCinema().toString() + ":text:" + text + ":timestamp:" + dateFormat.format(new Date()));
            put("setting:lastCommentKey", String.valueOf(idComment));
        
        */
        /*
          // controllo dati inseriti
        if(text == null || user == null || film == null){
            System.err.println("the data given to createFilmComment is not valid");
            return;
        }
        
        int idComment = Integer.parseInt(get("setting:lastCommentKey")) + 1;
       
        // la chiave per accedere ai vari campi è, ad es "comment:1:user"
        put("comment:" + idComment + ":" + "user", user.getIdUser().toString());
        put("comment:" + idComment + ":" + "film", film.getIdFilm().toString());
        put("comment:" + idComment + ":" + "text", text);
        put("comment:" + idComment + ":" + "timestamp", dateFormat.format(new Date()));
        
        String listaIdCommenti = get("film:" + film.getIdFilm().toString() + "comments");
        
        if(listaIdCommenti == null){ // non esiste, me la creo
            put("film:" + film.getIdFilm().toString() + "comments", String.valueOf(idComment));
            return;
        }
        
        // altrimenti mi limito ad appendere ":idComment"
        listaIdCommenti = listaIdCommenti.concat(":" + String.valueOf(idComment));
        delete("film:" + film.getIdFilm().toString() + "comments");
        put("film:" + film.getIdFilm().toString() + "comments", listaIdCommenti);
        
        put("setting:lastCommentKey", String.valueOf(idComment));
        */
    }
    
    
    public void updateComment(int commentId, String text) {
        
        String oldText = get("comment:" + commentId + ":" + "text");
        
        if(text == null || oldText == null) {
            System.out.println("Comment: " + commentId + " not updated");
            return;
        }
        
        delete("comment:" + commentId + ":" + "text");
        put("comment:" + commentId + ":" + "text", text);
    }

    // da fare
    public void deleteComment(int commentId) {
        
        // controllare che il commento esista, cancella tutte le tuple,
        // prendi l'indice dei commenti associati film e cancella quello 
        // del commento eliminato (ovviamente reinserisci il nuovo indice)
    }

    // da fare
    public Comment getCommentById(int commentId) {
        String value = get(String.valueOf("comment:" + commentId));
        if(value == null) return null;
        
        String[] field = value.split(":");
        try {
            return new Comment(commentId, dateFormat.parse(field[7]), field[5]);
        } catch (ParseException ex) {
            Logger.getLogger(KeyValueDBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    // parte projection 
    
    // da fare
    public void createProjection(Date dateTime, int room, Cinema cinema, Film film){
        int idProjection = Integer.parseInt(get("setting:lastProjectionKey")) + 1;
        put("projection:" + String.valueOf(idProjection), "dateTime:" + dateFormat.format(dateTime) + ":room:" + String.valueOf(room) + ":cinema:" + cinema.getIdCinema().toString() + ":film:" + film.getIdFilm().toString());
        put("setting:lastProjectionKey", String.valueOf(idProjection));
    
    }
    
    // da fare
    public void updateProjection(int projectionId, Date dateTime, int room){
    
        Projection projection = getProjectionById(projectionId);
        String content = get(String.valueOf("projection:" + projectionId));
        
        if(projection == null || dateTime == null || content == null) {
            System.out.println("Projection: " + projectionId + " not updated");
            return;
        }
        
        deleteProjection(projectionId);
        String[] field = content.split(":");
        field[3] = String.valueOf(room);
        field[1] = dateFormat.format(dateTime);
        
        content = "dateTime:" + field[1] + ":room:" + field[3] + ":cinema:" + field[5] + ":film:" + field[7];

        put("projection:" + String.valueOf(projectionId), content);
    }
    
    // da fare
    public void deleteProjection(int projectionId){
       
        Projection projection = getProjectionById(projectionId);
        if(projection == null) {
            System.out.println("Projection: " + projectionId + " not deleted");
            return;
        }
        delete(String.valueOf("projection:" + projectionId));
        System.out.println("Projection: " + projectionId + " deleted");
    
    }
    
    // da fare
    public Projection getProjectionById(int projectionId){ 
        String value = get(String.valueOf("projection:" + projectionId));
            if(value == null) return null;

            String[] field = value.split(":");
            try {
                return new Projection(projectionId, dateFormat.parse(field[1]), Integer.parseInt(field[3]));
            } catch (ParseException ex) {
                Logger.getLogger(KeyValueDBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
    }
    
    
}
