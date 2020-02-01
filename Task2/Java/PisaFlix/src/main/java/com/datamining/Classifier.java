package com.datamining;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflix.dbmanager.FilmManager;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;


public class Classifier {

    // Funzione che appende la descrizione dei film sul file "to_be_cassified.csv" per essere prelevati dallo script python
    public static boolean writePlotToFile(Set<Film> filmSet) {
        try (FileWriter writer = new FileWriter("src/main/resources/datamining/resources/datasets/to_be_classified.csv")) {
            writer.write("MPAA,Plot");
            for (Film film : filmSet) {
                writer.append("\nto_be_classified," + film.getDescription().replaceAll(",", " ; ").replaceAll("\n", " "));
            }
        } catch (IOException e) {
            System.out.println("Error in writing the plot: " + e.getMessage());
            return false;
        }
        return true;
    }

    //Funzione che si occupa di gestire la classificazione dei film nel set passato
    public static HashMap<String, Double> classify(Set<Film> filmSet) {

        HashMap<String, Double> adultness = new HashMap<>();

        // Se il salvataggio delle descrzioni nel file csv non va a buon fine ritona "null"
        if (!writePlotToFile(filmSet)) {
            return null;
        }

        // Chiamata allo script per la classificazione
        Process pythonScript = null;
        try {
            pythonScript = Runtime.getRuntime().exec("python src/main/resources/datamining/scripts/classifier.py -get t");
        } catch (IOException ex) {
            System.out.println("Error trying to run the script: " + ex.getMessage());
            return null;
        }

        //Aggancio degli degli stream di standard output ed error
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(pythonScript.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(pythonScript.getErrorStream()));

        //STampa di eventuali errori a video
        String line;
        try {
            while ((line = stdError.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            return null;
        }

        //Prelievo dei valori di adultness appaiati agli id dei ripettivi film
        try {
            for (Film film : filmSet) {
                adultness.put(film.getId(), Double.valueOf(stdInput.readLine()));
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
        //Ritorna una hashmap che abbina i valori di adultness ai rispettivi film
        return adultness;
    }

    //Classifica tutti i film presenti nel database
    public static void classifyAllDatabase(int n, Date date) {
        //Preleva un campione di film ancora da classificare
        Set<Film> filmSet = FilmManager.getIstance().getFilmToBeClassified(date, n, 0);
        //Finchè non sono stati classificati tutti i film
        while (!filmSet.isEmpty()) {
            //Vengono classificati quelli prelevati a questa iterazione
            HashMap<String, Double> adultness = classify(filmSet);
            //Vengono aggiornati i valori di adultness dei ripettivi film nel database
            adultness.keySet().forEach((idFilm) -> {
                FilmManager.getIstance().updateClass(idFilm, adultness.get(idFilm));
                System.out.println("Updated " + idFilm + " with adultness " + adultness.get(idFilm));
            });
            //Si preleva un nuovo campione di film 
            filmSet = FilmManager.getIstance().getFilmToBeClassified(date, n, 0);
        }
        //Viene ciusa la connessione al database
        DBManager.stop();
        System.out.println("All films have been successfully classified!");
    }

    public static void main(String[] args) {

        classifyAllDatabase(100, new Date());

    }
}
