/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces;

import com.lsmsdbgroup.pisaflix.Entities.User;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;

/**
 *
 * @author FraRonk
 */
public interface IAuthenticationService {
        void Register(String username, String password, String email, String firstName, String lastName);

        void Login(String username, String password) throws UserAlredyLoggedException, InvalidCredentialsException ;

        void Logout();

        boolean isUserLogged();

        String getInfoString();

        User getLoggedUser();
    }
