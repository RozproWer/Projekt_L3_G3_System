package com.giga.htask.controllers.content.admin;

import com.giga.htask.controllers.MainAuthedController;
import com.giga.htask.controllers.content.ContentController;
import com.giga.htask.model.Context;
import com.giga.htask.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;


import java.net.URL;
import java.util.ResourceBundle;

public class EditDoctorController extends ContentController implements Initializable {

    User user;

    @FXML
    TableView doctorPatientsTable;
    @FXML
    TableColumn idColumn;
    @FXML
    TableColumn nameColumn;
    @FXML
    TableColumn surnameColumn;
    @FXML
    TableColumn emailColumn;
    @FXML
    TableColumn peselColumn;
    @FXML
    TableColumn editColumn;
    /*
    @FXML
    TableColumn tasksColumn;
    @FXML
    TableColumn visitsColumn;
*/
    public EditDoctorController(int userId) {
        super();
      user = Context.getInstance().getEntityById(User.class, userId);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        contentTitle.setText("Edit Doctor: " + user.getName() + " " + user.getSurname());

        idColumn.setCellValueFactory(new PropertyValueFactory<User,Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<User,String>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<User,String>("surname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<User,String>("email"));
        peselColumn.setCellValueFactory(new PropertyValueFactory<User,String>("pesel"));
        editColumn.setCellValueFactory(new PropertyValueFactory<User,Integer>("id"));
      /*
        tasksColumn.setCellValueFactory(new PropertyValueFactory<User,Integer>("id"));
        visitsColumn.setCellValueFactory(new PropertyValueFactory<User,Integer>("id"));
*/
        Callback<TableColumn<User, Integer>, TableCell<User, Integer>> cellEditFactory =
                new Callback<>() {
                    @Override
                    public TableCell call(final TableColumn<User, Integer> column) {
                        final TableCell<User, Integer> cell = new TableCell<User, Integer>() {
                            final Button btn = new Button(column.getText());

                            @Override
                            public void updateItem(Integer userId, boolean empty) {
                                super.updateItem(userId, empty);
                                if (empty) {

                                    setGraphic(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        MainAuthedController.getInstance().addTab("Edit patient", "content/admin/EditPatient",new EditPatientController(userId),true);
                                    });
                                    setGraphic(btn);
                                }
                                setText(null);
                            }
                        };
                        return cell;
                    }
                };
        Callback<TableColumn<User, Integer>, TableCell<User, Integer>> cellTasksFactory =
                new Callback<>() {
                    @Override
                    public TableCell call(final TableColumn<User, Integer> column) {
                        final TableCell<User, Integer> cell = new TableCell<User, Integer>() {
                            final Button btn = new Button(column.getText());

                            @Override
                            public void updateItem(Integer userId, boolean empty) {
                                super.updateItem(userId, empty);
                                if (empty) {

                                    setGraphic(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        MainAuthedController.getInstance().addTab("Patient Tasks", "content/admin/EditTasksPatient",new EditTaskPatientController(userId),true);
                                    });
                                    setGraphic(btn);
                                }
                                setText(null);
                            }
                        };
                        return cell;
                    }
                };
        Callback<TableColumn<User, Integer>, TableCell<User, Integer>> cellVisitsFactory =
                new Callback<>() {
                    @Override
                    public TableCell call(final TableColumn<User, Integer> column) {
                        final TableCell<User, Integer> cell = new TableCell<User, Integer>() {
                            final Button btn = new Button(column.getText());

                            @Override
                            public void updateItem(Integer userId, boolean empty) {
                                super.updateItem(userId, empty);
                                if (empty) {

                                    setGraphic(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        MainAuthedController.getInstance().addTab("Edit patient", "content/admin/EditPatient",new EditPatientController(userId),true);
                                    });
                                    setGraphic(btn);
                                }
                                setText(null);
                            }
                        };
                        return cell;
                    }
                };
        editColumn.setCellFactory(cellEditFactory);
        /*
        tasksColumn.setCellFactory(cellTasksFactory);
        visitsColumn.setCellFactory(cellVisitsFactory);

         */
        doctorPatientsTable.setItems(Context.getInstance().getDoctorPatientsTable());


    }
}
