package com.lsmsdbgroup.pisaflixg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class App extends Application {

    private static Scene scene;
    private static MainPageController mainPageController;

    static void resetLogin() {
        try {
            mainPageController.resetLogin();
        } catch (Exception ex) {
            App.printErrorDialog("PisaFlix", "An error occurred", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        try {
            try {
                scene = new Scene(loadFXML("MainPage"), 1280, 720);
                stage.setScene(scene);
                stage.setTitle("Pisaflix");
                stage.getIcons().add(new Image("/img/PF.png"));
                stage.show();
            } catch (IOException ex) {
                App.printErrorDialog("PisaFlix", "I/O Error", ex.toString() + "\n" + ex.getMessage());
            }
        } catch (Exception ex) {
            App.printErrorDialog("PisaFlix", "An error occurred starting the application", ex.toString() + "\n" + ex.getMessage());
        }
    }

    static void setRoot(String fxml) throws IOException {
        try {
            scene.setRoot(loadFXML(fxml));
        } catch (IOException ex) {
            App.printErrorDialog("PisaFlix", "An error occurred", ex.toString() + "\n" + ex.getMessage());
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        } catch (Exception ex) {
            App.printErrorDialog("PisaFlix", "An error occurred", ex.toString() + "\n" + ex.getMessage());
        }
        return fxmlLoader.load();
    }

    public static void setMainPageController(MainPageController m) {
        mainPageController = m;
    }

    public static Object setMainPageReturnsController(String fxml) {
        return mainPageController.setMainPaneReturnsController(fxml);
    }

    public static void setMainPane(Pane pane) {
        mainPageController.setMainPane(pane);
    }

    public static void main(String[] args) {
        try {
            launch();
        } catch (Exception ex) {
            App.printErrorDialog("PisaFlix", "An error occurred lunching the application", ex.toString() + "\n" + ex.getMessage());
        }
    }

    public static boolean printConfirmationDialog(String title, String header, String content) {
        Optional<ButtonType> result = null;
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);

            URL resource = App.class.getResource("/styles/PisaFlix.css");

            if (resource != null) {
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(resource.toExternalForm());
            }

            result = alert.showAndWait();
        } catch (Exception ex) {
            App.printErrorDialog("PisaFlix", "An error occurred", ex.toString() + "\n" + ex.getMessage());
        }
        return result.get() == ButtonType.OK;
    }

    public static boolean printWarningDialog(String title, String header, String content) {
        Optional<ButtonType> result = null;
        try {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            URL resource = App.class.getResource("/styles/PisaFlix.css");
            if (resource != null) {
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(resource.toExternalForm());
            }
            result = alert.showAndWait();
        } catch (Exception ex) {
            App.printErrorDialog("PisaFlix", "An error occurred", ex.toString() + "\n" + ex.getMessage());
        }
        return result.get() == ButtonType.OK;
    }

    public static void printErrorDialog(String title, String header, String content) {
        printDialog(title, header, content, Alert.AlertType.ERROR);
    }

    private static void printDialog(String title, String header, String content, Alert.AlertType type) {
        try {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);

            URL resource = App.class.getResource("/styles/PisaFlix.css");

            if (resource != null) {
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(resource.toExternalForm());
            }

            alert.showAndWait();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
