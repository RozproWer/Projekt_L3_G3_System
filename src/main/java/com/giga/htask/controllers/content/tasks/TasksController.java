package com.giga.htask.controllers.content.tasks;

import com.giga.htask.controllers.content.ContentController;
import com.giga.htask.controllers.content.doctors.DoctorController;
import com.giga.htask.controllers.content.patients.PatientController;
import com.giga.htask.model.Context;
import com.giga.htask.model.Task;
import com.giga.htask.model.User;
import com.giga.htask.utils.ButtonCellAddTabFactory;
import com.giga.htask.utils.SortedFilteredObservableList;
import com.giga.htask.utils.TimestampValueFactory;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class TasksController extends ContentController implements Initializable {
    @FXML
    private TableView tasksTable;
    @FXML
    private TableColumn taskIdColumn;
    @FXML
    private TableColumn taskTitleColumn;
    @FXML
    private TableColumn taskCreatedOnColumn;
    @FXML
    private TableColumn taskFinishedOnColumn;
    @FXML
    private TableColumn taskStatusOnColumn;
    @FXML
    private TableColumn<Task, String> doctorNameColumn;
    @FXML
    private TableColumn<Task, String> doctorSurnameColumn;
    @FXML
    private TableColumn<Task, String> doctorPeselColumn;
    @FXML
    private TableColumn<Task, String> doctorSpecializationColumn;
    @FXML
    private TableColumn<Task, String> patientNameColumn;
    @FXML
    private TableColumn<Task, String> patientSurnameColumn;
    @FXML
    private TableColumn<Task, String> patientPeselColumn;
    @FXML
    private TableColumn<Task, Integer> patientShowColumn;
    @FXML
    private TableColumn<Task, Integer> doctorShowColumn;
    @FXML
    private TableColumn<Task, Integer> editColumn;
    @FXML
    private TableColumn<Task, Integer> deleteColumn;
    @FXML
    private TextField filterField;

    @FXML
    private Button addTaskButton;
    @FXML
    private Label addUserLabel;
    @FXML
    private Label AddTitleLabel;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private ComboBox userComboBox;
    @FXML
    private TextField titleField;

    SortedFilteredObservableList<Task> sfoList = Context.getInstance().getSortedFilteredObservableTasksTable(user.getId());
    /**
     * Constructs a new instance of the {@code TasksController} class with the specified user ID.
     * This constructor calls the constructor of the superclass ({@code UserController}) with the same argument.
     *
     * @param userId The ID of the user who tasks will be edited/showed.
     */
    public TasksController(Integer userId) {
        super(userId);
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        contentTitle.setText("Tasks of " + user.getName() + " " + user.getSurname());
        handleTables();
        updateTables();
        handleAddTask();
    }

    private void handleTables(){

        //set columns
        taskIdColumn.setCellValueFactory(new PropertyValueFactory<Task,Integer>("id"));
        TimestampValueFactory<Task> createdOnFactory = new TimestampValueFactory<>(Task::getCreatedOn);
        taskCreatedOnColumn.setCellValueFactory(createdOnFactory);
        TimestampValueFactory<Task> taskFinishedOnFactory = new TimestampValueFactory<>(Task::getCreatedOn);
        taskFinishedOnColumn.setCellValueFactory(taskFinishedOnFactory);

        taskStatusOnColumn.setCellValueFactory(new PropertyValueFactory<Task,String>("status"));

        editColumn.setCellValueFactory(new PropertyValueFactory<Task,Integer>("id"));

        taskTitleColumn.setCellValueFactory(new PropertyValueFactory<Task,String>("title"));

        patientNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDoctorPatient().getPatient().getName()));
        patientSurnameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDoctorPatient().getPatient().getSurname()));
        patientPeselColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDoctorPatient().getPatient().getPesel()));
        doctorNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDoctorPatient().getDoctor().getName()));
        doctorSurnameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDoctorPatient().getDoctor().getSurname()));
        doctorPeselColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDoctorPatient().getDoctor().getPesel()));
        doctorSpecializationColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDoctorPatient().getDoctor().getSpecialization()));

        doctorShowColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getDoctorPatient().getDoctor().getId()));
        patientShowColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getDoctorPatient().getPatient().getId()));


        Callback<TableColumn<Task, Integer>, TableCell<Task, Integer>> cellEditFactory =
                new ButtonCellAddTabFactory( "Edit task", "content/tasks/Task", TaskController.class);

        Callback<TableColumn<Task, Integer>, TableCell<Task, Integer>> cellDoctorFactory =
                new ButtonCellAddTabFactory( "Edit Doctor", "content/doctors/Doctor", DoctorController.class);

        Callback<TableColumn<Task, Integer>, TableCell<Task, Integer>> cellPatientFactory =
                new ButtonCellAddTabFactory( "Edit Patient", "content/patients/Patient", PatientController.class);


        deleteColumn.setCellValueFactory(new PropertyValueFactory<Task, Integer>("id"));
        sfoList.getSortedList().comparatorProperty().bind(tasksTable.comparatorProperty());
        tasksTable.setItems(sfoList.getSortedList());

        //sorting
        tasksTable.sortPolicyProperty().set(new Callback<TableView<Task>, Boolean>() {
            @Override
            public Boolean call(TableView<Task> param) {
                final Comparator<Task> tableComparator = tasksTable.getComparator();
                // if the column is set to unsorted, tableComparator can be null
                Comparator<Task> comparator = tableComparator == null ? null : new Comparator<Task>() {
                    @Override
                    public int compare(Task o1, Task o2) {
                        // secondly sort by the comparator that was set for the table
                        return tableComparator.compare(o1, o2);
                    }
                };
                tasksTable.setItems(sfoList.getFilteredList().sorted(comparator));
                return true;
            }
        });

        //filtering
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            sfoList.getSortedList().comparatorProperty().bind(tasksTable.comparatorProperty());

            sfoList.getFilteredList().setPredicate(task -> {

                if( !task.getDoctorPatient().getPatient().equals(user) && !task.getDoctorPatient().getDoctor().equals(user)){
                    return false;
                }
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                //filter by name, surname, email, pesel, specialization
                if (task.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (task.getCreatedOn().toString().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (task.getStatus().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if (task.getFinishedOn().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
            tasksTable.setItems(sfoList.getSortedList());
        });


        Callback<TableColumn<Task, Integer>, TableCell<Task, Integer>> cellDeleteFactory =
                new Callback<TableColumn<Task, Integer>, TableCell<Task, Integer>>() {
                    @Override
                    public TableCell call(final TableColumn<Task, Integer> column) {
                        final TableCell<Task, Integer> cell = new TableCell<Task, Integer>() {
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
                                        alert.setHeaderText("Are sure you want to delete this task?");
                                        alert.showAndWait();

                                        if (alert.getResult() == ButtonType.OK) {
                                            Context.getInstance().deleteEntityById(Task.class,id);
                                            updateTables();
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
        doctorShowColumn.setCellFactory(cellDoctorFactory);
        patientShowColumn.setCellFactory(cellPatientFactory);
    }

    @Override
    protected void updateTablesIfNeeded(Boolean refresh){
        sfoList.updateLists(Context.getInstance().getTasksTable(user.getId()), p -> true);
        tasksTable.setItems(sfoList.getSortedList());
        tasksTable.refresh();
    }

    private void handleAddTask(){
        AddTitleLabel.setText("Create task for "+user.getRole()+" "+user.getName()+" "+user.getSurname());
        if(user.getRole().equals("doctor")){
            addUserLabel.setText("Select Patient");
            userComboBox.setItems(Context.getInstance().getAssignedPatients(user.getId()));
        }else{
            addUserLabel.setText("Select Doctor");
            userComboBox.setItems(Context.getInstance().getAssignedDoctors(user.getId()));
        }

        titleField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 255 ? change : null));
        descriptionTextArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 2048 ? change : null));

        addTaskButton.setOnAction(event -> addTask());
    }

    private void addTask(){
        if(userComboBox.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("You have to select user");
            alert.showAndWait();
            return;
        }
        if(titleField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Title cannot be empty");
            alert.showAndWait();
            return;
        }

        Task task = new Task();
        task.setTitle(titleField.getText());
        task.setDescription(descriptionTextArea.getText());
        task.setStatus("unfinished");
        //get doctorPatient using context function getDoctorPatient
        User patientOrDoctor = (User) userComboBox.getSelectionModel().getSelectedItem();
        if (user.getRole().equals("doctor")){
          task.setDoctorPatient(Context.getInstance().getDoctorPatientByDoctorAndPatientId(user.getId(),patientOrDoctor.getId()));

        }else {
            task.setDoctorPatient(Context.getInstance().getDoctorPatientByDoctorAndPatientId(patientOrDoctor.getId(),user.getId()));
        }
        Boolean success = Context.getInstance().saveOrUpdateEntity(task);
        if(success){
            setSuccess("Task added successfully");
            updateTables();

        }else{
            setError("Error while adding task");
        }
    }


}
