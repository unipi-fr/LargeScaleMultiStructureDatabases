package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.User;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.UserManagerDatabaseInterface;
import org.neo4j.driver.v1.Driver;

public class UserManager implements UserManagerDatabaseInterface {

    private static UserManager UserManager;
    
    private final Driver driver;

    public static UserManager getIstance() {
        if (UserManager == null) {
            UserManager = new UserManager();
        }
        return UserManager;
    }

    public UserManager() {
        driver = DBManager.getDB();
    }

    @Override
    public User getById(Long idUser) {
        User user = null;
        
        return user;
    }

    @Override
    public void create(String username, String password, String firstName, String lastName, String email, int privilegeLevel) {
        
    }

    @Override
    public void delete(Long idUser) {
        
    }

    @Override
    public void update(User user) {
        
    }

    @Override
    public void update(Long userId, String username, String firstName, String lastName, String email, String password, int privilegeLevel) {
        
    }

    @Override
    public Set<User> getAll() {
        Set<User> userSet = new LinkedHashSet<>();
        
        return userSet;
    }

    @Override
    public Set<User> getByUsername(String username) {
        Set<User> userSet = new LinkedHashSet<>();
        
        return userSet;
    }

    @Override
    public Set<User> getByEmail(String email) {
        Set<User> userSet = new LinkedHashSet<>();
        
        return userSet;
    }

    @Override
    public boolean checkDuplicates(String username, String email) {
        Set<User> userSet = new LinkedHashSet<>();
        
        return !userSet.isEmpty();
    }

    @Override
    public Set<User> getFiltered(String usernameFilter) {
        Set<User> userSet = new LinkedHashSet<>();
        
        return userSet;
    }
}
