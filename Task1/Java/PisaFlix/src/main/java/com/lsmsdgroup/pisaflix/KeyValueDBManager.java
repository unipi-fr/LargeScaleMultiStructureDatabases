package com.lsmsdgroup.pisaflix;

import java.io.File;
import java.io.IOException;
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

    public void start() { //Sono obbligato a metterle non statiche
        try {
            levelDBStore = factory.open(new File("KeyValueDB"), options);
        } catch (IOException ex) {
            Logger.getLogger(KeyValueDBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stop(){
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
        if(value != null){
            return Iq80DBFactory.asString(value); 
        }else{
            System.out.println("Chiave non trovata");
            return null;
        }    
    }

}
