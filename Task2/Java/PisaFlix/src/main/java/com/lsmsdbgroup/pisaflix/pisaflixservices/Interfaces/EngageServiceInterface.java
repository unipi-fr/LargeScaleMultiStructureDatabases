package com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.Entities.Engage.EngageType;
import java.util.Set;

public interface EngageServiceInterface {
    void create(User user, Film film, EngageType type);

    void delete(String idEngage);
    
    Engage getById(String engageId);
    
    Set<Engage> getEngageSet(Entity entity, int limit, int skip, EngageType type);

    void deleteFiltred(User user, Film film, EngageType type);
    
    long count(Entity entity, EngageType type);

    boolean isAlreadyPresent(User userLogged, Film film, EngageType entityType);
}
