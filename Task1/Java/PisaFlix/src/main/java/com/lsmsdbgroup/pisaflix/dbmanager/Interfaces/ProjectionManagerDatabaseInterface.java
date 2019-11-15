package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.Projection;
import java.util.Date;
import java.util.Set;

public interface ProjectionManagerDatabaseInterface {

    void create(Date dateTime, int room, Film film, Cinema cinema);

    void delete(int idProjection);

    void update(int idProjection, Date dateTime, int room);

    Set<Projection> getAll();

    Projection getById(int projectionId);

    Set<Projection> queryProjection(int cinemaId, int filmId, String date, int room);

    boolean checkDuplicates(int cinemaId, int filmId, String date, int room);

}
