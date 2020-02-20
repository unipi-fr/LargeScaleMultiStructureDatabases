package com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.Post;
import com.lsmsdbgroup.pisaflix.Entities.User;
import java.util.Date;

public interface PostServiceInterface {
    Post getById(Long idPost);
    
    void create(String text, User user, Film film);
    
    void delete(Long idPost);
    
    void update(Long idPost, String text);
}
