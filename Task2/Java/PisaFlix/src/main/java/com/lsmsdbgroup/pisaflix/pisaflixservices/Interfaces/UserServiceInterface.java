package com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.pisaflixservices.UserPrivileges;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.util.Set;

public interface UserServiceInterface {

    void deleteLoggedAccount() throws UserNotLoggedException, InvalidPrivilegeLevelException;

    User getById(String id);

    Set<User> getAll();

    Set<User> getFiltered(String nameFilter);

    void register(String username, String password, String email, String firstName, String lastName) throws InvalidFieldException;

    void updateUser(User user);

    void deleteUserAccount(User u) throws UserNotLoggedException, InvalidPrivilegeLevelException;

    void changeUserPrivileges(User u, UserPrivileges newPrivilegeLevel) throws UserNotLoggedException, InvalidPrivilegeLevelException;

    void getFavourites(User user);
}
