package com.giga.htask.controllers.content.shared;

import com.giga.htask.model.*;
import com.giga.htask.controllers.content.ContentController;
import com.giga.htask.model.Visit;
import com.giga.htask.utils.ButtonCellAddTabFactory;
import com.giga.htask.utils.SortedFilteredObservableList;
import com.giga.htask.utils.TimestampValueFactory;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.ResourceBundle;

public class VisitsController  extends ContentController implements Initializable {

    SortedFilteredObservableList<Visit> sfoList = Context.getInstance().getSortedFilteredObservableVisitsTable(user.getId());
    @FXML
    private BorderPane calendarPane;
    @FXML
    private TableView visitsTable;
    @FXML
    private TableColumn visitIdColumn;
    @FXML
    private TableColumn visitTitleColumn;
    @FXML
    private TableColumn visitCreatedOnColumn;
    @FXML
    private TableColumn visitAppointmentOnColumn;
    @FXML
    private TableColumn<Visit, String> doctorNameColumn;
    @FXML
    private TableColumn<Visit, String> doctorSurnameColumn;
    @FXML
    private TableColumn<Visit, String> doctorPeselColumn;
    @FXML
    private TableColumn<Visit, String> doctorSpecializationColumn;
    @FXML
    private TableColumn<Visit, String> patientNameColumn;
    @FXML
    private TableColumn<Visit, String> patientSurnameColumn;
    @FXML
    private TableColumn<Visit, String> patientPeselColumn;
    @FXML
    private TableColumn<Visit, Integer> patientShowColumn;
    @FXML
    private TableColumn<Visit, Integer> doctorShowColumn;
    @FXML
    private TableColumn<Visit, Integer> editColumn;
    @FXML
    private TableColumn<Visit, Integer> deleteColumn;
    @FXML
    private TextField filterField;
    @FXML
    private Button addVisitButton;
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
    @FXML
    private DatePicker appointmentOnDatePicker;


    public VisitsController(Integer userId) {
        super(userId);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        contentTitle.setText("Visits of " + user.getName() + " " + user.getSurname());
        handleTables();
        updateTables();
        handleAddVisit();



    }
    private void handleTables(){
        //set columns
        visitIdColumn.setCellValueFactory(new PropertyValueFactory<Visit,Integer>("id"));
        TimestampValueFactory<Visit> createdOnFactory = new TimestampValueFactory<>(Visit::getCreatedOn);
        visitCreatedOnColumn.setCellValueFactory(createdOnFactory);
        TimestampValueFactory<Visit> visitAppointmentOnFactory = new TimestampValueFactory<>(Visit::getCreatedOn);
        visitAppointmentOnColumn.setCellValueFactory(visitAppointmentOnFactory);

        visitTitleColumn.setCellValueFactory(new PropertyValueFactory<Visit,String>("title"));

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

        Callback<TableColumn<Visit, Integer>, TableCell<Visit, Integer>> cellEditFactory =
                new ButtonCellAddTabFactory( "Edit visit", "content/admin/Visit", VisitController.class);

        sfoList.getSortedList().comparatorProperty().bind(visitsTable.comparatorProperty());
        visitsTable.setItems(sfoList.getSortedList());

        editColumn.setCellFactory(cellEditFactory);
        //sorting

        visitsTable.sortPolicyProperty().set(new Callback<TableView<Visit>, Boolean>() {
            @Override
            public Boolean call(TableView<Visit> param) {
                final Comparator<Visit> tableComparator = visitsTable.getComparator();
                // if the column is set to unsorted, tableComparator can be null
                Comparator<Visit> comparator = tableComparator == null ? null : new Comparator<Visit>() {
                    @Override
                    public int compare(Visit o1, Visit o2) {
                        // secondly sort by the comparator that was set for the table
                        return tableComparator.compare(o1, o2);
                    }
                };
                visitsTable.setItems(sfoList.getFilteredList().sorted(comparator));
                return true;
            }
        });

        //filtering
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            sfoList.getSortedList().comparatorProperty().bind(visitsTable.comparatorProperty());

            sfoList.getFilteredList().setPredicate(visit -> {

                if( !visit.getDoctorPatient().getPatient().equals(user) && !visit.getDoctorPatient().getDoctor().equals(user)){
                    return false;
                }
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                //filter by name, surname, email, pesel, specialization
                if (visit.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (visit.getCreatedOn().toString().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (visit.getAppointmentOn().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
            visitsTable.setItems(sfoList.getSortedList());
        });
    }
    @Override
    protected void updateTablesIfNeeded(Boolean refresh){
        sfoList.updateLists(Context.getInstance().getVisitsTable(user.getId()), p -> true);
        visitsTable.setItems(sfoList.getSortedList());
        visitsTable.refresh();
    }
    private void handleAddVisit(){
        AddTitleLabel.setText("Create visit for "+user.getRole()+" "+user.getName()+" "+user.getSurname());
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

        addVisitButton.setOnAction(event -> addVisit());
    }

    private void addVisit(){
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

        Visit visit = new Visit();
        visit.setTitle(titleField.getText());
        visit.setDescription(descriptionTextArea.getText());

        LocalDate time = appointmentOnDatePicker.getValue();
        System.out.println(time);
        Timestamp timestamp = Timestamp.valueOf(time.atStartOfDay());
        visit.setAppointmentOn(timestamp);

        User patient = (User) userComboBox.getSelectionModel().getSelectedItem();
        if (user.getRole().equals("doctor")){
            visit.setDoctorPatient(Context.getInstance().getDoctorPatientByDoctorAndPatientId(user.getId(),patient.getId()));
        }else{
            visit.setDoctorPatient(Context.getInstance().getDoctorPatientByDoctorAndPatientId(patient.getId(),user.getId()));
        }

        Boolean success = Context.getInstance().saveOrUpdateEntity(visit);
        if(success){
            setSuccess("Visit added successfully");
            updateTables();

        }else{
            setError("Error while adding Visit");
        }
    }
}
