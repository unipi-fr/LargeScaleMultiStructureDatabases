package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.Entities.Entity;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.Post;
import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.PostManagerDatabaseInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.AuthenticationServiceInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.PostServiceInterface;
import java.util.Set;

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
    public void create(String text, User user, Set<Film> films) {
        postManager.create(text, user, films);
    }

    @Override
    public void delete(Long idPost) {
        DBManager.postManager.delete(idPost);
    }

    @Override
    public void update(Long idPost, String text) {
        DBManager.postManager.update(idPost, text);
    }

    @Override
    public int count(Entity entity) {
       return DBManager.postManager.count(entity); 
    }

    @Override
    public Set<Post> getPostFollowed(User user, int currentPageIndex) {
        return DBManager.postManager.getPostFollowed(user, DBManager.postManager.getLimit()*currentPageIndex);
    }

    @Override
    public int countPostFollowed(User user) {
        return DBManager.postManager.countPostFollowed(user);
    }

    @Override
    public Set<Post> getUserPosts(User user, int currentPageIndex) {
        return DBManager.postManager.getUserPosts(user, DBManager.postManager.getLimit()*currentPageIndex);
    }

    @Override
    public int countUserPosts(User user) {
        return DBManager.postManager.countPostFollowed(user);
    }

    @Override
    public int getHomePostPerPageLimit() {
        return DBManager.postManager.getLimit();
    }
  
}
