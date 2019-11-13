/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.User;
import java.util.Set;

public interface IUserManagerDB {

    User getById(int userId) ;

    void create(String username, String password, String firstName, String lastName, String email, int privilegeLevel);

    void updateFavorites(User user);

    void delete(int userId);

    void clearCinemaSetAndFilmSet(User user);

    void update(User u);

    void update(int userId, String username, String firstName, String lastName, String email, String password, int privilegeLevel);

    Set<User> getAll();

    Set<User> getByUsername(String username);

}
