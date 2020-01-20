package com.datamining;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflix.dbmanager.FilmManager;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;

public class Classifier {

    public static boolean writePlotToFile(Set<Film> filmSet) {
        try (FileWriter writer = new FileWriter("src/main/resources/datamining/resources/datasets/to_be_classified.csv")) {
            writer.write("MPAA,Plot");
            for (Film film : filmSet) {
                writer.append("\nto_be_classified," + film.getDescription().replaceAll(",", " ").replaceAll("\n", " "));
            }
        } catch (IOException e) {
            System.out.println("Error in writing the plot: " + e.getMessage());
            return false;
        }
        return true;
    }

    public static HashMap<String, Double> classify(Set<Film> filmSet) {

        HashMap<String, Double> adultness = new HashMap<>();

        if (!writePlotToFile(filmSet)) {
            return null;
        }

        Process pythonScript = null;

        try {
            pythonScript = Runtime.getRuntime().exec("python src/main/resources/datamining/scripts/classifier.py -get t");
        } catch (IOException ex) {
            System.out.println("Error trying to run the script: " + ex.getMessage());
            return null;
        }

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(pythonScript.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(pythonScript.getErrorStream()));

        String line;
        try {
            while ((line = stdError.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            return null;
        }

        try {
            for (Film film : filmSet) {
                adultness.put(film.getId(), Double.valueOf(stdInput.readLine()));
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
        return adultness;
    }

    public static void classifyAllDatabase(int n) {
        Set<Film> filmSet = FilmManager.getIstance().getFilmToBeClassified(n, 0);
        while (!filmSet.isEmpty()) {
            HashMap<String, Double> adultness = classify(filmSet);
            adultness.keySet().forEach((idFilm) -> {
                FilmManager.getIstance().updateClass(idFilm, adultness.get(idFilm));
                System.out.println("Updated " + idFilm + " with adultness " + adultness.get(idFilm));
            });
            filmSet = FilmManager.getIstance().getFilmToBeClassified(n, 0);
        }
        DBManager.stop();
        System.out.println("All films have been successfully classified!");
    }

    public static void main(String[] args) {

        classifyAllDatabase(100);

    }
}
