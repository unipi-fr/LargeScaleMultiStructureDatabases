/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author FraRonk
 */
public interface ICinemaManagerDB {

    void create(String name, String address);

    Cinema getById(int cinemaId);

    Set<Cinema> getFiltered(String nameFilter, String addressFilter);

    void delete(int idCinema);

    void clearUserSet(Cinema cinema);

    void update(int idCinema, String name, String address);

    Set<Cinema> getAll();

    void updateFavorites(Cinema cinema);
}
