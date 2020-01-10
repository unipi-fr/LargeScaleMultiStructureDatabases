package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.Entities.Engage.EngageType;
import java.util.Set;

public interface EngageManagerDatabaseInterface {
    
    void create(User user, Film film, EngageType type);

    void delete(String idEngage);
    
    void deleteAllRelated(Entity entity);

    Engage getById(String engageId);
    
    Set<Engage> getEngageSet(Entity entity, int limit, int skip , EngageType type );

    void deleteFiltred(User user, Film film, EngageType type);
    
    long count(Entity entity, EngageType type);
    
    long count(User user, Film film, EngageType type);
}
