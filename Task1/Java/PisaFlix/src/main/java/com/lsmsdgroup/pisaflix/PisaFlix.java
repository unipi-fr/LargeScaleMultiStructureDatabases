package com.lsmsdgroup.pisaflix;

public class PisaFlix {

    public static void main(String[] args){
        DBManager.setup();
        
        DBManager.UserManager.create("prova", "prova12345", 0);
        DBManager.UserManager.update(102,"provaModificata", "prova12345", 0);
        DBManager.exit();
    }
}
