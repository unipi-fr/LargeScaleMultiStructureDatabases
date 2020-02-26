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

public class HomeController implements Initializable {

    @FXML
    private VBox postVBox;

    @FXML
    private Pagination pagination;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            if(PisaFlixServices.authenticationService.isUserLogged()){
                refreshPosts();
            }
            pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex)
                    -> refreshPosts());
        } catch (Exception ex) {
            App.printErrorDialog("Film Details", "An error occurred in inizialization", ex.toString() + "\n" + ex.getMessage());
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

    public void pagination(User user) {

        int count = PisaFlixServices.postService.countFollowed(user);
        if (count == 0) {
            pagination.pageCountProperty().setValue(1);
        } else {
            pagination.setPageCount((int) Math.ceil(count * 1.0 / PisaFlixServices.filmService.getPostPageSize() * 1.0));
        }

        refreshPosts();

    }

    public void refreshPosts() {
        
        postVBox.getChildren().clear();

        Set<Post> posts = PisaFlixServices.postService.getPostFollowed(PisaFlixServices.authenticationService.getLoggedUser(), pagination.getCurrentPageIndex());

        posts.forEach((post) -> {
            postVBox.getChildren().add(createPost(post.getUser().getUsername(), post.getTimestamp().toString(), post.getText(), post));
        });
        
    }

}
