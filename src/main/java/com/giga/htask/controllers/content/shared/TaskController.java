package com.giga.htask.controllers.content.shared;

import com.giga.htask.controllers.content.ContentController;
import com.giga.htask.model.Context;
import com.giga.htask.model.Task;
import com.giga.htask.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskController extends ContentController implements Initializable {

    Task task;


    public TaskController(Integer taskId) {
        super(Context.getInstance().getLoggedUser().getId());
        task = Context.getInstance().getEntityById(Task.class, taskId);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        contentTitle.setText("Task #" + task.getId() + " of Patient " + task.getDoctorPatient().getPatient().getName()+ " " + task.getDoctorPatient().getPatient().getSurname());
    }

}
