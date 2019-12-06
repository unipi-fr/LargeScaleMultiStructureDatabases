package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;
import java.util.logging.*;
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

        String hashedPassword = SHA256(password);
        Set<User> tmpSet = userManager.getByUsername(username, 0, 0);

        for (User u : tmpSet) {
            if( u.getPassword().equals(hashedPassword) ){
            //if (u.getPassword().equals(password)) {
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

    private String SHA256(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Change this to UTF-16 if needed
            md.update(text.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            String hex = String.format("%064x", new BigInteger(1, digest));

            return hex;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PisaFlixServices.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public User getLoggedUser() {
        return loggedUser;
    }
    
    @Override
    public void checkUserPrivilegesForOperation(UserPrivileges privilegesToAchieve) throws UserNotLoggedException, InvalidPrivilegeLevelException {
        checkUserPrivilegesForOperation(privilegesToAchieve, "do this operation");
    }

    @Override
    public void checkUserPrivilegesForOperation(UserPrivileges privilegesToAchieve, String operation) throws UserNotLoggedException, InvalidPrivilegeLevelException {
        if (!this.isUserLogged()) {
            throw new UserNotLoggedException("You must be logged in order to " + operation);
        }
        if (this.loggedUser.getPrivilegeLevel() < privilegesToAchieve.getValue()) {
            throw new InvalidPrivilegeLevelException("You don't have enought privilege to " + operation);
        }
    }
}
