package com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.util.*;

public interface ProjectionServiceInterface {

    void addProjection(Cinema c, Film f, Date d, int room) throws UserNotLoggedException, InvalidPrivilegeLevelException;

    void removeProjection(int projectionId);

    Set<Projection> queryProjections(String cinemaId, String filmId, String date, int room);

    boolean checkDuplicates(String cinemaId, String filmId, String date, int room);

}
