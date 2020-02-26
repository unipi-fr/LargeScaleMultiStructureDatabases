package com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.Entity;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.Post;
import com.lsmsdbgroup.pisaflix.Entities.User;
import java.util.Set;

public interface PostServiceInterface {
    Post getById(Long idPost);
    
    void create(String text, User user, Set<Film> films);
    
    void delete(Long idPost);
    
    void update(Long idPost, String text);
    
    int count(Entity entity);
    
    Set<Post> getPostFollowed(User user, int currentPageIndex);

    public int countFollowed(User user);

}
