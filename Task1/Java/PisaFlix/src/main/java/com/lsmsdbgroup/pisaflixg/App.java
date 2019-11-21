package com.lsmsdbgroup.pisaflixg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class App extends Application {

    private static Scene scene;
    private static MainPageController mainPageController;


    static void resetLogin() {
        mainPageController.resetLogin();
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("MainPage"), 640, 480);
        stage.setScene(scene);
        stage.getIcons().add(new Image("/img/Cinema.png"));
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void setMainPageController(MainPageController m){
        mainPageController = m;
    }
    
    public static Object setMainPageReturnsController(String fxml){
        return mainPageController.setMainPaneReturnsController(fxml);
    }
    
    
    public static void setMainPane(Pane pane){
        mainPageController.setMainPane(pane);
    }
    
    public static void main(String[] args) {
        launch();
    }
    
    public static boolean printConfirmationDialog(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        
        URL resource = App.class.getResource("/styles/PisaFlix.css");
        
        if(resource != null){
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(resource.toExternalForm());
        }

        Optional<ButtonType> result = alert.showAndWait();
        
        return result.get() == ButtonType.OK;
    }
    
    public static boolean printWarningDialog(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        
        URL resource = App.class.getResource("/styles/PisaFlix.css");
        
        if(resource != null){
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(resource.toExternalForm());
        }

        Optional<ButtonType> result = alert.showAndWait();
        
        return result.get() == ButtonType.OK;
    }
    
    public static void printErrorDialog(String title, String header, String content){
        printDialog(title, header, content, Alert.AlertType.ERROR);
    }
    
    private static void printDialog(String title, String header, String content, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        
        URL resource = App.class.getResource("/styles/PisaFlix.css");
        
        if(resource != null){
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(resource.toExternalForm());
        }
        
        alert.showAndWait();
    }

}