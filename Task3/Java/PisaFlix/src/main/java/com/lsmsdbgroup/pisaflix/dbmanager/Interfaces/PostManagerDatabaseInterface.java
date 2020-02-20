package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.*;

public interface PostManagerDatabaseInterface {
            
    Post getById(Long idPost);
    
    void create(String text, User user, Film film);
    
    void delete(Long idPost);
    
    void update(Long idPost, String text);
}
