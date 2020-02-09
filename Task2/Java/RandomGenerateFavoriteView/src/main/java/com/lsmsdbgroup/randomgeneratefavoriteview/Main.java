package com.lsmsdbgroup.randomgeneratefavoriteview;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.bson.Document;
import org.bson.types.ObjectId;

public class Main {

    public static void main(String[] args) {
        RandomGen rg = new RandomGen();

        rg.start();
        rg.getAllFilm();
        rg.getAllUSer();

        List<ObjectId> filmSet = rg.getFilmSet();
        List<ObjectId> userSet = rg.getUserSet();

        Set<Document> documents = new LinkedHashSet<>();

        Random rand = new Random();

        Date date = new Date();
        rg.filmSet.forEach((film) -> {
            Document doc;
            if ((rand.nextInt() % 3) == 0) {
                doc = rg.generateViewDocument(film.toString(), userSet.get(rand.nextInt(userSet.size())).toString(), rg.generateRandomIsoDate());
                documents.add(doc);
                System.out.println(doc);
            }
            if ((rand.nextInt() % 5) == 0) {
                doc = rg.generateViewDocument(film.toString(), userSet.get(rand.nextInt(userSet.size())).toString(), rg.generateRandomIsoDate());
                documents.add(doc);
                System.out.println(doc);
            }
            if ((rand.nextInt() % 7) == 0) {
                doc = rg.generateViewDocument(film.toString(), userSet.get(rand.nextInt(userSet.size())).toString(), rg.generateRandomIsoDate());
                documents.add(doc);
                System.out.println(doc);
            }
            if ((rand.nextInt() % 13) == 0) {
                doc = rg.generateViewDocument(film.toString(), userSet.get(rand.nextInt(userSet.size())).toString(), rg.generateRandomIsoDate());
                documents.add(doc);
                System.out.println(doc);
            }
        });

        /* for (int i = 0; i < 50000; i++) {
            int filmIndex = rand.nextInt(filmSet.size());
            int userIndex = rand.nextInt(userSet.size());

            Document doc;

            if ((rand.nextInt() % 3) == 0) {
                boolean duplicate = false;
                for (Document docu : documents) {
                    if ("FAVOURITE".equals(docu.get("Type").toString()) && docu.get("User").toString().equals(userSet.get(userIndex).toString()) && docu.get("Film").toString().equals(filmSet.get(filmIndex).toString())) {
                        duplicate = true;
                    }
                }
                if (!duplicate) {
                    doc = rg.generateFavoriteDocument(filmSet.get(filmIndex).toString(), userSet.get(userIndex).toString());
                    documents.add(doc);
                    System.out.println(doc);
                }
            }
            doc = rg.generateViewDocument(filmSet.get(filmIndex).toString(), userSet.get(userIndex).toString());
            documents.add(doc);
            System.out.println(doc);
        }*/
        rg.insertMany(new ArrayList(documents));
        rg.stop();
    }
}
