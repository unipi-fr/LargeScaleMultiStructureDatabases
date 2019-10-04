
package com.mycompany.task0;
import java.sql.*;
import java.util.*;

public class Main {
    
    private static Connection co;
    private static final Scanner S = new Scanner(System.in);
    private static int operation;
    
    
    static{
        try{
        co = DriverManager.getConnection("", "root", "root");
        }
        catch(SQLException e) {e.printStackTrace(System.out);}
    }
    
    private void addRating(){}
    private void showUsers(){}
    private void showFilms(){}
    private void modifyRating(){}
    private void deleteRating(){}
    
    
    
    public static void main(String[] args){
        
        System.out.println(
                "List of possible actions: \n"
                + "1. Add new rating \n"
                + "2. Show users list \n"
                + "3. Show films list \n"
                + "4. Modify rating \n"
                + "5. Delete user");
        
        while(true){
            System.out.println("Select the command (integer) plus argument nedeed:");
            
            operation = S.nextInt();
            
            switch(operation){
                // TODO:
                // aggiungere argomenti necessari ai metodi
                // prima di leggere gli argomenti necessari stampare indicazioni
                
                case 1: //{leggo argomenti 
                    //addRating(); break;}
                case 2: //{
                    //showUsers(); break;}
                case 3: //{
                    //showFilms(); break;} ricorda di mettere anche la media delle valutazioni
                case 4: //{ leggo argomenti
                    //modifyRating(); break;}
                case 5: //{ leggo argomenti
                    //deleteRating(); break;}
                default: System.out.println("Comando non riconosciuto!!!");
           }
        }
        
    }
    
    
}
