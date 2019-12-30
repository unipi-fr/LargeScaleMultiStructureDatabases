package com.lsmsdbgroup.pisaflixg;

import com.lsmsdbgroup.pisaflix.DateConverter;
import com.lsmsdbgroup.pisaflix.Entities.Film;
import com.lsmsdbgroup.pisaflix.pisaflixservices.PisaFlixServices;
import com.lsmsdbgroup.pisaflix.pisaflixservices.exceptions.*;
import java.net.URL;
import java.time.*;
import java.util.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

public class AddFilmController implements Initializable {

    private Film film;

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

    void setFilm(Film film) {
        this.film = film;
        resetFields();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            successLabel.setVisible(false);
            successLabel.setManaged(false);
            resetFields();
        } catch (Exception ex) {
            App.printErrorDialog("Add Film", "An error occurred loading the page", ex.toString() + "\n" + ex.getMessage());
        }
    }

    private void resetFields() {
        if (film == null) {
            titleTextField.setText("");
            descriptionTextArea.setText("");
            datePicker.setValue(null);
            addFilmButton.setText("Add film");
        } else {
            titleTextField.setText(film.getTitle());
            descriptionTextArea.setText(film.getDescription());
            Date d = film.getPublicationDate();
            long time = d.getTime();
            LocalDate toLocalDate = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDate();
            datePicker.setValue(toLocalDate);
            addFilmButton.setText("Modify film");
        }
    }

    private void errorLabel(String s) {
        successLabel.setTextFill(Color.RED);
        successLabel.setText(s);
        successLabel.setManaged(true);
        successLabel.setVisible(true);
    }

    private void addFilm() {
        try {
            boolean success = false;
            Date date = DateConverter.LocalDateToDate(datePicker.getValue());
            String title = titleTextField.getText();
            String description = descriptionTextArea.getText();

            try {
                success = PisaFlixServices.filmService.addFilm(title, date, description);
            } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
                System.out.println(ex.getMessage());
                return;
            }
            
            if(success){
                successLabel.setTextFill(Color.GREEN);
            successLabel.setText("Film successfully added!");
            successLabel.setVisible(true);
            successLabel.setManaged(true);
            resetFields();
            }else{
                successLabel.setTextFill(Color.RED);
            successLabel.setText("An indentical film already exists");
            successLabel.setVisible(true);
            successLabel.setManaged(true);
            }
        } catch (Exception ex) {
            App.printErrorDialog("Add Film", "An error occurred creating the film", ex.toString() + "\n" + ex.getMessage());
        }
    }

    private void modifyFilm() {
        try {
            Date date = DateConverter.LocalDateToDate(datePicker.getValue());
            String title = titleTextField.getText();
            String description = descriptionTextArea.getText();

            film.setTitle(title);
            film.setDescription(description);
            film.setPublicationDate(date);

            try {
                PisaFlixServices.filmService.updateFilm(film);
            } catch (UserNotLoggedException | InvalidPrivilegeLevelException ex) {
                System.out.println(ex.getMessage());
                return;
            }

            successLabel.setTextFill(Color.GREEN);
            successLabel.setText("Film modify successfully!");
            successLabel.setVisible(true);
            successLabel.setManaged(true);
            resetFields();
        } catch (Exception ex) {
            App.printErrorDialog("Modify Film", "An error occurred updating the film", ex.toString() + "\n" + ex.getMessage());
        }
    }

    @FXML
    private void clickAddFilmButton() {
        try {

            successLabel.setVisible(false);
            successLabel.setManaged(false);

            if (titleTextField.getText() == null || titleTextField.getText().isBlank()) {
                errorLabel("Title is mandatory");
                return;
            }
            if (datePicker.getValue() == null) {
                errorLabel("Date is mandatory");
                return;
            }

            if (film == null) {
                addFilm();
            } else {
                modifyFilm();
            }
        } catch (Exception ex) {
            App.printErrorDialog("Add Film", "An error occurred", ex.toString() + "\n" + ex.getMessage());
        }
    }

}
