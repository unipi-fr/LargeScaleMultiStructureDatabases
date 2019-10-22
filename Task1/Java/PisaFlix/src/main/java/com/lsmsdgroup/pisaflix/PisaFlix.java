package com.lsmsdgroup.pisaflix;

public class PisaFlix {

    public static void main(String[] args){
        DBManager.setup();
       
        for(int i = 1; i <103; i++ ){
            DBManager.UserManager.read(i);
        }
        
        DBManager.FilmManager.read(1);
        DBManager.exit();
    }
}
