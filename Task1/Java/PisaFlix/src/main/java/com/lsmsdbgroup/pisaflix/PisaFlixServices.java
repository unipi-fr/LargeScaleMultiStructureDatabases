package com.lsmsdbgroup.pisaflix;

import com.lsmsdbgroup.pisaflix.Entities.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            Set<User> tmpSet = DBManager.UserManager.getByUsername(username);

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
                return "Logged as: " + loggedUser.getUsername();
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

        public static User getLoggedUser(){
            return loggedUser;
        }
    }
    
    public static class FilmManager{
        
        public static Set<Film> getAll(){
            Set<Film> films = null;
            
            films = DBManager.FilmManager.getAll();
            
            return films;
        }
        
        public static Film getById(int id){
            Film film;
            
            film = DBManager.FilmManager.getById(id);
            
            return film;
        }
        
        public static void addComment(String comment, User user, Film film){
            DBManager.CommentManager.createFilmComment(comment, user, film);
        }
        
        public static void addFavorite(Film film, User user){
            user.getFilmSet().add(film);
            film.getUserSet().add(user);
            DBManager.UserManager.updateFavorites(user);
        }
        
        public void removeFavourite(Film film, User user) {
            user.getFilmSet().remove(film);
            film.getUserSet().remove(user);
            DBManager.UserManager.updateFavorites(user);
            DBManager.FilmManager.updateFavorites(film);
        }
    }
    
    public static class CinemaManager{
        
        public static Set<Cinema> getAll(){
            Set<Cinema> cinemas = null;
            
            cinemas = DBManager.CinemaManager.getAll();
            
            return cinemas;
        }
        
        public static Cinema getById(int id){
            Cinema cinema;
            
            cinema = DBManager.CinemaManager.getById(id);
            
            return cinema;
        }
        
        public static void addComment(String comment, User user, Cinema cinema){
            DBManager.CommentManager.createCinemaComment(comment, user, cinema);
        }
        
        public static void addFavorite(Cinema cinema, User user){
            user.getCinemaSet().add(cinema);
            cinema.getUserSet().add(user);
            DBManager.UserManager.updateFavorites(user);
        }
        
        public void removeFavourite(Cinema cinema, User user) {
            user.getCinemaSet().remove(cinema);
            cinema.getUserSet().remove(user);
            DBManager.UserManager.updateFavorites(user);
            DBManager.CinemaManager.updateFavorites(cinema);
        }
    }
    
    public static class ProjectionManager {
        
        public static Set<Projection> queryProjections(int cinemaId, int filmId){
            Set<Projection> projections;
            
        public static Set<Projection> queryProjections(int cinemaId, int filmId, String date){
            Set<Projection> projections;
            
            projections = DBManager.ProjectionManager.queryProjection(cinemaId, filmId, date);
            
            return projections;
        }
    }
    
    public static class UserManager{
        
        public static void deleteLoggedAccount() throws UserNotLoggedException, InvalidPrivilegeLevelException{
            deleteUserAccount(Authentication.loggedUser);
            
        }
        
        public static void deleteUserAccount(User u) throws UserNotLoggedException, InvalidPrivilegeLevelException{
            if(!Authentication.isUserLogged()){
                throw new UserNotLoggedException("You must be logged in order to delete accounts");
            }
            
            if(Authentication.loggedUser.getIdUser()!= u.getIdUser() && Authentication.loggedUser.getPrivilegeLevel()<3 ){
                throw new InvalidPrivilegeLevelException("You must have administrator privileges in order to delete other user accounts");
            }
            DBManager.UserManager.delete(u.getIdUser());
            if(Authentication.loggedUser.getIdUser() == u.getIdUser()){
                Authentication.Logout();
            }
        }
        
        public static void changeUserPrivileges(User u, int newPrivilegeLevel) throws UserNotLoggedException, InvalidPrivilegeLevelException{
            if(!Authentication.isUserLogged()){
                throw new UserNotLoggedException("You must be logged in order to change account privileges");
            }
            if(newPrivilegeLevel < 0){
                throw new InvalidPrivilegeLevelException("Privilege level must me greater or equal than 0");
            }
            if(newPrivilegeLevel > Authentication.loggedUser.getPrivilegeLevel()){
                throw new InvalidPrivilegeLevelException("You can't set privileges greater than yours");
            }
            u.setPrivilegeLevel(newPrivilegeLevel);
            DBManager.UserManager.update(u);
        }
        
        public static class UserNotLoggedException extends Exception {

            public UserNotLoggedException(String errorMessage) {
                super(errorMessage);
            }
        }
        
        public static class InvalidPrivilegeLevelException extends Exception {

            public InvalidPrivilegeLevelException(String errorMessage) {
                super(errorMessage);
            }
        }
        public static class InvalidPrivilegeLevelValueException extends Exception {

            public InvalidPrivilegeLevelValueException(String errorMessage) {
                super(errorMessage);
            }
        }
    }
}
