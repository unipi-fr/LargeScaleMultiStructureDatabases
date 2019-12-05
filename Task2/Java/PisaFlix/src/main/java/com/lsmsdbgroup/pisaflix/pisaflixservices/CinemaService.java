package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.util.Set;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.CinemaManagerDatabaseInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.*;

public class CinemaService implements CinemaServiceInterface {

    private final CinemaManagerDatabaseInterface commentManager;
    private final AuthenticationServiceInterface authenticationService;

    public CinemaService(CinemaManagerDatabaseInterface commentManager, AuthenticationServiceInterface authenticationService) {
        this.commentManager = commentManager;
        this.authenticationService = authenticationService;
    }

    @Override
    public Set<Cinema> getAll() {
        Set<Cinema> cinemas = null;

        cinemas = commentManager.getAll();

        return cinemas;
    }

    @Override
    public Set<Cinema> getFiltered(String name, String address) {
        Set<Cinema> cinemas = null;

        cinemas = commentManager.getFiltered(name, address);

        return cinemas;
    }

    @Override
    public Cinema getById(String id) {
        Cinema cinema;

        cinema = commentManager.getById(id);

        return cinema;
    }

    @Override
    public void addCinema(String name, String address) throws UserNotLoggedException, InvalidPrivilegeLevelException {
        authenticationService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "add a new cinema");
        commentManager.create(name, address);
    }

    @Override
    public void updateCinema(Cinema cinema) throws UserNotLoggedException, InvalidPrivilegeLevelException {
        authenticationService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "update a cinema");
        commentManager.update(cinema.getId(), cinema.getName(), cinema.getAddress());
    }

    @Override
    public void deleteCinema(Cinema cinema) throws UserNotLoggedException, InvalidPrivilegeLevelException {
        authenticationService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "delete a cinema");
        commentManager.delete(cinema.getId());
    }

    @Override
    public void addFavorite(Cinema cinema, User user) {
        user.getCinemaSet().add(cinema);
        cinema.getUserSet().add(user);
        commentManager.updateFavorites(cinema);
    }

    @Override
    public void removeFavourite(Cinema cinema, User user) {
        user.getCinemaSet().remove(cinema);
        cinema.getUserSet().remove(user);
        commentManager.updateFavorites(cinema);
    }

    @Override
    public void refreshCommentSet(Cinema cinema) {
        cinema.setCommentSet(PisaFlixServices.commentService.getCommentSet(cinema));
    }   

}
