package PisaFlix;


import javax.persistence.*;
import java.util.*;


public class Main {  //HO RICOPIATO TUTTO DALLA TASK 0 VA MODIFICATO
        
    private static final Scanner S = new Scanner(System.in);
    private static int operation;
    private static final boolean end = false;
    private static EntityManagerFactory factory;
    private static EntityManager entityManager;
    
    
    public static void setup() {
    	 factory = Persistence.createEntityManagerFactory("PisaFlixDB");

    }
 
    public static void exit() {
    	factory.close();
    }
    
    
     public static void read(int userId) {
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
    
    
    
    public static void main(String[] args){
        
        
       setup();
       
       for(int i = 1; i <103; i++ ){
       read(i);
    }
       exit();
        
    }
    
}

