package com.giga.htask.controllers.content.shared;

import com.giga.htask.controllers.content.ContentController;
import com.giga.htask.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class VisitController extends ContentController implements Initializable {


    User user;

    @FXML
    VBox contentTasks;

    public VisitController(Integer userId) {
        super(userId);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        contentTitle.setText("Visit of " + user.getName() + " " + user.getSurname());

        //show calendar with tasks of user here
    }

}
