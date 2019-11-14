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
    private final IProjectionManagerDB projectionManager;
    private final IUserService userManager;
    
    public ProjectionService(IProjectionManagerDB projectionManager, IUserService userManager) {
        this.projectionManager = projectionManager;
        this.userManager = userManager;
    }

    @Override
    public void addProjection(Cinema cinema, Film film, Date date, int room) throws UserNotLoggedException, InvalidPrivilegeLevelException{
        userManager.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "add a new projection");
        projectionManager.create(date, room, film, cinema);
    }

    @Override
    public void removeProjection(int projectionId){
        projectionManager.delete(projectionId);
    }

    @Override
    public Set<Projection> queryProjections(int cinemaId, int filmId, String date, int room){
        Set<Projection> projections;

        projections = projectionManager.queryProjection(cinemaId, filmId, date, room);

        return projections;
    }
    
    @Override
    public boolean checkDuplicates(int cinemaId, int filmId, String date, int room){
        return projectionManager.checkDuplicates(cinemaId, filmId, date, room);
    }
}
