package com.lsmsdbgroup.pisaflix;

import com.lsmsdbgroup.pisaflix.Entities.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PisaFlixServices {
    
        public enum UserPrivileges {
        NORMAL_USER(0), SOCIAL_MODERATOR(1), MODERATOR(2), ADMIN(3);

        private final int value;
        private UserPrivileges(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

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
        
        public static Set<Film> getFilmsFiltered(String titleFilter, Date startDateFilter, Date endDateFilter){
            Set<Film> films = null;
            
            films = DBManager.FilmManager.getFiltered(titleFilter,startDateFilter,endDateFilter);
            
            return films;
        }
        
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
        
        public static void addFilm(String title, Date publicationDate, String description){
            if(title == null || title.isBlank()){
                System.out.println("Il titolo non può essere vuoto");
                return;
            }
            if(publicationDate == null){
                System.out.println("la data non può essere vuota");
                return;
            }
            if(description == null){
                System.out.println("la descrizione non può essere vuota");
                return;
            }
            DBManager.FilmManager.create(title, publicationDate, description);
        }
        
        public static void deleteFilm(int idFilm) throws UserManager.UserNotLoggedException, UserManager.InvalidPrivilegeLevelException{
            PisaFlixServices.UserManager.checkUserPrivilegesForOperation(PisaFlixServices.UserPrivileges.MODERATOR, "delete a film");
            DBManager.FilmManager.delete(idFilm);
        }
        
        public static void addComment(String comment, User user, Film film){
            DBManager.CommentManager.createFilmComment(comment, user, film);
        }
        
        public static void addFavorite(Film film, User user){
            user.getFilmSet().add(film);
            film.getUserSet().add(user);
            DBManager.UserManager.updateFavorites(user);
        }
        
        public static void removeFavourite(Film film, User user) {
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
        
        public static Set<Cinema> getFiltered(String name, String address){
            Set<Cinema> cinemas = null;
            
            cinemas = DBManager.CinemaManager.getFiltered(name, address);
            
            return cinemas;
        }
        
        public static Cinema getById(int id){
            Cinema cinema;
            
            cinema = DBManager.CinemaManager.getById(id);
            
            return cinema;
        }
        
        public static void AddCinema(String name, String address) throws UserManager.UserNotLoggedException, UserManager.InvalidPrivilegeLevelException{
            PisaFlixServices.UserManager.checkUserPrivilegesForOperation(PisaFlixServices.UserPrivileges.MODERATOR, "add a new cinema");
            DBManager.CinemaManager.create(name, address);
        }
        
        public static void addComment(String comment, User user, Cinema cinema){
            DBManager.CommentManager.createCinemaComment(comment, user, cinema);
        }
        
        public static void addFavorite(Cinema cinema, User user){
            user.getCinemaSet().add(cinema);
            cinema.getUserSet().add(user);
            DBManager.UserManager.updateFavorites(user);
        }
        
        public static void removeFavourite(Cinema cinema, User user) {
            user.getCinemaSet().remove(cinema);
            cinema.getUserSet().remove(user);
            DBManager.UserManager.updateFavorites(user);
            DBManager.CinemaManager.updateFavorites(cinema);
        }

        public static void deleteCinema(Cinema cinema) throws UserManager.UserNotLoggedException, UserManager.InvalidPrivilegeLevelException {
           PisaFlixServices.UserManager.checkUserPrivilegesForOperation(PisaFlixServices.UserPrivileges.MODERATOR, "delete a cinema");
            DBManager.CinemaManager.delete(cinema.getIdCinema());
        }
    }
    
    public static class ProjectionManager {
        
        public static void addProjection(Cinema c, Film f, Date d, int room) throws UserManager.UserNotLoggedException, UserManager.InvalidPrivilegeLevelException{
            UserManager.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "add a new projection");
            DBManager.ProjectionManager.create(d, room, f, c);
        }
        
        public static void removeProjection(int projectionId){
            DBManager.ProjectionManager.delete(projectionId);
        }
            
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
        
        public static User getUserById(int id){
        
            User user = DBManager.UserManager.getById(id);
            return user;
        }
        
        public static void updateUser(User user){
            DBManager.UserManager.update(user);
        }
        
        public static void deleteUserAccount(User u) throws UserNotLoggedException, InvalidPrivilegeLevelException{
            if(!Authentication.isUserLogged()){
                throw new UserNotLoggedException("You must be logged in order to delete accounts");
            }

            if(Authentication.loggedUser.getIdUser()!= u.getIdUser() && Authentication.loggedUser.getPrivilegeLevel()< PisaFlixServices.UserPrivileges.ADMIN.getValue() ){
                throw new InvalidPrivilegeLevelException("You must have administrator privileges in order to delete other user accounts");
            }
            DBManager.UserManager.delete(u.getIdUser());
            if(Authentication.loggedUser.getIdUser() == u.getIdUser()){
                Authentication.Logout();
            }
        }
        
        public static void checkUserPrivilegesForOperation(UserPrivileges privilegesToAchieve) throws UserManager.UserNotLoggedException, UserManager.InvalidPrivilegeLevelException{
            checkUserPrivilegesForOperation(privilegesToAchieve, "do this operation");
        }
        
        public static void checkUserPrivilegesForOperation(UserPrivileges privilegesToAchieve, String operation) throws UserManager.UserNotLoggedException, UserManager.InvalidPrivilegeLevelException{
            if(!PisaFlixServices.Authentication.isUserLogged()){
                throw new UserManager.UserNotLoggedException("You must be logged in order to "+operation);
            }
            if(PisaFlixServices.Authentication.getLoggedUser().getPrivilegeLevel() < privilegesToAchieve.getValue() ){
                throw new UserManager.InvalidPrivilegeLevelException("You don't have enought privilege to "+operation);
            }
        }
        
        public static void changeUserPrivileges(User u, int newPrivilegeLevel) throws UserNotLoggedException, InvalidPrivilegeLevelException{
            if(!Authentication.isUserLogged()){
                throw new UserNotLoggedException("You must be logged in order to change account privileges");
            }
            if(newPrivilegeLevel < PisaFlixServices.UserPrivileges.NORMAL_USER.getValue()){
                throw new InvalidPrivilegeLevelException("Privilege level must me greater or equal than Normal user");
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
    
    public static class CommentManager{
        public static void update(Comment comment){
            int id = comment.getIdComment();
            String text = comment.getText();
            
            DBManager.CommentManager.update(id, text);
        }
        
        public static Comment getById(int id){
            Comment comment;
            
            comment = DBManager.CommentManager.getById(id);
            
            return comment;
        }
        
        public static void delete(int id){
            DBManager.CommentManager.delete(id);
        }
    }
}
