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
    public Set<Film> getFilmsFiltered(String titleFilter, Date startDateFilter, Date endDateFilter, double adultnessMargin) {
        Set<Film> films = null;
        films = filmManager.getFiltered(titleFilter, startDateFilter, endDateFilter, 0, 0, adultnessMargin);
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
    public boolean addFilm(String title, Date publicationDate, String description) throws UserNotLoggedException, InvalidPrivilegeLevelException {
        authenticationService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "add a film");
        if (title == null || title.isBlank()) {
            System.out.println("Title can't be empty");
            return false;
        }
        if (publicationDate == null) {
            System.out.println("Date can't be empty");
            return false;
        }
        return filmManager.create(title, publicationDate, description);
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
    public void getRecentComments(Film film){
        filmManager.getRecentComments(film);
    }
    
    @Override
    public void addComment(Film film, User user, String text){
        filmManager.addComment(film, user, text);
    }
    
        
    @Override
    public long getCommentPageSize(){
       return filmManager.getCommentPageSize();
    }
    
    @Override
    public void getCommentPage(Film film, int page){
        int pageSize = (int) getCommentPageSize();
        if(page == 0){
            getRecentComments(film);
        }else{
           filmManager.getComments(film, (page - 1)*pageSize, pageSize); 
        }
    }
    
}
