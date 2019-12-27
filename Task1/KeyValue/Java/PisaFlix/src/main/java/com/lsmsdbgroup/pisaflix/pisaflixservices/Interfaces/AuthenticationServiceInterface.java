package com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;

public interface AuthenticationServiceInterface {

    void Register(String username, String password, String email, String firstName, String lastName);

    void login(String username, String password) throws UserAlredyLoggedException, InvalidCredentialsException;

    void logout();

    boolean isUserLogged();

    String getInfoString();

    User getLoggedUser();
}
