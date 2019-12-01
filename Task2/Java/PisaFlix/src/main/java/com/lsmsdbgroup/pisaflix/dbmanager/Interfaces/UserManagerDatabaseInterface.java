package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.User;
import java.util.Set;

public interface UserManagerDatabaseInterface {

    User getById(String userId);

    void create(String username, String password, String firstName, String lastName, String email, int privilegeLevel);

    void updateFavorites(User user);

    void delete(String userId);

    void clearCinemaSetAndFilmSet(User user);

    void update(User u);

    void update(String userId, String username, String firstName, String lastName, String email, String password, int privilegeLevel);

    Set<User> getAll();

    Set<User> getByUsername(String username);

    Set<User> getByEmail(String email);

    boolean checkDuplicates(String username, String email);

    Set<User> getFiltered(String nameFilter);

}
