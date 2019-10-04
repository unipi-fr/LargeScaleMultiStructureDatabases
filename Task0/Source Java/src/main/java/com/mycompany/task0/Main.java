
package com.mycompany.task0;
import java.sql.*;
import java.util.*;

public class Main {
    
    private static Connection co;
    private static final Scanner S = new Scanner(System.in);
    private static int operation;
    private static boolean end = false;
    
    private static final String DBIp = "localhost";
    private static final String DBPort = "3306";
    private static final String DBUser = "root";
    private static final String DBPass = "root";
    private static final String DBName = "";
    
    static{
        try{
        co = DriverManager.getConnection("", "root", "root");
        }
        catch(SQLException e) {e.printStackTrace(System.out);}
    }
    
    
    public static void main(String[] args){
        
        System.out.println(
                "List of possible actions: \n"
                + "1. Add new rating \n"
                + "2. Show users list \n"
                + "3. Show films list \n"
                + "4. Modify rating \n"
                + "5. Delete user\n"
                + "0. Exit application");
        
        while(!end){
            System.out.println("Select the command (integer) plus argument nedeed:");
            
            operation = S.nextInt();
            
            switch(operation){
                // TODO:
                // aggiungere argomenti necessari ai metodi
                // prima di leggere gli argomenti necessari stampare indicazioni
                case 0:
                    end = true;
                    break;
                case 1: //{leggo argomenti 
                    addRating();
                    break;
                case 2: //{
                    showUsers();
                    break;
                case 3: //{
                    showFilms(); 
                    break;
                case 4: //{ leggo argomenti
                    modifyRating(); 
                    break;
                case 5: //{ leggo argomenti
                    deleteRating(); 
                    break;
                default: 
                    System.out.println("Insert a not valid command!!!");
                    break;
           }
        }
        
    }
    
    private static void addRating(){
        System.out.println("[Add rating] This function must be implemented");
    }
    private static void showFilms(){
        System.out.println("[Show films list] This function must be implemented");
    }
    private static void showUsers(){
        System.out.println("[Show users list] This function must be implemented");
    }
    private static void modifyRating(){
        System.out.println("[Modify rating] This function must be implemented");
    }
    private static void deleteRating(){
        System.out.println("[Delete rating] This function must be implemented");
    }
    
}
