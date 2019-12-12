package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.*;

public class PisaFlixServices {

    public final static AuthenticationServiceInterface authenticationService;
    public final static UserServiceInterface userService;
    public final static FilmServiceInterface filmService;
    public final static CinemaServiceInterface cinemaService;
    public final static CommentServiceInterface commentService;
    public final static ProjectionServiceInterface projectionService;

    static {
        authenticationService = new AuthenticationService(DBManager.userManager);
        userService = new UserService(DBManager.userManager, authenticationService);
        filmService = new FilmService(DBManager.filmManager, authenticationService, DBManager.userManager);
        cinemaService = new CinemaService(DBManager.cinemaMamager, authenticationService, DBManager.userManager);
        commentService = new CommentService(DBManager.commentManager, authenticationService);
        projectionService = new ProjectionService(DBManager.projectionManager, authenticationService);
    }
}
