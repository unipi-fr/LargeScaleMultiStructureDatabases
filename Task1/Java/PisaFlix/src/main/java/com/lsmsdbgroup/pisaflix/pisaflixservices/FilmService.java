package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.util.Date;
import java.util.Set;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.FilmManagerDatabaseInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.*;

public class FilmService implements FilmServiceInterface {

    private final FilmManagerDatabaseInterface filmManager;
    private final UserServiceInterface userService;

    FilmService(FilmManagerDatabaseInterface filmManager, UserServiceInterface userService) {
        this.filmManager = filmManager;
        this.userService = userService;
    }

    @Override
    public Set<Film> getFilmsFiltered(String titleFilter, Date startDateFilter, Date endDateFilter) {
        Set<Film> films = null;
        films = filmManager.getFiltered(titleFilter, startDateFilter, endDateFilter);
        return films;
    }

    @Override
    public Set<Film> getAll() {
        Set<Film> films = null;
        films = filmManager.getAll();
        return films;
    }

    @Override
    public Film getById(int id) {
        Film film;
        film = filmManager.getById(id);
        return film;
    }

    @Override
    public void addFilm(String title, Date publicationDate, String description) throws UserNotLoggedException, InvalidPrivilegeLevelException {
        userService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "add a film");
        if (title == null || title.isBlank()) {
            System.out.println("Title can't be empty");
            return;
        }
        if (publicationDate == null) {
            System.out.println("Date can't be empty");
            return;
        }
        if (description == null) {
            System.out.println("Description can't be empty");
            return;
        }
        filmManager.create(title, publicationDate, description);
    }

    @Override
    public void deleteFilm(int idFilm) throws UserNotLoggedException, InvalidPrivilegeLevelException {
        userService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "delete a film");
        filmManager.delete(idFilm);
    }
    
    @Override
    public void updateFilm(Film film) throws UserNotLoggedException, InvalidPrivilegeLevelException {
        userService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "update a film");
        filmManager.update(film.getIdFilm(), film.getTitle(), film.getPublicationDate(), film.getDescription());
    }

    @Override
    public void addFavorite(Film film, User user) {
        user.getFilmSet().add(film);
        film.getUserSet().add(user);
        filmManager.updateFavorites(film);
    }

    @Override
    public void removeFavourite(Film film, User user) {
        user.getFilmSet().remove(film);
        film.getUserSet().remove(user);
        filmManager.updateFavorites(film);
    }

    

}
