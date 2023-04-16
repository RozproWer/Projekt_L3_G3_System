package com.giga.htask.controllers.content.admin;

import com.giga.htask.controllers.MainAuthedController;
import com.giga.htask.controllers.TimestampValueFactory;
import com.giga.htask.controllers.content.ContentController;
import com.giga.htask.model.Context;
import com.giga.htask.model.User;
import com.giga.htask.model.UserDoctor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class DoctorsController extends ContentController implements Initializable {

    @FXML
    private TableView<User> doctorsTable;
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> surnameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> telephoneColumn;
    @FXML
    private TableColumn addressColumn;
    @FXML
    private TableColumn peselColumn;
    @FXML
    private TableColumn specializationColumn;
    @FXML
    private TableColumn roleColumn;
    @FXML
    private TableColumn deleteColumn;
    @FXML
    private TableColumn editColumn;
    @FXML
    private TableColumn createdOnColumn;
    @FXML
    private TextField filterField;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        idColumn.setCellValueFactory(new PropertyValueFactory<User,Integer>("id")); //probowałem
        nameColumn.setCellValueFactory(new PropertyValueFactory<User,String>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<User,String>("surname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<User,String>("email"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<User,String>("telephone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<User,String>("address"));
        peselColumn.setCellValueFactory(new PropertyValueFactory<User,String>("pesel"));
        specializationColumn.setCellValueFactory(new PropertyValueFactory<User,String>("specialization"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<User,String>("role"));
        deleteColumn.setCellValueFactory(new PropertyValueFactory<User,Integer>("id"));
        editColumn.setCellValueFactory(new PropertyValueFactory<User,Integer>("id"));
        createdOnColumn.setCellValueFactory(new TimestampValueFactory<>());

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
                                     MainAuthedController.getInstance().addTab("Edit doctor", "content/admin/EditDoctor",new EditDoctorController(userId),true);
                                    });
                                    setGraphic(btn);
                                }
                                setText(null);
                            }
                        };
                        return cell;
                    }
                };

        //delete button
        deleteColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        Callback<TableColumn<User, String>, TableCell<User, Integer>> cellDeleteFactory =
                new Callback<TableColumn<User, String>, TableCell<User, Integer>>() {
                    @Override
                    public TableCell call(final TableColumn<User, String> column) {
                        final TableCell<User, Integer> cell = new TableCell<User, Integer>() {
                            final Button btn = new Button(column.getText());

                            @Override
                            public void updateItem(Integer vID, boolean empty) {
                                super.updateItem(vID, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {

                                    btn.setOnAction(event -> {
                                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                        alert.setTitle("Delete operation");
                                        alert.setHeaderText("Are sure you want to delete this entity?");
                                        alert.showAndWait();

                                        if (alert.getResult() == ButtonType.OK) {
                                            Context.getInstance().deleteEntityById(User.class,vID);
                                            doctorsTable.setItems(Context.getInstance().getSortedDoctorsTable());
                                        }
                                    });
                                    setGraphic(btn);
                                }
                                setText(null);
                            }
                        };
                        return cell;
                    }
                };

        deleteColumn.setCellFactory(cellDeleteFactory);
        editColumn.setCellFactory(cellEditFactory);


        Context.getInstance().getSortedDoctorsTable().comparatorProperty().bind(doctorsTable.comparatorProperty());
        doctorsTable.setItems(Context.getInstance().getSortedDoctorsTable());



        //sorting
        doctorsTable.sortPolicyProperty().set(new Callback<TableView<User>, Boolean>() {
            @Override
            public Boolean call(TableView<User> param) {
                final Comparator<User> tableComparator = doctorsTable.getComparator();
                // if the column is set to unsorted, tableComparator can be null
                Comparator<User> comparator = tableComparator == null ? null : new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        // secondly sort by the comparator that was set for the table
                        return tableComparator.compare(o1, o2);
                    }
                };
                doctorsTable.setItems(Context.getInstance().getFilteredDoctorsTable().sorted(comparator));
                return true;
            }
        });

        //filtering
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            Context.getInstance().getSortedDoctorsTable().comparatorProperty().bind(doctorsTable.comparatorProperty());

            Context.getInstance().getFilteredDoctorsTable().setPredicate(user -> {
                if(!user.getRole().equals("doctor")) {
                   return false;
                }
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                //filter by name, surname, email, pesel, specialization
                if (user.getSurname().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (user.getEmail().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (user.getPesel().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if (user.getSpecialization().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
            doctorsTable.setItems(Context.getInstance().getSortedDoctorsTable());
        });


    }
}
//https://stackoverflow.com/questions/17958337/javafx-tableview-with-filteredlist-jdk-8-does-not-sort-by-column
//https://stackoverflow.com/questions/50109815/javafx-tableview-sort-by-custom-rule-then-by-column-selection