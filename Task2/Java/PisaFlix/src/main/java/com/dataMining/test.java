package com.dataMining;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class test {

    public static void main(String[] args) throws IOException {
        Process p = Runtime.getRuntime().exec("python src/main/java/com/dataMining/test.py");
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String ret = in.readLine();
        System.out.println("Python says: "+ret);
    }
}
