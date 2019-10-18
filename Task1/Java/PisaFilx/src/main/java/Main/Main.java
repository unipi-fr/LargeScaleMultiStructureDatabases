/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.util.*;

/**
 *
 * @author 
 */
public class Main {  //HO RICOPIATO TUTTO DALLA TASK 0 VA MODIFICATO
        
    private static final Scanner S = new Scanner(System.in);
    private static int operation;
    private static boolean end = false;
    
    
    public static void main(String[] args){
        
        /*
        System.out.println(
                "List of possible actions: \n"
                + "1. Add new rating \n"
                + "2. Show users list \n"
                + "3. Show films list \n"
                + "4. Modify rating \n"
                + "5. Delete user\n"
                + "0. Exit application");
        
        while(!end){
            System.out.println("Select the command (integer) plus argument needed:");
            
            operation = S.nextInt();
            
            switch(operation){
                // TODO:
                // aggiungere argomenti necessari ai metodi
                // prima di leggere gli argomenti necessari stampare indicazioni
                case 0:
                    end = true;
                    break;
                case 1:  
                    System.out.println("Insert username:");
                    String u = S.next();
                    System.out.println("Insert film:");
                    String f = S.next();
                    System.out.println("Insert rating (integer [1,5]):");
                    int r = S.nextInt();
                    addRating(u, f, r);
                    break;
                case 2:
                    List<String> utenti = showUsers();
                    stampaLista(utenti);
                    break;
                case 3: 
                    List<String> film = showFilms(); 
                    stampaLista(film);
                    break;
                case 4: //{ leggo argomenti
                    System.out.println("Insert username:");
                    u = S.next();
                    System.out.println("Insert film:");
                    f = S.next();
                    System.out.println("Insert new rating (integer [1,5]):");
                    r = S.nextInt();
                    //modifyRating(u,f,r); 
                    break;
                case 5: //{ leggo argomenti
                    System.out.println("Wich user do you want delete (insert the username):");
                    String username = S.next();
                    deleteUser(username); 
                    break;
                default: 
                    System.out.println("Insert a not valid command!!!");
                    break;
           }
        }
        
    }
    
    
    private static void stampaLista(List<String> lista){
    
        for(String aux : lista){
        
            System.out.println(aux);
        }
    }
    
    
    private static void addRating(String user, String film, int rating){
        
        //user check
        List<String> listaUtenti = showUsers();
        if(!listaUtenti.contains(user)){
            System.out.println("User unknown! \n Operation not executed...");
            return;
        }
        // film check
        List<String> listaFilm = showFilms();
        if(!listaFilm.contains(film)){
            System.out.println("Film unknown! \n Operation not executed...");
            return;
        }
        // rating check
        if(rating < 1 || rating > 5){
            System.out.println("Invalid rating! \n Operation not executed...");
            return;
        }
        
        // get film primar key
        int filmPK;
        
        try(
                PreparedStatement ps = co.prepareStatement("SELECT idFilm FROM film WHERE Name = ?");
                ){
            ps.setString(1, film);
            ResultSet rs = ps.executeQuery();
            if(rs.next()==false){
                //LANCIARE ECCEZIONE APPROPRIATA
            }
            filmPK = rs.getInt("idFilm");
            System.out.println("DEBUG| filmPK: "+filmPK);
        }
       // if I can't find the PK of the film better to shut down th method
        catch(SQLException e){e.printStackTrace(System.out); return;} 
        
        //Inserisco la riga
        try(
                PreparedStatement ps = co.prepareStatement("INSERT INTO rating(Username,idFilm,rate) VALUES (?,?,?)");   
           ){
            ps.setString(1,user);
            ps.setInt(2,filmPK);
            ps.setInt(3, rating);
            
            int insertedRows = ps.executeUpdate();
            System.out.println("DEBUG| Righe inserite: "+insertedRows);
        }
        catch(SQLException e){e.printStackTrace(System.out); return;}
        
    }
    
    
    private static List<String> showFilms(){
        
        List<String> nomiFilm = new ArrayList();
        try(
                PreparedStatement s = co.prepareStatement("SELECT Name FROM film;");
                ResultSet r = s.executeQuery();
                ){
            while (r.next()){
                nomiFilm.add(r.getString("Name"));
            }
        }
        catch(SQLException e){e.printStackTrace(System.out);}
        
        return nomiFilm;
    }
    
    
    private static List<String> showUsers(){
        List<String> listaUtenti = new ArrayList();
        
          try(
                PreparedStatement s = co.prepareStatement("SELECT username FROM user;");
                ResultSet r = s.executeQuery();
                ){
            while (r.next()){
                listaUtenti.add(r.getString("username"));
            }
        }
        catch(SQLException e){e.printStackTrace(System.out);}
        
        return listaUtenti;
    }
    
    
    private static void modifyRating(){
        System.out.println("[Modify rating] This function must be implemented");
    }
    
    
    private static void deleteUser(String username){
        try(
                PreparedStatement ps = co.prepareStatement("DELETE FROM user WHERE username = ? ;");
                ){
            ps.setString(1, username);
            int r = ps.executeUpdate();
                
        }catch(SQLException SQLe){SQLe.printStackTrace(System.out); return;}
        catch(Exception e){ e.printStackTrace(System.out); return; }
                             
        System.out.println("User deleted successfully");
        */
    }
    
}

