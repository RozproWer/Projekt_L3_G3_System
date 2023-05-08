package com.giga.htask.controllers.content.doctors;

import com.giga.htask.controllers.content.patients.PatientController;
import com.giga.htask.controllers.content.tasks.TasksController;
import com.giga.htask.model.DoctorPatient;
import com.giga.htask.utils.ButtonCellAddTabFactory;
import com.giga.htask.controllers.content.ContentController;
import com.giga.htask.controllers.content.visits.VisitsController;
import com.giga.htask.model.Context;
import com.giga.htask.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;


import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for single doctor view.
 */
public class DoctorController extends ContentController implements Initializable {

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
    @FXML
    TableColumn deleteColumn;
    @FXML
    TableColumn visitsColumn;
    @FXML
    TableColumn tasksColumn;
    @FXML
    private Label name;
    @FXML
    private Label surname;
    @FXML
    private Label role;
    @FXML
    private Label email;
    @FXML
    private Label telephone;
    @FXML
    private Label address;
    @FXML
    private Label pesel;
    @FXML
    private Label specialization;
    @FXML
    private Label description;
    @FXML
    private Label createdOn;
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
    private Button submitEditDoctor;
    @FXML
    private ComboBox patientsComboBox;
    @FXML
    private Button assignPatientButton;

    /**
     * Constructs a new instance of the {@code DoctorController} class with the specified user ID.
     * This constructor calls the constructor of the superclass ({@code UserController}) with the same argument.
     *
     * @param userId The ID of the userDoctor to be edited/showed.
     */
    public DoctorController(Integer userId) {
        super(userId);
    }

    /**
     * Initializes the controller with the specified location and resources. Sets the content title to display the
     * name and surname of the doctor being edited, and calls the handleTable() and handleSummary() methods to set up
     * the patient table view and summary view with appropriate data and cell factories.
     *
     * @param location  The location of the FXML file.
     * @param resources The resources used by the FXML file.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        contentTitle.setText("Edit Doctor: " + user.getName() + " " + user.getSurname());
        handleTable();
        updateTables();
        handleSummary();
        handleEdit();
        handleAssignPatient();
    }

    /**
     * Populates the summary view with information from the currently selected user.
     * This method sets the text of the labels in the view to the corresponding properties of the user object.
     */
    public void handleSummary() {
        name.setText(user.getName());
        surname.setText(user.getSurname());
        role.setText(user.getRole());
        email.setText(user.getEmail());
        telephone.setText(user.getTelephone());
        address.setText(user.getAddress());
        pesel.setText(user.getPesel());
        specialization.setText(user.getSpecialization());
        description.setText(user.getUserDoctor().getDescription());
        createdOn.setText(user.getCreatedOn().toString());
    }

    /**
     * Sets up the patient table view with the appropriate columns and cell factories, and populates it with data
     * rom the Context instance. Also sets up the edit, tasks, visits, and delete buttons for each patient row.
     */
    public void handleTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<DoctorPatient, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("surname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        peselColumn.setCellValueFactory(new PropertyValueFactory<User, String>("pesel"));
        editColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        tasksColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        visitsColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));

        Callback<TableColumn<User, Integer>, TableCell<User, Integer>> cellEditFactory =
                new ButtonCellAddTabFactory("View patient", "content/patients/Patient", PatientController.class);
        Callback<TableColumn<User, Integer>, TableCell<User, Integer>> cellVisitsFactory =
                new ButtonCellAddTabFactory("Patient's visits", "content/visits/Visits", VisitsController.class);
        Callback<TableColumn<User, Integer>, TableCell<User, Integer>> cellTasksFactory =
                new ButtonCellAddTabFactory("Patient's tasks", "content/tasks/Tasks", TasksController.class);

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
                                        alert.setTitle("Unassign operation");
                                        alert.setHeaderText("Are sure you want to unassign this patient??");
                                        alert.showAndWait();

                                        if (alert.getResult() == ButtonType.OK) {
                                            if (Context.getInstance().deleteEntityById(DoctorPatient.class, Context.getInstance().getDoctorPatientId(user.getId(), id))) {
                                                setSuccess("Patient unassigned successfully");
                                                updateTables();
                                            } else {
                                                setError("Error while unassigning patient");
                                            }
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
    }


    /**
     * Handles the edit button and sets up the text fields with the appropriate data from the currently selected user.
     */
    private void handleEdit() {
        //set max fields length to match database
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
                change.getControlNewText().length() <= 512 ? change : null));

        nameField.setText(user.getName());
        surnameField.setText(user.getSurname());
        emailField.setText(user.getEmail());
        telephoneField.setText(user.getTelephone());
        addressField.setText(user.getAddress());
        peselField.setText(user.getPesel());
        specializationField.setText(user.getSpecialization());
        descriptionTextArea.setText(user.getUserDoctor().getDescription());
        submitEditDoctor.setOnAction(event -> editDoctor());
    }

    /**
     * Edits doctor details and saves them to database. if successfull, displays success message, otherwise displays error message
     */
    private void editDoctor() {
        user.setName(nameField.getText());
        user.setSurname(surnameField.getText());
        user.setEmail(emailField.getText());
        user.setTelephone(telephoneField.getText());
        user.setAddress(addressField.getText());
        user.setPesel(peselField.getText());
        user.getUserDoctor().setSpecialization(specializationField.getText());
        user.getUserDoctor().setDescription(descriptionTextArea.getText());

        boolean isSuccess = Context.getInstance().saveOrUpdateEntity(user);

        if (isSuccess) {
            setSuccess("Successfully updated doctor details");
        } else {
            setError("Failed to update user");
        }
    }

    /**
     * Handles the assign doctor button
     */
    private void handleAssignPatient() {
        assignPatientButton.setOnAction(event -> assignPatient());
    }

    /**
     * Handles the assign patient button and fills the combobox with unassigned doctors
     */
    private void assignPatient() {
        if (patientsComboBox.getSelectionModel().getSelectedItem() != null) {
            User patient = (User) patientsComboBox.getSelectionModel().getSelectedItem();
            DoctorPatient doctorPatient = new DoctorPatient();
            doctorPatient.setPatient(patient);
            doctorPatient.setDoctor(user);
            boolean isSuccess = Context.getInstance().saveOrUpdateEntity(doctorPatient);
            if (isSuccess) {
                setSuccess("Successfully assigned patient");
                updateTables();
            } else {
                setError("Failed to assign patient");
            }
        } else {
            setError("Please select a patient");
        }
    }

    protected void updateTables() {
        doctorPatientsTable.setItems(Context.getInstance().getDoctorPatientsTable(user.getId()));
        patientsComboBox.setItems(Context.getInstance().getUnassignedPatients(user.getId()));
        doctorPatientsTable.refresh();
    }

}
