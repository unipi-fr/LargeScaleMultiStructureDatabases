package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.*;

public class PisaFlixServices {

    public static final AuthenticationServiceInterface authenticationService;

    public static final UserServiceInterface userService;

    public static final FilmServiceInterface filmService;

    public static final CinemaServiceInterface cinemaService;

    public static final CommentServiceInterface commentService;

    public static final ProjectionServiceInterface projectionService;

    static {
        authenticationService = new AuthenticationService(DBManager.userManager);
        userService = new UserService(DBManager.userManager, authenticationService);
        filmService = new FilmService(DBManager.filmManager, userService);
        cinemaService = new CinemaService(DBManager.cinemaMamager, userService);
        commentService = new CommentService(DBManager.commentManager, authenticationService, userService);
        projectionService = new ProjectionService(DBManager.projectionManager, userService);
    }
}
