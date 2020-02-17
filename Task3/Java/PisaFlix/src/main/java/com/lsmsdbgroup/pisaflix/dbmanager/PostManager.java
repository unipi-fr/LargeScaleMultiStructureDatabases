package com.lsmsdbgroup.pisaflix.dbmanager;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.Post;
import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.PostManagerDatabaseInterface;
import java.util.Date;
import org.neo4j.driver.v1.Driver;

public class PostManager implements PostManagerDatabaseInterface{

    private static PostManager postManager;
    private Driver driver;
    
    public static PostManager getIstance() {
        if (postManager == null) {
            postManager = new PostManager();
        }
        
        return postManager;
    }

    private PostManager() {
        driver = DBManager.getDB();
    }
    
    @Override
    public Post getById(Long idPost) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(String text, Date timestamp, User user, Film film) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Long idPost) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Long idPost, String text, Date timestamp, User user, Film film) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
