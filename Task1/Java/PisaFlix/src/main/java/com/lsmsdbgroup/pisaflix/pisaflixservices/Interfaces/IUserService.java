/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.pisaflixservices.UserPrivileges;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;

/**
 *
 * @author FraRonk
 */
public interface IUserService {        
    void deleteLoggedAccount() throws UserNotLoggedException, InvalidPrivilegeLevelException;

    User getUserById(int id);
    
    void registerUser(User user);

    void updateUser(User user);

    void deleteUserAccount(User u) throws UserNotLoggedException, InvalidPrivilegeLevelException;

    void checkUserPrivilegesForOperation(UserPrivileges privilegesToAchieve) throws UserNotLoggedException, InvalidPrivilegeLevelException;

    void checkUserPrivilegesForOperation(UserPrivileges privilegesToAchieve, String operation) throws UserNotLoggedException, InvalidPrivilegeLevelException;

    void changeUserPrivileges(User u, UserPrivileges newPrivilegeLevel) throws UserNotLoggedException, InvalidPrivilegeLevelException;
}
