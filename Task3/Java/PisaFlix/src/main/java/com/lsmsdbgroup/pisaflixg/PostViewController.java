package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.Entities.*;
import com.lsmsdbgroup.pisaflix.pisaflixservices.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class PostViewController implements Initializable {

    private int type = 0;
    
    @FXML
    private VBox postVBox;

    @FXML
    private Pagination pagination;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(type == 0)
        {
            try {
                if (PisaFlixServices.authenticationService.isUserLogged()) {
                    int count = PisaFlixServices.postService.countPostFollowed(PisaFlixServices.authenticationService.getLoggedUser());
                    pagination(count);
                }
                
                pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex)
                        -> refreshPosts(PisaFlixServices.postService.getPostFollowed(PisaFlixServices.authenticationService.getLoggedUser(), pagination.getCurrentPageIndex())));
            } catch (Exception ex) {
                App.printErrorDialog("Home", "An error occurred in inizialization", ex.toString() + "\n" + ex.getMessage());
            }
        }
    }

    private Pane createPost(String username, String timestamp, String postStr, Post post) {
        Pane pane = new Pane();
        try {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Post.fxml"));
                PostController postController = new PostController(username, timestamp, postStr, 0);
                postController.setPost(post);
                loader.setController(postController);
                pane = loader.load();
            } catch (IOException ex) {
                App.printErrorDialog("Film Details", "IOException", ex.toString() + "\n" + ex.getMessage());
            }
        } catch (Exception ex) {
            App.printErrorDialog("Film Details", "An error occurred creating the post", ex.toString() + "\n" + ex.getMessage());
        }
        return pane;
    }

    public void pagination(int count) {
        if (count == 0) {
            pagination.pageCountProperty().setValue(1);
        } else {
            System.out.println();
            pagination.setPageCount((int) Math.ceil(count * 1.0 / PisaFlixServices.postService.getHomePostPerPageLimit() * 1.0));
        }

        Set<Post> posts = PisaFlixServices.postService.getPostFollowed(PisaFlixServices.authenticationService.getLoggedUser(), pagination.getCurrentPageIndex());
        
        refreshPosts(posts);

    }

    public void refreshPosts(Set<Post> posts) {

        postVBox.getChildren().clear();

        posts.forEach((post) -> {
            postVBox.getChildren().add(createPost(post.getUser().getUsername(), post.getTimestamp().toString(), post.getText(), post));
        });

    }

}
