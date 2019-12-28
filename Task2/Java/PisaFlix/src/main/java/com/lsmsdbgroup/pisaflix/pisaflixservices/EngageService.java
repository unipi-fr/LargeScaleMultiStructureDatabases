package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.Entities.Engage;
import com.lsmsdbgroup.pisaflix.Entities.Entity;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.EngageManagerDatabaseInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.AuthenticationServiceInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.EngageServiceInterface;
import java.util.Set;

public class  EngageService implements EngageServiceInterface{
    private final EngageManagerDatabaseInterface engageManager;
    private final AuthenticationServiceInterface authenticationService;

    EngageService(EngageManagerDatabaseInterface engageManager, AuthenticationServiceInterface authenticationService) {
        this.engageManager = engageManager;
        this.authenticationService = authenticationService;
    }
    @Override
    public void create(User user, Film film, Entity.EntityType type){
        DBManager.engageManager.create(user, film, type);
    }

    @Override
    public void delete(String idEngage){
        DBManager.engageManager.delete(idEngage);
    }

    @Override
    public Engage getById(String engageId){
        return DBManager.engageManager.getById(engageId);
    }
    
    @Override
    public Set<Engage> getEngageSet(Entity entity, int limit, int skip){
        return DBManager.engageManager.getEngageSet(entity, limit, skip);
    }
}
