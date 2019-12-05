package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.ProjectionManagerDatabaseInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.*;

public class ProjectionService implements ProjectionServiceInterface {

    private final ProjectionManagerDatabaseInterface projectionManager;

    private final UserServiceInterface userManager;

    public ProjectionService(ProjectionManagerDatabaseInterface projectionManager, UserServiceInterface userManager) {
        this.projectionManager = projectionManager;
        this.userManager = userManager;
    }

    @Override
    public void addProjection(Cinema cinema, Film film, Date date, int room) throws UserNotLoggedException, InvalidPrivilegeLevelException {
        userManager.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "add a new projection");
        projectionManager.create(date, room, film, cinema);
    }

    @Override
    public void removeProjection(int projectionId) {
        projectionManager.delete(projectionId);
    }

    @Override
    public Set<Projection> queryProjections(int cinemaId, int filmId, String date, int room) {
        Set<Projection> projections;
        projections = projectionManager.queryProjection(cinemaId, filmId, date, room);
        return projections;
    }

    @Override
    public boolean checkDuplicates(int cinemaId, int filmId, String date, int room) {
        return projectionManager.checkDuplicates(cinemaId, filmId, date, room);
    }
}
