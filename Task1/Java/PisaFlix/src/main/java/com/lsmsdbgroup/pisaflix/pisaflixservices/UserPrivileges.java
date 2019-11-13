/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsmsdbgroup.pisaflix.pisaflixservices;

/**
 *
 * @author FraRonk
 */
public enum UserPrivileges {
    NORMAL_USER(0), SOCIAL_MODERATOR(1), MODERATOR(2), ADMIN(3);

    private final int value;
    private UserPrivileges(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
