package com.giga.htask.controllers.content.admin;

import com.giga.htask.controllers.content.ContentController;
import com.giga.htask.model.Context;
import com.giga.htask.model.User;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class EditPatientController extends ContentController implements Initializable {

    User user;
    public EditPatientController(int userId) {
        super();
        user = Context.getInstance().getEntityById(User.class, userId);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        contentTitle.setText("Edit Patient: " + user.getName() + " " + user.getSurname());
    }

}
