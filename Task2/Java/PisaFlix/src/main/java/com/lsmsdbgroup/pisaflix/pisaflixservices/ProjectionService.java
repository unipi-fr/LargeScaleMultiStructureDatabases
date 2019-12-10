package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.DateConverter;
import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.util.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.ProjectionManagerDatabaseInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.*;
import java.text.SimpleDateFormat;

public class ProjectionService implements ProjectionServiceInterface {

    private final ProjectionManagerDatabaseInterface projectionManager;
    private final AuthenticationServiceInterface authenticationService;

    public ProjectionService(ProjectionManagerDatabaseInterface projectionManager, AuthenticationServiceInterface authenticationService) {
        this.projectionManager = projectionManager;
        this.authenticationService = authenticationService;
    }

    @Override
    public void addProjection(Cinema cinema, Film film, Date date, int room) throws UserNotLoggedException, InvalidPrivilegeLevelException, InvalidFieldException {
        authenticationService.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "add a new projection");
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(date);
        /*if (checkDuplicates(cinema.getId(), film.getId(), formattedDate, room)) {
            throw new InvalidFieldException("a projection arledy exists with those fields");
        }*/
        projectionManager.create(date, room, film, cinema);
    }

    @Override
    public void removeProjection(String projectionId) {
        projectionManager.delete(projectionId);
    }

    @Override
    public Set<Projection> queryProjections(Cinema cinema, Film film, Date startDate,Date endDate, int room) {
        Set<Projection> projections;
        
        String cinemaID = "-1";
        String filmID = "-1";
        
        if(cinema != null){
            cinemaID = cinema.getId();
        }
        if(film != null){
            filmID = film.getId();
        }
        
        projections = projectionManager.queryProjection(cinemaID, filmID, startDate, endDate, room, 0, 0);

        return projections;
    }

    private boolean checkDuplicates(String cinemaId, String filmId, Date date, int room) {
        Date startDate = date;
        Date endDate = DateConverter.addMinutesToDate(date, 5);

        Set<Projection> projections = projectionManager.queryProjection(cinemaId, filmId, startDate, endDate, room, 0, 0);
        return !projections.isEmpty();
    }
}
