package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.Projection;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.IProjectionManagerDB;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.IProjectionService;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.IUserService;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.util.Date;
import java.util.Set;

public class ProjectionService implements IProjectionService{
    private IProjectionManagerDB pm;
    private IUserService us;
    
    public ProjectionService(IProjectionManagerDB pm, IUserService us) {
        this.pm = pm;
        this.us = us;
    }

    @Override
    public void addProjection(Cinema c, Film f, Date d, int room) throws UserNotLoggedException, InvalidPrivilegeLevelException{
        us.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "add a new projection");
        pm.create(d, room, f, c);
    }

    @Override
    public void removeProjection(int projectionId){
        pm.delete(projectionId);
    }

    @Override
    public Set<Projection> queryProjections(int cinemaId, int filmId, String date){
        Set<Projection> projections;

        projections = pm.queryProjection(cinemaId, filmId, date);

        return projections;
    }
}
