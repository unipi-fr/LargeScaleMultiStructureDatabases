/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.Cinema;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.Projection;
import com.lsmsdbgroup.pisaflix.pisaflixservices.UserPrivileges;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author alessandromadonna
 */
public interface IProjectionService {

    void addProjection(Cinema c, Film f, Date d, int room) throws UserNotLoggedException, InvalidPrivilegeLevelException;

    void removeProjection(int projectionId);

    Set<Projection> queryProjections(int cinemaId, int filmId, String date);
}
