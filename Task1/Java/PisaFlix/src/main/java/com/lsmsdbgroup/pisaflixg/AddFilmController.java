package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflix.pisaflixservices.UserPrivileges;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

public class AddFilmController implements Initializable {
    @FXML
    private TextField titleTextField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Label successLabel;

    @FXML
    private Button addFilmButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        successLabel.setVisible(false);
        successLabel.setManaged(false);
        resetFields();
    } 
    
    private void resetFields(){
        titleTextField.setText("");
        descriptionTextArea.setText("");
        datePicker.setValue(null);
        
    }
    
    private void errorLabel(String s){       
        successLabel.setTextFill(Color.RED);
        successLabel.setText(s);
        successLabel.setManaged(true);
        successLabel.setVisible(true);
    }
    
    @FXML
    private void clickAddFilmButton(){
        successLabel.setVisible(false);
        successLabel.setManaged(false);
        try {
            PisaFlixServices.UserManager.checkUserPrivilegesForOperation(UserPrivileges.MODERATOR, "add a new film");
        } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
            System.out.println(ex.getMessage());
            return;
        }
        
        if(titleTextField.getText() == null || titleTextField.getText().isBlank()){
            errorLabel("Title is mandatory");
            return;
        }
        if(datePicker.getValue() == null){
            errorLabel("Date is mandatory");
            return;
        }
        Date date = Date.from(datePicker.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        String title = titleTextField.getText();
        String description = descriptionTextArea.getText();
        
        PisaFlixServices.FilmManager.addFilm(title, date, description);
        
        successLabel.setTextFill(Color.GREEN);
        successLabel.setText("Film successfully added!");
        successLabel.setVisible(true);
        successLabel.setManaged(true);
        resetFields();
    }
    
}
