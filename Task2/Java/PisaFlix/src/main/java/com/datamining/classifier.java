package com.datamining;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class classifier {

    public static boolean writePlotToFile(Film film) {
        try (FileWriter writer = new FileWriter("src/main/resources/datamining/resources/datasets/to_be_classified.txt")) {
            writer.write(film.getDescription());
        } catch (IOException e) {
            System.out.println("Error in writing the plot: " + e.getMessage());
            return false;
        }
        return true;
    }

    public static String classify(Film film) {
        
        if(!writePlotToFile(film)){
            return null;
        }
        
        Process pythonScript = null;

        try {
            pythonScript = Runtime.getRuntime().exec("python src/main/resources/datamining/scripts/classifier.py -get t");
        } catch (IOException ex) {
            Logger.getLogger(classifier.class.getName()).log(Level.SEVERE, null, ex);
        }

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(pythonScript.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(pythonScript.getErrorStream()));

        String s;
        try {
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException ex) {
            Logger.getLogger(classifier.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            return stdInput.readLine();
        } catch (IOException ex) {
            Logger.getLogger(classifier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void main(String[] args){
        System.out.println(classify(new Film("1234", "Prova", "kill punch murder shoot dead sex")));
        System.out.println(classify(new Film("1234", "Prova", "sweet adorable funny play sing hug")));
    }
}
