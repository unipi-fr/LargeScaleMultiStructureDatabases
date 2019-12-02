package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.User;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.UserManagerDatabaseInterface;

public class UserManager implements UserManagerDatabaseInterface {

    private static UserManager userManager;

    public static UserManager getIstance() {
        if (userManager == null) {
            userManager = new UserManager();
        }

        return userManager;
    }

    private UserManager() {
        throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
    }

    @Override
    public User getById(String userId) {
        // code to get a user
        User user = null;
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in retriving a user!");
        } finally {

        }
        return user;
    }

    @Override
    public void create(String username, String password, String firstName, String lastName, String email, int privilegeLevel) {
        // code to create a user
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPrivilegeLevel(privilegeLevel);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in creating the user!");
        } finally {

        }
    }

    @Override
    public void updateFavorites(User user) {
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating favorites!");
        } finally {
            
        }
    }

    @Override
    public void delete(String userId) {
        clearCinemaSetAndFilmSet(getById(userId));
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in removing a User!");
        } finally {
            
        }
    }

    @Override
    public void clearCinemaSetAndFilmSet(User user) {
        user.setCinemaSet(new LinkedHashSet<>());
        user.setFilmSet(new LinkedHashSet<>());
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred clearing the user's cinemaset and filmset!");
        } finally {
            
        }
    }

    @Override
    public void update(User user) {
        update(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getPrivilegeLevel());
    }

    @Override
    public void update(String userId, String username, String firstName, String lastName, String email, String password, int privilegeLevel) {
        // code to update a user
        User user = new User(userId);
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPrivilegeLevel(privilegeLevel);
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("A problem occurred in updating a user!");
        } finally {
            
        }
    }

    @Override
    public Set<User> getAll() {
        // code to retrieve all users
        System.out.println("Retrieving users");
        Set<User> users = null;
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retriving a user!");
        } finally {

        }
        return users;
    }

    @Override
    public Set<User> getByUsername(String username) {
        Set<User> users = null;
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retriving a user!");
        } finally {

        }
        return users;
    }

    @Override
    public Set<User> getByEmail(String email) {
        Set<User> users = null;
        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retriving a user!");
        } finally {

        }
        return users;
    }

    @Override
    public boolean checkDuplicates(String username, String email) {
        return !(getByUsername(username).isEmpty() && getByEmail(email).isEmpty());
    }

    @Override
    public Set<User> getFiltered(String nameFilter) {
        Set<User> users = null;
        String name = "";

        if (nameFilter != null) {
            name = nameFilter;
        }

        String query = "SELECT u "
                + "FROM User u "
                + "WHERE ('" + name + "'='' OR u.username LIKE '%" + name + "%') ";

        try {
            throw new UnsupportedOperationException("DA IMPLEMENTARE!!!!!!!!!!!!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("A problem occurred in retrieve users filtered!");
        } finally {

        }
        return users;
    }
}
