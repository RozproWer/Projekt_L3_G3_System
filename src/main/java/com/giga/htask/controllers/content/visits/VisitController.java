package com.giga.htask.controllers.content.visits;

import com.giga.htask.model.Visit;
import com.giga.htask.controllers.MainAuthedController;
import com.giga.htask.controllers.content.ContentController;
import com.giga.htask.controllers.content.doctors.DoctorController;
import com.giga.htask.controllers.content.patients.PatientController;
import com.giga.htask.model.Context;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import tornadofx.control.DateTimePicker;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class VisitController extends ContentController implements Initializable {
    Visit visit;
    @FXML
    Label titleLabel;
    @FXML
    Label descriptionLabel;
    @FXML
    Label statusLabel;
    @FXML
    Label createdOnLabel;
    @FXML
    Label appointedOnLabel;
    @FXML
    Label doctorLabel;
    @FXML
    Label patientLabel;
    @FXML
    Button showPatientButton;
    @FXML
    Button showDoctorButton;
    @FXML
    Button submitEditTask;
    @FXML
    TextField titleField;
    @FXML
    TextArea descriptionTextArea;
    @FXML
    DateTimePicker appointmentOnDateTimePicker;
    @FXML
    private VBox editVBox;


    public VisitController(Integer visitId) {
        super(Context.getInstance().getLoggedUser().getId());
        visit = Context.getInstance().getEntityById(Visit.class, visitId);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);


        handleTabButtons();
        handleEditTask();
        updateTables();
        handleRoles();
    }
    @Override
    protected void updateTables(){
        contentTitle.setText("Visit of Patient " + visit.getDoctorPatient().getPatient().getName()+ " " + visit.getDoctorPatient().getPatient().getSurname());
        doctorLabel.setText(visit.getDoctorPatient().getDoctor().getName() + " " + visit.getDoctorPatient().getDoctor().getSurname());
        patientLabel.setText(visit.getDoctorPatient().getPatient().getName() + " " + visit.getDoctorPatient().getPatient().getSurname());
        titleLabel.setText(visit.getTitle());
        titleField.setText(visit.getTitle());
        descriptionTextArea.setText(visit.getDescription());

        appointmentOnDateTimePicker.setDateTimeValue(visit.getAppointmentOn().toLocalDateTime());
        descriptionLabel.setText(visit.getDescription());
        createdOnLabel.setText(visit.getCreatedOn().toString());
        appointedOnLabel.setText(visit.getAppointmentOn().toString());

    }

    private void handleRoles() {
        switch (Context.getInstance().getLoggedUser().getRole()) {
            case ("patient"):
                editVBox.setVisible(false);
                break;
            case ("receptionist"):
                editVBox.setVisible(false);
                break;
        }
    }


    private void editVisit(){
        if(titleField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Title cannot be empty");
            alert.showAndWait();
            return;
        }
        if(appointmentOnDateTimePicker.getDateTimeValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Appointment date cannot be empty");
            alert.showAndWait();
            return;
        }

        visit.setDescription(descriptionTextArea.getText());
        visit.setTitle(titleField.getText());
        visit.setAppointmentOn(Timestamp.valueOf(appointmentOnDateTimePicker.getDateTimeValue()));
        if(Context.getInstance().saveOrUpdateEntity(visit)){
            setSuccess("Visit updated successfully");
            updateTables();
        }else{
            setError("Error while updating visit");
        }

    }
    private void handleEditTask(){
        descriptionTextArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 2048 ? change : null));
        titleField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 255 ? change : null));

        submitEditTask.setOnAction(event -> editVisit());
    }

    private void handleTabButtons(){
        showPatientButton.setOnAction(event -> {
            try {
                ContentController controller = PatientController.class.getDeclaredConstructor(Integer.class).newInstance((Integer) visit.getDoctorPatient().getPatient().getId());
                MainAuthedController.getInstance().addTab("View patient", "content/patients/Patient", controller, true);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
        showDoctorButton.setOnAction(event -> {
            try {
                ContentController controller = DoctorController.class.getDeclaredConstructor(Integer.class).newInstance((Integer) visit.getDoctorPatient().getDoctor().getId());
                MainAuthedController.getInstance().addTab("View doctor", "content/doctors/Doctor", controller, true);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
    }
}
