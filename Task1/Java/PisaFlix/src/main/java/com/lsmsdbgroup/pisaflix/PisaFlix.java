package com.lsmsdbgroup.pisaflix;

import com.lsmsdbgroup.pisaflix.Entities.*; // serve per usare la parte keyValue
import com.lsmsdbgroup.pisaflixg.App;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class PisaFlix {
    
    private static final Scanner S = new Scanner(System.in);
    
    public static void register(){
        String username, password, email, firstName, lastName;
        System.out.println("Insert username:");
        username = S.nextLine();
        System.out.println("Insert password:");
        password = S.nextLine();
        System.out.println("Insert email:");
        email = S.nextLine();
        System.out.println("Insert first name:");
        firstName = S.nextLine();
        System.out.println("Insert last name:");
        lastName = S.nextLine();
        
        PisaFlixServices.Authentication.Register(username, password, email, firstName, lastName); 
    }
    
    public static void login(){
        String username, password;
        System.out.println("Insert username:");
        username = S.nextLine();
        System.out.println("Insert password:");
        password = S.nextLine();
        
        try { 
            PisaFlixServices.Authentication.Login(username, password);
            System.out.println("Logged successfully!");
        } catch (PisaFlixServices.Authentication.UserAlredyLoggedException | PisaFlixServices.Authentication.InvalidCredentialsException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private static void logout() {
        PisaFlixServices.Authentication.Logout();
    }
    
    public static void authenticationMenu(){
        boolean end = false;
        int operation;
        while(!end){
            System.out.println(
                "List of possible actions: \n"
                + "1. Register \n"
                + "2. Login\n"
                + "3. Logout\n"      
                + "0. Back");
            System.out.println("Select the command (integer) =>");
            
            operation = S.nextInt();
            S.nextLine();
            
            switch(operation){
                case 0:
                    return;
                case 1: 
                    register();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    logout();
                    break;
                default: 
                    System.out.println("Inserted a not valid command!!!");
                    break;
           }
        }
        
    }
    
    public static void userManagerMenu(){
        boolean end = false;
        int operation;
        while(!end){
            System.out.println(
                "List of possible actions: \n"    
                + "0. Back");
            System.out.println("Select the command (integer) =>");
            
            operation = S.nextInt();
            S.nextLine();
            
            switch(operation){
                case 0:
                    return;
                default: 
                    System.out.println("Inserted a not valid command!!!");
                    break;
           }
        }
        
    }
    
    public static void mainMenu(){
        int operation;
        boolean end = false;
        
        while(!end){
            System.out.println(
                "List of possible actions: \n"
                + "1. Authentication (Register, Login, etc...) \n"
                + "2. User manager (delete account, change privileges, etc..)\n"
                + "0. Exit application");
            System.out.println("Select the command (integer) =>");
            
            
            operation = S.nextInt();
            S.nextLine();
            
            switch(operation){
                // TODO:
                // aggiungere argomenti necessari ai metodi
                // prima di leggere gli argomenti necessari stampare indicazioni
                case 0:
                    end = true;
                    break;
                case 1: 
                    authenticationMenu();
                    break;
                case 2:
                    userManagerMenu();
                    break;
                default: 
                    System.out.println("Inserted a not valid command!!!");
                    break;
           }
        }
    }

    public static void main(String[] args){
        LogManager.getLogManager().getLogger("").setLevel(Level.OFF);
        DBManager.setup();
        //mainMenu();
        App.main(args);
        
        /*
        KeyValueDBManager db = new KeyValueDBManager();
        db.start();
        
        db.createCinemaComment("non saprei", new User(1), new Cinema(1));
        db.createCinemaComment("dddddddddddd", new User(2), new Cinema(1));
        db.createCinemaComment("ccccccccccccc", new User(1), new Cinema(1));
        db.createCinemaComment("bbbbbbbbbbbbbbb", new User(1), new Cinema(2));
        db.createCinemaComment("aaaaaaaaaaaaaa", new User(2), new Cinema(2));
        
        db.createFilmComment("eeeeeeeeeeee", new User(1), new Film(1));
        db.createFilmComment("ffffffffffffff", new User(1), new Film(2));
        db.createFilmComment("ggggggggggggg", new User(2), new Film(1));
        db.createFilmComment("hhhhhhhhhhh", new User(2), new Film(2));
        db.createFilmComment("iiiiiiiiiiii", new User(1), new Film(2));
        */
        
        //db.deleteComment(4);
        //db.deleteComment(9);
        /*
        System.out.println(db.getCommentsOfFilm(1));
        System.out.println(db.getCommentsOfFilm(2));
        System.out.println(db.getCommentsOfCinema(1));
        System.out.println(db.getCommentsOfCinema(2));
        
        Comment aux;
        for(int i = 1; i <= 10; i++){
        
            aux = db.getCommentById(i);
            if(aux == null) continue;
            
            System.out.println(aux);
            
        }
        
        db.stop();
        */
        DBManager.exit();
    }
}
