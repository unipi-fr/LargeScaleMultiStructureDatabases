package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.*;

public class PisaFlixServices {
    public static IAuthenticationService authenticationService;
    public static IUserService userService;
    public static IFilmService filmService;
    public static ICinemaService cinemaService;
    public static ICommentService commentService;
    public static IProjectionService projectionService;
    
    static{
        authenticationService = new AuthenticationService(DBManager.userManager);
        userService = new UserService(DBManager.userManager, authenticationService);
        filmService = new FilmService(DBManager.filmManager,userService);
        cinemaService = new CinemaService(DBManager.cinemaMamager,userService);
        commentService = new CommentService(DBManager.commentManager);
        projectionService = new ProjectionService(DBManager.projectionManager,userService);
    }
}
