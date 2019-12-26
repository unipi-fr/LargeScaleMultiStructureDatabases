package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.Engage;
import com.lsmsdbgroup.pisaflix.Entities.Entity;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.User;
import java.util.Set;

public interface EngageManagerDatabaseInterface {
    
    void create(User user, Film film, Entity.EntityType type);

    void delete(String idEngage);
    
    void deleteAllRelated(Entity entity);

    Engage getById(String engageId);
    
    Set<Engage> getEngageSet(Entity entity, int limit, int skip);
}
