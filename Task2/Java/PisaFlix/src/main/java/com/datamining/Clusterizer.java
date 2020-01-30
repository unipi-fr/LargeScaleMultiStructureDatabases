package com.datamining;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflix.dbmanager.FilmManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class Clusterizer {

    static class StreamGobbler extends Thread {

        InputStream is;

        StreamGobbler(InputStream is) {
            this.is = is;
        }

        @Override
        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException ioe) {
                System.out.println("IOError: " + ioe.getMessage());
            }
        }
    }

    public static boolean writePlotToFile(Set<Film> filmSet) {
        try (FileWriter writer = new FileWriter("src/main/resources/datamining/resources/datasets/to_be_clusterized.csv")) {
            writer.write("Plot");
            for (Film film : filmSet) {

                String generaSet = "";
                String castSet = "";
                String directorSet = "";

                if (film.getCastSet() != null) {
                    castSet = film.getCastSet().stream().map((s) -> " . " + s.trim().replace(" ", "_")).reduce(castSet, String::concat);
                }

                if (film.getDirectorSet() != null) {
                    directorSet = film.getDirectorSet().stream().map((s) -> " . " + s.trim().replace(" ", "_")).reduce(directorSet, String::concat);
                }

                if (film.getGeneraSet() != null) {
                    generaSet = film.getGeneraSet().stream().map((s) -> " . " + s.trim().replace(" ", "_")).reduce(generaSet, String::concat);
                }

                String otherFactors = "";

                for (int i = 0; i < 2; i++) {
                    otherFactors += generaSet + directorSet + castSet;
                }

                writer.append("\n" + (film.getDescription() + otherFactors).replaceAll(",", " ; ").replaceAll("\n", " "));
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
            pythonScript = Runtime.getRuntime().exec("python src/main/resources/datamining/scripts/clusterizer.py " + sizeCluster);
        } catch (IOException ex) {
            System.out.println("Error trying to run the script: " + ex.getMessage());
            return null;
        }

        StreamGobbler errorGobbler = new StreamGobbler(pythonScript.getErrorStream());
        StreamGobbler outputGobbler = new StreamGobbler(pythonScript.getInputStream());
        errorGobbler.start();
        outputGobbler.start();

        try {
            pythonScript.waitFor();
        } catch (InterruptedException ex) {
            System.out.println("Script interrupted: " + ex.getMessage());
        }

        String line;
        HashMap<Integer, Integer> clusters = new HashMap<>();
        String seed = (new Date()).toString();
        try (BufferedReader bufferReader = new BufferedReader(new FileReader("src/main/resources/datamining/resources/elaborations/clustering_results.txt"))) {
            bufferReader.readLine();
            for (Film film : filmSet) {
                int numCluster = Integer.valueOf(bufferReader.readLine());
                int clusterId = (numCluster + seed).hashCode();
                clusters.put(numCluster, clusterId);
                film.setCluster(clusterId);
            }
            int i = 0;
            filmSet.forEach((film) -> {
                film.setTags(new LinkedHashSet<>());
            });
            while ((line = bufferReader.readLine()) != null) {
                if (line.charAt(0) == '$') {
                    i++;
                } else {
                    for (Film film : filmSet) {
                        if (film.getcluster() == clusters.get(i)) {
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

    public static void clusterizeAllDatabaseSampling(int n, int sizeCluster, Date date) {
        FilmManager.getIstance().resetClusters();
        Set<Film> filmSet = FilmManager.getIstance().getFilmToBeClusterized(n, date);
        while (!filmSet.isEmpty()) {
            clusterize(filmSet, sizeCluster);
            filmSet.forEach((film) -> {
                FilmManager.getIstance().updateCluster(film);
                System.out.println("Updated " + film.getTitle() + " with Cluster " + film.getcluster());
                System.out.println("Tags: " + film.getTags());
            });
            filmSet = FilmManager.getIstance().getFilmToBeClusterized(n, date);
        }
        DBManager.stop();
        System.out.println("All films have been successfully clusterized!");
    }
    
    public static void clusterizeAllDatabase(int sizeCluster, Date date) {
        FilmManager.getIstance().resetClusters();
        Set<Film> filmSet = FilmManager.getIstance().getAll(0, 0);
            clusterize(filmSet, sizeCluster);
            filmSet.forEach((film) -> {
                FilmManager.getIstance().updateCluster(film);
                System.out.println("Updated " + film.getTitle() + " with Cluster " + film.getcluster());
                System.out.println("Tags: " + film.getTags());
            });
        DBManager.stop();
        System.out.println("All films have been successfully clusterized!");
    }

    public static void main(String[] args) {
        clusterizeAllDatabaseSampling(8000, 4, new Date());
    }
}
