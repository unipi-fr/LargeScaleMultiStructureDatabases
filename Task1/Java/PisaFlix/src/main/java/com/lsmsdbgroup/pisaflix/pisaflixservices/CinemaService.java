/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.ICinemaManagerDB;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.ICommentManagerDB;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.ICinemaService;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.IUserService;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.util.Set;

/**
 *
 * @author alessandromadonna
 */
public class CinemaService implements ICinemaService{
    private final ICinemaManagerDB cm;
    private final IUserService us;
    
    public CinemaService(ICinemaManagerDB cm, IUserService us) {
        this.cm = cm;
        this.us = us;
    }
    
    @Override
    public Set<Cinema> getAll(){
        Set<Cinema> cinemas = null;

        cinemas = cm.getAll();

        return cinemas;
    }

    @Override
    public Set<Cinema> getFiltered(String name, String address){
        Set<Cinema> cinemas = null;

        cinemas = cm.getFiltered(name, address);

        return cinemas;
    }

    @Override
    public Cinema getById(int id){
        Cinema cinema;

        cinema = cm.getById(id);

        return cinema;
    }

    @Override
    public void AddCinema(String name, String address) throws UserNotLoggedException, InvalidPrivilegeLevelException{
        us.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "add a new cinema");
        cm.create(name, address);
    }

    @Override
    public void addFavorite(Cinema cinema, User user){
        user.getCinemaSet().add(cinema);
        cinema.getUserSet().add(user);
        cm.updateFavorites(cinema);
    }

    @Override
    public void removeFavourite(Cinema cinema, User user) {
        user.getCinemaSet().remove(cinema);
        cinema.getUserSet().remove(user);
        cm.updateFavorites(cinema);
    }

    @Override
    public void deleteCinema(Cinema cinema) throws UserNotLoggedException, InvalidPrivilegeLevelException {
       us.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "delete a cinema");
       cm.delete(cinema.getIdCinema());
    }

}
