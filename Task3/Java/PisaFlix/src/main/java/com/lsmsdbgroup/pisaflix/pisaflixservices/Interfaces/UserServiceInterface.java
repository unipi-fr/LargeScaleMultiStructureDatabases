package com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.pisaflixservices.UserPrivileges;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.util.Set;

public interface UserServiceInterface {

    void deleteLoggedAccount() throws UserNotLoggedException, InvalidPrivilegeLevelException;

    User getById(Long id);

    Set<User> getAll(int limit);

    Set<User> getFiltered(String nameFilter, int limit, int skip);

    void register(String username, String password, String email, String firstName, String lastName) throws InvalidFieldException;

    void updateUser(User user);

    void deleteUserAccount(User u) throws UserNotLoggedException, InvalidPrivilegeLevelException;

    void changeUserPrivileges(User u, UserPrivileges newPrivilegeLevel) throws UserNotLoggedException, InvalidPrivilegeLevelException;
    
    void follow(User follower, User followed);

    boolean isFollowing(User follower, User followed);

    void unfollow(User follower, User followed);
    
    long countFollowers(User user);
            
    long countFollowingUsers(User user);
    
    long countFollowingFilms(User user);
    
    long countTotalFollowing(User user);
    
    Set<User> getFollowers(User user);
    
    Set<User> getFollowingUsers(User user);
    
    Set<Film> getFollowingFilms(User user);
    
    Set<User> getSuggestedUsers(User user, int limit);
    
    Set<User> getMixedUsers(User user);
}
