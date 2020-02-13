package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.FilmManagerDatabaseInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.*;

public class FilmService implements FilmServiceInterface {

    private final FilmManagerDatabaseInterface filmManager;
    private final AuthenticationServiceInterface authenticationService;

    FilmService(FilmManagerDatabaseInterface filmManager, AuthenticationServiceInterface authenticationService) {
        this.filmManager = filmManager;
        this.authenticationService = authenticationService;
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
    public Film getById(Long id) {
        Film film;
        film = filmManager.getById(id);
        return film;
    }

    @Override
    public boolean addFilm(String title, Date publicationDate) throws UserNotLoggedException, InvalidPrivilegeLevelException {
        authenticationService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "add a film");
        if (title == null || title.isBlank()) {
            System.out.println("Title can't be empty");
            return false;
        }
        if (publicationDate == null) {
            System.out.println("Date can't be empty");
            return false;
        }
        return filmManager.create(title, publicationDate);
    }

    @Override
    public void deleteFilm(Long idFilm) throws UserNotLoggedException, InvalidPrivilegeLevelException {
        authenticationService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "delete a film");
        filmManager.delete(idFilm);
    }

    @Override
    public void updateFilm(Film film) throws UserNotLoggedException, InvalidPrivilegeLevelException {
        authenticationService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "update a film");
        filmManager.update(film.getId(), film.getTitle(), film.getPublicationDate());
    }
    
}
