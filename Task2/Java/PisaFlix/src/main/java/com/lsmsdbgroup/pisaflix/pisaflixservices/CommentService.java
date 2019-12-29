package com.lsmsdbgroup.pisaflix.pisaflixservices;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.dbmanager.DBManager;
import com.lsmsdbgroup.pisaflix.dbmanager.Interfaces.CommentManagerDatabaseInterface;
import com.lsmsdbgroup.pisaflix.pisaflixservices.Interfaces.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.util.Objects;

public class CommentService implements CommentServiceInterface {

    private final CommentManagerDatabaseInterface commentManager;
    private final AuthenticationServiceInterface authenticationService;

    CommentService(CommentManagerDatabaseInterface commentManager, AuthenticationServiceInterface authenticationService) {
        this.commentManager = commentManager;
        this.authenticationService = authenticationService;
    }

    @Override
    public void update(Comment comment) throws InvalidPrivilegeLevelException, UserNotLoggedException {
        if (canUpdateOrDeleteComment(comment, "update other user comment")) {
            if(comment.isRecent()){
              DBManager.filmManager.updateComment(comment);
            }else{
              commentManager.update(comment, comment.getText());  
            }
        }
    }

    private boolean canUpdateOrDeleteComment(Comment comment, String operation) throws InvalidPrivilegeLevelException, UserNotLoggedException {
        if (!authenticationService.isUserLogged()) {
            throw new UserNotLoggedException("You must be logged in order to " + operation);
        }
        if (!Objects.equals(authenticationService.getLoggedUser().getId(), comment.getUser().getId())) {
            authenticationService.checkUserPrivilegesForOperation(UserPrivileges.SOCIAL_MODERATOR, operation);
        }
        return true;
    }

    @Override
    public Comment getById(String id) {
        Comment comment;
        comment = commentManager.getById(id);
        return comment;
    }

    @Override
    public void delete(Comment comment) throws InvalidPrivilegeLevelException, UserNotLoggedException {
        if (canUpdateOrDeleteComment(comment, "delete other user comment")) {
            commentManager.delete(comment);
            comment.getFilm().getCommentSet().remove(comment);
        }

    }

    @Override
    public void addComment(String text, User user, Entity entity) {
        commentManager.createComment(text, user, entity, null);
    }
    
}
