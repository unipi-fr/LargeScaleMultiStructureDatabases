package com.lsmsdbgroup.pisaflix.pisaflixservices;

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
