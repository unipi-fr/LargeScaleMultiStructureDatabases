package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.*;
import java.util.Set;

public interface PostManagerDatabaseInterface {
            
    Post getById(Long idPost);
    
    void create(String text, User user, Set<Film> films);
    
    void delete(Long idPost);
    
    void update(Long idPost, String text);
}
