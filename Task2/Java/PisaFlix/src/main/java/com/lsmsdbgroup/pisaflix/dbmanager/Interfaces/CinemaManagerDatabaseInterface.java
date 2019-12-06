package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import java.util.Set;

public interface CinemaManagerDatabaseInterface {

    void create(String name, String address);

    Cinema getById(String cinemaId);

    Set<Cinema> getFiltered(String nameFilter, String addressFilter, int limit, int skip);

    void delete(String idCinema);

    void clearUserSet(Cinema cinema);

    void update(String idCinema, String name, String address);

    Set<Cinema> getAll(int limit, int skip);

    void updateFavorites(Cinema cinema);
}
