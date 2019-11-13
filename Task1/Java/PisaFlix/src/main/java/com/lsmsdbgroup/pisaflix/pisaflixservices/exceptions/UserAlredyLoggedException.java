/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions;

/**
 *
 * @author FraRonk
 */
public class UserAlredyLoggedException extends Exception {

    public UserAlredyLoggedException(String errorMessage) {
        super(errorMessage);
    }
}
