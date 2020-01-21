package com.datamining;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflix.dbmanager.FilmManager;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class Clusterizer {

    public static boolean writePlotToFile(Set<Film> filmSet) {
        try (FileWriter writer = new FileWriter("src/main/resources/datamining/resources/datasets/to_be_clusterized.csv")) {
            writer.write("Plot");
            for (Film film : filmSet) {

                String generaSet = "";
                String castSet = "";
                String directorSet = "";

                if (film.getCastSet() != null) {
                    castSet = film.getCastSet().stream().map((s) -> " . " + s).reduce(castSet, String::concat);
                }

                if (film.getDirectorSet() != null) {
                    directorSet = film.getDirectorSet().stream().map((s) -> " . " + s).reduce(directorSet, String::concat);
                }
                
                if (film.getGeneraSet() != null) {
                    generaSet = film.getGeneraSet().stream().map((s) -> " . " + s).reduce(generaSet, String::concat);
                }

                writer.append("\n" +(film.getDescription() + generaSet + directorSet + castSet).replaceAll(",", " ; ").replaceAll("\n", " "));
            }
        } catch (IOException e) {
            System.out.println("Error in writing the plot: " + e.getMessage());
            return false;
        }
        return true;
    }

    public static Set<Film> clusterize(Set<Film> filmSet, int sizeCluster) {

        if (!writePlotToFile(filmSet)) {
            return null;
        }

        Process pythonScript = null;

        try {
            pythonScript = Runtime.getRuntime().exec("python src/main/resources/datamining/scripts/clusterizer.py " + sizeCluster  +" -get t");
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
                film.setCluster(Integer.valueOf(stdInput.readLine()));
            }
            int i = 0;
            while ((line = stdInput.readLine()) != null) {
                if(line.charAt(0) == '$'){
                    i ++;
                }else{
                    for(Film film : filmSet){
                        if(film.getcluster() == i){
                            film.getTags().add(line);
                        }
                    }
                }
            } 
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
        return filmSet;
    }

    public static void classifyAllDatabase(int n, int sizeCluster) {
        Set<Film> filmSet = FilmManager.getIstance().getFilmToBeClassified(n, 0);
        while (!filmSet.isEmpty()) {
            clusterize(filmSet, sizeCluster);
        }
        DBManager.stop();
        System.out.println("All films have been successfully clusterized!");
    }

    public static void main(String[] args) {
        
        Set<Film> filmSet = FilmManager.getIstance().getFilmToBeClusterized(100);
        clusterize(filmSet, 2);
        for(Film film : filmSet){
            System.out.println(film.getTitle());
            System.out.println(film.getcluster());
            System.out.println(film.getTags());
        }
    }
}
