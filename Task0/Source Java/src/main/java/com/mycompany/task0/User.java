
package com.mycompany.task0;


public class User {
    
    public int id, accountLevel;
    public String username, password, email, name, surname;
    
    public User(int id, String username, String password, String email, String name, String surname, int accountLevel){
        
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.accountLevel = accountLevel;
        
    }
    
}
