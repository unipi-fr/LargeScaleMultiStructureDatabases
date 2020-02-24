package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.FilmManagerDatabaseInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.*;

public class FilmService implements FilmServiceInterface {

    private final FilmManagerDatabaseInterface filmManager;
    private final AuthenticationServiceInterface authenticationService;
    private final int postPageSize = 20;

    FilmService(FilmManagerDatabaseInterface filmManager, AuthenticationServiceInterface authenticationService) {
        this.filmManager = filmManager;
        this.authenticationService = authenticationService;
    }

    @Override
    public Set<Film> getFilmsFiltered(String titleFilter, Date startDateFilter, Date endDateFilter, int limit, int skip) {
        Set<Film> films = null;
        films = filmManager.getFiltered(titleFilter, startDateFilter, endDateFilter, limit, skip);
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
    
    @Override
    public void follow(Film film, User user){
        filmManager.follow(film, user);
    }
    
    @Override
    public boolean isFollowing(Film film, User user){
        return filmManager.isFollowing(film, user);
    }

    @Override
    public void unfollow(Film film, User user) {
       filmManager.unfollow(film, user); 
    }

    @Override
    public long countFollowers(Film film) {
       return filmManager.countFollowers(film);
    }

    @Override
    public Set<User> getFollowers(Film film) {
      return filmManager.getFollowers(film);
    }

    @Override
    public Set<Film> getSuggestedFilms(User user, int limit) {
        return filmManager.getSuggestedFilms(user, limit);
    }
    
    @Override
    public Set<Film> getVerySuggestedFilms(User user, int limit) {
        return filmManager.getVerySuggestedFilms(user, limit);
    }
    
    @Override
    public Set<Film> getFriendCommentedFilms(User user, int limit) {
        return filmManager.getFriendCommentedFilms(user, limit);
    }

    @Override
    public Set<Film> getMixSuggestedRecent(User user) {
        
        Set<Film> mix= filmManager.getVerySuggestedFilms(user, 0);
        
        if(mix.size() < filmManager.getLimit()){
            
            mix.addAll(filmManager.getSuggestedFilms(user, filmManager.getLimit() - mix.size()));
            
        }
        
        if(mix.size() < filmManager.getLimit()){
            
            mix.addAll(filmManager.getFriendCommentedFilms(user, filmManager.getLimit() - mix.size()));
            
        }
        
        if(mix.size() < filmManager.getLimit()){
            
            mix.addAll(filmManager.getDifferentFilms(mix, filmManager.getLimit() - mix.size()));
            
        }
        
        return mix;
        
    }

    @Override
    public Set<Post> getRelatedPosts(Film film, int page) {
        return filmManager.getRelatedPosts(film, postPageSize, postPageSize*page);
    }

    @Override
    public int getPostPageSize() {
        return postPageSize;
    }

    @Override
    public Set<Film> getDifferentFilms(Set<Film> filmSet, int limit) {
        return filmManager.getDifferentFilms(filmSet, limit);
    }
    
}
