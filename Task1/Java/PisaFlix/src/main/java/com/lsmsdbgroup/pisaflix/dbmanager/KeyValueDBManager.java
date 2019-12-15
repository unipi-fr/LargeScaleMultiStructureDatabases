package com.lsmsdbgroup.pisaflix.dbmanager;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iq80.leveldb.DB;
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
    
}
