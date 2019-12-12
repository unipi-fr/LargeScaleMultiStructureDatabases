package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.util.Set;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.CinemaManagerDatabaseInterface;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.UserManagerDatabaseInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.*;

public class CinemaService implements CinemaServiceInterface {

    private final CinemaManagerDatabaseInterface cinemaManager;
    private final AuthenticationServiceInterface authenticationService;
    private final UserManagerDatabaseInterface userManager;

    public CinemaService(CinemaManagerDatabaseInterface commentManager, AuthenticationServiceInterface authenticationService, UserManagerDatabaseInterface userManager) {
        this.cinemaManager = commentManager;
        this.authenticationService = authenticationService;
        this.userManager = userManager;
    }

    @Override
    public Set<Cinema> getAll() {
        Set<Cinema> cinemas = null;

        cinemas = cinemaManager.getAll(0, 0);

        return cinemas;
    }

    @Override
    public Set<Cinema> getFiltered(String name, String address) {
        Set<Cinema> cinemas = null;

        cinemas = cinemaManager.getFiltered(name, address, 0, 0);

        return cinemas;
    }

    @Override
    public Cinema getById(String id) {
        Cinema cinema;

        cinema = cinemaManager.getById(id);

        return cinema;
    }

    @Override
    public void addCinema(String name, String address) throws UserNotLoggedException, InvalidPrivilegeLevelException {
        authenticationService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "add a new cinema");
        cinemaManager.create(name, address);
    }

    @Override
    public void updateCinema(Cinema cinema) throws UserNotLoggedException, InvalidPrivilegeLevelException {
        authenticationService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "update a cinema");
        cinemaManager.update(cinema.getId(), cinema.getName(), cinema.getAddress());
    }

    @Override
    public void deleteCinema(Cinema cinema) throws UserNotLoggedException, InvalidPrivilegeLevelException {
        authenticationService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "delete a cinema");
        cinemaManager.delete(cinema.getId());
    }
    
    @Override
    public void addFavorite(Cinema cinema, User user){
        int counter = cinema.getFavoriteCounter();
        counter += 1;
        
        cinema.setFavoriteCounter(counter);
        
        cinemaManager.updateFavorites(cinema);
        
        user.getCinemaSet().add(cinema);
        
        userManager.updateFavoritesCinema(user);
    }

    @Override
    public void removeFavourite(Cinema cinema, User user){
        int counter = cinema.getFavoriteCounter();
        counter -= 1;
        
        cinema.setFavoriteCounter(counter);
        
        cinemaManager.updateFavorites(cinema);
        
        user.getCinemaSet().remove(cinema);
        
        userManager.updateFavoritesCinema(user);
    }

    @Override
    public void refreshCommentSet(Cinema cinema) {
        cinema.setCommentSet(PisaFlixServices.commentService.getCommentSet(cinema));
    }   

}
