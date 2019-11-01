package com.lsmsdbgroup.pisaflixg;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PrimaryController {

    @FXML
    private Label hwLabel;
    
    @FXML
    private void switchToSecondary() throws IOException {
        App.setMainPane("secondary");
        System.out.println(hwLabel.getText());
    }
}
