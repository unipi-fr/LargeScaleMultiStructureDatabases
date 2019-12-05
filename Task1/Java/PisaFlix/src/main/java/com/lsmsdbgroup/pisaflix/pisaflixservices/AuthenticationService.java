package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.util.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.AuthenticationServiceInterface;

public class AuthenticationService implements AuthenticationServiceInterface {

    private User loggedUser;

    private final UserManagerDatabaseInterface userManager;

    AuthenticationService(UserManagerDatabaseInterface userManager) {
        loggedUser = null;
        this.userManager = userManager;
    }

    @Override
    public void login(String username, String password) throws UserAlredyLoggedException, InvalidCredentialsException {
        if (isUserLogged()) {
            throw new UserAlredyLoggedException("User is alredy logged as " + loggedUser.toString() + ".");
        }
        Set<User> tmpSet = this.userManager.getByUsername(username);
        for (User u : tmpSet) {
            if (u.getPassword().equals(password)) {
                loggedUser = u;
                return;
            }
        }
        throw new InvalidCredentialsException("Invalid credentials for log in");
    }

    @Override
    public void logout() {
        if (!isUserLogged()) {
            System.out.println("WARNING: Logout() called when alredy not logged.");
        }
        loggedUser = null;
    }

    @Override
    public boolean isUserLogged() {
        return loggedUser != null;
    }

    @Override
    public String getInfoString() {
        if (isUserLogged()) {
            return "Logged as: " + loggedUser.getUsername();
        } else {
            return "User not logged";
        }
    }

    @Override
    public User getLoggedUser() {
        return loggedUser;
    }
}
