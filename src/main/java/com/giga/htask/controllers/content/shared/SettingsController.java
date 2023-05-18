package com.giga.htask.controllers.content.shared;

import com.giga.htask.App;
import com.giga.htask.controllers.MainAuthedController;
import com.giga.htask.controllers.content.ContentController;
import com.giga.htask.model.Context;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController extends ContentController implements Initializable {

    @FXML
    RadioButton radioButtonIsDarkMode;

    public SettingsController() {
        super(Context.getInstance().getLoggedUser().getId());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (Context.getInstance().isDarkMode()) {
            radioButtonIsDarkMode.setSelected(true);
        } else {
            radioButtonIsDarkMode.setSelected(false);
        }

        radioButtonIsDarkMode.setOnAction(event -> {
            Context.getInstance().setDarkMode(radioButtonIsDarkMode.isSelected());
            App.changeTheme();
        });
    }
}
