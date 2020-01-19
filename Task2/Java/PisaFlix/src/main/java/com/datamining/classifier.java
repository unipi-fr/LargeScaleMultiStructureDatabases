package com.datamining;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class classifier {

    public static String classify() {
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

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(classify());
    }
}
