package com.mazzogroup.randomgeneratefavoriteview;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bson.Document;
import org.bson.types.ObjectId;

public class Main {
    
    public static void main(String[] args){
        RandomGen rg = new RandomGen();
        
        rg.start();
        rg.getAllFilm();
        rg.getAllUSer();
        
        List<ObjectId> filmSet = rg.getFilmSet();
        List<ObjectId> userSet = rg.getUserSet();
        
        List<Document> documents = new ArrayList<>();
        
        Random rand = new Random();
        for(int i = 0; i < 200000; i++){
            int filmIndex = rand.nextInt(filmSet.size());
            int userIndex = rand.nextInt(userSet.size());
            
            Document doc;
            
            doc = rg.generateViewDocument(filmSet.get(filmIndex).toString(), userSet.get(userIndex).toString());
            
            documents.add(doc);
        }
        
        rg.insertMany(documents);
        
        rg.stop();
    }
}
