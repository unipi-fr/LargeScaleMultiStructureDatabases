package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.User;
import java.util.Set;

public interface UserManagerDatabaseInterface {

    User getById(Long userId);

    void create(String username, String password, String firstName, String lastName, String email, int privilegeLevel);

    void delete(Long userId);

    void update(User u);

    void update(Long userId, String username, String firstName, String lastName, String email, String password, int privilegeLevel);

    Set<User> getAll();

    Set<User> getByUsername(String username);

    Set<User> getByEmail(String email);

    boolean checkDuplicates(String username, String email);

    Set<User> getFiltered(String nameFilter);
    
    void follow(User follower, User followed);

    boolean isFollowing(User follower, User followed);

    void unfollow(User follower, User followed);
    
    long countFollowers(User user);
            
    long countFollowing(User user);

}
