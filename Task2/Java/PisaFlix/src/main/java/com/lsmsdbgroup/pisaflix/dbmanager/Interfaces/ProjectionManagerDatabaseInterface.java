package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.*;
import java.util.*;

public interface ProjectionManagerDatabaseInterface {

    void create(Date dateTime, int room, Film film, Cinema cinema);

    void delete(String idProjection);

    void update(String idProjection, Date dateTime, int room);

    Set<Projection> getAll(int limit, int skip);

    Projection getById(String projectionId);

    Set<Projection> queryProjection(String cinemaId, String filmId, Date startDate, Date endDate, int room, int limit, int skip);

}
