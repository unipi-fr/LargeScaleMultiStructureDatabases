/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

/**
 *
 * @author 
 */
public class User {
    public int id, privilegeLevel;
    public String username, password, email, firstName, secondName;
    
    public User(int id, String username, String password, String email, String firstName, String secondName, int privilegeLevel){
        
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.secondName = secondName;
        this.privilegeLevel = privilegeLevel;
        
    }
}
