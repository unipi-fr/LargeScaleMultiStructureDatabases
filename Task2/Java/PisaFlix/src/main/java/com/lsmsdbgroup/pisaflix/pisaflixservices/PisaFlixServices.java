package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.*;

public class PisaFlixServices {

    public final static AuthenticationServiceInterface authenticationService;
    public final static UserServiceInterface userService;
    public final static FilmServiceInterface filmService;
    public final static CommentServiceInterface commentService;
    public final static EngageServiceInterface engageService;
    public final static AnalyticsServiceInterface analyticService;
    
    static {
        authenticationService = new AuthenticationService(DBManager.userManager);
        userService = new UserService(DBManager.userManager, authenticationService);
        filmService = new FilmService(DBManager.filmManager, authenticationService);
        commentService = new CommentService(DBManager.commentManager, authenticationService);
        engageService = new EngageService(DBManager.engageManager, authenticationService);
        analyticService = new AnalyticsService(DBManager.analyticsManager);
    }
}
