package com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.util.Set;

public interface CinemaServiceInterface {

    Set<Cinema> getAll();

    Set<Cinema> getFiltered(String name, String address);

    Cinema getById(int id);

    void AddCinema(String name, String address) throws UserNotLoggedException, InvalidPrivilegeLevelException;

    void addFavorite(Cinema cinema, User user);

    void removeFavourite(Cinema cinema, User user);

    void deleteCinema(Cinema cinema) throws UserNotLoggedException, InvalidPrivilegeLevelException;
}
