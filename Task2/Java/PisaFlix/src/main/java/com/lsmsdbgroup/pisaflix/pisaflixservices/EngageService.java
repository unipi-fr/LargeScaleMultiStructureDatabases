package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.Entities.Engage.EngageType;
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
    public void create(User user, Film film, EngageType type){
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
    public Set<Engage> getEngageSet(Entity entity, int limit, int skip, EngageType type){
        return DBManager.engageManager.getEngageSet(entity, limit, skip, type);
    }

    @Override
    public void deleteFiltred(User user, Film film, EngageType type) {
        DBManager.engageManager.deleteFiltred(user, film, type);
    }

    @Override
    public long count(Entity entity, EngageType type) {
        return DBManager.engageManager.count(entity, type);
    }
    
    @Override
    public boolean isAlreadyPresent(User userLogged, Film film, EngageType entityType){
        if (DBManager.engageManager.count(userLogged, film, entityType) > 0){
            return true;
        }else{
            return(false);
        }
    }
}
