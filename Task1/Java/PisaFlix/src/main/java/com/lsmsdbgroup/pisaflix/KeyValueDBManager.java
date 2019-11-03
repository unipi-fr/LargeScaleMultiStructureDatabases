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
        int idComment = Integer.parseInt(get("setting:lastCommentKey")) + 1;
        put("comment:" + String.valueOf(idComment), "user:" + user.getIdUser().toString() + ":film:" + film.getIdFilm().toString() + ":text:" + text + ":timestamp:" + dateFormat.format(new Date()));
        put("setting:lastCommentKey", String.valueOf(idComment));
    }

    
    public void createCinemaComment(String text, User user, Cinema cinema) {
        int idComment = Integer.parseInt(get("setting:lastCommentKey")) + 1;
        put("comment:" + String.valueOf(idComment), "user:" + user.getIdUser().toString() + ":cinema:" + cinema.getIdCinema().toString() + ":text:" + text + ":timestamp:" + dateFormat.format(new Date()));
        put("setting:lastCommentKey", String.valueOf(idComment));
    }

    
    public void updateComment(int commentId, String text) {
        Comment comment = getCommentById(commentId);
        String content = get(String.valueOf("comment:" + commentId));
        
        if(comment == null || text == null || content == null) {
            System.out.println("Comment: " + commentId + " not updated");
            return;
        }
        
        deleteComment(commentId);
        String[] field = content.split(":");
        field[5] = text;
        field[7] = dateFormat.format(new Date());
        
        content = "user:" + field[1] + ":film:" + field[3] + ":text:" + field[5] + ":timestamp:" + field[7];
        put("comment:" + String.valueOf(commentId), content);
        
    }

    public void deleteComment(int commentId) {
        
        Comment comment = getCommentById(commentId);
        if(comment == null) {
            System.out.println("Comment: " + commentId + " not deleted");
            return;
        }
        delete(String.valueOf("comment:" + commentId));
        System.out.println("Comment: " + commentId + " deleted");
    }

    
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
    
    
    public void createProjection(Date dateTime, int room, Cinema cinema, Film film){
        int idProjection = Integer.parseInt(get("setting:lastProjectionKey")) + 1;
        put("projection:" + String.valueOf(idProjection), "dateTime:" + dateFormat.format(dateTime) + ":room:" + String.valueOf(room) + ":cinema:" + cinema.getIdCinema().toString() + ":film:" + film.getIdFilm().toString());
        put("setting:lastProjectionKey", String.valueOf(idProjection));
    
    }
    
    
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
    
    public void deleteProjection(int projectionId){
       
        Projection projection = getProjectionById(projectionId);
        if(projection == null) {
            System.out.println("Projection: " + projectionId + " not deleted");
            return;
        }
        delete(String.valueOf("projection:" + projectionId));
        System.out.println("Projection: " + projectionId + " deleted");
    
    }
    
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
