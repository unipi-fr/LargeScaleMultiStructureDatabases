package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.User;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.UserManagerDatabaseInterface;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;
import static org.neo4j.driver.v1.Values.parameters;

public class UserManager implements UserManagerDatabaseInterface {

    private static UserManager UserManager;
    
    private final Driver driver;
    
    private final int limit = 27;

    public static UserManager getIstance() {
        if (UserManager == null) {
            UserManager = new UserManager();
        }
        return UserManager;
    }

    public UserManager() {
        driver = DBManager.getDB();
    }

    private User getUserFromRecord(Record record){
        Value value = record.get("n");
        Long id = value.asNode().id();
        
        String username = value.get("Username").asString();
        String firstName = value.get("FirstName").asString();
        String lastName = value.get("LastName").asString();
        int privilegeLevel = value.get("PrivilegeLevel").asInt();
        String email = value.get("Email").asString();
        
        return new User(id, email, username, privilegeLevel, firstName, lastName);
    }
    
    @Override
    public User getById(Long idUser) {
        User user = null;
        
        try(Session session = driver.session())
        {
            StatementResult result = session.run("MATCH (n:User) WHERE ID(n) = $id RETURN n", parameters("id", idUser));
            
            while(result.hasNext())
            {
                Record record = result.next();
                
                user = getUserFromRecord(record);
            }
        }
        
        return user;
    }

    @Override
    public void create(String username, String password, String firstName, String lastName, String email, int privilegeLevel) {
        try(Session session = driver.session())
        {
            session.run("CREATE (u:User {Email: $email, FirtName: $firstName, LastName: $lastName, Password: $password, PrivilegeLevel: $privilegeLevel, Username: $username})",
                    parameters("username", username, 
                                "password", password, 
                                "firstName", firstName, 
                                "lastName", lastName, 
                                "email", email, 
                                "privilegeLevel", privilegeLevel));
        }
    }

    @Override
    public void delete(Long idUser) {
        try(Session session = driver.session())
        {
            session.run("MATCH (n:User) WHERE ID(n) = $id DELETE n", 
                    parameters("id", idUser));
        }
    }

    @Override
    public void update(User user) {
        update(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getPrivilegeLevel());
    }

    @Override
    public void update(Long userId, String username, String firstName, String lastName, String email, String password, int privilegeLevel) {
        try(Session session = driver.session())
        {
            session.run("MATCH (u:User) "
                    + "WHERE ID(u) = $id"
                    + "SET u.Email: $email, u.FirtName: $firstName, u.LastName: $lastName, u.Password: $password, u.PrivilegeLevel: $privilegeLevel, u.Username: $username "
                    + "RETURN u",
                    parameters( "id", userId,
                                "username", username, 
                                "password", password, 
                                "firstName", firstName, 
                                "lastName", lastName, 
                                "email", email, 
                                "privilegeLevel", privilegeLevel));
        }
    }

    @Override
    public Set<User> getAll() {
        Set<User> userSet = new LinkedHashSet<>();
        
        try(Session session = driver.session())
        {
            StatementResult result = session.run("MATCH (n:User) RETURN n LIMIT " + limit);
            
            while(result.hasNext())
            {
                Record record = result.next();
                
                User user = getUserFromRecord(record);
                
                userSet.add(user);
            }
        }
        
        return userSet;
    }

    @Override
    public Set<User> getByUsername(String username) {
        Set<User> userSet = new LinkedHashSet<>();
        
        try(Session session = driver.session())
        {
            StatementResult result = session.run("MATCH (n:User) WHERE n.Username = $username RETURN n",
                    parameters("username", username));
            
            while(result.hasNext())
            {
                Record record = result.next();
                
                User user = getUserFromRecord(record);
                
                userSet.add(user);
            }
        }
        
        return userSet;
    }

    @Override
    public Set<User> getByEmail(String email) {
        Set<User> userSet = new LinkedHashSet<>();
        
        try(Session session = driver.session())
        {
            StatementResult result = session.run("MATCH (n:User) WHERE n.email = $email RETURN n",
                    parameters("email", email));
            
            while(result.hasNext())
            {
                Record record = result.next();
                
                User user = getUserFromRecord(record);
                
                userSet.add(user);
            }
        }
        
        return userSet;
    }

    @Override
    public boolean checkDuplicates(String username, String email) {
        Set<User> userSetUsername;
        Set<User> userSetEmail;
        
        userSetEmail = getByEmail(email);
        userSetUsername = getByUsername(username);
        
        return !(userSetEmail.isEmpty() && userSetUsername.isEmpty());
    }

    @Override
    public Set<User> getFiltered(String usernameFilter) {
        Set<User> userSet = new LinkedHashSet<>();
        
        userSet = getAll();
        
        return userSet;
    }
}
