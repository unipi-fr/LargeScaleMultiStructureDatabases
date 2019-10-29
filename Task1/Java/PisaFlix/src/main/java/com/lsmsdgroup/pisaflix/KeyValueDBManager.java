package com.lsmsdgroup.pisaflix;

import com.lsmsdgroup.pisaflix.Entities.*;
import java.io.File;
import java.io.IOException;
import static java.lang.Integer.parseInt;
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

    DB levelDBStore;
    Options options = new Options();
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY HH.mm.ss");

    public void start() { //Sono obbligato a metterle non statiche
        try {
            levelDBStore = factory.open(new File("KeyValueDB"), options);
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
        }
    }

    public void stop() {
        try {
            levelDBStore.close();
        } catch (IOException ex) {
            Logger.getLogger(KeyValueDBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void put(String key, String value) {
        levelDBStore.put(bytes(key), bytes(value));
    }

    public void delete(String key) {
        levelDBStore.delete(bytes(key));
    }

    public String get(String key) {
        byte[] value = levelDBStore.get(bytes(key));
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

    }

    public void update(int idComment, String text) {

    }

    public void delete(int idComment) {

    }

    public Comment getCommentById(int commentId) {
        String value = get(String.valueOf("comment:" +commentId));
        String[] field = value.split(":");
        try {
            return new Comment(commentId, dateFormat.parse(field[7]), field[5]);
        } catch (ParseException ex) {
            Logger.getLogger(KeyValueDBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
