package com.giga.htask.controllers.content.tasks;

import com.giga.htask.controllers.MainAuthedController;
import com.giga.htask.controllers.content.ContentController;
import com.giga.htask.controllers.content.doctors.DoctorController;
import com.giga.htask.controllers.content.patients.PatientController;
import com.giga.htask.model.Context;
import com.giga.htask.model.Message;
import com.giga.htask.model.Task;
import com.giga.htask.model.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class TaskController extends ContentController implements Initializable {

    Task task;
    @FXML
    Label titleLabel;
    @FXML
    Label descriptionLabel;
    @FXML
    Label statusLabel;
    @FXML
    Label createdOnLabel;
    @FXML
    Label finishedOnLabel;
    @FXML
    Label doctorLabel;
    @FXML
    Label patientLabel;
    @FXML
    Button showPatientButton;
    @FXML
    Button showDoctorButton;
    @FXML
    CheckBox statusCheckBox;
    @FXML
    Button submitEditTask;
    @FXML
    TextField titleField;
    @FXML
    TextArea descriptionTextArea;
    @FXML
    ScrollPane chatScrollPane;
    @FXML
    TextField chatTextField;
    @FXML
    Button chatSendButton;
    @FXML
    VBox chatContent;
    @FXML
    private VBox editVBox;


    private ObservableList<Message> messages;

    private ScheduledExecutorService scheduler;


    public TaskController(Integer taskId) {
        super(Context.getInstance().getLoggedUser().getId());
        task = Context.getInstance().getEntityById(Task.class, taskId);
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


    private void stopScheduler() {
        scheduler.shutdown();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        messages = Context.getInstance().getMessagesByTask(task.getId());
        handleTabButtons();
        handleEditTask();
        updateTables();
        handleChat();
        handleRoles();
        chatSendButton.setOnAction(event -> {
            handleSendMessage();
        });
        handleFillChat();

        // Create a scheduled executor service with a single thread
        scheduler = Executors.newSingleThreadScheduledExecutor();

        // Schedule the task to run every 30 seconds
        scheduler.scheduleAtFixedRate(this::handleFillChat, 0, 30, TimeUnit.SECONDS);

    }


    @Override
    protected void finalize() throws Throwable {
        // Perform cleanup here
        stopScheduler();
        super.finalize();
    }


    @Override
    protected void updateTables() {
        contentTitle.setText("Task of Patient " + task.getDoctorPatient().getPatient().getName() + " " + task.getDoctorPatient().getPatient().getSurname());
        doctorLabel.setText(task.getDoctorPatient().getDoctor().getName() + " " + task.getDoctorPatient().getDoctor().getSurname());
        patientLabel.setText(task.getDoctorPatient().getPatient().getName() + " " + task.getDoctorPatient().getPatient().getSurname());
        titleLabel.setText(task.getTitle());
        titleField.setText(task.getTitle());
        descriptionTextArea.setText(task.getDescription());
        descriptionLabel.setText(task.getDescription());
        statusLabel.setText(task.getStatus());
        createdOnLabel.setText(task.getCreatedOn().toString());
        if (task.getFinishedOn() != null) {
            finishedOnLabel.setText(task.getFinishedOn().toString());
        } else {
            finishedOnLabel.setText("Not finished yet");
        }
        if (task.getStatus().equals("finished")) {
            statusCheckBox.setSelected(true);
        } else {
            statusCheckBox.setSelected(false);
        }
    }

    private void editTask() {
        if (titleField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Title cannot be empty");
            alert.showAndWait();
            return;
        }
        task.setDescription(descriptionTextArea.getText());
        task.setTitle(titleField.getText());


        if (statusCheckBox.isSelected()) {
            task.setStatus("finished");
            Timestamp time = new Timestamp(System.currentTimeMillis());
            task.setFinishedOn(time);
        } else {
            task.setStatus("unfinished");
            task.setFinishedOn(null);
        }

        if (Context.getInstance().saveOrUpdateEntity(task)) {
            setSuccess("Task updated successfully");
            updateTables();
        } else {
            setError("Error while updating task");
        }
    }

    private void handleEditTask() {
        descriptionTextArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 2048 ? change : null));
        titleField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 255 ? change : null));
        submitEditTask.setOnAction(event -> editTask());

    }

    private void handleFillChat() {
        chatContent.getChildren().clear();

        User prevAuthor = null;
        for (Message message : messages) {
            Boolean reciever = true;
            String author = "";

            if (prevAuthor == null || message.getSender() != prevAuthor) {
                prevAuthor = message.getSender();
                author = "(" + message.getSender().getRole() + ") " + message.getSender().getName() + " " + message.getSender().getSurname();
            }

            //check if message is from the "me"
            if (message.getSender().equals(user)) {
                reciever = false;
            }
            addMessageToUi(message.getMessage(), reciever, author);
        }
    }

    private void handleTabButtons() {
        showPatientButton.setOnAction(event -> {
            try {
                ContentController controller = PatientController.class.getDeclaredConstructor(Integer.class).newInstance((Integer) task.getDoctorPatient().getPatient().getId());
                MainAuthedController.getInstance().addTab("View patient", "content/patients/Patient", controller, true);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
        showDoctorButton.setOnAction(event -> {
            try {
                ContentController controller = DoctorController.class.getDeclaredConstructor(Integer.class).newInstance((Integer) task.getDoctorPatient().getDoctor().getId());
                MainAuthedController.getInstance().addTab("View doctor", "content/doctors/Doctor", controller, true);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
    }

    private void handleChat() {

        chatContent.heightProperty().addListener(observable -> chatScrollPane.setVvalue(1D));
    }

    public void handleSendMessage() {
        String messageText = chatTextField.getText().trim();

        if (!messageText.isEmpty()) {
            // Create label with message text

            Message message = new Message();
            message.setMessage(messageText);
            message.setTask(task);
            message.setSender(Context.getInstance().getLoggedUser());

            if (Context.getInstance().saveOrUpdateEntity(message)) {
                messages.add(message);
                handleFillChat();
            } else {
                setError("Error while sending message");
            }

            // Clear the chatTextField
            chatTextField.setText("");

        }
    }

    private void addMessageToUi(String message, Boolean reciever, String author) {
        Label messageLabel = new Label(message);
        Label authorLabel = new Label();
        if (author != null || !author.isEmpty()) {
            authorLabel.setText(author);
        }


        VBox messageVBox = new VBox();
        messageVBox.setFillWidth(true);
        messageVBox.setMinWidth(400.D);

        //add labels to vbox
        messageVBox.getChildren().addAll(authorLabel, messageLabel);
        authorLabel.setMaxWidth(350.D);
        messageLabel.setMaxWidth(350.D);
        authorLabel.setWrapText(true);
        messageLabel.setWrapText(true);
        if (reciever) {
            messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: red;");
            authorLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: blue;");
            authorLabel.setAlignment(Pos.TOP_LEFT);
            messageLabel.setAlignment(Pos.TOP_LEFT);
            messageVBox.setAlignment(Pos.TOP_LEFT);
        } else {

            messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: red;");
            authorLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: green;");

            authorLabel.setAlignment(Pos.TOP_RIGHT);
            messageLabel.setAlignment(Pos.TOP_RIGHT);
            messageVBox.setAlignment(Pos.TOP_RIGHT);
        }

        VBox content = (VBox) chatScrollPane.getContent();
        content.getChildren().add(messageVBox);
    }

}
