package com.lsmsdbgroup.pisaflix.dbmanager.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.User;
import java.util.HashMap;
import java.util.Set;
import org.neo4j.driver.v1.Record;

public interface UserManagerDatabaseInterface {

    int getLimit();
    
    User getById(Long userId);

    void create(String username, String password, String firstName, String lastName, String email, int privilegeLevel);

    void delete(Long userId);

    void update(User u);

    void update(Long userId, String username, String firstName, String lastName, String email, String password, int privilegeLevel);

    Set<User> getAll(int limit);

    Set<User> getByUsername(String username);

    Set<User> getByEmail(String email);

    boolean checkDuplicates(String username, String email);

    Set<User> getFiltered(String nameFilter, int limit, int skip);
    
    void follow(User follower, User followed);

    boolean isFollowing(User follower, User followed);

    void unfollow(User follower, User followed);
    
    long countFollowers(User user);
            
    HashMap<String, Long> countFollowing(User user);
    
    User getUserFromRecord(Record record);
    
    Set<User> getFollowers(User user);
    
    Set<User> getFollowingUsers(User user);
    
    Set<Film> getFollowingFilms(User user);
     
    Set<User> getSuggestedUsers(User user, int limit);
            
}
