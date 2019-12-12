package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.FilmManagerDatabaseInterface;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.UserManagerDatabaseInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.*;

public class FilmService implements FilmServiceInterface {

    private final FilmManagerDatabaseInterface filmManager;
    private final AuthenticationServiceInterface authenticationService;
    private final UserManagerDatabaseInterface userManager;

    FilmService(FilmManagerDatabaseInterface filmManager, AuthenticationServiceInterface authenticationService, UserManagerDatabaseInterface userManager) {
        this.filmManager = filmManager;
        this.authenticationService = authenticationService;
        this.userManager = userManager;
    }

    @Override
    public Set<Film> getFilmsFiltered(String titleFilter, Date startDateFilter, Date endDateFilter) {
        Set<Film> films = null;
        films = filmManager.getFiltered(titleFilter, startDateFilter, endDateFilter, 0, 0);
        return films;
    }

    @Override
    public Set<Film> getAll() {
        Set<Film> films = null;
        films = filmManager.getAll(0, 0);
        return films;
    }

    @Override
    public Film getById(String id) {
        Film film;
        film = filmManager.getById(id);
        return film;
    }

    @Override
    public void addFilm(String title, Date publicationDate, String description) throws UserNotLoggedException, InvalidPrivilegeLevelException {
        authenticationService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "add a film");
        if (title == null || title.isBlank()) {
            System.out.println("Title can't be empty");
            return;
        }
        if (publicationDate == null) {
            System.out.println("Date can't be empty");
            return;
        }
        filmManager.create(title, publicationDate, description);
    }

    @Override
    public void deleteFilm(String idFilm) throws UserNotLoggedException, InvalidPrivilegeLevelException {
        authenticationService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "delete a film");
        filmManager.delete(idFilm);
    }

    @Override
    public void updateFilm(Film film) throws UserNotLoggedException, InvalidPrivilegeLevelException {
        authenticationService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "update a film");
        filmManager.update(film.getId(), film.getTitle(), film.getPublicationDate(), film.getDescription());
    }
    
    
    
    @Override
    public void addFavorite(Film film, User user){
        Set<Film> films = user.getFilmSet();
        
        if (films.contains(film))
           return;
        
        int counter = film.getFavoriteCounter();
        counter += 1;
        
        film.setFavoriteCounter(counter);
        
        filmManager.updateFavorites(film);
        
        user.getFilmSet().add(film);
        
        userManager.updateFavoritesFilm(user);
    }

    @Override
    public void removeFavourite(Film film, User user){
        Set<Film> films = user.getFilmSet();
        
        if (!films.contains(film))
           return;
        
        int counter = film.getFavoriteCounter();
        counter -= 1;
        
        film.setFavoriteCounter(counter);
        
        filmManager.updateFavorites(film);
        
        user.getFilmSet().remove(film);
        
        userManager.updateFavoritesFilm(user);
    }
}
