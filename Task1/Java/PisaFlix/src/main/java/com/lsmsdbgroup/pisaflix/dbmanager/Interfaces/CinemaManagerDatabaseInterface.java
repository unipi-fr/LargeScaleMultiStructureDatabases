package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import java.util.Set;

public interface CinemaManagerDatabaseInterface {

    void create(String name, String address);

    Cinema getById(int cinemaId);

    Set<Cinema> getFiltered(String nameFilter, String addressFilter);

    void delete(int idCinema);

    void clearUserSet(Cinema cinema);

    void update(int idCinema, String name, String address);

    Set<Cinema> getAll();

    void updateFavorites(Cinema cinema);
}
