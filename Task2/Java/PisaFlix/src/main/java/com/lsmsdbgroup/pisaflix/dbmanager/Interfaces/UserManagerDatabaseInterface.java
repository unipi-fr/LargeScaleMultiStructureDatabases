package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.User;
import java.util.Set;

public interface UserManagerDatabaseInterface {

    User getById(String userId);

    void create(String username, String password, String firstName, String lastName, String email, int privilegeLevel);

    void delete(String userId);

    void update(User u);

    void update(String userId, String username, String firstName, String lastName, String email, String password, int privilegeLevel);

    Set<User> getAll(int limit, int skip);

    Set<User> getByUsername(String username, int limit, int skip);

    Set<User> getByEmail(String email, int limit, int skip);

    boolean checkDuplicates(String username, String email, int limit, int skip);

    Set<User> getFiltered(String nameFilter, int limit, int skip);

}
