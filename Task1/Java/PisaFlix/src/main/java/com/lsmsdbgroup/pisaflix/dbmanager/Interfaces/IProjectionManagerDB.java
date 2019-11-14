/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.Projection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author FraRonk
 */
public interface IProjectionManagerDB {

    void create(Date dateTime, int room, Film film, Cinema cinema);

    void delete(int idProjection);

    void update(int idProjection, Date dateTime, int room);

    Set<Projection> getAll();

    Projection getById(int projectionId);

    Set<Projection> queryProjection(int cinemaId, int filmId, String date, int room);
    
    boolean checkDuplicates(int cinemaId, int filmId, String date, int room);

}
