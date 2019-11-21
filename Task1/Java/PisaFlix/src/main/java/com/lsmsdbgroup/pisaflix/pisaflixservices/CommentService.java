package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.CommentManagerDatabaseInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.AuthenticationServiceInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.CommentServiceInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.UserServiceInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.InvalidPrivilegeLevelException;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.UserNotLoggedException;

public class CommentService implements CommentServiceInterface {

    private final CommentManagerDatabaseInterface commentManager;
    private final UserServiceInterface userService;
    private final AuthenticationServiceInterface authenticationService;

    CommentService(CommentManagerDatabaseInterface commentManager,AuthenticationServiceInterface authenticationService, UserServiceInterface userService) {
        this.commentManager = commentManager;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @Override
    public void update(Comment comment) throws InvalidPrivilegeLevelException, UserNotLoggedException {
        if(canUpdateOrDeleteComment(comment,"update other user comment")){
            commentManager.update(comment, comment.getText());
        }
    }
    
    private boolean canUpdateOrDeleteComment(Comment comment, String operation) throws InvalidPrivilegeLevelException, UserNotLoggedException{
        if(!authenticationService.isUserLogged()){
            throw new UserNotLoggedException("You must be logged in order to " + operation);
        }
        if(authenticationService.getLoggedUser().getIdUser() != comment.getUser().getIdUser()){
            userService.checkUserPrivilegesForOperation(UserPrivileges.SOCIAL_MODERATOR, operation);
        }
        return true;
    }

    @Override
    public Comment getById(int id) {
        Comment comment;
        comment = commentManager.getById(id);
        return comment;
    }

    @Override
    public void delete(Comment comment) throws InvalidPrivilegeLevelException, UserNotLoggedException {
        if(canUpdateOrDeleteComment(comment,"delete other user comment")){
            commentManager.delete(comment.getIdComment());
        }
        
    }

    @Override
    public void addFilmComment(String comment, User user, Film film) {
        commentManager.createFilmComment(comment, user, film);
    }

    @Override
    public void addCinemaComment(String comment, User user, Cinema cinema) {
        commentManager.createCinemaComment(comment, user, cinema);
    }
}
