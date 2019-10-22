package com.lsmsdgroup.pisaflix;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBManager {

    private static EntityManagerFactory factory;
    private static EntityManager entityManager;
    
    
    public static void setup() {
    	 factory = Persistence.createEntityManagerFactory("pisaflixdb");

    }
 
    public static void exit() {
    	factory.close();
    }
    
    
    public static class UserManager{
        private static void read(int userId) {
        // code to get a user
        System.out.println("Getting a User");
        
        try {
	    	entityManager = factory.createEntityManager();
	        entityManager.getTransaction().begin();
	        User user = entityManager.find(User.class, userId);
                if(user == null){System.out.println("User not found!");}
                else{
                    System.out.println(user.toString());
                    System.out.println("User retrieved");
                }
            } catch (Exception ex) {
			ex.printStackTrace(System.out);
			System.out.println("A problem occurred in retriving a user!");
	    } finally {
			entityManager.close();
	    }
        } 
    }
    
    public static class FilmManager{
        private static void read(int filmId) {
        // code to get a user
        System.out.println("Getting a User");
        
        try {
	    	entityManager = factory.createEntityManager();
	        entityManager.getTransaction().begin();
	        Film film = entityManager.find(Film.class, filmId);
                if(film == null){System.out.println("film not found!");}
                else{
                    System.out.println(film.toString());
                    System.out.println("Film retrieved");
                }
            } catch (Exception ex) {
			ex.printStackTrace(System.out);
			System.out.println("A problem occurred in retriving a film!");
	    } finally {
			entityManager.close();
	    }
        } 
    }
    
    
    
    public static void main(String[] args){
        
        
        setup();
       
        for(int i = 1; i <103; i++ ){
            DBManager.UserManager.read(i);
        }
        DBManager.FilmManager.read(1);
        exit();
        
    }
    
}
