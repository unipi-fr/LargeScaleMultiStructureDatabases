package com.lsmsdgroup.pisaflix;

public class PisaFlix {

    public static void main(String[] args){
        try{
            DBManager.setup();
        }catch(Exception ex){
	    System.out.println("Error during DB setup!");
        }
        DBManager.UserManager.getAllUsers();
        DBManager.exit();
    }
}
