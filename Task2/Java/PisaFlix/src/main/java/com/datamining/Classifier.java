package com.datamining;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.dbmanager.FilmManager;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Classifier {

    public static boolean writePlotToFile(Set<Film> filmSet) {
        try (FileWriter writer = new FileWriter("src/main/resources/datamining/resources/datasets/to_be_classified.csv")) {
            writer.write("MPAA,Plot");
            for(Film film : filmSet){
                writer.append("\nto_be_classified,"+ film.getDescription().replaceAll(",", " ").replaceAll("\n", " "));
            }
        } catch (IOException e) {
            System.out.println("Error in writing the plot: " + e.getMessage());
            return false;
        }
        return true;
    }

    public static HashMap<String,Double> classify(Set<Film> filmSet) {
        
        HashMap<String,Double> adultness = new HashMap<>();
        
        if(!writePlotToFile(filmSet)){
            return null;
        }
        
        Process pythonScript = null;

        try {
            pythonScript = Runtime.getRuntime().exec("python src/main/resources/datamining/scripts/classifier.py -get t");
        } catch (IOException ex) {
            System.out.println("Error trying to run the script: " + ex.getMessage());
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
        }

        try {
            for(Film film : filmSet){
                adultness.put(film.getId(), Double.valueOf(stdInput.readLine()));
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return adultness;
    }

    public static void main(String[] args){

       Set<Film> filmSet = FilmManager.getIstance().getFilmToBeClassified(100, 0);
       System.out.println(classify(filmSet));
        
    }
}
