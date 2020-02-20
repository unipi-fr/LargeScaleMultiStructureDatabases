package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.Post;
import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.PostManagerDatabaseInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.AuthenticationServiceInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.PostServiceInterface;

public class PostService implements PostServiceInterface{

    private final PostManagerDatabaseInterface postManager;
    private final AuthenticationServiceInterface authenticationService;
    
    PostService(PostManagerDatabaseInterface postManager, AuthenticationServiceInterface authenticationService){
        this.postManager = postManager;
        this.authenticationService = authenticationService;
    }
    
    @Override
    public Post getById(Long idPost) {
        return postManager.getById(idPost);
    }

    @Override
    public void create(String text, User user, Film film) {
        postManager.create(text, user, film);
    }

    @Override
    public void delete(Long idPost) {
        DBManager.postManager.delete(idPost);
    }

    @Override
    public void update(Long idPost, String text) {
        DBManager.postManager.update(idPost, text);
    }
    
}
