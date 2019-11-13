/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.IAuthenticationService;
import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.*;
import com.lsmsdbgroup.pisaflix.dbmanager.UserManager;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthenticationService implements IAuthenticationService{
    private User loggedUser;
    private final IUserManagerDB um;

    AuthenticationService(IUserManagerDB userManager) {
        loggedUser = null;
        um = userManager;
    }
    
    public void Register(String username, String password, String email, String firstName, String lastName) {
            String hashedPassword = SHA256(password);
            // TODO: aggiornare il campo password nel DB ad almeno 64 caratteri e
            // Sostituire password con hashedPassword nella chiamata alla create()
            // Controllare se l'username esist già nel db
            um.create(username, password, firstName, lastName, email, 0);
        }

    @Override
        public void Login(String username, String password) throws UserAlredyLoggedException, InvalidCredentialsException {
            if (isUserLogged()) {
                throw new UserAlredyLoggedException("User is alredy logged as " + loggedUser.toString() + ".");
            }
            
            String hashedPassword = SHA256(password);
            Set<User> tmpSet = um.getByUsername(username);

            for (User u : tmpSet) {
                // TODO: aggiornare il campo password nel DB ad almeno 64 caratteri e sostituire l'if con
                // if( u.getPassword().equals(hashedPassword) ){
                if (u.getPassword().equals(password)) {
                    loggedUser = u;
                    return;
                }
            }

            throw new InvalidCredentialsException("Invalid credentials for log in");
        }

        public void Logout() {
            if (!isUserLogged()) {
                System.out.println("WARNING: Logout() called when alredy not logged.");
            }
            loggedUser = null;
        }

        public boolean isUserLogged() {
            return loggedUser != null;
        }

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

        public User getLoggedUser(){
            return loggedUser;
        }
}
