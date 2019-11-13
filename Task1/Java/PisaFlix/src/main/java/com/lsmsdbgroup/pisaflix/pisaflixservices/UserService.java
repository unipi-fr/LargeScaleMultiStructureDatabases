/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.IUserManagerDB;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.IAuthenticationService;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.IUserService;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;

/**
 *
 * @author FraRonk
 */
public class UserService implements IUserService {
    private IAuthenticationService as;
    private IUserManagerDB um;
    
    
    UserService(IUserManagerDB userManager, IAuthenticationService authenticationService){
        as = authenticationService;
        um = userManager;
    }
    @Override
    public void deleteLoggedAccount() throws UserNotLoggedException, InvalidPrivilegeLevelException{
        deleteUserAccount(as.getLoggedUser());
    }
        
    @Override
    public User getUserById(int id){

        User user = um.getById(id);
        return user;
    }
        
    @Override
    public void updateUser(User user){
        um.update(user);
    }
        
    @Override
    public void deleteUserAccount(User u) throws UserNotLoggedException, InvalidPrivilegeLevelException{
        if(!as.isUserLogged()){
            throw new UserNotLoggedException("You must be logged in order to delete accounts");
        }

        if(as.getLoggedUser().getIdUser()!= u.getIdUser() && as.getLoggedUser().getPrivilegeLevel()< UserPrivileges.ADMIN.getValue() ){
            throw new InvalidPrivilegeLevelException("You must have administrator privileges in order to delete other user accounts");
        }
        um.delete(u.getIdUser());
        if(as.getLoggedUser().getIdUser() == u.getIdUser()){
            as.Logout();
        }
    }
        
    @Override
    public void checkUserPrivilegesForOperation(UserPrivileges privilegesToAchieve) throws UserNotLoggedException, InvalidPrivilegeLevelException{
        checkUserPrivilegesForOperation(privilegesToAchieve, "do this operation");
    }
        
    @Override
    public void checkUserPrivilegesForOperation(UserPrivileges privilegesToAchieve, String operation) throws UserNotLoggedException, InvalidPrivilegeLevelException{
            if(!as.isUserLogged()){
                throw new UserNotLoggedException("You must be logged in order to "+operation);
            }
            if(as.getLoggedUser().getPrivilegeLevel() < privilegesToAchieve.getValue() ){
                throw new InvalidPrivilegeLevelException("You don't have enought privilege to "+operation);
            }
        }
        
    @Override
    public void changeUserPrivileges(User u, UserPrivileges newPrivilegeLevel) throws UserNotLoggedException, InvalidPrivilegeLevelException{
            if(!as.isUserLogged()){
                throw new UserNotLoggedException("You must be logged in order to change account privileges");
            }
            if(newPrivilegeLevel.getValue() < UserPrivileges.NORMAL_USER.getValue()){
                throw new InvalidPrivilegeLevelException("Privilege level must me greater or equal than Normal user");
            }
            if(newPrivilegeLevel.getValue() > as.getLoggedUser().getPrivilegeLevel()){
                throw new InvalidPrivilegeLevelException("You can't set privileges greater than yours");
            }
            u.setPrivilegeLevel(newPrivilegeLevel.getValue());
            um.update(u);
        }

    @Override
    public void register(String username, String password, String firstName, String lastName, String email) {
        //TODO fare il check dei campi se c'è tempo
        um.create(username, password, firstName, lastName, email, 0);
    }
}
