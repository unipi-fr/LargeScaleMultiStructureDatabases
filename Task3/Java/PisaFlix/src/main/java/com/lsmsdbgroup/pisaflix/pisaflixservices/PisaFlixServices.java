package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.*;

public class PisaFlixServices {

    public final static AuthenticationServiceInterface authenticationService;
    public final static UserServiceInterface userService;
    public final static FilmServiceInterface filmService;
    public final static PostServiceInterface postService;
    
    static {
        authenticationService = new AuthenticationService(DBManager.userManager);
        userService = new UserService(DBManager.userManager, authenticationService);
        filmService = new FilmService(DBManager.filmManager, authenticationService);
        postService = new PostService(DBManager.postManager, authenticationService);
    }
}
