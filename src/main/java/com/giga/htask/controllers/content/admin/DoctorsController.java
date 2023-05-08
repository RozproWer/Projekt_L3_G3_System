package com.giga.htask.controllers.content.admin;

import com.giga.htask.controllers.content.shared.TasksController;
import com.giga.htask.controllers.content.shared.VisitsController;
import com.giga.htask.model.UserDoctor;
import com.giga.htask.utils.TimestampValueFactory;
import com.giga.htask.utils.ButtonCellAddTabFactory;
import com.giga.htask.controllers.content.ContentController;
import com.giga.htask.model.Context;
import com.giga.htask.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

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
    private TableColumn tasksColumn;
    @FXML
    private TableColumn visitsColumn;
    @FXML
    private TableColumn createdOnColumn;
    @FXML
    private TextField filterField;

    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField telephoneField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField peselField;
    @FXML
    private TextField specializationField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Button addDoctor;

    public DoctorsController() {
        super(Context.getInstance().getLoggedUser().getId());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        handleTables();
        updateTables();
        handleAddDoctor();
    }

    private void handleTables(){
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
        visitsColumn.setCellValueFactory(new PropertyValueFactory<User,Integer>("id"));
        tasksColumn.setCellValueFactory(new PropertyValueFactory<User,Integer>("id"));
        TimestampValueFactory<User> createdOnFactory = new TimestampValueFactory<>(User::getCreatedOn);
        createdOnColumn.setCellValueFactory(createdOnFactory);

        Callback<TableColumn<User, Integer>, TableCell<User, Integer>> cellEditFactory =
                new ButtonCellAddTabFactory( "Edit doctor", "content/admin/EditDoctor", DoctorController.class);
        Callback<TableColumn<User, Integer>, TableCell<User, Integer>> cellVisitsFactory =
                new ButtonCellAddTabFactory( "Doctor's visits", "content/shared/Visits", VisitsController.class);
        Callback<TableColumn<User, Integer>, TableCell<User, Integer>> cellTasksFactory =
                new ButtonCellAddTabFactory( "Doctor's tasks", "content/shared/Tasks", TasksController.class);

        deleteColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        Callback<TableColumn<User, String>, TableCell<User, Integer>> cellDeleteFactory =
                new Callback<TableColumn<User, String>, TableCell<User, Integer>>() {
                    @Override
                    public TableCell call(final TableColumn<User, String> column) {
                        final TableCell<User, Integer> cell = new TableCell<User, Integer>() {
                            final Button btn = new Button(column.getText());

                            @Override
                            public void updateItem(Integer id, boolean empty) {
                                super.updateItem(id, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {

                                    btn.setOnAction(event -> {
                                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                        alert.setTitle("Delete operation");
                                        alert.setHeaderText("Are sure you want to delete this doctor?");
                                        alert.showAndWait();

                                        if (alert.getResult() == ButtonType.OK) {
                                            Context.getInstance().deleteEntityById(User.class,id);
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
        visitsColumn.setCellFactory(cellVisitsFactory);
        tasksColumn.setCellFactory(cellTasksFactory);

        Context.getInstance().getSortedDoctorsTable().comparatorProperty().bind(doctorsTable.comparatorProperty());

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

    /**
     * Method used to set text formaters, and handle add doctor button
     */
    private void handleAddDoctor(){
        nameField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 255 ? change : null));
        surnameField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 255 ? change : null));
        emailField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 255 ? change : null));
        telephoneField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 15 ? change : null));
        addressField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 255 ? change : null));
        peselField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 11 ? change : null));
        specializationField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 255 ? change : null));
        descriptionTextArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 2048 ? change : null));
        addDoctor.setOnAction(event -> addDoctor());
    }

    /**
    * Creates doctor objects and set its fields with values from text fields
    */
    private void addDoctor() {
        if(nameField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Name cannot be empty");
            alert.showAndWait();
            return;
        }
        if(surnameField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Surname cannot be empty");
            alert.showAndWait();
            return;
        }
        if(emailField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Email cannot be empty");
            alert.showAndWait();
            return;
        }
        if(telephoneField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Telephone cannot be empty");
            alert.showAndWait();
            return;
        }
        if(addressField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Address cannot be empty");
            alert.showAndWait();
            return;
        }
        if(peselField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Pesel cannot be empty");
            alert.showAndWait();
            return;
        }
        if(!peselField.getText().matches("[0-9]+")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Pesel must contain only numbers");
            alert.showAndWait();
            return;
        }
        if(peselField.getText().length() != 11){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Pesel must contain 11 numbers");
            alert.showAndWait();
            return;
        }
        if(!emailField.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Email is not valid");
            alert.showAndWait();
            return;
        }
        if(!telephoneField.getText().matches("[0-9]+")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Telephone must contain only numbers");
            alert.showAndWait();
            return;
        }
        if(telephoneField.getText().length() > 15){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Telephone is too long");
            alert.showAndWait();
            return;
        }

        User user = new User();
        user.setRole("doctor");
        UserDoctor userDoctor = new UserDoctor();
        userDoctor.setDoctor(user);

        user.setName(nameField.getText());
        user.setSurname(surnameField.getText());
        user.setEmail(emailField.getText());
        user.setTelephone(telephoneField.getText());
        user.setAddress(addressField.getText());
        user.setPesel(peselField.getText());
        userDoctor.setSpecialization(specializationField.getText());
        userDoctor.setDescription(descriptionTextArea.getText());
        user.setPassword(Context.getInstance().generatePassword());
        if(Context.getInstance().saveOrUpdateEntity(user) &&  Context.getInstance().saveOrUpdateEntity(userDoctor)) {
            Context.getInstance().reportGenerator.generateNewUserReport(user);
            updateTables(true);
            setSuccess("Doctor added successfully");
        }else{
            setError("Error while adding doctor");
        }
    }

    /**
     * Updates patients table in case of adding new patient or editing existing one.
     * It affects all tabs that are using doctors or patient table throught Context.
     * @param refresh if true, table will be refreshed
     */
    @Override
    protected void updateTablesIfNeeded(Boolean refresh){
        doctorsTable.setItems(Context.getInstance().getSortedDoctorsTable(refresh));
        doctorsTable.refresh();
    }

}
