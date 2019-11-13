package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.*;

public class PisaFlixServices {
    public static IAuthenticationService Authentication;
    public static IUserService UserManager;
    public static IFilmService FilmManager;
    public static ICinemaService CinemaManager;
    public static ICommentService CommentManager;
    public static IProjectionService ProjectionManager;
    
    static{
        Authentication = new AuthenticationService(DBManager.um);
        UserManager = new UserService(DBManager.um, Authentication);
        FilmManager = new FilmService(DBManager.fm,DBManager.com,UserManager);
        CinemaManager = new CinemaService(DBManager.cm,DBManager.com,UserManager);
        CommentManager = new CommentService(DBManager.com);
        ProjectionManager = new ProjectionService(DBManager.pm,UserManager);
    }
}
