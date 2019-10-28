/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdgroup.pisaflix;

import com.lsmsdgroup.pisaflix.Entities.User;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FraRonk
 */
public class PisaFlixServices {

    public static class Authentication {

        private static User loggedUser;

        static {
            loggedUser = null;
        }

        public static void Register(String username, String password, String email, String firstName, String lastName) {
            String hashedPassword = SHA256(password);
            // TODO: aggiornare il campo password nel DB ad almeno 64 caratteri e
            // Sostituire password con hashedPassword nella chiamata alla create()
            // Controllare se l'username esist già nel db
            DBManager.UserManager.create(username, password, firstName, lastName, email, 0);
        }

        public static void Login(String username, String password) throws UserAlredyLoggedException, InvalidCredentialsException {
            if (isUserLogged()) {
                throw new UserAlredyLoggedException("User is alredy logged as " + loggedUser.toString() + ".");
            }
            
            String hashedPassword = SHA256(password);
            List<User> tmpList = DBManager.UserManager.getByUsername(username);

            for (User u : tmpList) {
                // TODO: aggiornare il campo password nel DB ad almeno 64 caratteri e sostituire l'if con
                // if( u.getPassword().equals(hashedPassword) ){
                if (u.getPassword().equals(password)) {
                    loggedUser = u;
                    return;
                }
            }

            throw new InvalidCredentialsException("Invalid credentials for log in");
        }

        public static void Logout() {
            if (!isUserLogged()) {
                System.out.println("WARNING: Logout() called when alredy not logged.");
            }
            loggedUser = null;
        }

        public static boolean isUserLogged() {
            return loggedUser != null;
        }

        public static String getInfoString() {
            if (isUserLogged()) {
                return "Logged as: " + loggedUser.toString();
            } else {
                return "User not logged";
            }
        }

        private static String SHA256(String text) {
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

        public static class UserAlredyLoggedException extends Exception {

            public UserAlredyLoggedException(String errorMessage) {
                super(errorMessage);
            }
        }

        public static class InvalidCredentialsException extends Exception {

            public InvalidCredentialsException(String errorMessage) {
                super(errorMessage);
            }
        }

    }
}
