package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.Entities.*;
import java.util.Date;
import java.util.Set;

public class PisaFlixServices {
    
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
            DBManager.CinemaManager.updateFavorites(cinema);
        }
        
        public static void removeFavourite(Cinema cinema, User user) {
            user.getCinemaSet().remove(cinema);
            cinema.getUserSet().remove(user);
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
    
    public static class CommentManager{
        public static void update(Comment comment){
            DBManager.CommentManager.update(comment, comment.getText());
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
